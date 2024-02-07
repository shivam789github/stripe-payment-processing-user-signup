package com.midas.app.providers.payment;

public class CreateAccount {
  private String userId;
  private String firstName;
  private String lastName;
  private String email;
  private String country;

  private CreateAccount(Builder builder) {
    this.userId = builder.userId;
    this.firstName = builder.firstName;
    this.lastName = builder.lastName;
    this.email = builder.email;
    this.country = builder.country;
  }

  public String getUserId() {
    return userId;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getCountry() {
    return country;
  }

  public static class Builder {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String country;

    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder firstName(String firstName) {
      this.firstName = firstName;
      return this;
    }

    public Builder lastName(String lastName) {
      this.lastName = lastName;
      return this;
    }

    public Builder email(String email) {
      this.email = email;
      return this;
    }

    public Builder country(String country) {
      this.country = country;
      return this;
    }

    public CreateAccount build() {
      return new CreateAccount(this);
    }
  }

  CreateAccount account =
      new CreateAccount.Builder()
          .userId("123")
          .firstName("John")
          .lastName("Doe")
          .email("john.doe@example.com")
          .country("USA")
          .build();
}
