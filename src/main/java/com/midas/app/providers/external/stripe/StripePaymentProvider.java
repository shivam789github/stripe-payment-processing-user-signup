package com.midas.app.providers.external.stripe;

import com.midas.app.models.Account;
import com.midas.app.providers.payment.CreateAccount;
import com.midas.app.providers.payment.PaymentProvider;
import com.stripe.Stripe;
import com.stripe.param.AccountCreateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class StripePaymentProvider implements PaymentProvider {
  private final Logger logger = LoggerFactory.getLogger(StripePaymentProvider.class);

  private final StripeConfiguration configuration;

  /** providerName is the name of the payment provider */
  @Override
  public String providerName() {
    return "stripe";
  }

  /**
   * createAccount creates a new account in the payment provider.
   *
   * @param details is the details of the account to be created.
   * @return Account
   */
  @Override
  public MidasAccount createAccount(CreateAccount details) {
    try {
      Stripe.apiKey = configuration.getApiKey();

      // Create a Stripe account
      Account stripeAccount =
          Account.create(
              AccountCreateParams.builder()
                  .setType(AccountCreateParams.Type.CUSTOM)
                  .setCountry(details.getCountry())
                  .setEmail(details.getEmail())
                  .build());

      // Extract relevant information from the Stripe account and create a MidasAccount
      MidasAccount midasAccount = new MidasAccount();
      midasAccount.setAccountId(stripeAccount.getId());
      // Set other relevant information from the Stripe account

      // Log success
      logger.info("Stripe account created successfully. Account ID: {}", stripeAccount.getId());

      return midasAccount;
    } catch (Error e) {
      // Handle Stripe API exception
      logger.error("Error creating Stripe account", e);
      // You might want to throw a custom exception or handle the error appropriately
      throw new RuntimeException("Error creating Stripe account", e);
    }
  }
}
