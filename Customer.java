package com.iiitd.byteme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.iiitd.byteme.Main.*;

public class Customer extends User{
    private boolean vip;
    private Map<Item, Integer> cart = new HashMap<>();
    private ArrayList<Order> orderHistory = new ArrayList<>();
    private ArrayList<Order> currentOrders = new ArrayList<>();

    public Customer(String email, String password, String name){
        super (email, password, name);
        this.vip = false;
        userMap.put(email,this);
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip() {
        this.vip = true;
    }

    public void addToCart(int itemId, int quantity){
        Item item = null;
        item = menu.get(itemId);
        if (item==null){
            System.out.println("Not a valid ID");
            return;
        }
        cart.put(item,quantity);
        System.out.println("Item added to cart | "+item.getName()+" : "+quantity);
        return;
    }

    public void viewCart(){
        if (this.cart==null){
            System.out.println("Your cart is empty :(");
            return;
        }
        float total=0;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()){
            Item item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("----------------------------------------------------");
            System.out.println(item.getName()+" | "+item.getId()+" | Category: "+item.getCategory());
            System.out.println("Price: "+item.getPrice()+" | Quantity: "+quantity);
            total+=(item.getPrice()* entry.getValue());
        }
        System.out.println("----------------------------------------------------");
        System.out.println("Cart total : "+total);
    }


    public void modifyCart() {
        if (cart.isEmpty()){
            System.out.println("Your cart is empty");
            return;
        }
        System.out.println("Enter item ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Item item=null;
        for (Map.Entry<Item, Integer> entry : cart.entrySet()){
            if (entry.getKey().getId()==id){
                item=entry.getKey();
                break;
            }
        }

        if (item==null){
            System.out.println("Item ID not found in cart");
            return;
        }

        System.out.println("Enter new quantity (Enter 0 to remove): ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity<=0){
            cart.remove(item);
        }

        cart.replace(item,quantity);
        System.out.println("Cart updated");
    }

    public void checkout() {
        System.out.println("Checking out...");
        this.viewCart();

        System.out.println("Enter Item ID if you want to include a special request (Enter -1 if no requests): ");
        int id = scanner.nextInt();
        scanner.nextLine();


        for (Map.Entry<Item, Integer> entry : cart.entrySet()){
            boolean req = false;
            String request = "";
            if (entry.getKey().getId()==id) {
                req = true;
                System.out.println("Enter request: ");
                request = scanner.nextLine();
            }
            Order ord = new Order(this, entry.getKey(), entry.getValue(), req, request);

            currentOrders.add(ord);
            cart.remove(entry.getKey());
        }
        System.out.println("Processing payment...");

        System.out.println("Check out complete!");
    }

    public void manageOrders() {
        System.out.println("Enter your choice- ");
        System.out.println("1. Track orders");
        System.out.println("2. Cancel order");
        System.out.println("3. View order history");
        System.out.println("4. Back");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice){
            case 1:
                if (currentOrders==null){
                    System.out.println("No current orders");
                    break;
                }
                for (Order order:currentOrders){
                    System.out.println(order.getId()+" | "+order.getItem().getName());
                    System.out.println("Quantity: "+order.getQuantity()+" | Amount Paid: "+(order.getQuantity()*order.getItem().getPrice()));
                    System.out.println("Request: "+order.getRequests());
                    System.out.println("Status: "+order.getStatus());
                    System.out.println("\n");
                }
                break;
            case 2:
                if (currentOrders==null){
                    System.out.println("No current orders");
                    break;
                }
                int ordId = scanner.nextInt();
                scanner.nextLine();
                Order o=null;
                for (Order order:currentOrders){
                    if (order.getId()==ordId){
                        o=order;
                    }
                }
                if (o==null){
                    System.out.println("ID not found in your current orders");
                }
                currentOrders.remove(o);
                break;
            case 3:
                if (orderHistory==null){
                    System.out.println("Your order history is empty");
                    break;
                }
                for (Order order:orderHistory){
                    System.out.println("Date: "+order.getTimestamp());
                    System.out.println(order.getId()+" | "+order.getItem().getName());
                    System.out.println("Quantity: "+order.getQuantity()+" | Amount Paid: "+(order.getQuantity()*order.getItem().getPrice()));
                    System.out.println("Request: "+order.getRequests());
                    System.out.println("\n");
                }
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid input");
                break;
        }
    }

    public void becomeVIP() {
        System.out.println("Become a VIP member and enjoy exclusive benefits by paying Rs 25L/-");
        System.out.println("Processing payment...");
        this.setVip();
        System.out.println("Congratulations! You are now a VIP member!");
    }
}
