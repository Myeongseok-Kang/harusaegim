package io.github.myeongseokkang.harusaegim.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateUserRequest {
    @NotBlank
    private String displayName;

}
