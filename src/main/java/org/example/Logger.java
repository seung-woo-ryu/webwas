package org.example;

public class Logger {
    private static final String INFO = "[INFO]: ";
    public static final void info(String s) {
        System.out.println(INFO + s);
    }
}
