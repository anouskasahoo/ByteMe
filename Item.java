package com.iiitd.byteme;

import java.util.HashMap;
import java.util.Map;

import static com.iiitd.byteme.Main.menu;

public class Item {
    private int id;
    private String name;
    private float price;

    private boolean available;
    private String category;
    private Map<Customer, String> reviews = new HashMap<>();

    int iterator=0;
    public Item(String name, float price, String category){
        this.category = category;
        this.available = true;
        this.id = iterator;
        iterator++;
        this.name = name;
        this.price = price;
        menu.put(id,this);
    }


    public boolean isAvailable() {
        return available;
    }

    public void switchAvailable() {
        this.available = !this.available;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //----------------------------------------------------------------------(getters and setters above)

    public void printReviews(){
        if (reviews==null){
            System.out.println("No reviews yet");
            return;
        }
        System.out.println(" Item Reviews -");
        for (Map.Entry<Customer, String> entry : reviews.entrySet()){
            User user = entry.getKey();
            String rev = entry.getValue();
            System.out.println("----------------------------------------------------");
            System.out.println(user.getName());
            System.out.println(rev);
        }
    }

    public void addReview(Customer c, String rev) {
        reviews.put(c,rev);
    }
}
