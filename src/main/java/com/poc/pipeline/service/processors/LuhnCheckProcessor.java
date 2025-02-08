package com.poc.pipeline.service.processors;

import com.poc.pipeline.config.PipelineExecutorService;
import com.poc.pipeline.model.Card;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

@Slf4j
public class LuhnCheckProcessor<T> implements Processor<Card> {

  @Override
  public CompletableFuture<Card> process(Card input) {
    log.info("blah");
    return CompletableFuture.supplyAsync(() -> {
      System.out.println("Process luhn check: "+input.getCardNumber()+" Thread: "+ Thread.currentThread().getName());
      int sum = 0;
      boolean alternate = false;
      for (int i = input.getCardNumber().length() - 1; i >= 0; i--) {
        int n = Integer.parseInt(input.getCardNumber().substring(i, i + 1));
        if (alternate) {
          n *= 2;
          if (n > 9) {
            n -= 9;
          }
        }
        sum += n;
        alternate = !alternate;
      }
      if (sum % 10 != 0){
         throw new IllegalArgumentException();
      }
      return input;
    }, PipelineExecutorService.getExecutor());
  }
}
