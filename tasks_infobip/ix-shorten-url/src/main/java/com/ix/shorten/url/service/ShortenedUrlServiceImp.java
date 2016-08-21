package com.ix.shorten.url.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ix.shorten.url.dao.AccountDao;
import com.ix.shorten.url.dao.RedirectionDao;
import com.ix.shorten.url.dto.AccountResponse;
import com.ix.shorten.url.dto.RegisterRequest;
import com.ix.shorten.url.dto.RegisterResponse;
import com.ix.shorten.url.dto.RegisterResponseHolder;
import com.ix.shorten.url.dto.StatisticResponseHolder;
import com.ix.shorten.url.exception.ShortenUrlError;
import com.ix.shorten.url.model.Account;
import com.ix.shorten.url.model.Redirection;

@Service
@Transactional(readOnly = false, rollbackFor = Exception.class)
public class ShortenedUrlServiceImp implements ShortenedUrlService {

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private RedirectionDao redirectionDao;

  @Override
  public AccountResponse openAccount(String accountId) {
    AccountResponse result = new AccountResponse();
    try {
      accountDao.findAccountId(accountId);
    } catch (NoResultException ex) {
      Account account = new Account();
      account.setUsername(accountId);
      account.setPassword(RandomStringUtils.randomAlphanumeric(8));
      accountDao.saveAccountId(account);
      result.setSuccess(true);
      result.setDescription("Your account is opened");
      result.setPassword(account.getPassword());
      return result;
    }
    result.setSuccess(false);
    result.setDescription(
        "Account with that ID already exists");
    return result;
  }

  final static String baseUrl = "http://short.com/";

  @Override
  public RegisterResponseHolder registerUrl(String userName, RegisterRequest registerRequest) {
    RegisterResponseHolder registerResponseHolder = new RegisterResponseHolder();
    Redirection redirection = null;

    try {
      redirection = redirectionDao.findByUsernameAndUrl(userName, registerRequest.getUrl());
    } catch (NoResultException ex) {
      return processNewRedirectionCase(userName, registerRequest, registerResponseHolder);
    }

    if (isDoNothingCase(registerRequest, redirection))
      return registerResponseHolder;
    else
      return processUpdateRedirectionCase(registerRequest, registerResponseHolder, redirection);
  }

  private RegisterResponseHolder processNewRedirectionCase(
      String userName, RegisterRequest registerRequest,
      RegisterResponseHolder registerResponseHolder) {
    Redirection redirection;
    redirection = createNewRedirectionObjectAndAddItToDb(userName, registerRequest);
    registerResponseHolder.getRegisterResponse().setShortUrl(redirection.getShortUrl());
    registerResponseHolder.setStatus(HttpStatus.CREATED);
    return registerResponseHolder;
  }
  
  private Redirection
  createNewRedirectionObjectAndAddItToDb(String userName, RegisterRequest registerRequest) {
    Redirection redirection;
    redirection = new Redirection();
    redirection.setUsername(userName);
    redirection.setUrl(registerRequest.getUrl());
    redirection.setShortUrl(baseUrl + RandomStringUtils.randomAlphabetic(8));
    redirectionDao.save(redirection);
    return redirection;
  }

  private boolean isDoNothingCase(RegisterRequest registerRequest, Redirection redirection) {
    return registerRequest.getRedirectionType() == redirection.getRedirectionCode();
  }
  
  private RegisterResponseHolder processUpdateRedirectionCase(
      RegisterRequest registerRequest, RegisterResponseHolder registerResponseHolder,
      Redirection redirection) {
    redirection.setRedirectionCode(registerRequest.getRedirectionType());
    redirectionDao.save(redirection);

    registerResponseHolder.getRegisterResponse().setShortUrl(redirection.getShortUrl());
    registerResponseHolder.setStatus(HttpStatus.OK);
    return registerResponseHolder;
  }

  @Override
  public StatisticResponseHolder createStatisticForAccountId(String accountId) {
    List<Redirection> redirections = redirectionDao.findByUserName(accountId);
    return null;
  }


}
