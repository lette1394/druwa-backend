package me.druwa.be.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import me.druwa.be.domain.user.annotation.CurrentUser;
import me.druwa.be.domain.user.model.User;

@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/users/me")
    public ResponseEntity<?> getCurrentUser(@CurrentUser User user) {
        return ResponseEntity.ok(user.toResponse());
    }
}
