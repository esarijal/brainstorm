package com.mitrais.brainstorm.persistence.repository;

import java.util.Set;

import com.mitrais.brainstorm.persistence.domain.Privilege;
import com.mitrais.brainstorm.persistence.domain.User;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserRepositoryTest {
	@Autowired
	ReactiveMongoOperations operations;

	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void init(){
		Set<Privilege> privileges = initPrivilege();
		Mono<MongoCollection<Document>> recreateCollection = operations.collectionExists(User.class) //
				.flatMap(exists -> exists ? operations.dropCollection(User.class) : Mono.just(exists)) //
				.then(operations.createCollection(User.class, CollectionOptions.empty() //
						.size(1024 * 1024) //
						.maxDocuments(100) //
						.capped()));

		StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();
		User user = new User("John Doe", "john.doe@brainstorm.com", "123");
		user.setPrivileges(privileges);
		Flux<User> insertAll = operations.insertAll(Flux.just(user).collectList());
		StepVerifier.create(insertAll).expectNextCount(1).verifyComplete();
	}

	@Test
	void findByEmail() {
		StepVerifier.create(userRepository.findByEmail(Mono.just("john.doe@brainstorm.com")).doOnNext(System.out::println)) //
				.assertNext(u-> assertThat(u.getAuthorities().size()).isEqualTo(4))
				.expectNextCount(0)
				.verifyComplete();
	}

	private Set<Privilege> initPrivilege(){
		Mono<MongoCollection<Document>> recreateCollection = operations.collectionExists(Privilege.class) //
				.flatMap(exists -> exists ? operations.dropCollection(Privilege.class) : Mono.just(exists)) //
				.then(operations.createCollection(Privilege.class, CollectionOptions.empty() //
						.size(1024 * 1024) //
						.maxDocuments(100) //
						.capped()));

		StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();
		Set<Privilege> privileges = Set.of(new Privilege("CREATE_POST"),
				new Privilege("UPDATE_POST"), //
				new Privilege("DELETE_POST"), //
				new Privilege("CREATE_COMMENT"));
		Flux<Privilege> insertAll = operations.insertAll(Flux.fromIterable(privileges).collectList());
		StepVerifier.create(insertAll).expectNextCount(4).verifyComplete();
		return privileges;
	}
}