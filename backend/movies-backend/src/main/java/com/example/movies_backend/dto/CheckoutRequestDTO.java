package com.example.movies_backend.dto;

public class CheckoutRequestDTO {
    private String firstName;
    private String lastName;
    private String cardNumber;
    private String expiration;

    public CheckoutRequestDTO() {
    }

    public CheckoutRequestDTO(String firstName, String lastName, String cardNumber, String expiration) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.expiration = expiration;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
