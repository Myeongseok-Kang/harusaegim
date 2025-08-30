package io.github.myeongseokkang.harusaegim.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignupRequest {
    @Email @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String displayName;

}
