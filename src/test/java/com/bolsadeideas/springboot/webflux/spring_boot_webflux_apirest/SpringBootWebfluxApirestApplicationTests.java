package com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Categoria;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.documents.Producto;
import com.bolsadeideas.springboot.webflux.spring_boot_webflux_apirest.models.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringBootWebfluxApirestApplicationTests {

	@Autowired
	private WebTestClient client;

	@Autowired
	private ProductoService productoService;

	@Value("${config.base.endpoint}")
	private String url;

	@Test
	void listarTest() {
		client.get()
				.uri(url)
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
				.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
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

	@Test
	void crearTest() {

		Categoria cateoria = productoService.findCategoriaByNombre("Muebles").block();

		Producto producto = new Producto("Mesa Comedor", 100.00, cateoria);
		client.post().uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(producto), Producto.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.producto.id").isNotEmpty()
				.jsonPath("$.producto.nombre").isEqualTo("Mesa Comedor")
				.jsonPath("$.producto.categoria.nombre").isEqualTo("Muebles");
	}

	@Test
	void crear2Test() {

		Categoria cateoria = productoService.findCategoriaByNombre("Muebles").block();

		Producto producto = new Producto("Mesa Comedor", 100.00, cateoria);
		client.post().uri(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(producto), Producto.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody(new ParameterizedTypeReference<LinkedHashMap<String, Object>>() {
				})
				.consumeWith(response -> {
					Object objectResponse = response.getResponseBody().get("producto");
					Producto productoResponse = new ObjectMapper().convertValue(objectResponse, Producto.class);
					Assertions.assertThat(productoResponse.getId()).isNotEmpty();
					Assertions.assertThat(productoResponse.getNombre()).isEqualTo("Mesa Comedor");
					Assertions.assertThat(productoResponse.getCategoria().getNombre()).isEqualTo("Muebles");
				});
	}

	@Test
	void editarTest() {
		Producto producto = productoService.findByNombre("Sony Notebook").block();

		Categoria cateoriaEditada = productoService.findCategoriaByNombre("Electrónico").block();
		Producto productoEditado = new Producto("Asus Notebook", 700.00, cateoriaEditada);

		client.put().uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.body(Mono.just(productoEditado), Producto.class)
				.exchange()
				.expectStatus().isCreated()
				.expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBody()
				.jsonPath("$.id").isNotEmpty()
				.jsonPath("$.nombre").isEqualTo("Asus Notebook")
				.jsonPath("$.categoria.nombre").isEqualTo("Electrónico");
		// .consumeWith(response -> {
		// Producto productoResponse = response.getResponseBody();
		// Assertions.assertThat(productoResponse.getId()).isNotEmpty();
		// Assertions.assertThat(productoResponse.getNombre()).isEqualTo("Asus
		// Notebook");
		// Assertions.assertThat(productoResponse.getCategoria().getNombre()).isEqualTo("Electrónico");
		// });
	}

	@Test
	void eliminar() {
		Producto producto = productoService.findByNombre("Mica Cómoda 5 Cajones").block();

		client.delete()
				.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
				.exchange()
				.expectStatus().isNoContent()
				.expectBody().isEmpty();

		client.get()
				.uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
				.exchange()
				.expectStatus().isNotFound()
				.expectBody().isEmpty();
	}
}
