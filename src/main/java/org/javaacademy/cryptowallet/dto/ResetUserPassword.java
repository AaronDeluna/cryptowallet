package org.javaacademy.cryptowallet.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResetUserPassword {
    @NonNull
    private String login;
    @NonNull
    private String oldPassword;
    @NonNull
    private String newPassword;
}
