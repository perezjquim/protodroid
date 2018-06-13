package com.perezjquim.protodroid.db;

public enum Element
{
    ID,
    TYPE,
    LABEL,
    PAGE_ID,
    PAGE_DESTINATION_ID;

    public enum Types
    {
        BUTTON
                {
                    @Override
                    public String toString() { return "Button"; }
                },
        CHECKBOX
                {
                    @Override
                    public String toString() { return "Checkbox"; }
                },
        SWITCH
                {
                    @Override
                    public String toString() { return "Switch"; }
                },
        FIELD
                {
                    @Override
                    public String toString() { return "Field"; }
                },
        FIELD_PASSWORD
                {
                    @Override
                    public String toString() { return "Field (Password)"; }
                },
        NUMBER_PICKER
                {
                    @Override
                    public String toString() { return "Number picker"; }
                },
        DATE_PICKER
                {
                    @Override
                    public String toString() { return "Date picker"; }
                },
        TIME_PICKER
                {
                    @Override
                    public String toString() { return "Time picker"; }
                },
        SEARCH_BAR
                {
                    @Override
                    public String toString() { return "Search bar"; }
                },
        RATING_BAR
                {
                    @Override
                    public String toString() { return "Rating bar"; }
                },
        SEEK_BAR
                {
                    @Override
                    public String toString() { return "Seek bar"; }
                },
        RADIO_BUTTON
                {
                    @Override
                    public String toString() { return "Radio button"; }
                }
    }
}
