package com.iiitd.byteme;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.iiitd.byteme.Main.*;

public class Admin extends User {
    static final String adminPassword = "AdminPass";

    public Admin (String email, String name) {
        super (email, adminPassword, name);
        userMap.put(email,this);
    }

    public boolean adminLog (String password){
        return Objects.equals(password, adminPassword);
    }

    public void addItem(){
        System.out.println("Enter the item name: ");
        String name = scanner.nextLine();
        System.out.println("Enter the item price: ");
        float price = scanner.nextFloat();
        scanner.nextLine();
        System.out.println("Enter the item category: ");
        String category = scanner.nextLine();
        
        Item i = new Item(name,price,category);
        menu.put(i.getId(),i);
    }

    public void updateItem(Item item) {
        System.out.println(item.getId()+" | "+item.getName());
        System.out.println("Enter detail to be updated: ");
        System.out.println("1. Item name");
        System.out.println("2. Item price");
        System.out.println("3. Item category");
        System.out.println("4. Item availability");
        System.out.println("5. Back");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice){
            case 1:
                System.out.println("Current name: "+item.getName());
                System.out.println("Enter new name: ");
                String name = scanner.nextLine();
                
                item.setName(name);
                System.out.println("Name of item has been updated to "+item.getName());
                break;
            case 2:
                System.out.println("Current price: "+item.getPrice());
                System.out.println("Enter new price: ");
                int price = scanner.nextInt();
                scanner.nextLine();

                item.setPrice(price);
                System.out.println("Price of item has been updated to "+item.getPrice());
                break;
            case 3:
                System.out.println("Current category: "+item.getCategory());
                System.out.println("Enter new category: ");
                String category = scanner.nextLine();

                item.setCategory(category);
                System.out.println("Category of item has been updated to "+item.getCategory());
                break;
            case 4:
                System.out.println("Current availability status: "+(item.isAvailable()?"Available":"Not available"));
                item.switchAvailable();
                System.out.println("Availability of item has been updated to "+(item.isAvailable()?"Available":"Not available"));
                break;
            case 5:
                return;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    public static void processNextOrder() {
        Order nextOrder = orderQueue.poll();
        nextOrder.updateStatus();

        if (nextOrder!=null) {
            orderHistory.put(nextOrder.getId(),nextOrder);
            System.out.println("Order processed and moved to history: " + nextOrder);
        } else {
            System.out.println("No pending orders to process.");
        }
    }

    public void viewPendingOrders() {
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders.");
        } else {
            System.out.println("-----------------------------------------");
            System.out.println("Pending Orders- \n");
            System.out.println("-----------------------------------------");
            for (Order order : orderQueue) {
                System.out.println(order.getId()+" | "+order.getItem().getName());
                System.out.println("Quantity: "+order.getQuantity()+" | Amount Paid: "+(order.getQuantity()*order.getItem().getPrice()));
                System.out.println("Request: "+order.getRequests());
                System.out.println("Status: "+order.getStatus());
                System.out.println("\n");
            }
            System.out.println("-----------------------------------------");
        }
    }

    public void viewOrderHistory() {
        if (orderHistory==null){
            System.out.println("No orders yet");
            return;
        }
        System.out.println("-----------------------------------------");
        System.out.println("Order History");
        System.out.println("-----------------------------------------\n");
        for (Map.Entry<Integer, Order> entry : orderHistory.entrySet()){
            Order ord = entry.getValue();
            System.out.println("Date: "+ord.getTimestamp());
            System.out.println("ID: "+ord.getId()+" | Customer Name: "+ord.getCustomer().getName());
            System.out.println(ord.getItem().getName()+" | Category: "+ord.getItem().getCategory());
            System.out.println("Request: "+ord.getRequests());
            System.out.println("Amount Paid: "+ (ord.getItem().getPrice()* ord.getQuantity()));
        }
        System.out.println("-----------------------------------------\n");
    }

    public void updateOrderStatus() {
        System.out.println("Enter order ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Order ord=null;
        for (Order order : orderQueue) {
            if (order.getId()==id){
                ord = order;
                break;
            }
        }

        if (ord == null){
            System.out.println("Invalid Order ID");
            return;
        }

        System.out.println("Change order status to- ");
        System.out.println("1. Next step of process");
        System.out.println("2. Delivered");
        System.out.println("3. Denied");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice){
            case 1:
                ord.updateStatus();
                break;
            case 2:
                ord.updateStatus(5);
                break;
            case 3:
                ord.updateStatus(-1);
                break;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    public void viewPendingRefunds() {
        if (refunds==null){
            System.out.println("No pending refunds");
        }
        System.out.println("Pending Refunds- ");
        System.out.println("--------------------------------------------");
        for (Order ord : refunds){
            System.out.println("Date: "+ord.getTimestamp());
            System.out.println("ID: "+ord.getId()+" | Customer Name: "+ord.getCustomer().getName());
            System.out.println(ord.getItem().getName()+" | Category: "+ord.getItem().getCategory());
            System.out.println("Request: "+ord.getRequests());
            System.out.println("Amount Paid: "+ (ord.getItem().getPrice()* ord.getQuantity())+"\n");
        }
    }

    public void processRefund() {
        if (refunds==null){
            System.out.println("No pending refunds");
        }
        System.out.println("Enter ID of order you want to refund: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Order o=null;
        for (Order ord : refunds){
            if (ord.getId()==id){
                o=ord;
            }
        }

        if (o==null){
            System.out.println("Invalid ID, order not found for refund");
            return;
        }

        o.refund();
        refunds.remove(o);
    }

    public void getReport() {
        LocalDate today = LocalDate.now();
        double totalSales = 0.0;

        Map<Item, Integer> itemCount = new HashMap<>();

        Item mostPop=null;

        for (Map.Entry<Integer, Order> entry : orderHistory.entrySet()) {
            Order order = entry.getValue();
            LocalDateTime orderTime = order.getTimestamp();

            if (orderTime.toLocalDate().isEqual(today)) {
                totalSales += order.getItem().getPrice()* order.getQuantity();
                itemCount.replace(order.getItem(), itemCount.get(order.getItem())+1);
                if (itemCount.get(mostPop)==null||itemCount.get(mostPop)<itemCount.get(order.getItem())){
                    mostPop=order.getItem();
                }
            }
        }

        System.out.println("---- Daily Sales Report ----");
        System.out.printf("Total Sales: %.2f\n", totalSales);
        System.out.println("Most Popular Item: " + (mostPop != null ? mostPop.getName() : "None") + " (Sold: " + itemCount.get(mostPop) + ")");
        System.out.println("----------------------------");

    }

}
