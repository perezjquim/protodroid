package com.perezjquim.protodroid.db;

public enum Element
{
    ID(0),
    TYPE(1),
    LABEL(2),
    CONFIG(3),
    PAGE_ID(4),
    PAGE_DESTINATION_ID(5);

    public final int index;

    Element(final int index)
    {
        this.index = index;
    }
}
