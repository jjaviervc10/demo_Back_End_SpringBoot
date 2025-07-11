package com.example.demo.controller;
import com.example.demo.DTOs.EmpresaDTO;
import com.example.demo.DTOs.SucursalDTO;
import com.example.demo.Model.Empresa;
import com.example.demo.Model.Sucursal;
import com.example.demo.Model.Usuario;
import com.example.demo.repository.SucursalRepository;
import com.example.demo.service.SucursalService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/sucursales")
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class SucursalController {
    
    @Autowired
    private SucursalRepository sucursalRepository;

    @CrossOrigin(origins = "http://localhost:4200/")
    @GetMapping
    public List<SucursalDTO> getAllSucursals(){
        List<Sucursal> sucursales = sucursalRepository.findAll();

        //Convertimos la lista de entidades a lista DTOs
       return sucursales.stream()
                        .map(SucursalDTO::fromSucursal)
                        .collect(Collectors.toList());
    }    

   

    @GetMapping("/{id}")
public ResponseEntity<SucursalDTO> getSucursalById(@PathVariable Integer id) {
    Optional<Sucursal> sucursalOpt = sucursalRepository.findById(id);
    return sucursalOpt.map(sucursal -> ResponseEntity.ok(SucursalDTO.fromSucursal(sucursal)))
                      .orElseGet(() -> ResponseEntity.notFound().build());
}


    @PostMapping
    public ResponseEntity<Sucursal> createSucursal(@RequestBody SucursalDTO sucursalDTO){
        //Crear una nueva entiddad Sucursal apartir del DTO
        Sucursal sucursal = new Sucursal();
        sucursal.setNombreSucursal(sucursalDTO.getNombreSucursal());
        sucursal.setCiudad(sucursalDTO.getCiudad());
        sucursal.setActivo(sucursalDTO.getActivo());
        sucursal.setEstado(sucursalDTO.getEstado());

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(5);

        sucursal.setUsuario(usuario);
        
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(285);
        sucursal.setEmpresa(empresa);

        sucursal.setFechaAlta(new Date());
       // sucursal.setFechaServidor(new Date());

        Sucursal savedSucursal = sucursalRepository.save(sucursal);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSucursal);
        
    }

    //Endpoint para actualizar la Sucursal
   
     @PutMapping("/{id}")
     public ResponseEntity<SucursalDTO> updateSucursal(@PathVariable Integer id, @RequestBody SucursalDTO sucursalDTO){
        Optional<Sucursal> existingSucursal = sucursalRepository.findById(id);
        if(existingSucursal.isPresent()){
            Sucursal sucursal = existingSucursal.get();
            //Actualizando los campos que se desean
            sucursal.setNombreSucursal(sucursalDTO.getNombreSucursal());
            sucursal.setEstado(sucursalDTO.getEstado());
            sucursal.setCiudad(sucursalDTO.getCiudad());
            sucursal.setActivo(sucursalDTO.getActivo());
           

            //Salvamos los valores actualizados
            Sucursal  updateSucursal = sucursalRepository.save(sucursal);
            
            //Devolvemos el  DTO  de sucursal
            SucursalDTO responseDTO = SucursalDTO.fromSucursal(updateSucursal);
            return ResponseEntity.ok(responseDTO);
        }
        return ResponseEntity.notFound().build();
     }



    //Endpoint para eliminar una empresa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpresa(@PathVariable Integer id){
        Optional<Sucursal> existingSucursal = sucursalRepository.findById(id);
        if(existingSucursal.isPresent()){
            sucursalRepository.deleteById(id);
             return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
   

