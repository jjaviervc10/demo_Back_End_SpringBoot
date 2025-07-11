package com.example.demo.controller;

import com.example.demo.DTOs.AuthResponse;
import com.example.demo.DTOs.UsuarioDTO;
import com.example.demo.Model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtTokenProvider;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.userdetails.memory.UserAttribute;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("api/usuarios")
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UsuarioController {

       private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    private  UsuarioRepository usuarioRepository;

     @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @CrossOrigin(origins = "http://localhost:9091") 
     @GetMapping
    public List<UsuarioDTO> getAllUsuarios() {
        // Obtener todos los usuarios del repositorio
        List<Usuario> usuarios = usuarioRepository.findAll();
        // Convertir la lista de usuarios a lista de UsuarioDTO y devolverla
        return usuarios.stream()
                       .map(UsuarioDTO::fromUsuario)  // Convertimos cada Usuario a UsuarioDTO
                       .collect(Collectors.toList());
    }

    // Endpoint para obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Integer id) {
         System.out.println("=== INVOCADO /api/usuarios/" + id);
          log.info("=== INVOCADO /api/usuarios/{}", id);

      
         Optional<Usuario> usuario = usuarioRepository.findById(id);
         System.out.println("Buscando usuario con ID: " + id);
        if (usuario.isPresent()) {
             System.out.println("Buscando usuario con ID: " + id);
            return ResponseEntity.ok(UsuarioDTO.fromUsuario(usuario.get()));
        } else {
             System.out.println("Buscando usuario con ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found
        }
    }

    // Endpoint para crear un nuevo usuario
    @PostMapping("/")
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody @Validated UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(usuarioDTO.getIdUsuario());
        usuario.setNombreCompleto(usuarioDTO.getNombreCompleto());
       // usuario.setCorreo(usuarioDTO.getCorreo());
       // usuario.setTelefono(usuarioDTO.getTelefono());

        // Guardamos el usuario en la base de datos
        Usuario savedUsuario = usuarioRepository.save(usuario);
        
        // Retornamos el usuario creado con un status HTTP 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDTO.fromUsuario(savedUsuario));
    }

    // Endpoint para actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable Integer id, @RequestBody @Validated UsuarioDTO usuarioDTO) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            Usuario usuario = existingUsuario.get();
            usuario.setNombreCompleto(usuarioDTO.getNombreCompleto());
           // usuario.setCorreo(usuarioDTO.getCorreo());
           // usuario.setTelefono(usuarioDTO.getTelefono());

            // Guardamos el usuario actualizado
            Usuario updatedUsuario = usuarioRepository.save(usuario);
            return ResponseEntity.ok(UsuarioDTO.fromUsuario(updatedUsuario));  // 200 OK
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // 404 Not Found
        }
    }

    // Endpoint para eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario.isPresent()) {
            usuarioRepository.deleteById(id);  // Eliminamos el usuario
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found
        }
    }

 

    @CrossOrigin(origins = "http://localhost:8081")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        // Buscar el usuario por nombre de usuario
    Optional<Usuario> usuarioOpt = usuarioRepository.findByUsuario(usuarioDTO.getUsuario());

    // Si no existe, devolver error
    if (!usuarioOpt.isPresent()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
    }

    Usuario usuario = usuarioOpt.get();

    // Verificar la contraseña (usando SHA-1)
    if (!verificarContraseña(usuarioDTO.getPass(), usuario.getPass())) {
        System.out.println("SHA1: " + hashPassword("HolaMundo05")); // puedes remover esto después
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
    }

    // Generar token JWT
    String token = jwtTokenProvider.createToken(
        usuario.getUsuario(),
        usuario.getIdUsuario(),
        usuario.getNombreCompleto()
    );

    // Construir DTO sin contraseña
    UsuarioDTO usuarioDTOResponse = new UsuarioDTO(
        usuario.getIdUsuario(),
        usuario.getNombreCompleto(),
        usuario.getUsuario(),
        null // no incluir la contraseña
    );

    // Retornar token + usuario
    return ResponseEntity.ok(new AuthResponse(token, usuarioDTOResponse));
}

// Verificar contraseña con SHA-1
private boolean verificarContraseña(String contraseñaIngresada, String contraseñaAlmacenada) {
    String hashedInput = hashPassword(contraseñaIngresada);
    return hashedInput.equals(contraseñaAlmacenada);
}

// Generar hash SHA-1
private String hashPassword(String password) {
    try {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] hashedBytes = sha1.digest(password.getBytes());

        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException("Error al generar el hash SHA-1", e);
    }
}

}
