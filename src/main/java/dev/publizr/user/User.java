package dev.publizr.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record User(

        @NotEmpty
        @NotNull
        String username,

        @NotEmpty
        @NotNull
        String email,

        @NotEmpty
        @NotNull
        String password

) {
}
