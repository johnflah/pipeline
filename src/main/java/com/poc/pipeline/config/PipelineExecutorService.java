package com.poc.pipeline.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.stereotype.Service;

public class PipelineExecutorService {

  private static final ExecutorService executor = Executors.newFixedThreadPool(4);

  public static ExecutorService getExecutor() {
    return executor;
  }

  // Shutdown the executor after processing is complete
  public static void shutdown() {
    if (executor != null) {
      executor.shutdown();
    }
  }

}
