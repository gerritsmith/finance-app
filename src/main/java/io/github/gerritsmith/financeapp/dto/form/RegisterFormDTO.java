package io.github.gerritsmith.financeapp.dto.form;

import io.github.gerritsmith.financeapp.dto.form.LoginFormDTO;

public class RegisterFormDTO extends LoginFormDTO {

    private String verifyPassword;

    public String getVerifyPassword() {
        return verifyPassword;
    }

    public void setVerifyPassword(String verifyPassword) {
        this.verifyPassword = verifyPassword;
    }

}
