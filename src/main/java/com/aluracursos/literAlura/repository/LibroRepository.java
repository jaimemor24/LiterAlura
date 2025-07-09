package com.aluracursos.literAlura.repository;

import com.aluracursos.literAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro,Long> {
    Optional<Libro> findByTituloIgnoreCase(String titulo);

    @Query("SELECT l FROM Libro l JOIN l.lenguaje lang WHERE LOWER(lang) LIKE LOWER(CONCAT('%', :lenguaje, '%'))")
    List<Libro> findByLenguajeContainingIgnoreCase(@Param("lenguaje") String lenguaje);
}
