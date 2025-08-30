package io.github.myeongseokkang.harusaegim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class PhotoUploadRequest {
    @NotBlank
    private String clientPhotoId;
    @NotNull
    private Instant takenAt;
    private Double latitude;
    private Double longitude;

}
