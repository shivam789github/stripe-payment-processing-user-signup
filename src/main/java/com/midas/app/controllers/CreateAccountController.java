package com.midas.app.controllers;

import com.midas.app.mappers.Mapper;
import com.midas.app.models.Account;
import com.midas.app.models.ProviderType;
import com.midas.app.repositories.AccountRepository;
import com.midas.app.services.AccountService;
import com.midas.generated.api.AccountsApi;
import com.midas.generated.model.AccountDto;
import com.midas.generated.model.CreateAccountDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class CreateAccountController implements AccountsApi {
  private final AccountService accountService;
  private final Logger logger = LoggerFactory.getLogger(CreateAccountController.class);

  @Value("${stripe.api-key}")
  private String stripeApiKey;

  @Autowired
  public CreateAccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PostMapping("/signUp")
  @Override
  public ResponseEntity<AccountDto> createUserAccount(
      @RequestBody CreateAccountDto createAccountDto) {
    logger.info("Creating account for user with email: {}", createAccountDto.getEmail());

    String providerType = ProviderType.STRIPE;

    // Logic to generate and store the providerId based on providerType (Stripe in this case)
    String providerId = null;
    if (providerType == ProviderType.STRIPE) {
      // Logic to generate Stripe customer ID and store it
      try {
        providerId = generateAndStoreStripeCustomerId(createAccountDto.getEmail());
      } catch (StripeException e) {
        logger.error("Error generating and storing Stripe customer ID", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
      }
    }

    // Creating the account with providerType and providerId
    Account account =
        accountService.createAccount(
            Account.builder()
                .firstName(createAccountDto.getFirstName())
                .lastName(createAccountDto.getLastName())
                .email(createAccountDto.getEmail())
                .providerType(providerType)
                .providerId(providerId)
                .build());

    return new ResponseEntity<>(Mapper.toAccountDto(account), HttpStatus.CREATED);
  }

  @Autowired private AccountRepository accountRepository;

  // Method to generate and store the Stripe customer ID
  private String generateAndStoreStripeCustomerId(String email) throws StripeException {

    Stripe.apiKey = stripeApiKey;

    Map<String, Object> params = new HashMap<>();
    params.put("email", email);
    Customer customer = Customer.create(params);

    String customerId = customer.getId();

    CreateAccountDto createAccountDto = new CreateAccountDto();
    Account accountEntity = new Account();
    accountEntity.setFirstName(createAccountDto.getFirstName());
    accountEntity.setLastName(createAccountDto.getLastName());
    accountEntity.setEmail(createAccountDto.getEmail());
    accountEntity.setProviderType(ProviderType.STRIPE);
    accountEntity.setProviderId(customerId);

    accountRepository.save(accountEntity);

    return customerId;
  }
}
