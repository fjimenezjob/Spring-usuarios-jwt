package com.lluviadeideas.jwt.view.viewPdf;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lluviadeideas.jwt.models.entity.Factura;
import com.lluviadeideas.jwt.models.entity.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        Locale locale = localeResolver.resolveLocale(request);
        Factura factura = (Factura) model.get("factura");
        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);

        PdfPCell cell = null;
        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datosCliente", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla.addCell(cell);
        tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
        tabla.addCell(factura.getCliente().getEmail());

        PdfPTable tablaDos = new PdfPTable(1);
        tablaDos.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.factura.ver.datosFactura", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        tablaDos.addCell(cell);
        tablaDos.addCell(messageSource.getMessage("text.ver.folio", null, locale) + ": " + factura.getId());
        tablaDos.addCell(messageSource.getMessage("text.ver.desc", null, locale) + ": " + factura.getDescripcion());
        tablaDos.addCell(messageSource.getMessage("text.factura.ver.fecha", null, locale) + ": " + factura.getCreated_At());

        PdfPTable tablaTres = new PdfPTable(4);
        tablaTres.setWidths(new float[] { 2.5f, 1, 1, 1 });
        tablaTres.addCell(messageSource.getMessage("text.factura.ver.producto", null, locale));
        tablaTres.addCell(messageSource.getMessage("text.factura.ver.precio", null, locale));
        tablaTres.addCell(messageSource.getMessage("text.factura.ver.cantidad", null, locale));
        tablaTres.addCell(messageSource.getMessage("text.ver.total", null, locale));

        for (ItemFactura item : factura.getItems()) {
            tablaTres.addCell(item.getProducto().getNombre());
            tablaTres.addCell(item.getProducto().getPrecio().toString());
            cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tablaTres.addCell(cell);
            tablaTres.addCell(item.calcularImporte().toString());
        }

        document.add(tabla);
        document.add(tablaDos);

        PdfPCell cell1 = new PdfPCell(new Phrase("Total : "));
        cell1.setColspan(3);
        cell1.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tablaTres.addCell(cell1);
        tablaTres.addCell(factura.getTotal().toString());
        document.add(tablaTres);
    }

}