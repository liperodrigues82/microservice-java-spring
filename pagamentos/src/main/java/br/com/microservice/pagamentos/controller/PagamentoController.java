package br.com.microservice.pagamentos.controller;

import java.net.URI;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.microservice.pagamentos.dto.PagamentoDto;
import br.com.microservice.pagamentos.service.PagamentoService;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

	@Autowired
	private PagamentoService service;
	
	@GetMapping
	public Page<PagamentoDto> findAll(@PageableDefault(size=10) Pageable paginacao) {
		return service.findAll(paginacao);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<PagamentoDto> findById(@PathVariable @NotNull Long id) {
		PagamentoDto dto = service.findById(id);
		
		return ResponseEntity.ok(dto);
	}
	
	@PostMapping
	public ResponseEntity<PagamentoDto> insert(@RequestBody PagamentoDto dto, UriComponentsBuilder uriBuilder) {
		PagamentoDto pagamento = service.insert(dto);
		URI uri = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
		
		return ResponseEntity.created(uri).body(pagamento);		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PagamentoDto> update(@PathVariable Long id, @RequestBody PagamentoDto dto) {
		PagamentoDto updated = service.update(id, dto);
		
		return ResponseEntity.ok().body(updated);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
