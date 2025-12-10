package com.rokefeli.colmenares.api.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.rokefeli.colmenares.api.entity.DetalleVenta;
import com.rokefeli.colmenares.api.entity.Venta;
import com.rokefeli.colmenares.api.exception.ResourceNotFoundException;
import com.rokefeli.colmenares.api.repository.VentaRepository;
import com.rokefeli.colmenares.api.service.interfaces.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class ReporteServiceImpl implements ReporteService {

    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public byte[] generarReporteVentaPdf(Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta", idVenta));

        // 1. Crear documento y stream
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // 2. Estilos de Fuente
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.ORANGE);
            Font fontSubtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.DARK_GRAY);
            Font fontCuerpo = FontFactory.getFont(FontFactory.HELVETICA, 10, Color.BLACK);
            Font fontCabeceraTabla = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, Color.WHITE);

            // 3. Cabecera del Reporte
            Paragraph titulo = new Paragraph("COLMENARES ROKEFELI", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            Paragraph subtitulo = new Paragraph("Comprobante de Venta #" + venta.getId(), fontSubtitulo);
            subtitulo.setAlignment(Element.ALIGN_CENTER);
            subtitulo.setSpacingAfter(20);
            document.add(subtitulo);

            // 4. Datos del Cliente y Fecha
            document.add(new Paragraph("Cliente: " + venta.getUsuario().getNombres() + " " + venta.getUsuario().getApellidos(), fontCuerpo));
            document.add(new Paragraph("Email: " + venta.getUsuario().getEmail(), fontCuerpo));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            document.add(new Paragraph("Fecha: " + venta.getFecha().format(formatter), fontCuerpo));
            document.add(new Paragraph("Estado: " + venta.getEstado(), fontCuerpo));
            document.add(new Paragraph(" ")); // Espacio vacío

            // 5. Tabla de Productos
            PdfPTable table = new PdfPTable(4); // 4 columnas
            table.setWidthPercentage(100);
            table.setWidths(new float[]{4f, 1.5f, 2f, 2f}); // Anchos relativos
            table.setSpacingBefore(10);

            // Cabeceras de Tabla
            agregarCeldaCabecera(table, "Producto", fontCabeceraTabla);
            agregarCeldaCabecera(table, "Cant.", fontCabeceraTabla);
            agregarCeldaCabecera(table, "P. Unit", fontCabeceraTabla);
            agregarCeldaCabecera(table, "Subtotal", fontCabeceraTabla);

            // Filas de Productos
            for (DetalleVenta det : venta.getDetalles()) {
                table.addCell(new Phrase(det.getProducto().getNombre(), fontCuerpo));
                table.addCell(new Phrase(String.valueOf(det.getCantidad()), fontCuerpo));
                table.addCell(new Phrase("S/ " + det.getPrecioUnitario(), fontCuerpo));
                table.addCell(new Phrase("S/ " + det.getSubtotal(), fontCuerpo));
            }

            document.add(table);

            // 6. Total
            Paragraph total = new Paragraph("MONTO TOTAL: S/ " + venta.getMontoTotal(), fontSubtitulo);
            total.setAlignment(Element.ALIGN_RIGHT);
            total.setSpacingBefore(10);
            document.add(total);

            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar PDF", e);
        }

        return out.toByteArray();
    }

    private void agregarCeldaCabecera(PdfPTable table, String texto, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(texto, font));
        cell.setBackgroundColor(Color.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        table.addCell(cell);
    }
}