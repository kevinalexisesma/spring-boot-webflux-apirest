package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.service.ProductoService;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductoService productoService;

	@Test
	void listarTest() {
		client.get()
				.uri("/api/v2/productos")
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(Producto.class)
				.consumeWith(response -> {
					List<Producto> productos = response.getResponseBody();
					productos.forEach(p -> {
						System.out.println(p.getNombre());
					});

					Assertions.assertThat(productos.size() > 0).isTrue();
				});
		// .hasSize(9);
	}

	@Test
	void verTest() {
		Producto producto = productoService.findByNombre("TV Panasonic Pantalla LCD").block();
		client.get()
				.uri("/api/v2/productos/{id}", Collections.singletonMap("id", producto.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(Producto.class)
				.consumeWith(response -> {
					Producto productoResponse = response.getResponseBody();
					Assertions.assertThat(productoResponse.getId()).isNotEmpty();
					Assertions.assertThat(productoResponse.getId().length() > 0).isTrue();
					Assertions.assertThat(productoResponse.getNombre()).isEqualTo("TV Panasonic Pantalla LCD");
				});
		// .jsonPath("$.id").isNotEmpty()
		// .jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");
	}
}
