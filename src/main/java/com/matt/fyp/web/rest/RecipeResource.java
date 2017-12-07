package com.matt.fyp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matt.fyp.domain.Recipe;

import com.matt.fyp.repository.RecipeRepository;
import com.matt.fyp.repository.search.RecipeSearchRepository;
import com.matt.fyp.web.rest.errors.BadRequestAlertException;
import com.matt.fyp.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Recipe.
 */
@RestController
@RequestMapping("/api")
public class RecipeResource {

    private final Logger log = LoggerFactory.getLogger(RecipeResource.class);

    private static final String ENTITY_NAME = "recipe";

    private final RecipeRepository recipeRepository;

    private final RecipeSearchRepository recipeSearchRepository;

    public RecipeResource(RecipeRepository recipeRepository, RecipeSearchRepository recipeSearchRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeSearchRepository = recipeSearchRepository;
    }

    /**
     * POST  /recipes : Create a new recipe.
     *
     * @param recipe the recipe to create
     * @return the ResponseEntity with status 201 (Created) and with body the new recipe, or with status 400 (Bad Request) if the recipe has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/recipes")
    @Timed
    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) throws URISyntaxException {
        log.debug("REST request to save Recipe : {}", recipe);
        if (recipe.getId() != null) {
            throw new BadRequestAlertException("A new recipe cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recipe result = recipeRepository.save(recipe);
        recipeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /recipes : Updates an existing recipe.
     *
     * @param recipe the recipe to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated recipe,
     * or with status 400 (Bad Request) if the recipe is not valid,
     * or with status 500 (Internal Server Error) if the recipe couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/recipes")
    @Timed
    public ResponseEntity<Recipe> updateRecipe(@RequestBody Recipe recipe) throws URISyntaxException {
        log.debug("REST request to update Recipe : {}", recipe);
        if (recipe.getId() == null) {
            return createRecipe(recipe);
        }
        Recipe result = recipeRepository.save(recipe);
        recipeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, recipe.getId().toString()))
            .body(result);
    }

    /**
     * GET  /recipes : get all the recipes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of recipes in body
     */
    @GetMapping("/recipes")
    @Timed
    public List<Recipe> getAllRecipes() {
        log.debug("REST request to get all Recipes");
        return recipeRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /recipes/:id : get the "id" recipe.
     *
     * @param id the id of the recipe to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the recipe, or with status 404 (Not Found)
     */
    @GetMapping("/recipes/{id}")
    @Timed
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long id) {
        log.debug("REST request to get Recipe : {}", id);
        Recipe recipe = recipeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(recipe));
    }

    /**
     * DELETE  /recipes/:id : delete the "id" recipe.
     *
     * @param id the id of the recipe to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/recipes/{id}")
    @Timed
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        log.debug("REST request to delete Recipe : {}", id);
        recipeRepository.delete(id);
        recipeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/recipes?query=:query : search for the recipe corresponding
     * to the query.
     *
     * @param query the query of the recipe search
     * @return the result of the search
     */
    @GetMapping("/_search/recipes")
    @Timed
    public List<Recipe> searchRecipes(@RequestParam String query) {
        log.debug("REST request to search Recipes for query {}", query);
        return StreamSupport
            .stream(recipeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
