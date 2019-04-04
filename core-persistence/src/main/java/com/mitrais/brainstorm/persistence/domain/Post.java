package com.mitrais.brainstorm.persistence.domain;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 */
@Document(Post.COLLECTION_NAME)
@RequiredArgsConstructor
@ToString(exclude = {"comments"})
@EqualsAndHashCode
public class Post implements Auditable<User, String, LocalDateTime> {
	public static final String COLLECTION_NAME = "brainstorm.posts";

	@Getter
	private String id;

	@Getter
	private final Type type;

	@NonNull
	@Getter
	@Setter
	private String title;

	@NonNull
	@Getter
	@Setter
	private String content;

	@Getter
	@Setter
	private Set<Comment> comments = new HashSet<>();

	private User createdBy;

	private User lastModifiedBy;

	private LocalDateTime createdDate;

	private LocalDateTime lastModifiedDate;

	@Override
	public boolean isNew() {
		return id == null;
	}

	@Override
	public Optional<User> getCreatedBy() {
		return Optional.ofNullable(createdBy);
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public Optional<User> getLastModifiedBy() {
		return Optional.ofNullable(lastModifiedBy);
	}

	@Override
	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public Optional<LocalDateTime> getCreatedDate() {
		return Optional.ofNullable(createdDate);
	}

	@Override
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public Optional<LocalDateTime> getLastModifiedDate() {
		return Optional.ofNullable(lastModifiedDate);
	}

	@Override
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 *
	 */
	public enum Type {
		QUESTION, ANSWER;

		public static Type fromString(String string){
			Objects.requireNonNull(string);
			return Type.valueOf(string.toUpperCase());
		}
	}
}
