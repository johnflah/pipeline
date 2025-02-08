package com.poc.pipeline.service.processors;

import com.poc.pipeline.config.PipelineExecutorService;
import com.poc.pipeline.model.Card;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;
import org.springframework.core.annotation.Order;

@Order(1)
public class CardDetailsValidator<T> implements Processor<Card> {

  @Override
  public CompletableFuture<Card> process(Card input) {
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("CardDetailsValidator check: "+ Thread.currentThread().getName());

      if (input.getCardNumber() == null || input.getCardNumber().length() != 16) {
        throw new IllegalArgumentException("Invalid card number."+input.getCardNumber()+" Must be 16 digits.");
      }
      if (input.getCvv() == null || input.getCvv().length() != 3) {
        throw new IllegalArgumentException("Invalid CVV. Must be 3 digits.");
      }
      if (input.getExpiryDate() == null || input.getExpiryDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("Invalid expiry date. It cannot be in the past.");
      }

      return input;
    }, PipelineExecutorService.getExecutor());
  }
}
