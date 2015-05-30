package my.thereisnospoon.webm.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Document(collection = "users")
public class User implements UserDetails {

	public static final String ROLE_USER = "ROLE_USER";

	@Id
	private String id;

	private String username;
	private String description;
	private Date createdWhen;
	private String password;
	private String avatarId;
	private Set<String> roles = new HashSet<>();
	private Set<String> likedVideos = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return roles.parallelStream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedWhen() {
		return createdWhen;
	}

	public void setCreatedWhen(Date createdWhen) {
		this.createdWhen = createdWhen;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatarId() {
		return avatarId;
	}

	public void setAvatarId(String avatarId) {
		this.avatarId = avatarId;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getLikedVideos() {
		return likedVideos;
	}

	public void setLikedVideos(Set<String> likedVideos) {
		this.likedVideos = likedVideos;
	}

	public void addToLikedVideos(String webmId) {
		likedVideos.add(webmId);
	}

	public void removeFromLikedVideos(String wembId) {
		likedVideos.remove(wembId);
	}

	@Override
	public String toString() {
		return "User{" +
				"id='" + id + '\'' +
				", username='" + username + '\'' +
				", description='" + description + '\'' +
				", createdWhen=" + createdWhen +
				", password='" + password + '\'' +
				", avatarId='" + avatarId + '\'' +
				", roles=" + roles +
				'}';
	}
}
