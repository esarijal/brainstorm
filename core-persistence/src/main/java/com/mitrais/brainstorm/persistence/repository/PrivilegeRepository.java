package com.mitrais.brainstorm.persistence.repository;

import com.mitrais.brainstorm.persistence.domain.Privilege;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 */
public interface PrivilegeRepository extends ReactiveCrudRepository<Privilege, String> {
	Flux<Privilege> findByAuthority(Mono<String> authority);

	Flux<Privilege> findByIdExists(Mono<Boolean> exist, Pageable pageable);
}
