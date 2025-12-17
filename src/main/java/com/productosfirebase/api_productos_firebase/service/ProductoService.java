package com.productosfirebase.api_productos_firebase.service;

import com.productosfirebase.api_productos_firebase.dto.ProductoRequest;
import com.productosfirebase.api_productos_firebase.dto.ProductoResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

    Flux<ProductoResponse> listarProductos();
    Mono<ProductoResponse> obtenerProductoPorId(String id);
    Mono<ProductoResponse> crearProducto(ProductoRequest productoRequest);
    Mono<ProductoResponse> actualizarProducto(String id, ProductoRequest productoRequest);
    Mono<Boolean> eliminarProducto(String id);
}
