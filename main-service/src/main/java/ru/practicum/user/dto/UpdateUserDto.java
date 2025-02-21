package ru.practicum.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @Length(min = 6, max = 254)
    @NotBlank
    @Email
    private String email;
}
