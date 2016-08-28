package com.ix.shorten.url.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.ix.shorten.url.model.Account;

@RepositoryRestResource(path = "redirects")
public interface RedirectRepository extends PagingAndSortingRepository<Account, Long> {
  Account findById(@Param("id") long id);

  @Override
  Page<Account> findAll(Pageable pageable);
}
