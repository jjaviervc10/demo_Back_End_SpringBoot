package com.example.demo.Model;
//import java.sql.Date;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
//import com.nimbusds.jose.shaded.json.annotate.JsonIgnore;

import jakarta.persistence.*; // Importaciones de JPA

@Entity
//@Table(name = "cEmpresa")
@Table(name = "\"cEmpresa\"", schema = "public")


public class Empresa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "id")
    private Integer id;
    @Basic(optional = false)  
    @Size(min = 1, max = 3)
     @Column(name = "\"claveEmpresa\"")
    //@Column(name = "claveEmpresa")
    private String claveEmpresa;
    @Basic(optional = false)   
    @Size(min = 1, max = 50)
    @Column(name = "\"nombreEmpresa\"")
    private String nombreEmpresa;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @Basic(optional = false)
    @Column(name = "\"fechaAlta\"")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaAlta;
   /*  @Column(name = "fechaBaja")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaBaja;
    @Basic(optional = false)
    @Column(name = "fechaServidor")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaServidor;*/
    // Getters y setters
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "\"idUsuario\"", referencedColumnName = "\"idUsuario\"",nullable = false)
    @JsonManagedReference // Evita la recursividad aquí
    private Usuario usuario;  // Esta es la propiedad que debe existir en la entidad Empresa

    @OneToMany(mappedBy ="empresa", fetch = FetchType.LAZY)
    @JsonBackReference  // Evita la recursividad serializando solo la "parte manejada"
    private List<Sucursal> sucursales;

    public Empresa(){}

    public Empresa(Integer id){
        this.id = id;
    }

    public Empresa(Integer id, String claveEmpresa, String nombreEmpresa,
                   boolean activo, Date fechaAlta){

                    this.id = id;
                    this.claveEmpresa = claveEmpresa;
                    this.nombreEmpresa = nombreEmpresa;
                    this.activo = activo;
                    this.fechaAlta = fechaAlta;
                   // this.fechaServidor = fechaServidor;
                   
    };

    public Integer getIdEmpresa(){
        return id;
    }

    public void setIdEmpresa(Integer id){
        this.id = id;
    }

    public String getClaveEmpresa(){
        return claveEmpresa;
    }

    public void setClaveEmpresa(String claveEmpresa){
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

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

  /*   public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Date getFechaServidor() {
        return fechaServidor;
    }

    public void setFechaServidor(Date fechaServidor) {
        this.fechaServidor = fechaServidor;
    }*/

    public Usuario getUsuario(){
        return usuario;
     }
 
     public void setUsuario(Usuario usuario){
         this.usuario = usuario;
         
     }
     public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }

   
}