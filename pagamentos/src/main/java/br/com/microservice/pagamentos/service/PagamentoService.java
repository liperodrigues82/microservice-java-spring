package br.com.microservice.pagamentos.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.microservice.pagamentos.dto.PagamentoDto;
import br.com.microservice.pagamentos.http.PedidoClient;
import br.com.microservice.pagamentos.model.Pagamento;
import br.com.microservice.pagamentos.model.Status;
import br.com.microservice.pagamentos.repository.PagamentoRepository;

@Service
public class PagamentoService {

	@Autowired
	private PagamentoRepository repository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private PedidoClient pedido;
	
	public Page<PagamentoDto> findAll(Pageable paginacao) {
		return repository
				.findAll(paginacao)
				.map(p -> modelMapper.map(p, PagamentoDto.class));
	}
	
	public PagamentoDto findById(Long id) {
		Pagamento pagamento = repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException());
		
		return modelMapper.map(pagamento, PagamentoDto.class); 
	}
	
	public PagamentoDto insert(PagamentoDto dto) {
		Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
		pagamento.setStatus(Status.CRIADO);
		repository.save(pagamento);
		
		return modelMapper.map(pagamento, PagamentoDto.class);
	}
	
	public PagamentoDto update(Long id, PagamentoDto dto) {
		Pagamento pagamento = modelMapper.map(dto, Pagamento.class);
		pagamento.setId(id);
		pagamento = repository.save(pagamento);
		
		return modelMapper.map(pagamento, PagamentoDto.class);
	}
	
	public void delete(Long id) {
		repository.deleteById(id);
	}
	
	public void confirmPayment(@PathVariable Long id) {
		Optional<Pagamento> pagamento = repository.findById(id);
		
		if (!pagamento.isPresent()) {
			throw new EntityNotFoundException();
		}
		
		pagamento.get().setStatus(Status.CONFIRMADO);
		repository.save(pagamento.get());
		pedido.updatePayment(pagamento.get().getPedidoId());
	}
	
	public void changeStatus(Long id) {
		Optional<Pagamento> pagamento = repository.findById(id);

		if (!pagamento.isPresent()) {
			throw new EntityNotFoundException();
		}

		pagamento.get().setStatus(Status.CONFIRMADO_SEM_INTEGRACAO);
		repository.save(pagamento.get());
	}
}
