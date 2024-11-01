package br.com.alura.literAlura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ConsumoApi {
  private final String URL = "https://gutendex.com/books/?search=";
  private ObjectMapper mapper = new ObjectMapper();

  public String obterDados(String nomeLivro) {

    URI endereco = URI.create(URL + nomeLivro);
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(endereco).build();
    HttpResponse<String> response = null;

    try {
      response = client.send(request, HttpResponse.BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }

    String json = response.body();

    return json;
  }

  public <T> T converterDados(String json, Class<T> classe) {
    try{
      return mapper.readValue(json, classe);
    }catch (JsonProcessingException e){
      throw new RuntimeException(e);
    }
  }

  public <T> List<T> converterDadosLista(String json, Class<T> classe){
    try {
      JavaType type = mapper.getTypeFactory().constructParametricType(List.class, classe);
      return mapper.readValue(json, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

}
