package com.matiasnnr.market.web.controller;

import com.matiasnnr.market.domain.Product;
import com.matiasnnr.market.domain.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

//Garantiza a Spring que esta clase va a ser un controlador de una API REST
@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    @ApiOperation("Get all market products") //Describe en Swagger que hace nuestra api
    @ApiResponse(code = 200, message = "OK") //Respuesta en Swagger
    public ResponseEntity<?> getAll() {
        HashMap<String, List<Product>> productHashMap = new HashMap<String, List<Product>>();
        productHashMap.put("productList", productService.getAll());
        return ResponseEntity.ok(productHashMap);
        //return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation("Search a product with an Id") //Describe en Swagger que hace nuestra api
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Product not found"),
    }) //Respuesta en Swagger
    public ResponseEntity<Product> getProduct(
            //@ApiParam con esta anotación Swagger describe lo que va en la casilla de búsqueda,
            //lo muestra como *required y también muestra el productId 7 como ejemplo.
            @ApiParam(value = "The Id of the product", required = true, example = "7")
            @PathVariable("id") int productId) {
        return productService.getProduct(productId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getByCategory(@PathVariable("categoryId") int categoryId) {
        return productService.getByCategory(categoryId)
                .map(products -> new ResponseEntity<>(products, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("save")
    public ResponseEntity<Product> save(@RequestBody Product product) {
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") int productId) {
        if (productService.delete(productId)){
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
