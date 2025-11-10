package com.arka_proyect.msvc.products.infrastructure.persistence.adapter;

import com.arka_proyect.msvc.products.domain.ports.in.IProductReportUseCase;
import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.ports.out.IReportPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LowStockReportScheduler {

    private final IProductReportUseCase productReportUseCase;
    private final IReportPort reportPort;

    public LowStockReportScheduler(
            @Qualifier("productReportService") IProductReportUseCase productReportUseCase,
            IReportPort reportPort) {
        this.productReportUseCase = productReportUseCase;
        this.reportPort = reportPort;
    }

    @Scheduled(cron = "0 0 9 ? * MON")
    public void generateWeeklyLowStockReport() {
        System.out.println("Iniciando Reporte Semanal de Bajo Stock...");

        List<Product> lowStockProducts = productReportUseCase.generateLowStockReport(null);

        if (lowStockProducts.isEmpty()) {
            System.out.println("No se encontraron productos con bajo stock.");
            return;
        }
        System.out.println("🚨 Productos con bajo stock encontrados: " + lowStockProducts.size());

        // 💡 CAMBIO: Generar PDF
        String pdfPath = reportPort.savePdfReport(lowStockProducts, "low_stock_report_weekly");

        System.out.println("Reporte Semanal de Bajo Stock Finalizado. Archivo PDF en: " + pdfPath);
    }
}