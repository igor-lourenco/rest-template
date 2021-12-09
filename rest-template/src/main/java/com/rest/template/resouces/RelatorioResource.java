package com.rest.template.resouces;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.template.dto.PageDTO;
import com.rest.template.dto.ProductDTO;
import com.rest.template.services.RelatorioService;

@RestController
@RequestMapping(value = "/relatorios")
public class RelatorioResource {

	@Autowired
	private RelatorioService service;
	
	//Request para fornecer informações de solicitação para servlets HTTP, pra receber informações das APIs
	@Autowired
	private HttpServletRequest request;
	
	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody ProductDTO dto) {
		String token = request.getHeader("Authorization");
		dto = service.update(id, dto, token);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		String token = request.getHeader("Authorization");
		service.delete(id, token);
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@RequestBody ProductDTO dto) {
		String token = request.getHeader("Authorization"); //pra pegar o token que está no header da requisição
		dto = service.insert(dto, token);
		return ResponseEntity.ok(dto);
	}
	
	@GetMapping
	public ResponseEntity<PageDTO<ProductDTO>> findAll() {
		PageDTO<ProductDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	private ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		var dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}
}
