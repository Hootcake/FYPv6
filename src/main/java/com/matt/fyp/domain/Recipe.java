package com.matt.fyp.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Recipe.
 */
@Entity
@Table(name = "recipe")
@Document(indexName = "recipe")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_time")
    private String time;

    @Column(name = "prep")
    private String prep;

    @Column(name = "method")
    private String method;

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    private User created_by;

    @ManyToOne
    private User favorited_by;

    @ManyToMany
    @JoinTable(name = "recipe_ingredients",
               joinColumns = @JoinColumn(name="recipes_id", referencedColumnName="id"),
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

    public Recipe name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public Recipe time(String time) {
        this.time = time;
        return this;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrep() {
        return prep;
    }

    public Recipe prep(String prep) {
        this.prep = prep;
        return this;
    }

    public void setPrep(String prep) {
        this.prep = prep;
    }

    public String getMethod() {
        return method;
    }

    public Recipe method(String method) {
        this.method = method;
        return this;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getNotes() {
        return notes;
    }

    public Recipe notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getCreated_by() {
        return created_by;
    }

    public Recipe created_by(User user) {
        this.created_by = user;
        return this;
    }

    public void setCreated_by(User user) {
        this.created_by = user;
    }

    public User getFavorited_by() {
        return favorited_by;
    }

    public Recipe favorited_by(User user) {
        this.favorited_by = user;
        return this;
    }

    public void setFavorited_by(User user) {
        this.favorited_by = user;
    }

    public Set<Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe ingredients(Set<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public Recipe addIngredients(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.getRecipes().add(this);
        return this;
    }

    public Recipe removeIngredients(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
        ingredient.getRecipes().remove(this);
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
        Recipe recipe = (Recipe) o;
        if (recipe.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recipe.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Recipe{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", time='" + getTime() + "'" +
            ", prep='" + getPrep() + "'" +
            ", method='" + getMethod() + "'" +
            ", notes='" + getNotes() + "'" +
            "}";
    }
}
