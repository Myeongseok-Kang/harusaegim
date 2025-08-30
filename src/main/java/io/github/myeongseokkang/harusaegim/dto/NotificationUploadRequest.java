package io.github.myeongseokkang.harusaegim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class NotificationUploadRequest {
    @NotBlank
    private String packageName;
    private String title;
    private String text;
    @NotNull
    private Instant postedAt;

}
