package com.rest.template.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.rest.template.dto.PageDTO;
import com.rest.template.dto.ProductDTO;
import com.rest.template.services.exceptions.ConnectNotFoundException;

@Service
public class RelatorioService {

	@Autowired
	private RestTemplate restTemplate;
	
	private String host = "http://localhost:8080";
	private String path = host + "/products";
	
public ProductDTO insert(ProductDTO dto, String bearerToken) {

	    //Uma estrutura de dados que representa cabeçalhos de solicitação ou resposta HTTP
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", bearerToken);  //pra passar no cabeçalho da requição o token
		
		//HttpHeaders ->  Representa uma solicitação HTTP ou entidade de resposta, consistindo em cabeçalhos e corpo.
		HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(dto, headers);  // pra passar o body e o header na requisição
		
		ResponseEntity<ProductDTO> result =
				restTemplate.exchange(
						path,                      //caminho da API
						HttpMethod.POST,           // verbo
						httpEntity,                // argumentos que serão passados na API
						ProductDTO.class);         // tipo da resposta da requisição
		return result.getBody();
	}
	
	public ProductDTO update(Long id, ProductDTO dto, String bearerToken) {
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", bearerToken);
		
		HttpEntity<ProductDTO> httpEntity = new HttpEntity<>(dto, headers);
		
		ResponseEntity<ProductDTO> result = restTemplate.exchange(path + "/{id}", HttpMethod.PUT, httpEntity, ProductDTO.class, uriVariables);
		return result.getBody();
	}
	
	public Integer delete(Long id, String bearerToken) {
		
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("id", id.toString());

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", bearerToken);
		
		HttpEntity<?> httpEntity = new HttpEntity<Object>(headers);

		ResponseEntity<Void> result = restTemplate.exchange(path + "/{id}", HttpMethod.DELETE, httpEntity, Void.class, uriVariables);
		
		return result.getStatusCodeValue();
	}
	
	//https://stackoverflow.com/questions/34647303/spring-resttemplate-with-paginated-api
	public PageDTO<ProductDTO> findAll() {

		// pra reproduzir a paginação e passar como parametro na resposta da API
		var responseType = new ParameterizedTypeReference<PageDTO<ProductDTO>>() { }; 
		
		ResponseEntity<PageDTO<ProductDTO>> result = //atrubui na variavel a resposta da API
				restTemplate.exchange(                //exchange -> tipo mais generico 
				path,                                 //o caminho da API 
				HttpMethod.GET,                       // o verbo da API
				null,                                 // pra dizer que essa requisição não tem corpo
				responseType);                        // pra dizer que a resposta da API será paginado
		
		return result.getBody(); 
		//.getContent(); pega a lista da resposta da API e retorna
	}
	
	public ProductDTO findById(Long id) {
		
		Map<String, String> uriVariables = new HashMap<>();     //Map pra trabalhar com os valores da rota(API)
		uriVariables.put("id", id.toString());                 //recebe o valor que veio como parâmetro no método
		 
		try {
			ResponseEntity<ProductDTO> result =                     // atribui a resposta da requisição na variavel do tipo ResposeEntity	
					restTemplate.getForEntity(path + "/{id}",       //caminho da API 
					ProductDTO.class,                              //Tipo de dado que vai retornar da API 
					uriVariables);                                //os argumentos da API
			
			return result.getBody();                            //pra acessar o corpo(body) da requisição
		}
		catch(ResourceAccessException  e) {
			throw new ConnectNotFoundException("Conexão recusada");
		}
	}
		
}
