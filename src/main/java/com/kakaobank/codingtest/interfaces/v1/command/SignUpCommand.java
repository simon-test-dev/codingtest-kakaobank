package com.kakaobank.codingtest.interfaces.v1.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class SignUpCommand {
    @NotNull
    @NotEmpty
    @Size(min = 6, max = 40)
    private String username;
    @NotNull
    @NotEmpty
    @Size(min = 8, max = 40)
    private String password;
}
