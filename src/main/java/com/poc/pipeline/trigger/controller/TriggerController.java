package com.poc.pipeline.trigger.controller;

import com.poc.pipeline.config.PipelineExecutorService;
import com.poc.pipeline.model.Card;
import com.poc.pipeline.service.Pipeline;
import com.poc.pipeline.service.processors.CardDetailsValidator;
import com.poc.pipeline.service.processors.LuhnCheckProcessor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trigger")
public class TriggerController {

  @GetMapping("/")
  @Async
  public void index(){
    // Create the pipeline and add processors
    Pipeline pipeline = new Pipeline();
    pipeline.addProcessor(new CardDetailsValidator<>());
    pipeline.addProcessor(new LuhnCheckProcessor<>());

    // Measure the start time for performance tracking
    long startTime = System.nanoTime();

    // Process the dataset asynchronously with the pipeline
    List<CompletableFuture<Card>> futures = new ArrayList<>();
    List<Card> lstCard = generateCards();
    for (Card card : lstCard) {
      futures.add(pipeline.execute(card));
    }

    // Wait for all futures to complete
    CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

    // Measure the end time and print the duration
    long endTime = System.nanoTime();
    long duration = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
    System.out.println("All records processed in " + duration + " ms.");
    // Shutdown the custom executor after processing
    PipelineExecutorService.shutdown();
  }

  private List<Card> generateCards() {
    String cardno = "5105105105105100,4111111111111111,5555555555554444,378282246310005,6011111111111117,";

    return Arrays.stream(cardno.split(","))
        .map(Card::new)
        .map(card-> {card.setCardHolderName("Blah vlas"); return card;})
        .map(card-> {card.setCvv("201"); return card;})
        .map(card-> {card.setExpiryDate(LocalDate.now().plusMonths(3)); return card;})
        .toList();
  }
}
