package com.newtech.Services.Security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.newtech.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nom;
    private String email;


    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;
    private boolean active;

    public static UserPrincipal create(User user) {
        List<GrantedAuthority> authorities = (user.getRoles() != null) ?
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList())
                : List.of();

        return new UserPrincipal(
                user.getId(),
                user.getNom(),
                user.getEmail(),
                user.getMotDePasse(),
                authorities,
                user.isActive()
        );
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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
        return active;
    }

    // Méthode pratique pour obtenir l'ID de l'utilisateur
    public Long getUserId() {
        return id;
    }
}
