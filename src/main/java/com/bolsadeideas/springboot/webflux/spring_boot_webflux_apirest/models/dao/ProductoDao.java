package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Producto;

import reactor.core.publisher.Mono;

public interface ProductoDao extends ReactiveMongoRepository<Producto, String> {

    public Mono<Producto> findByNombre(String nombre);
}
