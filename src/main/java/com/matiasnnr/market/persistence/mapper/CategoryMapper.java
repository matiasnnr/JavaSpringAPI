package com.matiasnnr.market.persistence.mapper;

import com.matiasnnr.market.domain.Category;
import com.matiasnnr.market.persistence.entity.Categoria;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mappings({
            //donde sea idCategoria, lo mappee a donde sea categoryId
            //De esta forma estamos convirtiendo Categoria dentro de Category
            @Mapping(source = "idCategoria", target = "categoryId"),
            @Mapping(source = "descripcion", target = "category"),
            @Mapping(source = "estado", target = "active")
    })
    Category toCategory(Categoria categoria);

    //Hace el mapeo inverso al que ya tenemos, sin necesidad de definir @Mappings de nuevo
    @InheritInverseConfiguration
    //Esto se encarga de ignorar este atributo dentro de la clase
    @Mapping(target = "productos", ignore = true)
    Categoria toCategoria(Category category);
}
