package com.arka_proyect.msvc.products.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockLogEntity {

    public enum Reason {
        ENTRY,
        OUT,
        SALE,
        RESERVATION
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ProductEntity product;
    @Column(name = "quantity_change")
    private Integer quantityChange;
    @Column(name = "new_stock")
    private Integer newStock;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "change_at")
    private LocalDateTime changeAt;
    @Enumerated(EnumType.STRING)
    private Reason reason;
}