package com.matt.fyp.repository.search;

import com.matt.fyp.domain.Inventory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Inventory entity.
 */
public interface InventorySearchRepository extends ElasticsearchRepository<Inventory, Long> {
}
