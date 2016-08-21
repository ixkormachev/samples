package com.ix.shorten.url.api;

import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

public class CustomRestConfiguration extends RepositoryRestMvcConfiguration {
  @Override
  public RepositoryRestConfiguration config() {
    final RepositoryRestConfiguration config = super.config();
    config.setBasePath("/*");
    return config;
  }
}
