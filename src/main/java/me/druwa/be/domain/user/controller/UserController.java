package me.druwa.be.domain.user.controller;

import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.auth.service.TokenProvider;
import me.druwa.be.domain.user.annotation.AllowPublicAccess;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;
import me.druwa.be.domain.user.service.UserService;
import me.druwa.be.global.exception.DruwaException;
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

    @AllowPublicAccess
    @PostMapping("/users/signup")
    public ResponseEntity<?> create(@Valid @RequestBody final User.View.Create.Request body) {
        if (userService.isExistedByEmail(body.toPartialUser(passwordEncoder))) {
            throw DruwaException.badRequest(String.format("duplicate user email: %s", body.getEmail()))
                                .appendExplain(body.getEmail());
        }

        final User user = userService.save(body.toPartialUser(passwordEncoder));
        userService.sendVerifiedEmail(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(user.toCreateResponse(tokenProvider));
    }

    @AllowPublicAccess
    @GetMapping("/users/signup/validate")
    public ResponseEntity<?> check(@RequestParam("email") @Email final String email) {
        final Optional<User> userOptional = userService.findByEmail(email);

        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .build();
        }
        return ResponseEntity.ok()
                             .build();
    }

    @AllowPublicAccess
    @PostMapping("/users/login")
    public ResponseEntity<?> login(@Valid @RequestBody final User.View.Login.Request body) {
        final User user = userService.findByEmail(body.getEmail())
                                     .orElseThrow(() -> DruwaException.badRequest(String.format(
                                             "No Found user email: %s",
                                             body.getEmail())));
        if (passwordEncoder.matches(body.getPassword(), user.getPassword()) == false) {
            throw DruwaException.badRequest("Invalid password");
        }

        final String token = tokenProvider.createToken(user);
        return ResponseEntity.status(HttpStatus.OK)
                             .body(Maps.of("token", token));
    }

    @AllowPublicAccess
    @PostMapping("/users/find")
    public ResponseEntity<?> find(@Valid @RequestBody final User.View.Find.Request body) {
        final Optional<User> userOptional = userService.findByName(body.getName());
        if (userOptional.isPresent() == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }

        if (StringUtils.equals(body.getEmail(), userOptional.get().getEmail()) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .build();
        }

        // TODO: 재설정 process 변경 필요
        userService.sendVerifiedEmail(userOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                             .build();
    }

    @AllowPublicAccess
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
