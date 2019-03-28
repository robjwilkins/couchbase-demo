package com.wilkins.couchbase.demo;

import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import reactor.core.publisher.Mono;

public interface WaterRepository extends ReactiveCouchbaseRepository<Water, String> {
  Mono<Water> findByTypeAndVolume(String type, Double volume);
}
