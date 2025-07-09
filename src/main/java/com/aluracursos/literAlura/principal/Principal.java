package com.aluracursos.literAlura.principal;


import com.aluracursos.literAlura.model.Autor;
import com.aluracursos.literAlura.model.Datos;
import com.aluracursos.literAlura.model.DatosAutor;
import com.aluracursos.literAlura.model.DatosLibro;
import com.aluracursos.literAlura.model.Libro;
import com.aluracursos.literAlura.repository.AutorRepository;
import com.aluracursos.literAlura.repository.LibroRepository;
import com.aluracursos.literAlura.service.ConsumoApi;
import com.aluracursos.literAlura.service.ConvierteDatos;
import java.util.*;

public class Principal {
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private Scanner teclado = new Scanner(System.in);
    private List<DatosLibro> datosLibros = new ArrayList<>();
    private List<Datos> datos = new ArrayList<>();
    private List<Libro> libros;
    private Optional<Libro> libroBuscado;
    private LibroRepository libroRepositorio;
    private AutorRepository autorRepositorio;

    public Principal(LibroRepository libroRepository, AutorRepository autorRepository) {
        this.libroRepositorio = libroRepository;
        this.autorRepositorio = autorRepository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1- Buscar libro por titulo
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    0- Salir
                    """;
            System.out.println("\nPorfavor seleccione una opcion:");
            System.out.println(menu);
            try {
                String input = teclado.nextLine();
                opcion = Integer.parseInt(input);
                switch (opcion) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        listarLibros();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        autoresVivos();
                        break;
                    case 5:
                        librosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Finalizando el programa");
                        break;
                    default:
                        System.out.println("Opcion no valida, intenta otra vez");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debes ingresar un número válido.\n");
            } catch (Exception e) {
                System.out.println("Ha ocurrido un error inesperado: " + e.getMessage());
            }
        }
    }

    private DatosLibro getDatosLibro() {
        System.out.println("Escribe el nombre del libro que desea buscar: ");
        var nombreLibro = teclado.nextLine().trim();
        if (nombreLibro.isBlank()) {
            System.out.println("El nombre del libro no puede estar vacío.");
            return null;
        }
        try {
            var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "%20"));
            if (json == null || json.isBlank()) {
                System.out.println(" No se recibió respuesta de la Api.");
                return null;
            }
            Datos datos = convierteDatos.obtenerDatos(json, Datos.class);
            if (datos.resultado() == null || datos.resultado().isEmpty()) {
                System.out.println("No se encontraron resultados para el libro: " + nombreLibro);
                return null;
            }
            return datos.resultado().get(0);
        } catch (Exception e) {
            System.out.println("Error al buscar el libro: " + e.getMessage());
            return null;
        }
    }

    private void buscarLibro() {
        DatosLibro datosLibro = getDatosLibro();
        if (datosLibro == null) {
            System.out.println("No se pudo obtener información del libro.");
            return;
        }
        Optional<Libro> libroExistente = libroRepositorio.findByTituloIgnoreCase(datosLibro.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("El libro ya está registrado en la base de datos:");
            System.out.println(libroExistente.get());
            return;
        }
        Libro libro = new Libro(datosLibro);
        for (DatosAutor datosAutor : datosLibro.autor()) {
            Optional<Autor> autorExistente = autorRepositorio.findByNombreIgnoreCase(datosAutor.nombre());
            Autor autor;

            if (autorExistente.isPresent()) {
                autor = autorExistente.get();
            } else {
                autor = new Autor(datosAutor);
                autorRepositorio.save(autor);
            }
            libro.addAutor(autor);
        }
        libroRepositorio.save(libro);
        System.out.println(libro);
    }

    private void listarLibros() {
        List<Libro> libros1 = libroRepositorio.findAll();
        libros1.forEach(libro -> System.out.println(libro));

    }

    private void listarAutores() {
        List<Autor> autors = autorRepositorio.findAll();
        if (autors.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
        } else {
            System.out.println("Autores registrados: \n");
            autors.forEach(autor -> {
                System.out.println(autor);
            });
        }

    }

    private void autoresVivos() {
        System.out.println("Introduce el año para buscar autores vivos: ");
        int year = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autoresVivos = autorRepositorio.buscarAutoresVivosEn(year);
        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en ese año.");
        } else {
            System.out.println("Autores vivos en el año " + year + ":\n");
            autoresVivos.forEach(System.out::println);
        }
    }

    private void librosPorIdioma() {
        System.out.println("""
        Escribe el idioma del libro que deseas buscar:
        es - español
        en - ingles
        fr - frances
        pt - portugues
        it - italiano\n""");
        String lenguaje = teclado.nextLine();

        List<Libro> libros = libroRepositorio.findByLenguajeContainingIgnoreCase(lenguaje);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
        } else {
            System.out.println("Libros encontrados en el idioma '" + lenguaje + "':");
            libros.forEach(libro -> {
                System.out.println(libro);});
            }
        }
    }
