package com.example.demo.controller;

import com.example.demo.Model.Empresa;
import com.example.demo.Model.Usuario;
import com.example.demo.DTOs.EmpresaDTO;
import com.example.demo.repository.EmpresaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;

@RestController
@RequestMapping("/empresas")  // Define el prefijo de la URL para todos los endpoints de esta clase
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

   @CrossOrigin(origins = "http://localhost:4200/")
 

    @GetMapping
    public List<EmpresaDTO> getAllEmpresas() {
        List<Empresa> empresas = empresaRepository.findAll();

    // Convertir la lista de entidades a lista de DTOs
    return empresas.stream()
                   .map(EmpresaDTO::fromEmpresa)
                   .collect(Collectors.toList());
    }

    // Endpoint para obtener una empresa por ID
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaDTO> getEmpresaById(@PathVariable Integer id) {
        Optional<Empresa> empresaOpt = empresaRepository.findById(id);
        return empresaOpt.map(empresa -> ResponseEntity.ok(EmpresaDTO.fromEmpresa(empresa)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

 @PostMapping
public ResponseEntity<Empresa> createEmpresa(@RequestBody EmpresaDTO empresaDTO) {
    // Crear una nueva entidad Empresa a partir del DTO
    Empresa empresa = new Empresa();
    empresa.setClaveEmpresa(empresaDTO.getClaveEmpresa());
    empresa.setNombreEmpresa(empresaDTO.getNombreEmpresa());
    empresa.setActivo(empresaDTO.getActivo());
    //empresa.setFechaBaja(empresaDTO.getFechaBaja());

    // Aquí asignamos un Usuario con id 5 (o el valor dinámico que prefieras)
    Usuario usuario = new Usuario();
    usuario.setIdUsuario(5); // Establecer el idUsuario que deseas asignar
    
    // Asignamos el usuario a la empresa
    empresa.setUsuario(usuario);

    // Establecer las fechas automáticas (fechaAlta y fechaServidor)
    empresa.setFechaAlta(new Date());
    //empresa.setFechaServidor(new Date());

    // Guardar la empresa
    Empresa savedEmpresa = empresaRepository.save(empresa);

    // Retornamos la respuesta con el nuevo registro de Empresa
    return ResponseEntity.status(HttpStatus.CREATED).body(savedEmpresa);
}

    // Endpoint para actualizar una empresa existente
    @CrossOrigin(origins = "http://localhost:9091") 
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaDTO> updateEmpresa(@PathVariable Integer id, @RequestBody EmpresaDTO empresaDTO) {
        Optional<Empresa> existingEmpresa = empresaRepository.findById(id);
        if (existingEmpresa.isPresent()) {
            Empresa empresa = existingEmpresa.get();
             //Se actualizan los campos que deseemos
             empresa.setNombreEmpresa(empresaDTO.getNombreEmpresa());
             empresa.setClaveEmpresa(empresaDTO.getClaveEmpresa());
             empresa.setActivo(empresaDTO.getActivo());

             //Guardar empresa axtualizada
            Empresa updateEmpresa = empresaRepository.save(empresa);

            // Devuelve el DTO de la empresa
             EmpresaDTO responseDTO = EmpresaDTO.fromEmpresa(updateEmpresa);
            return ResponseEntity.ok(responseDTO);
           // return ResponseEntity.ok(updateEmpresa);
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint para eliminar una empresa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Integer id) {
        Optional<Empresa> existingEmpresa = empresaRepository.findById(id);
        if (existingEmpresa.isPresent()) {
            empresaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
