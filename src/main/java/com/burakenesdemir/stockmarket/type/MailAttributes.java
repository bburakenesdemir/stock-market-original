package com.burakenesdemir.stockmarket.type;

public enum MailAttributes {
    ACTIVATION_CODE("activationCode", "Aktivasyon İşlemi"),
    MAIL_SENDER("mailSender", "Qunectus"),
    REGISTRATION_COMPLETE("registrationComplete","Kullanıcı Kayıt İşlemi Tamamlandı"),
    AUTHORIZATION_COMPLETE("authorizationComplete","Kullanıcı Giriş İşlemi Başarılı"),
    ACTIVATION_SUCCESS("activationSuccessMailTemplate","Activasyon işleminiz başarıyla tamamlanmıştır."),
    RESET_PASSWORD("resetPassword","Şifre Sıfırlama");

    private final String name;
    private final String value;

    MailAttributes(String value, String name) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public static MailAttributes getMailAttributes(String value) {
        for (MailAttributes t : MailAttributes.values()) {
            if (t.getValue().equals(value)) {
                return t;
            }
        }
        return null;
    }
}