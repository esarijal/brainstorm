package com.mitrais.brainstorm.persistence.repository;

import com.mitrais.brainstorm.persistence.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 */
@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, String> {
	Flux<Post> findByTitleContaining(Mono<String> title);

	Flux<Post> findByType(Mono<Post.Type> type, Pageable pageable);
}
