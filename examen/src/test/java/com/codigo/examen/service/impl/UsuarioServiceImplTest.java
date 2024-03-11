package com.codigo.examen.service.impl;

import com.codigo.examen.entity.Usuario;
import com.codigo.examen.repository.RolRepository;
import com.codigo.examen.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private  UsuarioRepository usuarioRepository;
    @Mock
    private  RolRepository rolRepository;
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void SetUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createUsuario() {
        Usuario usuario  = new Usuario();
        usuario.setUsername("Isaac");
        Optional<Usuario> optionalUsuario = Optional.empty();

        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(optionalUsuario);
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> responseEntity = usuarioService.createUsuario(usuario);
        assertEquals(ResponseEntity.ok(usuario), responseEntity);
    }


    @Test
    void getUsuarioById() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> responseEntity = usuarioService.getUsuarioById(usuario.getIdUsuario());

        assertEquals(ResponseEntity.ok(usuario), responseEntity);
    }

    @Test
    void testUpdateUsuario() {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setUsername("Isaac1234");

        Usuario existUsers = new Usuario();
        existUsers.setIdUsuario(1L);
        existUsers.setUsername("ExistUser");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existUsers));
        when(usuarioRepository.findByUsername(usuario.getUsername())).thenReturn(Optional.empty());
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> responseEntity = usuarioService.updateUsuario(1L, usuario);

        assertEquals(ResponseEntity.ok(usuario), responseEntity);
    }

    @Test
    void testDeleteUsuarioWhenUserExists() {
        Long userId = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(userId);

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> responseEntity = usuarioService.deleteUsuario(userId);

        assertEquals(ResponseEntity.noContent().build(), responseEntity);
        verify(usuarioRepository, times(1)).delete(usuario);
    }

    @Test
    void DeleteUsuarioWhenUserDoesNotExist() {
        Long userId = 1L;

        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> responseEntity = usuarioService.deleteUsuario(userId);

        assertEquals(ResponseEntity.notFound().build(), responseEntity);
        verify(usuarioRepository, never()).delete(any());
    }


}