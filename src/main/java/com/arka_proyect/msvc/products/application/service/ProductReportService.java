package com.arka_proyect.msvc.products.application.service;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.in.IProductReportUseCase;
import com.arka_proyect.msvc.products.domain.ports.in.IStockLogUseCase;
import com.arka_proyect.msvc.products.domain.ports.out.IProductRepositoryPort;
import com.arka_proyect.msvc.products.domain.ports.out.IReportPort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductReportService implements IProductReportUseCase {

    private final IStockLogUseCase stockLogUseCase;
    private final IReportPort reportPort;
    private final IProductRepositoryPort productRepositoryPort;
    private final Integer LOW_STOCK_THRESHOLD = 50;

    public ProductReportService(IStockLogUseCase stockLogUseCase,
                                IReportPort reportPort,
                                IProductRepositoryPort productRepositoryPort) {
        this.stockLogUseCase = stockLogUseCase;
        this.reportPort = reportPort;
        this.productRepositoryPort = productRepositoryPort;
    }


    @Override
    public List<Product> generateLowStockReport(Integer threshold) {
        Integer finalThreshold = (threshold != null) ? threshold : LOW_STOCK_THRESHOLD;
        return productRepositoryPort.findByStockLessThan(finalThreshold);
    }

    @Override
    public byte[] getLowStockProductsAndGenerateReport() {
        List<Product> lowStockProducts = productRepositoryPort.findByStockLessThan(LOW_STOCK_THRESHOLD);

        if (lowStockProducts.isEmpty()) {
            return new byte[0];
        }

        byte[] pdfBytes = reportPort.exportToPdf(lowStockProducts, "low_stock_report_manual");
        return pdfBytes;
    }


    @Override
    public String generateAllStockHistoryCsv() {

        List<StockLog> allHistory = stockLogUseCase
                .listAllStockHistory(Pageable.unpaged())
                .getContent();
        return reportPort.exportStockLogsToCsv(allHistory);
    }

}