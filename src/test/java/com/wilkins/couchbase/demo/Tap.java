package com.wilkins.couchbase.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Tap {

  private static final String TYPE = "drink-";
  private static final double VOLUME = 1.00;
  private static final double TEMP = 20.0;
  private static final String COLOUR = "orange-";
  @Autowired WaterRepository waterRepository;
  private List<Callable<ConcurrencyHelper>> callableTasks;
  private ExecutorService executorService;

  @Before
  public void pourIntoBucket() {
    waterRepository.deleteAll();
    callableTasks = new ArrayList<>();
    executorService = Executors.newFixedThreadPool(50);
    add10DifferentWater();
  }

  @Test
  public void checkBucket() {
    Water expectedWater = createWater(1);
    Water actualWater = waterRepository.findByTypeAndVolume("drink-1", 1.0).block();
    assertThat(actualWater).isEqualTo(expectedWater);
  }

  @Test
  public void concurrencyTest() throws Exception {
    for (int i = 0; i < 1; i++) {
      add10DifferentFindWaterTasks();
    }
    List<Future<ConcurrencyHelper>> futures = executorService.invokeAll(callableTasks);
    executorService.shutdown();
    checkWaterReturnedCorrectly(futures);
  }

  public void add10DifferentWater() {
    List<Water> waterCollection = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      waterCollection.add(createWater(i));
    }
    waterRepository.saveAll(waterCollection);
  }

  private void checkWaterReturnedCorrectly(List<Future<ConcurrencyHelper>> futures)
      throws ExecutionException, InterruptedException {
    for (Future<ConcurrencyHelper> concurrencyHelperFuture : futures) {
      assertTrue(concurrencyHelperFuture.isDone());
      ConcurrencyHelper concurrencyHelper = concurrencyHelperFuture.get();
      assertEquals(concurrencyHelper.getType(), concurrencyHelper.getWater().getType());
      assertEquals(concurrencyHelper.getVolume(), concurrencyHelper.getWater().getVolume());
      assertEquals(concurrencyHelper.getVolume(), concurrencyHelper.getWater().getVolume());

      assertWaterIsEqual(concurrencyHelper.getWater(), createWater(concurrencyHelper.getIdentifier()));
    }
  }

  private void assertWaterIsEqual(Water returnedWater, Water expectedWater) {
    assertEquals(expectedWater, returnedWater);
  }

  private Water createWater(int identifier) {
    return new Water("water::" + identifier, TYPE + identifier, COLOUR + identifier, VOLUME * identifier, TEMP * identifier);
  }

  private void add10DifferentFindWaterTasks() {
    for (int i = 0; i < 10; i++) {
      callableTasks.add(callableTask(i));
    }
  }

  private Callable<ConcurrencyHelper> callableTask(int identifier) {
    return () -> {
      System.out.println("creating callable for identifier: " + identifier);
      Water water = waterRepository.findByTypeAndVolume(TYPE + identifier, VOLUME * identifier).block();
      System.out.println("found water for identifier: " + identifier + ", water: " + water.toString());
      ConcurrencyHelper concurrencyHelper = new ConcurrencyHelper();
      concurrencyHelper.setIdentifier(identifier);
      concurrencyHelper.setWater(water);
      concurrencyHelper.setType(TYPE + identifier);
      concurrencyHelper.setVolume(VOLUME * identifier);

      return concurrencyHelper;
    };
  }
}
