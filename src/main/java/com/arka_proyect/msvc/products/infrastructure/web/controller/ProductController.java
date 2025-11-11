package com.arka_proyect.msvc.products.infrastructure.web.controller;

import com.arka_proyect.msvc.products.domain.model.Product;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.domain.ports.in.*;
import com.arka_proyect.msvc.products.infrastructure.mapper.IProductMapper;
import com.arka_proyect.msvc.products.infrastructure.mapper.IStockLogMapper;
import com.arka_proyect.msvc.products.infrastructure.security.AuthUser;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.ProductRequest;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.StockItemRequest;
import com.arka_proyect.msvc.products.infrastructure.web.dto.request.StockUpdateRequest;
import com.arka_proyect.msvc.products.infrastructure.web.dto.response.ProductResponse;
import com.arka_proyect.msvc.products.infrastructure.web.dto.response.StockLogResponse;
import com.arka_proyect.msvc.products.shared.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    private final IProductUseCase productCrudUseCase;
    private final IProductStockManagementUseCase productStockManagementUseCase;
    private final IProductReportUseCase productReportUseCase;
    private final IStockLogUseCase stockLogUseCase;
    private final IProductMapper productMapper;
    private final IStockLogMapper stockLogMapper;

    public ProductController(IProductUseCase productCrudUseCase,
                             IProductStockManagementUseCase productStockManagementUseCase,
                             @Qualifier("productReportService") IProductReportUseCase productReportUseCase,
                             IStockLogUseCase stockLogUseCase,
                             IProductMapper productMapper,
                             IStockLogMapper stockLogMapper) {
        this.productCrudUseCase = productCrudUseCase;
        this.productStockManagementUseCase = productStockManagementUseCase;
        this.productReportUseCase = productReportUseCase;
        this.stockLogUseCase = stockLogUseCase;
        this.productMapper = productMapper;
        this.stockLogMapper = stockLogMapper;
    }


    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<ProductResponse>> listAll() {
        List<ProductResponse> response = productCrudUseCase.listAllProduct().stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getById(@PathVariable Long id) {
        return productCrudUseCase.getProduct(id)
                .map(productMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        productCrudUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        Product product = productMapper.toDomain(request);
        ProductResponse response = productMapper.toResponse(productCrudUseCase.createProduct(product));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                Map.of(
                        "message", "Producto '" + response.getName() + "' registrado exitosamente.",
                        "product", response
                )
        );
    }

    @GetMapping("/prices")
    public ResponseEntity<Map<Long, Double>> getProductPrices(
            @RequestParam(name = "productIds") List<Long> productIds) {
        Map<Long, Double> prices = productCrudUseCase.getProductPrices(productIds);
        return ResponseEntity.ok(prices);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        Product product = productMapper.toDomain(request);
        product.setId(id);
        return productCrudUseCase.updateProduct(product)
                .map(productMapper::toResponse)
                .map(response -> ResponseEntity.ok(
                        Map.of(
                                "message", "Producto '" + response.getName() + "' actualizado exitosamente.",
                                "product", response
                        )
                ))
                .orElseThrow(() -> new ProductNotFoundException("Product not found with ID: " + id));
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        List<ProductResponse> response = productCrudUseCase.getProductsByCategory(categoryId).stream()
                .map(productMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequest request,
            @AuthenticationPrincipal AuthUser authUser) {
        return productStockManagementUseCase.updateStock(id, request.getQuantity(), authUser.getId())
                .map(productMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ProductNotFoundException("Product not found for stock update with ID: " + id));

    }

    @PostMapping("/reserve-stock")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public ResponseEntity<Boolean> reserveStock(@RequestBody List<StockItemRequest> items) {
        boolean success = productStockManagementUseCase.reserveMultipleStock(items);
        if (success) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(false);
        }
    }

    @PostMapping("/reverse-stock")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_CLIENT')")
    public void reverseStock(
            @RequestBody List<StockItemRequest> items) {
        productStockManagementUseCase.reverseMultipleStock(items);
    }

    @GetMapping("/{id}/stock-history")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<StockLogResponse>> getStockHistoryByProduct(@PathVariable Long id) {

        List<StockLog> domainHistory = stockLogUseCase.getHistoryByProductId(id);
        List<StockLogResponse> history = stockLogMapper.toResponseList(domainHistory);

        if (history.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(history);
    }

    @GetMapping("/stock-history")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Page<StockLogResponse>> getAllStockHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<StockLog> domainPage = stockLogUseCase.listAllStockHistory(pageable);
        Page<StockLogResponse> historyPage = domainPage.map(stockLogMapper::toResponse);
        return ResponseEntity.ok(historyPage);
    }

    @GetMapping(value = "/stock-history/export-csv", produces = "text/csv")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> exportAllStockHistoryToCsv() {
        String csvContent = productReportUseCase.generateAllStockHistoryCsv();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"stock_history_report.csv\"")
                .body(csvContent);
    }

    @GetMapping(value = "/reports/low-stock/pdf")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<byte[]> generateLowStockReportPdf() {
        byte[] pdfBytes = productReportUseCase.getLowStockProductsAndGenerateReport();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "low_stock_report_" + System.currentTimeMillis() + ".pdf";
        headers.setContentDispositionFormData("attachment", filename);
        headers.setContentLength(pdfBytes.length);
        return new ResponseEntity<>(
                pdfBytes,
                headers,
                HttpStatus.OK
        );
    }

    @PostMapping("/convert-to-sale")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void convertReservationToSale(@RequestBody List<StockItemRequest> items){
        productStockManagementUseCase.convertReservationToSale(items);
    }
}