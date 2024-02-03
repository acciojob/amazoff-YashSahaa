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
        String id = order.getId();
        int dT = order.getDeliveryTime();
        if(id.length()!=0 && dT!=0 && !orderDb.containsKey(id))
            orderDb.put(id,order);
    }

    public void addPartner(String partnerId){
        if(partnerId.length()!=0 && !deliveryPartnerDb.containsKey(partnerId)){
            deliveryPartnerDb.put(partnerId,new DeliveryPartner(partnerId));
            odDb.put(partnerId,new ArrayList<>());
        }
    }

    public void addOrderPartnerPair(String orderId, String partnerId){
        if(!orderDb.containsKey(orderId) || !deliveryPartnerDb.containsKey(partnerId) || !odDb.containsKey(partnerId)) return ;
        odDb.get(partnerId).add(orderId);
        assignDB.put(orderId,partnerId);
        DeliveryPartner deliveryPartner = deliveryPartnerDb.get(partnerId);
        deliveryPartner.setNumberOfOrders(deliveryPartner.getNumberOfOrders()+1);
    }

    public Order getOrderById(String orderId){
        if(orderDb.containsKey(orderId)) return orderDb.get(orderId);
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId){
        if(deliveryPartnerDb.containsKey(partnerId)) return deliveryPartnerDb.get(partnerId);
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId){
        if(deliveryPartnerDb.containsKey(partnerId)) return deliveryPartnerDb.get(partnerId).getNumberOfOrders();
        return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId){
        if(odDb.containsKey(partnerId)) return odDb.get(partnerId);
        return  null;
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
        if(odDb.containsKey(partnerId) && time.length()>0) {
            String arr[] = time.split(":");
            int giventime = (Integer.parseInt(arr[0]) * 60) + Integer.parseInt(arr[1]);
            List<String> orders = odDb.get(partnerId);
            int count = 0;
            for (String id : orders) {
                if (orderDb.get(id).getDeliveryTime() > giventime) count++;
            }
            return count;
        }
        return 0;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId){
        if(!odDb.containsKey(partnerId)) return null;
        List<String> orders = odDb.get(partnerId);
        int last = 0;
        for(String id : orders){
            int dT = orderDb.get(id).getDeliveryTime();
            if(dT>last){
                last=dT;
            }
        }
        int h = last/60;
        int m = last-(h*60);
        String HH = String.format("%02d", h);
        String MM = String.format("%02d", m);
        return (HH+":"+MM) ;
    }

    public void deletePartnerById(String partnerId){
        if(!deliveryPartnerDb.containsKey(partnerId)) return;
        deliveryPartnerDb.remove(partnerId);
        List<String> orders = odDb.get(partnerId);
        odDb.remove(partnerId);
        for(String id : orders){
            assignDB.remove(id);
        }
    }

    public void deleteOrderById(String orderId){
        if(!orderDb.containsKey(orderId) || !assignDB.containsKey(orderId)) return;
        orderDb.remove(orderId);
        String partnerId = assignDB.get(orderId);
        assignDB.remove(orderId);
        List<String> orders = odDb.get(partnerId);
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).equals(orderId)){
                orders.remove(i);
            }
        }
        odDb.put(partnerId,orders);
    }
}
