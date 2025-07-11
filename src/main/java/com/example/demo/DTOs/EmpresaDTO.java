package com.example.demo.DTOs;

import java.util.Date;
import com.example.demo.Model.Empresa;

public class EmpresaDTO {
    private Integer id;
    private String claveEmpresa;
    private String nombreEmpresa;
    private boolean activo;
    //private Date fechaBaja;
    private UsuarioDTO usuarioDTO; // Aquí usamos UsuarioDTO
  
    // Constructor
    public EmpresaDTO(Integer id,String claveEmpresa, String nombreEmpresa, boolean activo, UsuarioDTO usuarioDTO) {
      this.id = id ;
      this.claveEmpresa = claveEmpresa;
      this.nombreEmpresa = nombreEmpresa;
      this.activo = activo;
      //this.fechaBaja = fechaBaja;
      this.usuarioDTO = usuarioDTO;
     
    }
  
    // Getters y setters

    public Integer getIdEmpresa() {
        return id;
    }

    public void setIdEmpresa(Integer id) {
        this.id = id;
    }
    public String getClaveEmpresa() {
      return claveEmpresa;
    }
  
    public void setClaveEmpresa(String claveEmpresa) {
      this.claveEmpresa = claveEmpresa;
    }
  
    public String getNombreEmpresa() {
      return nombreEmpresa;
    }
  
    public void setNombreEmpresa(String nombreEmpresa) {
      this.nombreEmpresa = nombreEmpresa;
    }
  
    public boolean getActivo() {
      return activo;
    }
  
    public void setActivo(boolean activo) {
      this.activo = activo;
    }
  
   /*  public Date getFechaBaja() {
      return fechaBaja;
    }
  
    public void setFechaBaja(Date fechaBaja) {
      this.fechaBaja = fechaBaja;
    }*/
  
    public UsuarioDTO getUsuarioDTO() {
      return usuarioDTO;
    }
  
    public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
      this.usuarioDTO = usuarioDTO;
    }
  
    // Método para crear un EmpresaDTO a partir de una entidad Empresa
    public static EmpresaDTO fromEmpresa(Empresa empresa) {
      if (empresa != null && empresa.getUsuario() != null) {
        // Convertir Usuario a UsuarioDTO
        UsuarioDTO usuarioDTO = UsuarioDTO.fromUsuario(empresa.getUsuario());
        return new EmpresaDTO(
            empresa.getIdEmpresa(),
            empresa.getClaveEmpresa(),
            empresa.getNombreEmpresa(),
            empresa.getActivo(),
           // empresa.getFechaBaja(),
            
            usuarioDTO // Incluir UsuarioDTO en lugar de Usuario
        );
      }
      return null;
    }
}
