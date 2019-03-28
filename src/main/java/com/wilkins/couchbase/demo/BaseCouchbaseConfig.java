package com.wilkins.couchbase.demo;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.cluster.ClusterInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.data.couchbase.core.WriteResultChecking;
import org.springframework.data.couchbase.core.convert.CouchbaseConverter;
import org.springframework.data.couchbase.core.convert.translation.TranslationService;

@Configuration
public abstract class BaseCouchbaseConfig extends AbstractCouchbaseConfiguration {

  @Autowired
  private CouchbaseProperties properties;

  @Override
  protected List<String> getBootstrapHosts() {
    return properties.getBootstrapHosts();
  }

  @Override
  protected String getBucketName() {
    return properties.getBucket().getName();
  }

  @Override
  protected String getUsername() {
    return properties.getBucket().getName();
  }

  @Bean
  public CouchbaseTemplate couchbaseTemplate(ClusterInfo clusterInfo, Bucket bucket,
      CouchbaseConverter converter, TranslationService translationService) {
    CouchbaseTemplate couchbaseTemplate = new CouchbaseTemplate(clusterInfo, bucket, converter,
        translationService);
    couchbaseTemplate.setWriteResultChecking(WriteResultChecking.EXCEPTION);
    return couchbaseTemplate;
  }

}

