package com.mitrais.brainstorm.persistence.repository;

import com.mitrais.brainstorm.persistence.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

/**
 *
 */
public interface UserRepository extends ReactiveCrudRepository<User, String> {
	Mono<User> findByEmail(Mono<String> title);
}
