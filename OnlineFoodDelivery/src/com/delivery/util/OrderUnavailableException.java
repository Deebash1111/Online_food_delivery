package com.delivery.util;

public class OrderUnavailableException extends Exception {
    public String toString() {
        return "Order unavailable";
    }
}
