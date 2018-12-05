package com.frohlich.it.domain.enumeration;

/**
 * The Flow enumeration.
 */
public enum Flow {
    BACKLOG, SPECIFICATION, DOCUMENTATION_START, CODING, TEST, DOCUMENTATION_END, FINISHED, CANCELED;

    private static Flow[] vals = values();

    public static boolean contains(String s) {
        for(Flow choice : values())
            if (choice.name().equals(s))
                return true;
        return false;
    }

    public Flow next() {
        return vals[(this.ordinal()+1) % vals.length];
    }

}
