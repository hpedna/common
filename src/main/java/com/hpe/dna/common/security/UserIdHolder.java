package com.hpe.dna.common.security;

/**
 * @author chun-yang.wang@hpe.com
 */
public class UserIdHolder {
    private static final InheritableThreadLocal<String> context = new InheritableThreadLocal<>();

    public static void add(String userId) {
        context.set(userId);
    }

    public static String get() {
        return context.get();
    }
}
