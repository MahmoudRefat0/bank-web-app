package com.mahmoud.bank.util;

import com.mahmoud.bank.model.User;
import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

public final class RequestUtil {

    private RequestUtil() {
    }

    public static User currentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute("user");
    }

    public static long parseId(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static BigDecimal parseAmount(String value) {
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    public static void flash(HttpServletRequest req, String type, String message) {
        req.getSession().setAttribute(type, message);
    }
}
