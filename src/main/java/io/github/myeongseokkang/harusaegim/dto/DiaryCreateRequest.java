package io.github.myeongseokkang.harusaegim.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class DiaryCreateRequest {
    @NotNull
    private LocalDate date;
    @NotBlank
    private String place;
    @NotBlank
    private String activity;
    @NotBlank
    private String feeling;
}
