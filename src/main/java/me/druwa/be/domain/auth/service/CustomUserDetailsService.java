package me.druwa.be.domain.auth.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import me.druwa.be.domain.auth.exception.ResourceNotFoundException;
import me.druwa.be.domain.auth.model.UserPrincipal;
import me.druwa.be.domain.common.cache.CacheKey;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    @Cacheable(cacheNames = CacheKey.User.EMAIL, key = "#email")
    public UserDetails loadUserByUsername(String email) {
        final User user = userRepository.findByEmail(email)
                                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));

        return UserPrincipal.create(user);
    }

    @Transactional
    @Cacheable(cacheNames = CacheKey.User.ID, key = "#id")
    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        return UserPrincipal.create(user);
    }
}