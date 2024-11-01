package br.com.alura.literAlura.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "livros")
public class Livro {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String titulo;
  @ManyToOne
  private Autor autor;
  private String idioma;
  private Double numeroDownloads;

  public Livro(){}

  public Livro(String titulo, String idioma, Double numeroDownloads) {
    this.titulo = titulo;
    this.idioma = idioma;
    this.numeroDownloads = numeroDownloads;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitulo() {
    return titulo;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public Autor getAutor() {
    return autor;
  }

  public void setAutor(Autor autor) {
    autor.setLivros(this);
    this.autor = autor;
  }

  public String getIdioma() {
    return idioma;
  }

  public void setIdioma(String idioma) {
    this.idioma = idioma;
  }

  public Double getNumeroDownloads() {
    return numeroDownloads;
  }

  public void setNumeroDownloads(Double numeroDownloads) {
    this.numeroDownloads = numeroDownloads;
  }

  @Override
  public String toString() {
    return "\tLivro:" +
            "\nTitulo= " + titulo +
            "\nAutor= " + autor.getNome() +
            "\nidioma= " + idioma +
            "\nNumeroDownloads= " + numeroDownloads +
            "\n\t***\t***\n";
  }
}
