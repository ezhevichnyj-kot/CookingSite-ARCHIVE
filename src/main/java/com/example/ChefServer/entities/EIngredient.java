package com.example.ChefServer.entities;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;

@Entity(name="Ingredient")
public class EIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public EIngredient() {  }
    public EIngredient(String name){ this.name = name;}

    private String name;
    public String getName() { return name; }
}
