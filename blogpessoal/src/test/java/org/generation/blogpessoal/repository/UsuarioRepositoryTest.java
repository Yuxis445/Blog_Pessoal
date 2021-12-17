package org.generation.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.generation.blogpessoal.model.Usuario;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeAll
    void start(){

        usuarioRepository.save(new Usuario(0L, "Joao Victor geng", "joao@joao.com.br", "1234567"));

        usuarioRepository.save(new Usuario(0L, "Guilherme maromba geng", "gui@gui.com.br", "1234567"));

        usuarioRepository.save(new Usuario(0L, "Jhonatan Filosofo", "jhon@jhon.com.br", "1234567"));

        usuarioRepository.save(new Usuario(0L, "Melquizedck geng", "melqui@melqui.com.br", "1234567"));
    }

    @Test
    @DisplayName("Retorna 1 usuario")
    public void deveRetornarUmUsuario(){

        Optional<Usuario> usuario = usuarioRepository.findByUsuario("joao@joao.com.br");
        assertTrue(usuario.get().getUsuario().equals("joao@joao.com.br"));
    }

    @Test
    @DisplayName("Retorna 3 usuario")
    public void deveRetornarTresUsuario(){

        List<Usuario> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("geng");
        assertEquals(3, listaDeUsuarios.size());
        assertTrue(listaDeUsuarios.get(0).getNome().equals("Joao Victor geng"));
        assertTrue(listaDeUsuarios.get(1).getNome().equals("Guilherme maromba geng"));
        assertTrue(listaDeUsuarios.get(2).getNome().equals("Melquizedck geng"));
    }


}
