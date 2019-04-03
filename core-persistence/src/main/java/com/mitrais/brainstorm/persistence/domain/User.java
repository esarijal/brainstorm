package com.mitrais.brainstorm.persistence.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Reference;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 */
@Document(User.COLLECTION_NAME)
@RequiredArgsConstructor
@Getter
@ToString
public class User implements Persistable<String>, UserDetails {
    public static final String COLLECTION_NAME = "brainstorm.users";

	private String id;

	@NonNull
	@Setter
	private String displayName;

	@NonNull
	@Setter
	private String email;

	@NonNull
	@Setter
	private String password;

	@Setter
	private String firstName;

	@Setter
	private String lastName;

	@Setter
	private boolean enabled;

	@Setter
	@Reference
	private Set<Privilege> privileges = new HashSet<>();

	public Set<Privilege> getPrivileges(){
		return Set.copyOf(privileges);
	}

	public Set<Privilege> addPrivileges(Privilege... privileges){
		this.privileges.addAll(Set.of(privileges));
		return Set.copyOf(this.privileges);
	}
	public Set<Privilege> removePrivileges(Privilege... privileges){
		this.privileges.removeAll(Set.of(privileges));
		return Set.copyOf(this.privileges);
	}

	@Override
	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return privileges;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
}