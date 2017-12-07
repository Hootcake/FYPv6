package com.matt.fyp.repository.search;

import com.matt.fyp.domain.Review;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Review entity.
 */
public interface ReviewSearchRepository extends ElasticsearchRepository<Review, Long> {
}
