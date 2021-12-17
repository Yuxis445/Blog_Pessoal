package org.generation.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.generation.blogpessoal.service.UsuarioService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    @Order(1)
    @DisplayName("Cadastrar Um Usuario")
    public void deveCriarUmUsuario() {
        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
            "Rodrigo Antunes","antunes@antunes.com.br", "123456"));

            ResponseEntity<Usuario> resposta = testRestTemplate
                .exchange("/usuarios/cadstrar", HttpMethod.POST, requisicao, Usuario.class);

            assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
            assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
            assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
    }

    @Test
    @Order(2)
    @DisplayName("Nao deve permitir duplicacao de Usuario")
    public void naoDeveDuplicarUsuario() {

        usuarioService.cadastrarUsuario(new Usuario(0L,
            "Maria da Silva", "maria_silva@email.com", "1223456"));

        HttpEntity<Usuario> requisicao = new HttpEntity<Usuario>(new Usuario(0L,
            "Maria da Silva", "maria_silva@email.com", "123456"));

        ResponseEntity<Usuario> resposta = testRestTemplate
            .exchange("/usuarios/cadastrar", HttpMethod.POST, requisicao, Usuario.class);

        assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
    }

    @Test
    @Order(3)
    @DisplayName("Alterar um Usuario")
    public void deveAtualizarUmUsuario() {
        Optional<Usuario> usuarioCreate = usuarioService.cadastrarUsuario(new Usuario(0L,
            "Juliana Flores", "julianaflores@email.com", "juliana"));

        Usuario usuarioUpdate = new Usuario(usuarioCreate.get().getId(),
        "Juliana Flores", "julianaflores@email.com", "juliana");

        HttpEntity<Usuario> requisacao = new HttpEntity<Usuario>(usuarioUpdate);

        ResponseEntity<Usuario> resposta = testRestTemplate.withBasicAuth("root", "root")
            .exchange("/usuarios/atualizar", HttpMethod.PUT,requisacao, Usuario.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
        assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
    }

    @Test
    @Order(3)
    @DisplayName("Listar todos os Usuarios")
    public void deveMostrarTodosUsuarios() {
        usuarioService.cadastrarUsuario(new Usuario(0L,
            "Sabrina Sanches", "sabrina@sabrina.com", "123456"));

        usuarioService.cadastrarUsuario(new Usuario(0L,
            "Ricardo", "ricardo@ricardo.com", "12345678"));

        ResponseEntity<String> resposta = testRestTemplate
            .withBasicAuth("root", "root")
            .exchange("usuarios/all", HttpMethod.GET, null, String.class);

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
    }
}
