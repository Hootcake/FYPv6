package com.matt.fyp.repository;

import com.matt.fyp.domain.Recipe;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Recipe entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("select recipe from Recipe recipe where recipe.created_by.login = ?#{principal.username}")
    List<Recipe> findByCreated_byIsCurrentUser();

    @Query("select recipe from Recipe recipe where recipe.favorited_by.login = ?#{principal.username}")
    List<Recipe> findByFavorited_byIsCurrentUser();
    @Query("select distinct recipe from Recipe recipe left join fetch recipe.ingredients")
    List<Recipe> findAllWithEagerRelationships();

    @Query("select recipe from Recipe recipe left join fetch recipe.ingredients where recipe.id =:id")
    Recipe findOneWithEagerRelationships(@Param("id") Long id);

}
