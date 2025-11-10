package com.arka_proyect.msvc.products.domain.ports.in;

import com.arka_proyect.msvc.products.domain.model.Product;

import java.util.List;
import java.util.Map;

public interface IProductReportUseCase {
    List<Product> generateLowStockReport(Integer threshold);
    byte[] getLowStockProductsAndGenerateReport();
    String generateAllStockHistoryCsv();
}