package io.github.myeongseokkang.harusaegim.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class PhotoEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String clientPhotoId; //중복 사진 식별
    private Instant takenAt;
    private Double latitude;
    private Double longitude;

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getClientPhotoId() { return clientPhotoId; }
    public void setClientPhotoId(String clientPhotoId) { this.clientPhotoId = clientPhotoId; }
    public Instant getTakenAt() { return takenAt; }
    public void setTakenAt(Instant takenAt) { this.takenAt = takenAt; }
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
}
