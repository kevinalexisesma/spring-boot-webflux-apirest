package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Categoria;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria, String> {

}
