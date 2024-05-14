package org.ahmedukamel.ecommerce.util;

import org.ahmedukamel.ecommerce.model.Customer;
import org.ahmedukamel.ecommerce.model.enumeration.Provider;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContextUtils {
    public static Customer getPrinciple() {
        return (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getEmail() {
        return getPrinciple().getEmail();
    }

    public static Provider getProvider() {
        return getPrinciple().getProvider();
    }
}
