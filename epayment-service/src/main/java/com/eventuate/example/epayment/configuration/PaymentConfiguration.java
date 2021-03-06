package com.eventuate.example.epayment.configuration;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.eventuate.example.entity.Transaction;
import com.eventuate.example.epayment.event.PaymentWorkflow;
import com.eventuate.example.epayment.service.IPaymentService;
import com.eventuate.example.epayment.service.impl.PaymentServiceImpl;
import com.eventuate.example.info.command.TransactionCommand;

import io.eventuate.javaclient.spring.EnableEventHandlers;
import io.eventuate.sync.AggregateRepository;
import io.eventuate.sync.EventuateAggregateStore;

@Configuration
@EnableEventHandlers
@ComponentScan
public class PaymentConfiguration {

	@Bean
	public HttpMessageConverters customConverters() {
		HttpMessageConverter<?> additional = new MappingJackson2HttpMessageConverter();
		return new HttpMessageConverters(additional);
	}

	@Bean(name="paymentWorkflow")
	public PaymentWorkflow paymentWorkflow() {
		return new PaymentWorkflow();
	}

	@Bean
	public IPaymentService paymentService(AggregateRepository<Transaction, TransactionCommand> transactionRepository) {
		return new PaymentServiceImpl(transactionRepository);
	}

	@Bean(name="transactionRepository")
	public AggregateRepository<Transaction, TransactionCommand> transactionRepository(
			EventuateAggregateStore eventStore) {
		return new AggregateRepository<>(Transaction.class, eventStore);
	}
}
