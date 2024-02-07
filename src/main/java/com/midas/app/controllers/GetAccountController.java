package com.midas.app.controllers;

import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.services.AccountService;
import com.midas.generated.api.AccountsApi;
import com.midas.generated.model.AccountDto;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetAccountController implements AccountsApi {
  private final AccountService accountService;
  private final Logger logger = LoggerFactory.getLogger(GetAccountController.class);

  @Autowired
  public GetAccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @GetMapping("/getUserAccounts")
  @Override
  public ResponseEntity<List<AccountDto>> getUserAccounts() {
    logger.info("Retrieving all accounts");

    List<Account> accounts = accountService.getAccounts();
    List<AccountDto> accountDtos = Mapper.toAccountDtoList(accounts);

    return new ResponseEntity<>(accountDtos, HttpStatus.OK);
  }
}
