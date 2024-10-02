package com.movie_theaters.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.movie_theaters.entity.enums.MembershipLevel;
import com.movie_theaters.entity.enums.SignupDevice;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "VARCHAR(50)")
    private String username;
    @Column(columnDefinition = "VARCHAR(50)")
    private String email;
    @Column(columnDefinition = "VARCHAR(1000)")
    private String password;
    @Column(columnDefinition = "NVARCHAR(255)")
    private String fullName;
    @CreationTimestamp
    private LocalDateTime createdDate;
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    @Column(name = "date_of_birth", columnDefinition = "DATE")
    private LocalDate dob;
    @Column(columnDefinition = "VARCHAR(11)")
    private String phone;
    @Column(name = "avatar_url", columnDefinition = "VARCHAR(1000)")
    private String avatarUrl;
    @Column(name = "signup_device")
    @Enumerated(EnumType.STRING)
    private SignupDevice signupDevice;
    @Column(name = "member_ship_level")
    @Enumerated(EnumType.STRING)
    private MembershipLevel membershipLevel;
    @Column(name = "is_enabled")
    private Boolean isEnabled;
    private Boolean isConfirm;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Bill> bills = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<Review> reviews = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                .collect(Collectors.toList());
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
        return this.isEnabled;
    }
}
