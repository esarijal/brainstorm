package com.mitrais.brainstorm.persistence.repository;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.IntStream;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.text.TextProducer;
import com.mitrais.brainstorm.persistence.domain.Comment;
import com.mitrais.brainstorm.persistence.domain.Post;
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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class PostRepositoryTest {
	@Autowired
	PostRepository postRepository;
	@Autowired
	ReactiveMongoOperations operations;

	TextProducer tp = Fairy.create().textProducer();

	@BeforeEach
	void init(){
		var recreateCollection = operations.collectionExists(Post.class)
				.flatMap(exists -> exists ? operations.dropCollection(Post.class) : Mono.just(exists))
				.then(operations
						.createCollection(Post.class, CollectionOptions.empty()
								.size(1024 * 1024)
								.maxDocuments(100)
								.capped()
						)
				);

		StepVerifier.create(recreateCollection).expectNextCount(1).verifyComplete();

		var insertAll = operations.insertAll(Flux
				.just(
						new Post(Post.Type.QUESTION, "1235 " + tp.sentence(), tp.paragraph(5)),
						new Post(Post.Type.QUESTION, tp.sentence(), tp.paragraph(5)),
						new Post(Post.Type.ANSWER, tp.sentence(), tp.paragraph(5)),
						new Post(Post.Type.ANSWER, tp.sentence(), tp.paragraph(5))
				)
				.collectList()
		);
		StepVerifier.create(insertAll).expectNextCount(4).verifyComplete();
	}

	@Test
	void findByTitleContaining() {
		StepVerifier.create(postRepository
				.findByTitleContaining(Mono.just("1235"))
				.doOnNext(System.out::println))
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void findByType() {
		StepVerifier.create(postRepository
				.findByType(Mono.just(Post.Type.QUESTION), Pageable.unpaged())
				.doOnNext(System.out::println)) //
				.expectNextCount(2) //
				.verifyComplete();
	}

	@Test
	void saveWithComments(){
		var posts = new ArrayList<Post>();
		IntStream.rangeClosed(1, 4).forEach(i->{
			var post = new Post(Post.Type.QUESTION, "1235 " + tp.sentence(), tp.paragraph(5));
			post.setComments(Set.of(new Comment(tp.paragraph(5))));
			posts.add(post);
		});
		var flux = Flux.fromIterable(posts);
		StepVerifier.create(postRepository.saveAll(flux).collectList())
				.assertNext(listOfPost ->
						listOfPost.forEach(post ->
								post.getComments()
										.forEach(comment ->
												assertThat(comment.getId()).isNotNull()
										)
						)
				)
				.verifyComplete();
	}
}