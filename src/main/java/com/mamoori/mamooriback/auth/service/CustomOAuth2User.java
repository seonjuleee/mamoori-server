package com.mamoori.mamooriback.auth.service;

import com.mamoori.mamooriback.api.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private String name;
    private Role role;

    private String socialType;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities,
                            Map<String, Object> attributes,
                            String nameAttributeKey,
                            String email,
                            String name,
                            String socialType,
                            Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.name = name;
        this.socialType = socialType;
        this.role = role;
    }


/*    private User user;
    private Map<String, Object> attributes;*/

/*    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }*/

/*
    // OAuth2User Interface
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // UserDetails Interface
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return String.valueOf(user.getRole());
            }
        });
        return collection;
    }

    @Override
    public String getName() {
        return null;
    }
*/


/*
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

        // 휴면 계정 검증하는 로직
        // ex) 현재 시간 - 마지막 로그인 시간 => 1년 초과하면 return false
        return true;
    }

    */

}
