package com.arka_proyect.msvc.products.infrastructure.mapper;

import com.arka_proyect.msvc.products.domain.model.StockChangeReason;
import com.arka_proyect.msvc.products.domain.model.StockLog;
import com.arka_proyect.msvc.products.infrastructure.persistence.entity.StockLogEntity;
import com.arka_proyect.msvc.products.infrastructure.web.dto.response.StockLogResponse;
import com.arka_proyect.msvc.products.shared.exception.BusinessException;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {IProductMapper.class})
public interface IStockLogMapper {

    // ENUM: Dominio (StockChangeReason) a Persistencia
    default StockLogEntity.Reason toEntityReason(StockChangeReason domainReason) {
        return switch (domainReason) {
            case ENTRY -> StockLogEntity.Reason.ENTRY;
            case OUT -> StockLogEntity.Reason.OUT;
            case SALE -> StockLogEntity.Reason.SALE;
            case RESERVATION -> StockLogEntity.Reason.RESERVATION;
            default -> throw new BusinessException("Unsupported StockChangeReason: " + domainReason + " for persistence.");
        };
    }

    //Entity -> Domain
    @Mappings({
            @Mapping(source = "reason", target = "operation")
    })
    StockLog toDomain(StockLogEntity entity);
    List<StockLog> toDomainList(List<StockLogEntity> entities);

    // Domain -> Entity
    @InheritInverseConfiguration(name = "toDomain")
    @Mappings({
            @Mapping(target = "reason", source = "operation"),
            @Mapping(target = "product.stockLogs", ignore = true)
    })
    StockLogEntity toEntity(StockLog domain);

    //Domain -> Response
    @Mappings({
            @Mapping(source = "product.id", target = "productId"),
            @Mapping(source = "newStock", target = "finalStock"),
            @Mapping(source = "operation", target = "reason"),
            @Mapping(source = "changeAt", target = "createAt"),
    })
    StockLogResponse toResponse(StockLog domain);
    List<StockLogResponse> toResponseList(List<StockLog> domains);
}