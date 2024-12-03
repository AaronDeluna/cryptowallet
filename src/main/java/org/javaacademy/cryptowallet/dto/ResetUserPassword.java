package org.javaacademy.cryptowallet.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class ResetUserPassword {
    @NonNull
    private String login;
    @NonNull
    private String oldPassword;
    @NonNull
    private String newPassword;
}
