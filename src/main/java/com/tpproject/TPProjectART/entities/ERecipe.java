package com.tpproject.TPProjectART.entities;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.*;

@Entity(name="Recipe")
public class ERecipe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Size(max=4000)
    private String description;

    @Size(max=4000)
    private ArrayList<String> ingredients;

    @Size(max=4000)
    private String cook_steps;

    private String image_path;

    private String cook_time;

    private String category;

    private Boolean checked;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }
    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setCook_steps(String cook_steps) { this.cook_steps = cook_steps; }
    public String getCook_steps() { return cook_steps; }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
    public String getImage_path() {
        return image_path;
    }

    public void setCook_time(String cook_time) {
        this.cook_time = cook_time;
    }
    public String getCook_time() {
        return cook_time;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }
    public Boolean getChecked() {
        return checked;
    }
}
