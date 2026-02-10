package com.delivery.util;

public class DeliveryActiveException extends Exception {
    public String toString() {
        return "Active delivery exists";
    }
}
