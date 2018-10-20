package com.frohlich.it.domain.enumeration;

/**
 * The IssueType enumeration.
 */
public enum IssueType {
    STORY, BUG, SPIKE;

    public static boolean contains(String s) {
        for(IssueType choice : values())
            if (choice.name().equals(s))
                return true;
        return false;
    }
}
