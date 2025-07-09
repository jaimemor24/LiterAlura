package com.aluracursos.literAlura.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(unique = true)
    private String titulo;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "autor_libro",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id"))
    private List<Autor> autor;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "libro_lenguajes", joinColumns = @JoinColumn(name = "libro_id"))
    @Column(name = "lenguaje")
    private List<String> lenguaje;
    private Integer descargas;

    public Libro(){}

    public Libro(DatosLibro datosLibro) {
        this.titulo = datosLibro.titulo();
        this.autor = new ArrayList<>();
        this.lenguaje = datosLibro.lenguaje();
        this.descargas = datosLibro.descargas();
    }

public void addAutor(Autor autor) {
    if (!this.autor.contains(autor)) {
        this.autor.add(autor);
        autor.getLibro().add(this);
    }
}

@Override
public String toString() {
    String nombresAutores = autor.stream()
            .map(Autor::getNombre)
            .collect(Collectors.joining(", "));
    return "\n********LIBRO********\n"+
            "Titulo: " + titulo + '\n' +
            "Autor: " + nombresAutores + '\n' +
            "Lenguaje: " + lenguaje + '\n' +
            "Descargas: " + descargas + '\n';
}


public Long getId() {
    return Id;
}

public void setId(Long id) {
    Id = id;
}

public String getTitulo() {
    return titulo;
}

public void setTitulo(String titulo) {
    this.titulo = titulo;
}

public List<Autor> getAutor() {
    return autor;
}

public void setAutor(List<Autor> autor) {
    this.autor = autor;
}

public Integer getDescargas() {
    return descargas;
}

public void setDescargas(Integer descargas) {
    this.descargas = descargas;
}

public List<String> getLenguaje() {
    return lenguaje;
}

public void setLenguaje(List<String> lenguaje) {
    this.lenguaje = lenguaje;
}
}
