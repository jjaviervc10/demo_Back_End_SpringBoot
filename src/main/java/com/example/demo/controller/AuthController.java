package com.example.demo.controller;

import com.example.demo.DTOs.AuthResponse;
import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.Model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtTokenProvider;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // o cambia por tu frontend
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO loginDto) {
    Optional<Usuario> userOpt = usuarioRepository.findByUsuario(loginDto.getUsuario());

    if (userOpt.isEmpty() || !userOpt.get().getPass().equals(loginDto.getPass())) {
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    Usuario user = userOpt.get();
    String token = jwtTokenProvider.createToken(
        user.getUsuario(),
        user.getIdUsuario(),
        user.getNombreCompleto()
    );

    UsuarioDTO userDto = new UsuarioDTO();
    userDto.setIdUsuario(user.getIdUsuario());
    userDto.setUsuario(user.getUsuario());
    userDto.setNombreCompleto(user.getNombreCompleto());

    return ResponseEntity.ok(new AuthResponse(token, userDto));
}
}