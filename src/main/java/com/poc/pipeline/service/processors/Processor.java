package com.poc.pipeline.service.processors;

import java.util.concurrent.CompletableFuture;

public interface Processor<T> {
  CompletableFuture<T> process(T input);
}
