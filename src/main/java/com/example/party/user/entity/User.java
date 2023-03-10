package com.example.party.user.entity;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.party.apply.entity.Apply;
import com.example.party.partypost.entity.PartyPost;
import com.example.party.user.type.Status;
import com.example.party.user.type.UserRole;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "email", nullable = false, unique = true, length = 50)
	private String email;
	@Column(name = "password", nullable = false, length = 60)
	private String password;
	@Column(name = "nickname", nullable = false, unique = true, length = 10)
	private String nickname;
	@Column(name = "phone_number", nullable = false, length = 13, columnDefinition = "CHAR(13)")
	private String phoneNum;

	// enum
	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 12)
	private UserRole role;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE', 'SUSPENDED')")
	private Status status;

	// 연관관계
	@OneToOne(optional = false)
	@JoinColumn(name = "profile_id", unique = true, referencedColumnName = "id")
	private Profile profile;
	@OneToMany(mappedBy = "user")
	private List<Apply> applies;
	@OneToMany(mappedBy = "user")
	private List<PartyPost> partyPosts;
	@ManyToMany
	@JoinTable(name = "likes",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "post_id")
	)
	private List<PartyPost> likePartyPosts;

	public String getProfileImg() {
		return this.profile.getImg();
	}

	public String getComment() {
		return this.profile.getComment();
	}

	public int getNoShowCnt() {
		return this.profile.getNoShowCnt();
	}

	public int getParticipationCnt() {
		return this.profile.getParticipationCnt();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<>();
		for (UserRole eachRole : UserRole.values()) {
			authorities.add(new SimpleGrantedAuthority(eachRole.name()));
		}
		return authorities;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}
}
