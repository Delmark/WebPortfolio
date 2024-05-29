package com.delmark.portfoilo.models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.delmark.portfoilo.models.User}
 */
@Value
public class UserDto implements Serializable {
    @NotBlank(message = "Логин не должен быть пустым")
    @Size(min = 5, max = 32, message = "Логин должен содержать в себе от 5 до 32 символов")
    @NotNull(message = "Требуется логин")
    String username;

    @NotNull(message = "Требуется пароль")
    @NotBlank(message = "Пароль не должен быть пустым")
    @Size(min = 5, max = 32, message = "Пароль должен быть не меньше от 5 до 32 символов")
    String password;
}