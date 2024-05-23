package com.tpproject.TPProjectART.entities;

import javax.persistence.*;

@Entity(name = "UserRecipeConnector")
public class EConnectorUserRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userid;
    private Long recipeid;

    public Long getId() {return id;}

    public Long getUserid() {return userid;}
    public void setUser_id(Long userid) {
        this.userid = userid;
    }

    public Long getRecipeid() {
        return recipeid;
    }
    public void setRecipe_id(Long recipeid) {
        this.recipeid = recipeid;
    }
}
