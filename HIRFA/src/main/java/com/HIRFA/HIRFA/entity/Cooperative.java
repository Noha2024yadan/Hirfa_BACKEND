package com.HIRFA.HIRFA.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "cooperatives")
@PrimaryKeyJoinColumn(name = "user_id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cooperative extends User {
    private String brand;
    private String description;
    private String adresse;
    private String licence;
    private String statutVerification;

    public Cooperative() {
        this.setUserType(UserType.COOPERATIVE);
    }
}
