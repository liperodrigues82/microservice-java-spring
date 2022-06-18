package br.com.microservice.pagamentos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuracao {

	@Bean
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
}
