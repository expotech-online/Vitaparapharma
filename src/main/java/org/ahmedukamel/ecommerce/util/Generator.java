package org.ahmedukamel.ecommerce.util;

import java.security.SecureRandom;
import java.util.Random;

public class Generator {
    private static final Random random = new SecureRandom();

    public static String couponCode() {
        StringBuilder sb = new StringBuilder();
        // Max: 20, Min: 10
        int length = random.nextInt(11) + 10;
        for (int i = 0; i < length; i++) {
            int randomInt = random.nextInt(26 + 10);
            char ch;

            if (randomInt < 10) {
                ch = (char) (randomInt + '0');
            } else {
                ch = (char) (randomInt + 'A' - 10);
            }

            sb.append(ch);
        }

        return sb.toString();
    }
}
