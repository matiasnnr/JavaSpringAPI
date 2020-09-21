package com.matiasnnr.market.persistence.mapper;

import com.matiasnnr.market.domain.service.PurchaseItem;
import com.matiasnnr.market.persistence.entity.ComprasProducto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface PurchaseItemMapper {

    @Mappings({
         @Mapping(source = "id.idProducto", target = "productId"),
         @Mapping(source = "cantidad", target = "quantity"),
         //@Mapping(source = "total", target = "total"), Como son iguales se pueden omitir
         @Mapping(source = "estado", target = "active")
    })
    PurchaseItem toPurchaseItem(ComprasProducto producto);

    @InheritInverseConfiguration
    @Mappings({ //Ignoramos estos atributos
            @Mapping(target = "compra", ignore = true),
            @Mapping(target = "producto", ignore = true),
            @Mapping(target = "id.idCompra", ignore = true)
    })
    ComprasProducto toComprasProducto(PurchaseItem item);
}
