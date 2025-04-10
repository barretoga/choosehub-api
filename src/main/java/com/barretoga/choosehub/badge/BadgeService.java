package com.barretoga.choosehub.badge;

import com.barretoga.choosehub.user.User;
import com.barretoga.choosehub.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BadgeService {

    private final UserRepository userRepository;

    public Set<Badge> getBadgesByUserId(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return user.getBadges();
    }
}
