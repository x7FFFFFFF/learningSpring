package com.noname.learningSpring.security;

import com.noname.learningSpring.entities.Account;
import com.noname.learningSpring.entities.Privilege;
import com.noname.learningSpring.entities.Role;
import com.noname.learningSpring.security.matchers.id.IdMatcher;
import com.noname.learningSpring.security.matchers.id.IdMatcherFactory;
import com.noname.learningSpring.security.matchers.request.RequestMatcherFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class CustomUserDetails implements UserDetails, CredentialsContainer {


    // ~ Instance fields
    // ================================================================================================
    private String password;
    private final String username;
    private final Set<GrantedAuthority> authorities;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final Set<RequestMatcher> requestMatchers;
    private final Set<IdMatcher> idMatchers;



    public CustomUserDetails(Account account, IdMatcherFactory idMatcherFactory, RequestMatcherFactory requestMatcherFactory) {
        this.username = account.getUserName();
        this.password = account.getEncrytedPassword();
        this.accountNonExpired = account.isActive();
        this.accountNonLocked = account.isActive();
        this.credentialsNonExpired = account.isActive();
        this.enabled = account.isActive();
        this.authorities = getAuthorities(account.getRoles());
        final List<String> privileges = getPrivileges(account.getRoles());
        Set<RequestMatcher> setReqMatch = new HashSet<>();
        Set<IdMatcher> setIdMatch = new HashSet<>();
        for (String privilege : privileges) {
            if (!privilege.contains(IdMatcherFactory.ID_SYMBOL)) {
                setReqMatch.add(requestMatcherFactory.get(privilege));
            } else {
                setIdMatch.add(idMatcherFactory.get(privilege, true));
            }
        }
        this.requestMatchers = setReqMatch;
        this.idMatchers = setIdMatch;
    }

    public boolean isAccessGranted(HttpServletRequest request) {
        for (RequestMatcher requestMatcher : requestMatchers) {
            if (requestMatcher.matches(request)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAccessGranted(HttpServletRequest request, String id) {
        if (!isAccessGranted(request)) {
            return false;
        }
        for (IdMatcher idMatcher : idMatchers) {
            if (idMatcher.matches(id)) {
                return true;
            }
        }

        return false;
    }

    private Set<GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private Set<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return authorities;
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
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }


    @Override
    public void eraseCredentials() {
        password = null;
    }


    @Override
    public boolean equals(Object rhs) {
        if (rhs instanceof CustomUserDetails) {
            return username.equals(((CustomUserDetails) rhs).username);
        }
        return false;
    }

    /**
     * Returns the hashcode of the {@code username}.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
