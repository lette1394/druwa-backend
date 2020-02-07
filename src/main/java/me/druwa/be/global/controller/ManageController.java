package me.druwa.be.global.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import me.druwa.be.domain.user.annotation.AllowPublicAccess;

@RestController
@RequestMapping("/manage")
public class ManageController {

    @AllowPublicAccess
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("UP");
    }
}
