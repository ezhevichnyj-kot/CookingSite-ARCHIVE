package com.example.ChefServer.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity(name="Recipe")
public class ERecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @Size(max=200)
    private String description;

    @Size(max=300)
    private ArrayList<String> ingredients;

    @Size(max=4000)
    private String cook_steps;

    private String image_path;
    private String cook_time;
    private String category;
    private Boolean verified;

    public Long getId() {
        return this.id;
    }

    public String getTitle() { return this.title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return this.description; }
    public void setDescription(String description) { this.description = description; }

    public ArrayList<String> getIngredients() { return ingredients; }
    public void setIngredients(ArrayList<String> ingredients) { this.ingredients = ingredients; }

    public void setCook_steps(String cook_steps) { this.cook_steps = cook_steps; }
    public String getCook_steps() { return this.cook_steps; }

    public void setImage_path(String image_path) { this.image_path = image_path; }
    public String getImage_path() { return image_path; }

    public void setCook_time(String cook_time) { this.cook_time = cook_time; }
    public String getCook_time() { return cook_time; }

    public void setCategory(String category) { this.category = category; }
    public String getCategory() { return category; }

    public void setVerified(Boolean verified) { this.verified = verified; }
    public Boolean getVerified() { return this.verified; }
}
