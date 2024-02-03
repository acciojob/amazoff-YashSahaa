package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        this.id = id;
        if(deliveryTime.length() != 0){
            String arr[] = deliveryTime.split(":");
            this.deliveryTime = (Integer.parseInt(arr[0])*60) + Integer.parseInt(arr[1]);
        }
        else this.deliveryTime = 0;
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
