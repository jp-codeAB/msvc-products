package com.arka_proyect.msvc.products.infrastructure.persistence.adapter;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.out.IReportPort;
import com.lowagie.text.Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;

@Component
public class ReportAdapter implements IReportPort {

    private final String baseReportPath;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public ReportAdapter(@Value("${app.report.base-path:./reports/inventory/}") String baseReportPath) {
        this.baseReportPath = baseReportPath;
    }

    private void ensureDirectoryExists() {
        new File(this.baseReportPath).mkdirs();
    }

    @Override
    public String savePdfReport(List<Product> products, String fileNamePrefix) {
        ensureDirectoryExists();
        String timestamp = LocalDateTime.now().format(formatter);
        String fullPath = baseReportPath + fileNamePrefix + "_" + timestamp + ".pdf";
        String content = createPdfContent(products);

        try (FileWriter writer = new FileWriter(fullPath)) {
            writer.write(content);
            System.out.println("💾 Reporte guardado en disco: " + fullPath);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de reporte programado: " + e.getMessage());
            return "ERROR al guardar el reporte: " + e.getMessage();
        }

        return fullPath;
    }

    @Override
    public byte[] exportToPdf(List<Product> products, String fileNamePrefix) {

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, Color.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.BLUE);

            Paragraph title = new Paragraph("REPORTE DE PRODUCTOS CON BAJO STOCK", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph("Fecha de Generación: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
            document.add(new Paragraph("Productos encontrados: " + products.size()));
            document.add(Chunk.NEWLINE);

            Table table = new Table(4);
            table.setPadding(5);

            table.addCell(new Paragraph("ID", headerFont));
            table.addCell(new Paragraph("Nombre", headerFont));
            table.addCell(new Paragraph("Stock Actual", headerFont));
            table.addCell(new Paragraph("Precio", headerFont));
            for (Product product : products) {
                table.addCell(String.valueOf(product.getId()));
                table.addCell(product.getName());
                table.addCell(String.valueOf(product.getStock()));
                table.addCell(String.format("%.2f", product.getPrice()));
            }

            document.add(table);

            document.close();

            System.out.println("✅ Generación binaria del PDF en memoria completada.");

            return outputStream.toByteArray();

        } catch (DocumentException | IOException e) {
            System.err.println("Error fatal al generar el PDF: " + e.getMessage());
            return new byte[0];
        }
    }

    private String createPdfContent(List<Product> products) {
        String title = "REPORTE DE PRODUCTOS CON BAJO STOCK";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        StringBuilder pdfContent = new StringBuilder();
        pdfContent.append("==================================================\n");
        pdfContent.append(String.format("TÍTULO: %s\n", title));
        pdfContent.append(String.format("FECHA DE GENERACIÓN: %s\n", date));
        pdfContent.append("==================================================\n\n");
        pdfContent.append(String.format("Se encontraron %d productos con stock inferior al umbral.\n\n", products.size()));

        pdfContent.append("ID | Nombre | Stock Actual | Precio\n");
        pdfContent.append("---|--------|--------------|-------\n");

        for (Product product : products) {
            pdfContent.append(String.format("%d | %s | %d | %.2f\n",
                    product.getId(),
                    product.getName(),
                    product.getStock(),
                    product.getPrice()
            ));
        }
        return pdfContent.toString();
    }

    @Override
    public String exportStockLogsToCsv(List<StockLog> stockLogs) {

        StringBuilder csvContent = new StringBuilder();

        csvContent.append("Log ID,Product ID,Product Name,Quantity Change,New Stock,User ID,Operation,Change Date\n");

        for (StockLog log : stockLogs) {
            String changeAt = log.getChangeAt() != null
                    ? log.getChangeAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    : "";

            csvContent.append(String.format("%d,%d,\"%s\",%d,%d,%d,%s,%s\n",
                    log.getId(),
                    log.getProduct().getId(),
                    log.getProduct().getName().replace("\"", "\"\""),
                    log.getQuantityChange(),
                    log.getNewStock(),
                    log.getUserId(),
                    log.getOperation(),
                    changeAt
            ));
        }

        return csvContent.toString();
    }

}