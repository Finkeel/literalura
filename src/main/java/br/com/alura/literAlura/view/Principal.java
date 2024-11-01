package br.com.alura.literAlura.view;

import br.com.alura.literAlura.model.*;
import br.com.alura.literAlura.repository.AutorRepository;
import br.com.alura.literAlura.repository.LivroRepository;
import br.com.alura.literAlura.service.ConsumoApi;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {
  private ConsumoApi consumoApi = new ConsumoApi();
  private LivroRepository repositorioLivro;
  private AutorRepository repositorioAutor;

  public Principal(LivroRepository repositorioLivro, AutorRepository repositorioAutor) {
    this.repositorioLivro = repositorioLivro;
    this.repositorioAutor = repositorioAutor;
  }

  public void exibeMenu() {
    int opcao = 9;
    Scanner leitura = new Scanner(System.in);
    while (opcao != 0) {
      System.out.println("""
              ---------\s
               Escolha uma das opções:\s
              1 - Busca de livro pelo título
              2 - Listar livros registrados
              3 - Listar autores registrados
              4 - Listar autores vivos em certo ano
              5 - Listar livros em certo idioma
              0 - Sair""");
      opcao = leitura.nextInt();
      leitura.nextLine();
      switch (opcao) {
        case 1:
          System.out.println("Informe o nome do livro");
          var nomeLivro = leitura.nextLine();
          adicionaLivro(nomeLivro);
          break;
        case 2:
          List<Livro> livrosRegistrados = repositorioLivro.findAll();
          livrosRegistrados.forEach(l -> System.out.println(l.toString()));
          break;
        case 3:
          List<Autor> autoresRegistrados = repositorioAutor.findAll();
          autoresRegistrados.forEach(a -> System.out.println(a.toString()));
          break;
        case 4:
          System.out.println("Informe o ano limite em que os autores estao vivos");
          int ano = leitura.nextInt();
          leitura.nextLine();
          List<Autor> autoresVivos = repositorioAutor.findByAnoFalecimentoGreaterThanEqual(ano);
          autoresVivos.forEach(a -> System.out.println(a.toString()));
          break;
        case 5:
          System.out.println("Informe o idioma dos livros que deseja listar: frances, português, inglês ou espanhol");
          String idioma = leitura.nextLine();
          String siglaIdioma = converteIdioma(idioma);
          if(siglaIdioma.contains("erro")){
            System.out.println("Opção invalida, tente novamente");
          } else{
            List<Livro> livrosDoIdioma = repositorioLivro.findByIdiomaEqualsIgnoreCase(siglaIdioma);
            livrosDoIdioma.forEach(l -> System.out.println(l.toString()));
          }
          break;
        case 0:
          System.out.println("Saindo ...");
          break;
        default:
          System.out.println("Opção invalida, tente novamente!");
      }
    }
  }

  private String converteIdioma(String idioma) {
    switch (idioma) {
      case "frances":
        idioma = "fr";
        break;

      case "português":
        idioma = "pt";
        break;

      case "inglês":
        idioma = "en";
        break;

      case "espanhol":
        idioma = "es";
        break;

      default:
        idioma = "erro";
        break;
    }
    return idioma;
  }

  private void adicionaLivro(String nomeLivro){
    Optional<Livro> livroRegistrado = verificaSeLivroRegistrado(nomeLivro);
    if(livroRegistrado.isPresent()){
      System.out.println("Livro já está registrado\n" +
              livroRegistrado.get().toString());
    }
    else {
      if (nomeLivro.contains(" ")) {
        nomeLivro = (nomeLivro.replace(" ", "%20"));
      }
      String livro = consumoApi.obterDados(nomeLivro);

      var livroJson = consumoApi.converterDados(livro, Resultados.class);

      DadosLivro dadosLivro = livroJson.resultados().get(0);

      Livro livroEncontrado = new Livro(dadosLivro.titulo(), dadosLivro.idiomas().get(0)
              , dadosLivro.numeroDownloads());


      DadosAutor dadosAutor = livroJson.resultados().get(0).autores().get(0);
      Optional<Autor> autorRegistrado = verificaSeAutorRegistrado(dadosAutor.nome());
      Autor autorEncontrado;
      if(autorRegistrado.isPresent()){
        autorEncontrado = autorRegistrado.get();

        livroEncontrado.setAutor(autorEncontrado);

        repositorioLivro.save(livroEncontrado);
      } else{

        autorEncontrado = new Autor(dadosAutor.nome(), dadosAutor.anoNascimento(),
                dadosAutor.anoFalecimento());

        livroEncontrado.setAutor(autorEncontrado);

        repositorioAutor.save(autorEncontrado);
        repositorioLivro.save(livroEncontrado);
      }

      System.out.println(livroEncontrado.toString());
      System.out.println(autorEncontrado.toString());
    }
  }
  private Optional<Livro> verificaSeLivroRegistrado(String nomeLivro){

    return repositorioLivro.findByTituloIgnoreCase(nomeLivro);
  }
  private Optional<Autor> verificaSeAutorRegistrado(String nomeAutor){
    return repositorioAutor.findByNomeIgnoreCase(nomeAutor);

  }
}
