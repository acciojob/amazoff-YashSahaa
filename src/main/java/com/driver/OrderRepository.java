package com.driver;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrderRepository {
    HashMap<String,Order> orderDb;
    HashMap<String,DeliveryPartner> deliveryPartnerDb;
    HashMap<String,List<String>> odDb;
    HashMap<String,String> assignDB ;
    public OrderRepository(){
        this.orderDb = new HashMap<>();
        this.deliveryPartnerDb = new HashMap<>();
        this.odDb = new HashMap<>();
        this.assignDB = new HashMap<>();
    }
    public void addOrder(Order order){
        orderDb.put(order.getId(),order);
    }

    public void addPartner(String partnerId){
        deliveryPartnerDb.put(partnerId,new DeliveryPartner(partnerId));
        odDb.put(partnerId,new ArrayList<>());
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        odDb.get(partnerId).add(orderId);
        assignDB.put(orderId,partnerId);
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
    }

    public Order getOrderById(String orderId){
        return orderDb.get(orderId);
    }

    public DeliveryPartner getPartnerById(String partnerId){
        return deliveryPartnerDb.get(partnerId);
    }

    public Integer getOrderCountByPartnerId(String partnerId){
        return deliveryPartnerDb.get(partnerId).getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        return odDb.get(partnerId);
    }

    public List<String> getAllOrders(){
        List<String> orders = new ArrayList<>();
        for(String id : orderDb.keySet()){
            orders.add(id);
        }
        return orders;
    }

    public Integer getCountOfUnassignedOrders(){
        return (orderDb.size()-assignDB.size());
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId){
        String arr[] = time.split(":");
        int giventime = (Integer.parseInt(arr[0])*60) + Integer.parseInt(arr[1]);
        List<String> orders = odDb.get(partnerId);
        int count = 0;
        for(String id : orders){
            if(orderDb.get(id).getDeliveryTime()>giventime) count++ ;
        }
        return count;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        List<String> orders = odDb.get(partnerId);
//        ArrayList<String> arr = new ArrayList<>();
        int last = 0;
        for(String id : orders){
            int dT = orderDb.get(id).getDeliveryTime();
            if(dT>last){
                last=dT;
            }
        }
//        Collections.sort(arr);
//        return arr.get(arr.size()-1);
        int h = last/60;
        int m = last-(h*60);
        String HH = String.format("%02d", h);;
        String MM = String.format("%02d", m);;
        return (HH+":"+MM) ;
    }

    public void deletePartnerById(String partnerId){
        deliveryPartnerDb.remove(partnerId);
        List<String> orders = odDb.get(partnerId);
        odDb.remove(partnerId);
        for(String id : orders){
            assignDB.remove(id);
        }
    }

    public void deleteOrderById(String orderId){
        orderDb.remove(orderId);
        String partnerId = assignDB.get(orderId);
        assignDB.remove(orderId);
        List<String> orders = odDb.get(partnerId);
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).equals(orderId)){
                orders.remove(i);
            }
        }
    }
}
