package com.matt.fyp.repository;

import com.matt.fyp.domain.Inventory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Inventory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("select distinct inventory from Inventory inventory left join fetch inventory.ingredients")
    List<Inventory> findAllWithEagerRelationships();

    @Query("select inventory from Inventory inventory left join fetch inventory.ingredients where inventory.id =:id")
    Inventory findOneWithEagerRelationships(@Param("id") Long id);

}
