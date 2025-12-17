package com.productosfirebase.api_productos_firebase.repository;

import org.springframework.stereotype.Repository;
import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.productosfirebase.api_productos_firebase.model.Producto;

@Repository
public interface ProductoRepository extends FirestoreReactiveRepository<Producto>{} 
