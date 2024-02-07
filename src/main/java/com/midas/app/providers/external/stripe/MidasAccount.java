package com.midas.app.providers.external.stripe;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MidasAccount {
  private String accountId;

  // Add other properties as needed

  public void setAccountId(UUID id) {
    this.accountId = id.toString(); // Convert UUID to string and set it as accountId
  }
}
