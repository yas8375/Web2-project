package com.example.movies_backend.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "creditcards")
public class CreditCard {

    @Id
    @Column(name = "id", nullable = false, length = 20)
    private String id;

    @Column(name = "firstName", nullable = false, length = 50)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 50)
    private String lastName;

    @Column(name = "expiration", nullable = false)
    private LocalDate expiration;

    public CreditCard() {}

    public CreditCard(String id, String firstName, String lastName, LocalDate expiration) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.expiration = expiration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getExpiration() { return expiration; }
    public void setExpiration(LocalDate expiration) { this.expiration = expiration; }
}
