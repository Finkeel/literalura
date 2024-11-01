package br.com.alura.literAlura.repository;

import br.com.alura.literAlura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository  extends JpaRepository<Livro, Long> {

    Optional<Livro> findByTituloIgnoreCase(String titulo);

    List<Livro> findByIdiomaEqualsIgnoreCase(String siglaIdioma);

}
