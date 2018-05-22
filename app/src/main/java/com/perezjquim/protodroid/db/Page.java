package com.perezjquim.protodroid.db;

public enum Page
{
    ID(0),
    PROJECT_ID(1),
    NAME(2);

    public final int index;

    Page(final int index)
    {
        this.index = index;
    }
}
