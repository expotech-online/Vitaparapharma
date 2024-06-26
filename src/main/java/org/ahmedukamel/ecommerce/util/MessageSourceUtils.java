package org.ahmedukamel.ecommerce.util;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class MessageSourceUtils {
    MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }

    public String getMessage(String code, Object... args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public String getMessageByLocale(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessageByLocale(String code, Locale locale, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
