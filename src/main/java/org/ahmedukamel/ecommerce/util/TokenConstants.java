package org.ahmedukamel.ecommerce.util;

public interface TokenConstants {
    String BASE_URL = "https://api.vitaparapharma.com/";
    String ACTIVATE_ACCOUNT_URL = "p/activate-account/";
    String FORGET_PASSWORD_URL = "p/change-password/";
    //    String VITA_ACTIVATE_ACCOUNT_URL = "http://localhost:8080/maile-sender/api/v1/email-template/VITA_ACTIVATE_ACCOUNT";
//    String VITA_FORGET_PASSWORD_URL = "http://localhost:8080/maile-sender/api/v1/email-template/VITA_FORGET_PASSWORD";
//    String VITA_PASSWORD_CHANGED_URL = "http://localhost:8080/maile-sender/api/v1/email-template/VITA_PASSWORD_CHANGED";
    String VITA_FORGET_PASSWORD_URL = "https://email.vitaparapharma.com/api/v1/email-template/VITA_FORGET_PASSWORD";
    String VITA_ACTIVATE_ACCOUNT_URL = "https://email.vitaparapharma.com/api/v1/email-template/VITA_ACTIVATE_ACCOUNT";
    String VITA_PASSWORD_CHANGED_URL = "https://email.vitaparapharma.com/api/v1/email-template/VITA_PASSWORD_CHANGED";
}
