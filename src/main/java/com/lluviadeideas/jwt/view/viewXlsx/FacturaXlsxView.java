package com.lluviadeideas.jwt.view.viewXlsx;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lluviadeideas.jwt.models.entity.Factura;
import com.lluviadeideas.jwt.models.entity.ItemFactura;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        response.setHeader("Content-Dsiposition","attachment; filename=\"factura_view.xlsx\"");
        Locale locale = localeResolver.resolveLocale(request);
        Factura factura = (Factura) model.get("factura");
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.factura.ver.datosCliente", null, locale));

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.factura.ver.factura", null, locale) + ": " + factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(messageSource.getMessage("text.cliente.email", null, locale) + ": " + factura.getCliente().getEmail());

        sheet.createRow(4).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.datosFactura", null, locale)+ ": ");
        sheet.createRow(5).createCell(0).setCellValue(messageSource.getMessage("text.ver.folio", null, locale) + ": " + factura.getId());
        sheet.createRow(6).createCell(0).setCellValue(messageSource.getMessage("text.ver.desc", null, locale) + ": " + factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.fecha", null, locale) + ": " + factura.getCreated_At());

        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.BLUE_GREY.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyStyle = workbook.createCellStyle();
        tbodyStyle.setBorderBottom(BorderStyle.THIN);
        tbodyStyle.setBorderTop(BorderStyle.THIN);
        tbodyStyle.setBorderLeft(BorderStyle.THIN);
        tbodyStyle.setBorderRight(BorderStyle.MEDIUM);


        
        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue(messageSource.getMessage("text.factura.ver.producto", null, locale));
        header.createCell(1).setCellValue(messageSource.getMessage("text.factura.ver.precio", null, locale));
        header.createCell(2).setCellValue(messageSource.getMessage("text.factura.ver.cantidad", null, locale));
        header.createCell(3).setCellValue(messageSource.getMessage("text.ver.total", null, locale));

        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);

        int rownum = 10;
        for(ItemFactura item : factura.getItems()){
            Row fila = sheet.createRow(rownum ++);

            cell = fila.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(tbodyStyle);

            cell =  fila.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(tbodyStyle);
        }

        Row filatotal = sheet.createRow(rownum ++);
        cell = filatotal.createCell(2);
        cell.setCellValue(messageSource.getMessage("text.ver.total", null, locale));
        cell.setCellStyle(tbodyStyle);

        cell = filatotal.createCell(3);
        cell.setCellValue(factura.getTotal());
        cell.setCellStyle(tbodyStyle);
    }
    
}