package com.matt.fyp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Ingredient.
 */
@Entity
@Table(name = "ingredient")
@Document(indexName = "ingredient")
public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_of_measure")
    private String unit_of_measure;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "weight")
    private Double weight;

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private Set<ShoppingList> shoppingLists = new HashSet<>();

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private Set<Inventory> inventories = new HashSet<>();

    @ManyToMany(mappedBy = "ingredients")
    @JsonIgnore
    private Set<Recipe> recipes = new HashSet<>();

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

    public Ingredient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit_of_measure() {
        return unit_of_measure;
    }

    public Ingredient unit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
        return this;
    }

    public void setUnit_of_measure(String unit_of_measure) {
        this.unit_of_measure = unit_of_measure;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Ingredient quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getWeight() {
        return weight;
    }

    public Ingredient weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Set<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public Ingredient shoppingLists(Set<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
        return this;
    }

    public Ingredient addShoppingLists(ShoppingList shoppingList) {
        this.shoppingLists.add(shoppingList);
        shoppingList.getIngredients().add(this);
        return this;
    }

    public Ingredient removeShoppingLists(ShoppingList shoppingList) {
        this.shoppingLists.remove(shoppingList);
        shoppingList.getIngredients().remove(this);
        return this;
    }

    public void setShoppingLists(Set<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }

    public Set<Inventory> getInventories() {
        return inventories;
    }

    public Ingredient inventories(Set<Inventory> inventories) {
        this.inventories = inventories;
        return this;
    }

    public Ingredient addInventories(Inventory inventory) {
        this.inventories.add(inventory);
        inventory.getIngredients().add(this);
        return this;
    }

    public Ingredient removeInventories(Inventory inventory) {
        this.inventories.remove(inventory);
        inventory.getIngredients().remove(this);
        return this;
    }

    public void setInventories(Set<Inventory> inventories) {
        this.inventories = inventories;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public Ingredient recipes(Set<Recipe> recipes) {
        this.recipes = recipes;
        return this;
    }

    public Ingredient addRecipes(Recipe recipe) {
        this.recipes.add(recipe);
        recipe.getIngredients().add(this);
        return this;
    }

    public Ingredient removeRecipes(Recipe recipe) {
        this.recipes.remove(recipe);
        recipe.getIngredients().remove(this);
        return this;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
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
        Ingredient ingredient = (Ingredient) o;
        if (ingredient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ingredient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unit_of_measure='" + getUnit_of_measure() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", weight='" + getWeight() + "'" +
            "}";
    }
}
