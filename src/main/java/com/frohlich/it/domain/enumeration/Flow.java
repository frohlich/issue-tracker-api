package com.frohlich.it.domain.enumeration;

/**
 * The Flow enumeration.
 */
public enum Flow {
    BACKLOG, SPECIFICATION, CODING, TEST, FINISHED, CANCELED;

    public static boolean contains(String s) {
        for(Flow choice : values())
            if (choice.name().equals(s))
                return true;
        return false;
    }

}
