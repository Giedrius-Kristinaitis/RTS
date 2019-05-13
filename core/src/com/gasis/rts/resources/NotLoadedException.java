package com.gasis.rts.resources;

/**
 * Custom exception class thrown when an asset is not loaded
 */
public class NotLoadedException extends RuntimeException {

    /**
     * Default class constructor
     * @param message exception message
     */
    public NotLoadedException(String message) {
        super(message);
    }
}
