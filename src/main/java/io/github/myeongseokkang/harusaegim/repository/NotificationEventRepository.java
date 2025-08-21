package io.github.myeongseokkang.harusaegim.repository;

import io.github.myeongseokkang.harusaegim.entity.NotificationEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface NotificationEventRepository extends JpaRepository<NotificationEvent,Long> {
    List<NotificationEvent> findByUserIdAndPostedAtBetween(Long userId, Instant start, Instant end);
}
