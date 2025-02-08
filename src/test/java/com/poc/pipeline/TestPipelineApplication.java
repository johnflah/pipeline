package com.poc.pipeline;

import org.springframework.boot.SpringApplication;

public class TestPipelineApplication {

  public static void main(String[] args) {
    SpringApplication.from(PipelineApplication::main).with(TestcontainersConfiguration.class)
        .run(args);
  }

}
