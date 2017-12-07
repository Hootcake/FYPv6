package com.matt.fyp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.matt.fyp.domain.Inventory;

import com.matt.fyp.repository.InventoryRepository;
import com.matt.fyp.repository.search.InventorySearchRepository;
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
 * REST controller for managing Inventory.
 */
@RestController
@RequestMapping("/api")
public class InventoryResource {

    private final Logger log = LoggerFactory.getLogger(InventoryResource.class);

    private static final String ENTITY_NAME = "inventory";

    private final InventoryRepository inventoryRepository;

    private final InventorySearchRepository inventorySearchRepository;

    public InventoryResource(InventoryRepository inventoryRepository, InventorySearchRepository inventorySearchRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventorySearchRepository = inventorySearchRepository;
    }

    /**
     * POST  /inventories : Create a new inventory.
     *
     * @param inventory the inventory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inventory, or with status 400 (Bad Request) if the inventory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/inventories")
    @Timed
    public ResponseEntity<Inventory> createInventory(@RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to save Inventory : {}", inventory);
        if (inventory.getId() != null) {
            throw new BadRequestAlertException("A new inventory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Inventory result = inventoryRepository.save(inventory);
        inventorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/inventories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inventories : Updates an existing inventory.
     *
     * @param inventory the inventory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inventory,
     * or with status 400 (Bad Request) if the inventory is not valid,
     * or with status 500 (Internal Server Error) if the inventory couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/inventories")
    @Timed
    public ResponseEntity<Inventory> updateInventory(@RequestBody Inventory inventory) throws URISyntaxException {
        log.debug("REST request to update Inventory : {}", inventory);
        if (inventory.getId() == null) {
            return createInventory(inventory);
        }
        Inventory result = inventoryRepository.save(inventory);
        inventorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, inventory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inventories : get all the inventories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inventories in body
     */
    @GetMapping("/inventories")
    @Timed
    public List<Inventory> getAllInventories() {
        log.debug("REST request to get all Inventories");
        return inventoryRepository.findAllWithEagerRelationships();
        }

    /**
     * GET  /inventories/:id : get the "id" inventory.
     *
     * @param id the id of the inventory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inventory, or with status 404 (Not Found)
     */
    @GetMapping("/inventories/{id}")
    @Timed
    public ResponseEntity<Inventory> getInventory(@PathVariable Long id) {
        log.debug("REST request to get Inventory : {}", id);
        Inventory inventory = inventoryRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(inventory));
    }

    /**
     * DELETE  /inventories/:id : delete the "id" inventory.
     *
     * @param id the id of the inventory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/inventories/{id}")
    @Timed
    public ResponseEntity<Void> deleteInventory(@PathVariable Long id) {
        log.debug("REST request to delete Inventory : {}", id);
        inventoryRepository.delete(id);
        inventorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/inventories?query=:query : search for the inventory corresponding
     * to the query.
     *
     * @param query the query of the inventory search
     * @return the result of the search
     */
    @GetMapping("/_search/inventories")
    @Timed
    public List<Inventory> searchInventories(@RequestParam String query) {
        log.debug("REST request to search Inventories for query {}", query);
        return StreamSupport
            .stream(inventorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
