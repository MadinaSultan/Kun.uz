package com.example.util;

import com.example.config.security.CustomUserDetails;
import com.example.entity.ProfileEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

    public static ProfileEntity getCurrentEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
       /*  System.out.println(user.getUsername());
        Collection<GrantedAuthority> roles = (Collection<GrantedAuthority>) user.getAuthorities();
         Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
       */
        return user.getProfile();
    }

    public static Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        return user.getProfile().getId();
    }
}
