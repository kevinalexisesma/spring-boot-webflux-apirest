package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Categoria;

import reactor.core.publisher.Mono;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {

    public Mono<Categoria> findCategoriaByNombre(String nombre);
}
