package com.mitrais.brainstorm.persistence.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Auditable;

/**
 * Store post's comments. This class should be embedded in the parent class.
 *
 * @author Kustian
 * @see Post
 */
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
@ToString
public class Comment implements Auditable<User, String, LocalDateTime> {
	@Getter
	private String id = new ObjectId().toString();

	@NonNull
	@Getter
	@Setter
	private String text;

	private User lastModifiedBy;

	private LocalDateTime lastModifiedDate;

	@Override
	public Optional<User> getCreatedBy() {
		return Optional.ofNullable(lastModifiedBy);
	}

	@Override
	public void setCreatedBy(User createdBy) {
		this.lastModifiedBy = createdBy;
	}

	@Override
	public Optional<LocalDateTime> getCreatedDate() {
		return Optional.ofNullable(lastModifiedDate);
	}

	@Override
	public void setCreatedDate(LocalDateTime creationDate) {
		this.lastModifiedDate = creationDate;
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
	public Optional<LocalDateTime> getLastModifiedDate() {
		return Optional.ofNullable(lastModifiedDate);
	}

	@Override
	public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Override
	public boolean isNew() {
		return id == null;
	}
}
