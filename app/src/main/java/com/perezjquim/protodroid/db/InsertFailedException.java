package com.perezjquim.protodroid.db;

import java.util.Arrays;

public class InsertFailedException extends Exception
{
    public InsertFailedException() { super("Failed to insert a value"); }
    public InsertFailedException(String[] args) { super("Failed to insert - values: "+ Arrays.toString(args));}
}
