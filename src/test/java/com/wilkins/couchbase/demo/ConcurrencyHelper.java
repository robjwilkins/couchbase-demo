package com.wilkins.couchbase.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class ConcurrencyHelper {
  int identifier;
  String id;
  Water water;
  String type;
  Double volume;
}
