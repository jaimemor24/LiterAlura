package com.aluracursos.literAlura.model;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String nombre;
    private Integer fechaNacimiento;
    private Integer fechaMuerte;
    @ManyToMany(mappedBy = "autor", fetch = FetchType.EAGER)
    private List<Libro> libro= new ArrayList<>();
    public Autor(){}

    public Autor(DatosAutor autor) {
        this.nombre = autor.nombre();
        this.fechaNacimiento = Integer.valueOf(autor.fechaNacimiento());
        this.fechaMuerte = Integer.valueOf(autor.fechaMuerte());
        this.libro=new ArrayList<>();
    }

    @Override
    public String toString() {
        return "********Autor******" + '\n'+
                "Nombre: " + nombre + '\n' +
                "Fecha de Nacimiento: " + fechaNacimiento +'\n'+
                "Fecha de Muerte: " + fechaMuerte +'\n'+
                "Libros: " + libro.stream()
                .map(Libro::getTitulo)
                .collect(Collectors.joining(" , ")) + '\n';
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Integer fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(Integer fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }
}
