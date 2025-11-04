package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.controllers;

import java.net.URI;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.service.ProductoService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public Mono<ResponseEntity<Flux<Producto>>> listar() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(productoService.findAll()));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> editar(@PathVariable String id) {
        return productoService.findById(id)
                .map(producto -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(producto))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<Producto>> crear(@RequestBody Producto producto) {
        if (producto.getCreateAt() == null)
            producto.setCreateAt(new Date());

        return productoService.save(producto)
                .map(p -> ResponseEntity.created(URI.create("api/productos/".concat(p.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

}
