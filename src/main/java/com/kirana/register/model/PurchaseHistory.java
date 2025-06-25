package com.kirana.register.model;

import jakarta.persistence.*;


@Entity
@Table (name = "orders")
public class PurchaseHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    

}
