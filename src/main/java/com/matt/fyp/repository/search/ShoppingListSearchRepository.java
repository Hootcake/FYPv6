package com.matt.fyp.repository.search;

import com.matt.fyp.domain.ShoppingList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the ShoppingList entity.
 */
public interface ShoppingListSearchRepository extends ElasticsearchRepository<ShoppingList, Long> {
}
