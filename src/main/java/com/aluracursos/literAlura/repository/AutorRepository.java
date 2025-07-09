package com.aluracursos.literAlura.repository;

import com.aluracursos.literAlura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor,Long> {

    @Query("SELECT a FROM Autor a WHERE a.fechaNacimiento <= :year AND (a.fechaMuerte IS NULL OR a.fechaMuerte >= :year)")
    List<Autor> buscarAutoresVivosEn(@Param("year") Integer year);
    Optional<Autor> findByNombreIgnoreCase(String nombre);
}
