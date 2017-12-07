package com.matt.fyp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matt.fyp.domain.ShoppingList;

import com.matt.fyp.repository.ShoppingListRepository;
import com.matt.fyp.repository.search.ShoppingListSearchRepository;
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
 * REST controller for managing ShoppingList.
 */
@RestController
@RequestMapping("/api")
public class ShoppingListResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingListResource.class);

    private static final String ENTITY_NAME = "shoppingList";

    private final ShoppingListRepository shoppingListRepository;

    private final ShoppingListSearchRepository shoppingListSearchRepository;

    public ShoppingListResource(ShoppingListRepository shoppingListRepository, ShoppingListSearchRepository shoppingListSearchRepository) {
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingListSearchRepository = shoppingListSearchRepository;
    }

    /**
     * POST  /shopping-lists : Create a new shoppingList.
     *
     * @param shoppingList the shoppingList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shoppingList, or with status 400 (Bad Request) if the shoppingList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/shopping-lists")
    @Timed
    public ResponseEntity<ShoppingList> createShoppingList(@RequestBody ShoppingList shoppingList) throws URISyntaxException {
        log.debug("REST request to save ShoppingList : {}", shoppingList);
        if (shoppingList.getId() != null) {
            throw new BadRequestAlertException("A new shoppingList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingList result = shoppingListRepository.save(shoppingList);
        shoppingListSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/shopping-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /shopping-lists : Updates an existing shoppingList.
     *
     * @param shoppingList the shoppingList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shoppingList,
     * or with status 400 (Bad Request) if the shoppingList is not valid,
     * or with status 500 (Internal Server Error) if the shoppingList couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/shopping-lists")
    @Timed
    public ResponseEntity<ShoppingList> updateShoppingList(@RequestBody ShoppingList shoppingList) throws URISyntaxException {
        log.debug("REST request to update ShoppingList : {}", shoppingList);
        if (shoppingList.getId() == null) {
            return createShoppingList(shoppingList);
        }
        ShoppingList result = shoppingListRepository.save(shoppingList);
        shoppingListSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, shoppingList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /shopping-lists : get all the shoppingLists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shoppingLists in body
     */
    @GetMapping("/shopping-lists")
    @Timed
    public List<ShoppingList> getAllShoppingLists() {
        log.debug("REST request to get all ShoppingLists");
        return shoppingListRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /shopping-lists/:id : get the "id" shoppingList.
     *
     * @param id the id of the shoppingList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shoppingList, or with status 404 (Not Found)
     */
    @GetMapping("/shopping-lists/{id}")
    @Timed
    public ResponseEntity<ShoppingList> getShoppingList(@PathVariable Long id) {
        log.debug("REST request to get ShoppingList : {}", id);
        ShoppingList shoppingList = shoppingListRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(shoppingList));
    }

    /**
     * DELETE  /shopping-lists/:id : delete the "id" shoppingList.
     *
     * @param id the id of the shoppingList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/shopping-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteShoppingList(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingList : {}", id);
        shoppingListRepository.delete(id);
        shoppingListSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/shopping-lists?query=:query : search for the shoppingList corresponding
     * to the query.
     *
     * @param query the query of the shoppingList search
     * @return the result of the search
     */
    @GetMapping("/_search/shopping-lists")
    @Timed
    public List<ShoppingList> searchShoppingLists(@RequestParam String query) {
        log.debug("REST request to search ShoppingLists for query {}", query);
        return StreamSupport
            .stream(shoppingListSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
