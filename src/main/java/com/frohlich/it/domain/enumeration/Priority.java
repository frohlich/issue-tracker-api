package com.frohlich.it.domain.enumeration;

/**
 * The Priority enumeration.
 */
public enum Priority {
    CRITICAL, HIGH, MEDIUM, LOW;

    public static boolean contains(String s) {
        for(Priority choice : values())
            if (choice.name().equals(s))
                return true;
        return false;
    }
}
