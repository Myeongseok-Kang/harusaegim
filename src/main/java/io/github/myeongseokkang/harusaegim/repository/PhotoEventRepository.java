package io.github.myeongseokkang.harusaegim.repository;

import io.github.myeongseokkang.harusaegim.entity.PhotoEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface PhotoEventRepository extends JpaRepository<PhotoEvent,Long> {
    List<PhotoEvent> findByUserIdAndTakenAtBetween(Long userId, Instant start, Instant end);
}
