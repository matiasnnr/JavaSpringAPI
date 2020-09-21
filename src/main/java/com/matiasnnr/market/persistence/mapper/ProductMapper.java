package com.matiasnnr.market.persistence.mapper;

import com.matiasnnr.market.domain.Product;
import com.matiasnnr.market.persistence.entity.Producto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

//De esta forma cuando vaya a convertir
//@Mapping(source = "categoria", target = "category")
//Tiene que usar uses = {CategoryMapper.class}
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface ProductMapper {
    @Mappings({
            @Mapping(source = "idProducto", target = "productId"),
            @Mapping(source = "nombre", target = "name"),
            @Mapping(source = "idCategoria", target = "categoryId"),
            @Mapping(source = "precioVenta", target = "price"),
            @Mapping(source = "cantidadStock", target = "stock"),
            @Mapping(source = "estado", target = "active"),
            @Mapping(source = "categoria", target = "category")
    })
    Product toProduct(Producto producto);
    //No es necesario asignarle un mappings(). MapStruct se va a encargar de que la clase sepa que
    //esta linea se debe comportar de la misma manera que la de arriba, ya que es el mismo tipo de conversión
    List<Product> toProducts(List<Producto> productos);

    @InheritInverseConfiguration
    //Esto se encarga de ignorar este atributo dentro de la clase
    @Mapping(target = "codigoBarras", ignore = true)
    Producto toProducto(Product product);
}
