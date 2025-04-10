package com.barretoga.choosehub.badge;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    Badge findTopByMinBooksLessThanEqualOrderByMinBooksDesc(long totalBooks);
}
