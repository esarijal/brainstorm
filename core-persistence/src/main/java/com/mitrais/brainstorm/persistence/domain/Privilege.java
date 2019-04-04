package com.mitrais.brainstorm.persistence.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

/**
 *
 */
@Document(Privilege.COLLECTION_NAME)
@RequiredArgsConstructor
@Getter
@ToString
public class Privilege implements GrantedAuthority {
	public static final String COLLECTION_NAME = "brainstorm.privileges";

	@Getter
	private String id;

	@NonNull
	@Setter
	private String authority;
}
