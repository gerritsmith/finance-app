package io.github.gerritsmith.financeapp.dto.form;

import javax.validation.constraints.NotBlank;

public class ChangePasswordFormDTO {

    private String currentPassword;

    @NotBlank(message = "Password cannot be blank")
    private String newPassword;

    private String verifyPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

}
