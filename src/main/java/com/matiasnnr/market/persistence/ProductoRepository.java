package com.matiasnnr.market.persistence;

import com.matiasnnr.market.domain.Product;
import com.matiasnnr.market.domain.repository.ProductRepository;
import com.matiasnnr.market.persistence.crud.ProductoCrudRepository;
import com.matiasnnr.market.persistence.entity.Producto;
import com.matiasnnr.market.persistence.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//Ahora ProductoRepository está enfocado al dominio, en vez de a la tabla puntual de la db productos
//Esta clase se encarga de conectarse a la db y mappear los valores leídos para mostrarlos en la API
//o mappearlos para ingresarlos a la db
@Repository
public class ProductoRepository implements ProductRepository {
    @Autowired
    private ProductoCrudRepository productoCrudRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public List<Product> getAll(){
        List<Producto> productos = (List<Producto>) productoCrudRepository.findAll();
        return productMapper.toProducts(productos);
    }

    @Override
    public Optional<List<Product>> getByCategory(int categoryId) {
        List<Producto> productos = productoCrudRepository.findByIdCategoriaOrderByNombreAsc(categoryId);
        return Optional.of(productMapper.toProducts(productos));
    }

    @Override
    public Optional<List<Product>> getScarseProductsOnStock(int quantity) {
        Optional<List<Producto>> productos = productoCrudRepository
                                                .findByCantidadStockLessThanAndEstado(quantity, true);
        return productos.map(prods -> productMapper.toProducts(prods));
    }

    @Override
    public Optional<Product> getProduct(int productId) {
        return productoCrudRepository.findById(productId).map(producto -> productMapper.toProduct(producto));
    }

    @Override
    public Product save(Product product) {
        Producto producto = productMapper.toProducto(product);
        return productMapper.toProduct(productoCrudRepository.save(producto));
    }

    @Override
    public void delete(int productId){
        productoCrudRepository.deleteById(productId);
    }
}
