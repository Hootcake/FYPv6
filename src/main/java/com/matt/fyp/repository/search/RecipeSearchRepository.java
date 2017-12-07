package com.matt.fyp.repository.search;

import com.matt.fyp.domain.Recipe;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Recipe entity.
 */
public interface RecipeSearchRepository extends ElasticsearchRepository<Recipe, Long> {
}
