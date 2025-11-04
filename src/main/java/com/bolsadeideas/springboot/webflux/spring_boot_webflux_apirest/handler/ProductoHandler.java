package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.service.ProductoService;

import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

    @Autowired
    private ProductoService productoService;

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productoService.findAll(), Producto.class);
    }
}
