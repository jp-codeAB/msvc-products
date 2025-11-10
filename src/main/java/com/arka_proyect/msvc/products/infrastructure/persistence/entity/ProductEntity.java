package com.arka_proyect.msvc.products.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products")
@Data
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer stock;
    private Double price;
    private String status;
    @Column(name = "create_at")
    private LocalDate createAt;
    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;
    @Transient
    private Integer currentPort;
    @OneToMany(
            mappedBy = "product"
    )
    private List<StockLogEntity> stockLogs;
}
