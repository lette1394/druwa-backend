package me.druwa.be.domain.user.controller;

import java.util.Optional;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.auth.service.TokenProvider;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.apache.groovy.util.Maps;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok(user.toReadResponse());
    }

    @PostMapping("/users/signup")
    public ResponseEntity<?> create(@Valid final User.View.Create.Request body) {
        final User user = userService.save(body.toPartialUser(passwordEncoder));

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(user.toCreateResponse(tokenProvider));
    }

    @GetMapping("/users/signup/validate")
    public ResponseEntity<?> check(@RequestParam("name") final String name) {
        final Optional<User> userOptional = userService.findByName(name);

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .build();
        }
        return ResponseEntity.ok()
                             .build();
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> login(@Valid final User.View.Login.Request body) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        body.getEmail(),
                        body.getPassword()
                )
        );

        final String token = tokenProvider.createToken(authentication);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Maps.of("token", token));
    }

    @PostMapping("/users/find")
    public ResponseEntity<?> find(@Valid final User.View.Find.Request body) {
        final Optional<User> userOptional = userService.findByName(body.getName());
        if (false == userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }

        if (false == StringUtils.equals(body.getEmail(), userOptional.get().getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .build();
        }

        userService.sendVerifiedEmail(userOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                             .build();
    }

    @GetMapping("/users/validation")
    public ResponseEntity<?> find(@RequestParam("token") final String token) {
        if (false == tokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                 .build();
        }

        final Long userIdFromToken = tokenProvider.getUserIdFromToken(token);
        final User user = userService.markVerifiedUser(userIdFromToken);
        return ResponseEntity.status(HttpStatus.OK)
                             .build();
    }
}
