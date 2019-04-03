package com.mitrais.brainstorm.web.controller;

import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitrais.brainstorm.persistence.domain.Comment;
import com.mitrais.brainstorm.persistence.domain.Post;
import com.mitrais.brainstorm.persistence.repository.PostRepository;
import com.mitrais.brainstorm.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(PostController.class)
@WithMockUser(roles = {"CREATE_POST"})
class PostControllerTest {
	@Autowired
	private	MockMvc mockMvc;

	@Autowired
	private ObjectMapper mapper;

	@MockBean
	private	PostRepository postRepository;

	@MockBean
	private UserRepository userRepository;

	@Test
	void findByType() throws Exception {
		Post.Type type = Post.Type.QUESTION;
		given(postRepository.findByType(any(), any())).willReturn(Flux.just(new Post(
				Post.Type.QUESTION, "123", "123")));
		mockMvc.perform(get(PostController.PATH).param("type", type.toString())).andExpect(status().isOk());
	}

	@Test
	void findById() throws Exception {
		String controllerPath = PostController.PATH + "/{id}";
		String id = UUID.randomUUID().toString();
		given(postRepository.findById(ArgumentMatchers.<String>any())).willReturn(Mono.just(new Post(
				Post.Type.QUESTION, "123", "123")));
		mockMvc.perform(get(controllerPath, id)).andExpect(status().isOk());
	}

	@Test
	void save() throws Exception {
		Post post = new Post(Post.Type.QUESTION, "123", "123");
		post.setComments(Set.of(new Comment("Great post!")));
		given(postRepository.save(any())).willReturn(Mono.just(post));
		mockMvc.perform(post(PostController.PATH)
				.content(mapper.writeValueAsString(post))
				.contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andDo(print());
	}
}