package com.lluviadeideas.jwt.models.service;

import java.util.List;

import com.lluviadeideas.jwt.models.entity.Cliente;
import com.lluviadeideas.jwt.models.entity.Factura;
import com.lluviadeideas.jwt.models.entity.Producto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


public interface IClienteService {
    
    public List<Cliente> findAll();

    public Page<Cliente> findAll(Pageable pageable);

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public Cliente fetchByIdWithFacturas(Long id);

    public void delete(Long id);

    public List<Producto> findByNombre(String term);

    public void saveFactura(Factura factura);

    public Producto finProductoById(Long id);

    public Factura findFacturaById(Long id);

    public void deleteFacturaById(Long id);

    public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id);
}