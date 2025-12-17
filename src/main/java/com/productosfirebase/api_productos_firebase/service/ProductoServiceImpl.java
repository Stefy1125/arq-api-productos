package com.productosfirebase.api_productos_firebase.service;

import com.productosfirebase.api_productos_firebase.dto.ProductoRequest;
import com.productosfirebase.api_productos_firebase.dto.ProductoResponse;
import com.productosfirebase.api_productos_firebase.model.Producto;
import com.productosfirebase.api_productos_firebase.repository.ProductoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;
import java.util.UUID;
import com.productosfirebase.api_productos_firebase.exception.ResourceNotFoundException;

@Service
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Flux<ProductoResponse> listarProductos() {
        return productoRepository.findAll().map(this::consultarProducto);
    }

    @Override
    public Mono<ProductoResponse> obtenerProductoPorId(String id) {
        return productoRepository.findById(id).switchIfEmpty(Mono.error(new ResourceNotFoundException("El producto con id: "+id+ " no se encuentra."))).map(this::consultarProducto);
    }

    @Override
    public Mono<ProductoResponse> crearProducto(ProductoRequest productoRequest) {
        
        Producto p = new Producto();
        // Puedes dejar id = null para que Firestore genere uno; aquí asignamos uno UUID
        p.setId(UUID.randomUUID().toString());
        p.setNombre(productoRequest.getNombre());
        p.setDescripcion(productoRequest.getDescripcion());
        p.setPrecio(productoRequest.getPrecio());
        return productoRepository.save(p).map(this::consultarProducto);
    }

    @Override
    public Mono<ProductoResponse> actualizarProducto(String id, ProductoRequest productoRequest) {

    return productoRepository.findById(id).switchIfEmpty(Mono.error(new ResourceNotFoundException("Producto no encontrado: " + id))).flatMap(p -> {
                p.setNombre(productoRequest.getNombre());
                p.setDescripcion(productoRequest.getDescripcion());
                p.setPrecio(productoRequest.getPrecio());
            return productoRepository.save(p);
        }).map(this::consultarProducto);
    }

   /* @Override
    public Mono<Boolean> eliminarProducto(String id) {
        return productoRepository.existsById(id).flatMap(exists -> exists
            ? productoRepository.deleteById(id)
            : Mono.error(new ResourceNotFoundException("Producto no encontrado: " + id)));
    }*/

            
    @Override
    public Mono<Boolean> eliminarProducto(String id) {
        return productoRepository.existsById(id)
            .flatMap(exists -> {
                if (exists) {
                    // deleteById devuelve Mono<Void>; lo convertimos a Mono<Boolean> con thenReturn(true)
                    return productoRepository.deleteById(id)
                            .thenReturn(true);
                } else {
                    // No existía → false (sin lanzar error)
                    return Mono.just(false);
                }
            });
    }

    private ProductoResponse consultarProducto(Producto p) {
        ProductoResponse r = new ProductoResponse();
        r.setId(p.getId());
        r.setNombre(p.getNombre());
        r.setDescripcion(p.getDescripcion());
        r.setPrecio(p.getPrecio());
        return r;
    }

}