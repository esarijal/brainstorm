package com.mitrais.brainstorm.persistence.repository;

import com.mitrais.brainstorm.persistence.domain.Privilege;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
class PrivilegeRepositoryTest {
	@Autowired
	ReactiveMongoOperations operations;


	@Autowired
	PrivilegeRepository privilegeRepository;

	@BeforeEach
	void init(){
		Mono<MongoCollection<Document>> recreateCollection = operations.collectionExists(Privilege.class) //
				.flatMap(exists -> exists ? operations.dropCollection(Privilege.class) : Mono.just(exists)) //
				.then(operations.createCollection(Privilege.class, CollectionOptions.empty() //
						.size(1024 * 1024) //
						.maxDocuments(100) //
						.capped()));

		StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

		Flux<Privilege> insertAll = operations.insertAll(Flux.just(new Privilege("CREATE_POST"),
				new Privilege("UPDATE_POST"), //
				new Privilege("DELETE_POST"), //
				new Privilege("CREATE_COMMENT")).collectList());
		StepVerifier.create(insertAll).expectNextCount(4).verifyComplete();
	}

	@Test
	void findByAuthority() {
		StepVerifier.create(privilegeRepository.findByAuthority(Mono.just("CREATE_POST")).doOnNext(System.out::println)) //
				.expectNextCount(1) //
				.verifyComplete();
	}

	@Test
	void findByIdExists() {
		StepVerifier.create(privilegeRepository.findByIdExists(Mono.just(true), Pageable.unpaged()).doOnNext(System.out::println)) //
				.expectNextCount(4) //
				.verifyComplete();
	}
}