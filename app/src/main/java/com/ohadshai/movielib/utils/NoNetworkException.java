package com.ohadshai.movielib.utils;

/**
 * Represents a network connection exception, which occurs when there's no connection the the internet.
 * Created by Ohad on 10/10/2016.
 */
public class NoNetworkException extends Exception {

    /**
     * Initializes a new instance of a network connection exception, which occurs when there's no connection the the internet.
     */
    public NoNetworkException() {
        super("No internet connection");
    }

}