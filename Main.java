package com.iiitd.byteme;
import java.util.*;

import static com.iiitd.byteme.User.findUser;

public class Main {
    static Map<String, User> userMap = new HashMap<>();
    static Map<Integer, Item> menu = new HashMap<>();
    static Map<Integer, Order> orderHistory = new HashMap<>();
    static final Scanner scanner = new Scanner(System.in);
    static ArrayList<Order> refunds = new ArrayList<>();
    static PriorityQueue<Order> orderQueue = new PriorityQueue<>(new Comparator<Order>() {
        @Override
        public int compare(Order o1, Order o2) {
            if (o1.getCustomer().isVip() && !o2.getCustomer().isVip()) {
                return -1; // VIP orders come first
            } else if (!o1.getCustomer().isVip() && o1.getCustomer().isVip()) {
                return 1;  // Regular orders come after VIPs
            } else {
                return o1.getTimestamp().compareTo(o2.getTimestamp());
            }
        }
    });

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n");
            System.out.println("Byte Me!");
            System.out.println("-------------------------------------------------------");
            System.out.println("\n     Welcome to the New IIITD Food Ordering System!\n");
            System.out.println("Enter Your Choice-");
            System.out.println("Press 1 to Enter the system\nPress 2 to exit the system\nPress 3 to see a cat\n");
            System.out.println("-------------------------------------------------------");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    System.out.println("Thank You for visiting our system!");
                    System.out.println("Exiting...");
                    return;
                case 3:
                    System.out.println(" /\\_/\\");
                    System.out.println("( o.o ) helo!");
                    System.out.println(" > ^ <\n");
                    System.out.println("Thank you for checking out the cat :)\nNow back to the system");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void login() {
        System.out.println("Login Page");
        System.out.println("-----------------------------------------\n");
        System.out.println("Enter Your Role-");
        System.out.println("Press 1 for Customer\nPress 2 for Administrator");
        System.out.println("\n\nEnter 3 to Exit");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                customerEnter();
                break;
            case 2:
                adminEnter();
                break;
            case 3:
                System.out.println("Exiting the application.");
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void customerEnter() {
        System.out.println("Welcome student!");
        System.out.println("Press 1 to Sign In\nPress 2 to Log In (if you have previously signed in)");
        System.out.println("Press 3 to go back");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                customerSignIn();
                break;
            case 2:
                try {
                    customerLogin();
                } catch (InvalidLoginException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please try logging in again.");
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void customerSignIn() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Customer customer = (Customer) findUser(email);
        if (customer!=null) {
            System.out.println("You have already signed in. Please log in with your password");
            customerEnter();
            return;
        }
        System.out.println("Set your password:");
        String password = scanner.nextLine();
        System.out.println("Enter your Name:");
        String name = scanner.nextLine();
        System.out.println("Enter your semester:");
        int sem = Integer.parseInt(scanner.nextLine());
        if (sem<1 || sem>8) {
            System.out.println("Semester is out of range (1-8), try again!");
            return;
        }
        Customer cu = new Customer(email,password,name);

        System.out.println("Signed In successfully, Kindly Log In now to use the registration system");
        customerEnter();
    }

    private static void customerLogin() {

        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Customer customer = (Customer) findUser(email);
        if (customer==null) {
            throw new InvalidLoginException("Student email not found");
        }
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        if (customer.login(email, password)) {
            System.out.println("-----------------------------------------");
            System.out.println("Successfully logged in!");
            System.out.println("Hello "+customer.name+"!");

            while (true) {
                System.out.println("-----------------------------------------");
                System.out.println("1. Browse Menu (Searching/Filter available)");
                System.out.println("2. Add to Cart");
                System.out.println("3. Modify Cart or Remove Item");
                System.out.println("4. View Cart and Total");
                System.out.println("5. Checkout");
                System.out.println("6. Order Tracking and Past Orders");
                System.out.println("7. View Reviews");
                System.out.println("8. Add Review");
                System.out.println("9. Become VIP");
                System.out.println("10. Exit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice){
                    case 1:
                        if (menu==null){
                            System.out.println("Menu is empty");
                            break;
                        }
                        System.out.println("Press 1 to apply filter on category\nPress 2 to sort Menu prices\nPress 3 to proceed without filter\nPress 4 to search an Item");
                        int filter = scanner.nextInt();
                        scanner.nextLine();

                        if (filter ==4){
                            System.out.println("Enter Item name: ");
                            Item i=null;
                            String name = scanner.nextLine();
                            for (Map.Entry<Integer, Item> entry : menu.entrySet()) {
                                Item item = entry.getValue();
                                if (item.getName()==name){
                                    i=item;
                                }
                            }
                            if (i==null){
                                System.out.println("Item not found");
                                break;
                            }
                            System.out.println("ID: " + i.getId() + " | Name: " + i.getName() + " | Category: " + i.getCategory());
                            System.out.println("Price: " + i.getPrice() + " | Availability: " + i.isAvailable() + "\n");
                            break;
                        }
                        if (filter==3) {
                            System.out.println("-----------------------------------------");
                            System.out.println("Menu");
                            System.out.println("-----------------------------------------\n");
                            for (Map.Entry<Integer, Item> entry : menu.entrySet()) {
                                Item item = entry.getValue();
                                System.out.println("ID: " + item.getId() + " | Name: " + item.getName() + " | Category: " + item.getCategory());
                                System.out.println("Price: " + item.getPrice() + " | Availability: " + item.isAvailable() + "\n");
                            }
                            System.out.println("-----------------------------------------\n");
                        }
                        else if (filter==2){
                            System.out.println("Enter category: ");
                            String cat = scanner.nextLine();
                            for (Map.Entry<Integer, Item> entry : menu.entrySet()) {
                                Item item = entry.getValue();
                                if (Objects.equals(item.getCategory(), cat)) {
                                    System.out.println("ID: " + item.getId() + " | Name: " + item.getName() + " | Category: " + item.getCategory());
                                    System.out.println("Price: " + item.getPrice() + " | Availability: " + item.isAvailable() + "\n");
                                }
                            }
                        }
                        else if (filter==1){
                            System.out.println("Choose sorting order for price:");
                            System.out.println("1. Lowest to Highest");
                            System.out.println("2. Highest to Lowest");

                            int pick = scanner.nextInt();
                            scanner.nextLine();

                            List<Item> itemsList = new ArrayList<>(menu.values());

                            switch (pick) {
                                case 1:
                                    itemsList.sort(Comparator.comparingDouble(Item::getPrice));
                                    break;
                                case 2:
                                    itemsList.sort(Comparator.comparingDouble(Item::getPrice).reversed());
                                    break;
                                default:
                                    System.out.println("Invalid choice");
                                    break;
                            }

                            System.out.println("-----------------------------------------");
                            System.out.println("Menu");
                            System.out.println("-----------------------------------------\n");
                            for (Map.Entry<Integer, Item> entry : menu.entrySet()) {
                                Item item = entry.getValue();
                                System.out.println("ID: " + item.getId() + " | Name: " + item.getName() + " | Category: " + item.getCategory());
                                System.out.println("Price: " + item.getPrice() + " | Availability: " + item.isAvailable() + "\n");
                            }
                            System.out.println("-----------------------------------------\n");
                        }
                        else{
                            System.out.println("Invalid choice");
                        }
                        break;
                    case 2:
                        System.out.println("Enter ID of item to add to cart: ");
                        int itemid = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        customer.addToCart(itemid, quantity);
                        break;
                    case 3:
                        customer.modifyCart();
                        break;
                    case 4:
                        customer.viewCart();
                        break;
                    case 5:
                        customer.checkout();
                        break;
                    case 6:
                        customer.manageOrders();
                        break;
                    case 7:
                        System.out.println("Enter ID of item to see reviews: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();

                        Item i=menu.get(id);
                        if (i==null){
                            System.out.println("Invalid ID");
                            break;
                        }
                        System.out.println("Enter your review: ");
                        String rev = scanner.nextLine();

                        i.addReview(customer, rev);
                        System.out.println("Review added");

                        break;
                    case 8:
                        System.out.println("Enter ID of item to see reviews: ");
                        int itid = scanner.nextInt();
                        scanner.nextLine();

                        Item item=menu.get(itid);
                        if (item==null){
                            System.out.println("Invalid ID");
                            break;
                        }
                        item.printReviews();
                        break;
                    case 9:
                        customer.becomeVIP();
                        break;
                    case 10:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } else {
            System.out.println("Invalid email or password. Try again");
        }
    }

    private static void adminEnter() {
        System.out.println("Welcome administrator!");
        System.out.println("Press 1 to Sign In\nPress 2 to Log In (if you have previously signed in)\nPress 3 to go back");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                adminSignIn();
                break;
            case 2:
                try {
                    adminLogin();
                } catch (InvalidLoginException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Please try logging in again.");
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void adminSignIn() {
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();
        Admin adm = (Admin) findUser(email);
        if (adm != null) {
            System.out.println("You have already signed in. Please log in with your password.");
            adminEnter();
            return;
        }
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        Admin admin = new Admin(email, name);
        System.out.println("Signed In successfully, kindly Log In now to use the registration system.");
        adminEnter();
    }

    private static void adminLogin() {
        System.out.println("-----------------------------------------\n");
        System.out.println("Welcome Admin!");
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        Admin admin = (Admin) findUser(email);
        if (admin == null) {
            throw new InvalidLoginException("Admin email not found");
        }
        System.out.println("Enter your password:");
        String password = scanner.nextLine();
        if (!Objects.equals(password, Admin.adminPassword)) {
            System.out.println("Incorrect Password.");
            return;
        }
        if (admin.adminLog(password)) {
            while (true) {
                System.out.println("Hello Admin!!");
                System.out.println("-----------------------------------------");
                System.out.println("Enter your choice-\n");
                System.out.println("1. Manage the Menu");
                System.out.println("2. Manage Orders");
                System.out.println("3. Generate Report");
                System.out.println("4. Back");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice){
                    case 1:
                        System.out.println("-----------------------------------------");
                        System.out.println("Menu Management-\n");
                        System.out.println("1. View Menu");
                        System.out.println("2. Add Item");
                        System.out.println("3. Update Item");
                        System.out.println("4. Remove Item");
                        System.out.println("5. Back");

                        int pick = scanner.nextInt();
                        scanner.nextLine();

                        switch (pick){
                            case 1:
                                if (menu==null){
                                    System.out.println("Menu is empty");
                                    break;
                                }
                                System.out.println("-----------------------------------------");
                                System.out.println("Menu");
                                System.out.println("-----------------------------------------\n");
                                for (Map.Entry<Integer, Item> entry : menu.entrySet()){
                                    Item item = entry.getValue();
                                    System.out.println("ID: "+item.getId()+" | Name: "+item.getName()+" | Category: "+item.getCategory());
                                    System.out.println("Price: "+item.getPrice()+" | Availability: "+item.isAvailable()+"\n");
                                }
                                System.out.println("-----------------------------------------\n");
                                break;
                            case 2:
                                admin.addItem();
                                System.out.println("Item added");
                                break;
                            case 3:
                                System.out.println("Enter ID of item to be updated: ");
                                int id = scanner.nextInt();
                                scanner.nextLine();
                                Item item=menu.get(id);
                                if (item==null){
                                    System.out.println("Invalid id");
                                    break;
                                }
                                admin.updateItem(item);
                                break;
                            case 4:
                                System.out.println("Enter ID of item to be updated: ");
                                int itemid = scanner.nextInt();
                                scanner.nextLine();
                                Item i=menu.get(itemid);
                                if (i==null){
                                    System.out.println("Invalid id");
                                    break;
                                }
                                menu.remove(itemid);
                                break;
                            case 5:
                                break;
                            default:
                                System.out.println("Invalid choice");
                                break;
                        }

                        break;
                    case 2:
                        System.out.println("-----------------------------------------");
                        System.out.println("Order Management-\n");
                        System.out.println("1. View Pending Orders");
                        System.out.println("2. View Order History");
                        System.out.println("3. Update Order Status");
                        System.out.println("4. View Pending Refunds");
                        System.out.println("5. Process a Refund");
                        System.out.println("6. Back");

                        int pick1 = scanner.nextInt();
                        scanner.nextLine();

                        switch(pick1){
                            case 1:
                                admin.viewPendingOrders();
                                break;
                            case 2:
                                admin.viewOrderHistory();
                                break;
                            case 3:
                                admin.updateOrderStatus();
                                break;
                            case 4:
                                admin.viewPendingRefunds();
                                break;
                            case 5:
                                admin.processRefund();
                                break;
                            case 6:
                                break;
                            default:
                                System.out.println("Invalid Input");
                                break;
                        }
                        break;
                    case 3:
                        admin.getReport();
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Invalid choice");
                        break;
                }
            }
        }
    }
}