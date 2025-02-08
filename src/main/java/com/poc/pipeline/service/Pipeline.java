package com.poc.pipeline.service;

import com.poc.pipeline.model.Card;
import com.poc.pipeline.service.processors.Processor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Pipeline {

  private List<Processor<Card>> processors = new ArrayList<>();

  // Add processor to the pipeline
  public <T> void addProcessor(Processor<Card> processor) {
    processors.add(processor);
  }

  // Execute all processors on an object asynchronously
  public <T> CompletableFuture<T> execute(T input) {
    CompletableFuture<T> result = CompletableFuture.completedFuture(input);

    for (Processor<Card> processor : processors) {
//      if (processor.getClass().getGenericSuperclass().getTypeName().contains(input.getClass().getName())) {
        // Chain asynchronous tasks using thenCompose
        result = result.thenCompose(value -> (CompletableFuture<T>) processor.process((Card) value));
//      }
    }
    return result;
  }

}
