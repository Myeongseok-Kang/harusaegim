package io.github.myeongseokkang.harusaegim.service;

import io.github.myeongseokkang.harusaegim.entity.NotificationEvent;
import io.github.myeongseokkang.harusaegim.entity.PhotoEvent;
import io.github.myeongseokkang.harusaegim.dto.NotificationUploadRequest;
import io.github.myeongseokkang.harusaegim.dto.PhotoUploadRequest;
import io.github.myeongseokkang.harusaegim.repository.NotificationEventRepository;
import io.github.myeongseokkang.harusaegim.repository.PhotoEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectorService {
    private final NotificationEventRepository notificationRepo;
    private final PhotoEventRepository photoRepo;

    public CollectorService(NotificationEventRepository notificationRepo, PhotoEventRepository photoRepo) {
        this.notificationRepo = notificationRepo;
        this.photoRepo = photoRepo;
    }

    @Transactional
    public void saveNotification(Long userId, NotificationUploadRequest r) {
        NotificationEvent e = new NotificationEvent();
        e.setUserId(userId);
        e.setPackageName(r.getPackageName());
        e.setTitle(r.getTitle());
        e.setText(r.getText());
        e.setPostedAt(r.getPostedAt());
        notificationRepo.save(e);
    }

    @Transactional
    public void savePhoto(Long userId, PhotoUploadRequest r) {
        PhotoEvent p = new PhotoEvent();
        p.setUserId(userId);
        p.setClientPhotoId(r.getClientPhotoId());
        p.setTakenAt(r.getTakenAt());
        p.setLatitude(r.getLatitude());
        p.setLongitude(r.getLongitude());
        photoRepo.save(p);
    }

    @Transactional
    public void saveNotifications(Long userId, List<NotificationUploadRequest> list) {
        for (NotificationUploadRequest r : list) saveNotification(userId, r);
    }

    @Transactional
    public void savePhotos(Long userId, List<PhotoUploadRequest> list) {
        for (PhotoUploadRequest r : list) savePhoto(userId, r);
    }
}
