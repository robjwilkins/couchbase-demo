package com.wilkins.couchbase.demo;

import com.couchbase.client.java.repository.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
@EqualsAndHashCode
@ToString
public class Water {

  @Id
  private String id;
  private String type;
  private String colour;
  private Double volume;
  private Double temperature;

}
