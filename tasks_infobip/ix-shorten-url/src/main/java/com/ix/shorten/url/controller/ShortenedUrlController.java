package com.ix.shorten.url.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.ix.shorten.url.dto.AccountRequest;
import com.ix.shorten.url.dto.AccountResponse;
import com.ix.shorten.url.dto.RegisterRequest;
import com.ix.shorten.url.dto.RegisterResponse;
import com.ix.shorten.url.dto.RegisterResponseHolder;
import com.ix.shorten.url.dto.StatisticResponse;
import com.ix.shorten.url.dto.StatisticResponseHolder;
import com.ix.shorten.url.exception.ShortenUrlError;
import com.ix.shorten.url.service.ShortenedUrlService;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

@BasePathAwareController
public class ShortenedUrlController {

  @Autowired
  private ShortenedUrlService shortenUrlService;

  @RequestMapping(value = "/account", method = RequestMethod.POST)
  @ResponseBody
  public AccountResponse openAccount(@RequestBody AccountRequest accountRequest,
      HttpServletResponse response) {

    AccountResponse accountResponse = shortenUrlService.openAccount(accountRequest.getAccountId());

    response.setContentType("application/json");
    response.setStatus(
        accountResponse.isSuccess() ? HttpStatus.CREATED.value() : HttpStatus.FOUND.value());

    return accountResponse;
  }

  @RequestMapping(value = "/register", method = RequestMethod.POST)
  @ResponseBody
  public RegisterResponse registerUrl(@RequestBody RegisterRequest registerRequest,
      HttpServletResponse response, Authentication authentication) {

    String userName = authentication.getName();
    RegisterResponseHolder registerResponseHolder =
        shortenUrlService.registerUrl(userName, registerRequest);

    response.setStatus(registerResponseHolder.getStatus().value());
    response.setContentType("application/json");
    return registerResponseHolder.getRegisterResponse();
  }

  @RequestMapping(value = "/statistic/{AccountId}", method = RequestMethod.GET)
  @ResponseBody
  public StatisticResponse statistic(@PathVariable("AccountId") String accountId,
      HttpServletResponse response, Authentication authentication) {

    StatisticResponseHolder statisticResponseHolder =
        shortenUrlService.createStatisticForAccountId(accountId);

    return null;
  }

}
