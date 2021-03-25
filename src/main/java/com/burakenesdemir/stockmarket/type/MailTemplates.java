package com.burakenesdemir.stockmarket.type;

public enum MailTemplates {
    REGISTER_ACTIVATION_MAIL("registerActivationMailTemplate"),
    AUTHORIZATION_CODE("authorizationCodeMailTemplate"),
    RESET_PASSWORD("resetPasswordMailTemplate"),
    ACTIVATION_SUCCESS("activationSuccessMailTemplate");

    private final String name;

    MailTemplates(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}