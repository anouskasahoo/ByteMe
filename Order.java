package com.iiitd.byteme;

import java.time.LocalDateTime;

import static com.iiitd.byteme.Main.*;


public class Order {
    private int id;
    private Customer customer;
    private Item item;
    private int quantity;
    private String requests = "No requests";
    private int status;
    private LocalDateTime timestamp = LocalDateTime.now();
    private int refunded;

    int iterator=1;

    public Order(Customer customer, Item item, int quantity, boolean req, String request){
        this.customer=customer;
        this.item=item;
        this.quantity=quantity;
        if (req){
            this.requests=request;
        }
        this.refunded=-1;
        this.status = 0;
        this.id=iterator;
        iterator++;
        orderHistory.put(id,this);
    }


    public Customer getCustomer() {
        return customer;
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRequests() {
        return requests;
    }

    public String getStatus() {
        switch(status){
            case -1:
                return "Denied";
            case 1:
                return "Order Waiting";
            case 2:
                return "Order Being Processed";
            case 3:
                return "Order Ready for Delivery";
            case 4:
                return "Order Out for Delivery";
            case 5:
                return "Order Delivered";
            default:
                return "";
        }
    }

    public void updateStatus() {
        status++;
    }

    public void updateStatus(int i) {
        status = i;
        if (i==-1){
            refunded=0;
            refunds.add(this);
        }
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void refund(){
        refunded=1;
    }

    public boolean isRefunded(){
        if (refunded==0){
            return false;
        }
        else{
            return true;
        }
    }

    public int getId() {
        return id;
    }
}
