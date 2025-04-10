package com.barretoga.choosehub.badge;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
public class BadgeController {

    private final BadgeService badgeService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Set<Badge>> getBadgesByUserId(@PathVariable UUID userId) {
        try {
            Set<Badge> badges = badgeService.getBadgesByUserId(userId);
            return ResponseEntity.ok(badges);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
