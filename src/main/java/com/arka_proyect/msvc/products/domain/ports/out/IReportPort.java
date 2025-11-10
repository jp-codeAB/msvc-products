package com.arka_proyect.msvc.products.domain.ports.out;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import java.util.List;

public interface IReportPort {
    String exportStockLogsToCsv(List<StockLog> stockLogs);
    byte[] exportToPdf(List<Product> products, String fileNamePrefix);
    String savePdfReport(List<Product> products, String fileNamePrefix);
}