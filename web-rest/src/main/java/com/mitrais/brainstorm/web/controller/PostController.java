package com.mitrais.brainstorm.web.controller;

import com.mitrais.brainstorm.persistence.domain.Post;
import com.mitrais.brainstorm.persistence.repository.PostRepository;
import com.mitrais.brainstorm.web.resource.PostResourceAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 */
@RestController
@RequestMapping(PostController.PATH)
@RequiredArgsConstructor
public class PostController {
	static final String PATH = "/posts";
	private final PostRepository postRepository;
	private final PostResourceAssembler postResourceAssembler;

	@PreAuthorize("hasRole('ROLE_CREATE_POST')")
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, produces = MediaTypes.HAL_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Resource<Post>> save(@RequestBody Post post){
		post = postRepository.save(post).block();
		Resource<Post> resource = postResourceAssembler.toResource(post);
		return ResponseEntity.ok(resource);
	}

	@GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resources<Resource<Post>>> findByType(@RequestParam Post.Type type, Pageable pageable) {
		Iterable<Post> posts = postRepository.findByType(Mono.just(type), pageable).toIterable();
		return ResponseEntity.ok(postResourceAssembler.toResources(posts));
	}

	@GetMapping(path = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
	public ResponseEntity<Resource<Post>> findById(@PathVariable String id) {
		Post post = postRepository.findById(id).block();
		Resource<Post> resource = postResourceAssembler.toResource(post);
		return ResponseEntity.ok(resource);
	}
}
