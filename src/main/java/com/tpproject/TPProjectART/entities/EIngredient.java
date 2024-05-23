package com.tpproject.TPProjectART.entities;

import javax.persistence.*;

@Entity(name = "Ingredient")
public class EIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public EIngredient() {}
    public EIngredient(String name){
        this.name = name;
    }

    private String name;
    public String getName() {
        return name;
    }
}
