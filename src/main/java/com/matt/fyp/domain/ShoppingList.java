package com.matt.fyp.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A ShoppingList.
 */
@Entity
@Table(name = "shopping_list")
@Document(indexName = "shoppinglist")
public class ShoppingList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    private User user;

    @ManyToMany
    @JoinTable(name = "shopping_list_ingredients",
               joinColumns = @JoinColumn(name="shopping_lists_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="ingredients_id", referencedColumnName="id"))
    private Set<Ingredient> ingredients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public ShoppingList name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public ShoppingList notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public ShoppingList user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public ShoppingList ingredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public ShoppingList addIngredients(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.getShoppingLists().add(this);
        return this;
    }

    public ShoppingList removeIngredients(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.getShoppingLists().remove(this);
        return this;
    }

    public void setIngredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShoppingList shoppingList = (ShoppingList) o;
        if (shoppingList.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shoppingList.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
