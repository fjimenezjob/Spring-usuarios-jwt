package com.lluviadeideas.jwt.controllers;

import java.util.List;
import java.util.Locale;

import com.lluviadeideas.jwt.models.entity.Cliente;
import com.lluviadeideas.jwt.models.entity.Factura;
import com.lluviadeideas.jwt.models.entity.ItemFactura;
import com.lluviadeideas.jwt.models.entity.Producto;
import com.lluviadeideas.jwt.models.service.IClienteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Secured("ROLE_ADMIN")
@Controller
@RequestMapping("/factura")
@SessionAttributes("factura")
public class FacturaController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private IClienteService clienteService;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash, Locale locale) {

        Factura factura = clienteService.fetchFacturaByIdWithClienteWithItemFacturaWithProducto(id);
        
        if (factura == null) {
            flash.addAttribute("error", "La factura No existe en la base de datos");
            return "redirect:/listar";
        }
        model.addAttribute("factura", factura);
        model.addAttribute("titulo", messageSource.getMessage("text.ver.titulo", null, locale).concat(":"+' ' + factura.getDescripcion()));
        return "factura/ver";
    }

    @GetMapping("/form/{clienteId}")
    public String crear(@PathVariable(value = "clienteId") Long clienteId, Model model) {

        Cliente cliente = clienteService.findOne(clienteId);
        if (cliente == null) {
            return "redirect:/listar";
        }
        Factura factura = new Factura();
        factura.setCliente(cliente);
        model.addAttribute("titulo", "Crear Factura");
        model.addAttribute("factura", factura);
        return "factura/form";
    }

    @GetMapping(value = "/cargar-productos/{term}", produces = { "application/json" })
    public @ResponseBody List<Producto> cargarProductos(@PathVariable String term) {
        return clienteService.findByNombre(term);
    }

    @PostMapping("/form")
    public String guardar(@Valid Factura factura, BindingResult result,
            @RequestParam(name = "item_id[]", required = false) Long[] itemId,
            @RequestParam(name = "cantidad[]", required = false) Integer[] cantidad, SessionStatus status,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Crear Factura");
            return "factura/form";
        }

        if (itemId == null | itemId.length == 0) {
            model.addAttribute("titulo", "Crear Factura");
            model.addAttribute("error", "Error: La Factura NO puede estar vacía");
        }

        for (int i = 0; i < itemId.length; i++) {

            Producto producto = clienteService.finProductoById(itemId[i]);

            ItemFactura linea = new ItemFactura();
            linea.setCantidad(cantidad[i]);
            linea.setProducto(producto);
            factura.addItemFactura(linea);

            log.info("ID: " + itemId[i].toString() + " Cantidad: " + cantidad[i].toString());
        }

        clienteService.saveFactura(factura);
        status.setComplete();
        return "redirect:/ver/" + factura.getCliente().getId();
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id) {
        Factura factura = clienteService.findFacturaById(id);
        if (factura != null) {
            clienteService.deleteFacturaById(id);
            return "redirect:/ver/" + factura.getCliente().getId();
        }
        return "redirect:/listar";
    }
}