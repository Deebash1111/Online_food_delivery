package com.delivery.app;

import java.util.Date;

import com.delivery.service.DeliveryService;

public class DeliveryMain {

    public static void main(String[] args) throws Exception {

        DeliveryService service = new DeliveryService();

        System.out.println(
            service.assignDelivery("ORD1001", "AGT5001", new Date())
            ? "ASSIGNED" : "FAILED"
        );

        System.out.println(
            service.completeDelivery(91001, new Date())
            ? "COMPLETED" : "FAILED"
            	
       );
    } 
}
