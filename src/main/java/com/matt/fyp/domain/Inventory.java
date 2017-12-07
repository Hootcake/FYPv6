package com.matt.fyp.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Inventory.
 */
@Entity
@Table(name = "inventory")
@Document(indexName = "inventory")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "inventory_ingredients",
               joinColumns = @JoinColumn(name="inventories_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="ingredients_id", referencedColumnName="id"))
    private Set<Ingredient> ingredients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public Inventory user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Inventory ingredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Inventory addIngredients(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.getInventories().add(this);
        return this;
    }

    public Inventory removeIngredients(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.getInventories().remove(this);
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
        Inventory inventory = (Inventory) o;
        if (inventory.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inventory.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Inventory{" +
            "id=" + getId() +
            "}";
    }
}
