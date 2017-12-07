package com.matt.fyp.repository;

import com.matt.fyp.domain.ShoppingList;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the ShoppingList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    @Query("select shopping_list from ShoppingList shopping_list where shopping_list.user.login = ?#{principal.username}")
    List<ShoppingList> findByUserIsCurrentUser();
    @Query("select distinct shopping_list from ShoppingList shopping_list left join fetch shopping_list.ingredients")
    List<ShoppingList> findAllWithEagerRelationships();

    @Query("select shopping_list from ShoppingList shopping_list left join fetch shopping_list.ingredients where shopping_list.id =:id")
    ShoppingList findOneWithEagerRelationships(@Param("id") Long id);

}
