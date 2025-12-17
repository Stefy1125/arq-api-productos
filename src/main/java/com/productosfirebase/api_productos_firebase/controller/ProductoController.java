package com.productosfirebase.api_productos_firebase.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.productosfirebase.api_productos_firebase.dto.ProductoRequest;
import com.productosfirebase.api_productos_firebase.dto.ProductoResponse;
import com.productosfirebase.api_productos_firebase.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    
    private final ProductoService service;

    public ProductoController(ProductoService service) { this.service = service; }

    @GetMapping("/listarProductos")
    public Flux<ProductoResponse> listarProductos() { return service.listarProductos(); }

    @GetMapping("/obtenerProducto/{id}")
    public Mono<ResponseEntity<ProductoResponse>> obtenerProducto(@PathVariable String id) {
        return service.obtenerProductoPorId(id).map(ResponseEntity::ok);
    }

    @PostMapping("/crearProducto")
    public Mono<ResponseEntity<ProductoResponse>> crearProducto(@Valid @RequestBody ProductoRequest req) {
        return service.crearProducto(req).map(r -> ResponseEntity.status(201).body(r));
    }

    @PutMapping("/actualizarProducto/{id}")
    public Mono<ResponseEntity<ProductoResponse>> actualizarProducto(@PathVariable String id, @Valid @RequestBody ProductoRequest req) {
        return service.actualizarProducto(id, req).map(ResponseEntity::ok);
    }
    
    @Operation(summary = "Eliminar producto por id")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })

    /* 
    @DeleteMapping("/eliminarProducto/{id}")
    public Mono<ResponseEntity<Void>> eliminarProducto(@PathVariable String id) {
        return service.eliminarProducto(id).thenReturn(ResponseEntity.noContent().build());
    }*/

    
    @DeleteMapping("/eliminarProducto/{id}")
    public Mono<ResponseEntity<Map<String, Object>>> eliminarProducto(@PathVariable String id) {
        
        return service.eliminarProducto(id)
            .map(isDeleted -> {
                if (isDeleted) {
                    return ResponseEntity.ok(
                        Map.<String, Object>of(
                            "message", "Producto eliminado",
                            "id", id,
                            "timestamp", Instant.now().toString()
                        )
                    );
                } else {
                    return ResponseEntity.status(404).body(
                        Map.<String, Object>of(
                            "error", "Not Found",
                            "message", "El producto no existe",
                            "id", id,
                            "timestamp", Instant.now().toString()
                        )
                    );
                }
            });
    }

}
