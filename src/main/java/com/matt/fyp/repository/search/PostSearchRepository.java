package com.matt.fyp.repository.search;

import com.matt.fyp.domain.Post;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Post entity.
 */
public interface PostSearchRepository extends ElasticsearchRepository<Post, Long> {
}
