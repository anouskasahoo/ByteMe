# Byte Me!
*A CLI-based Java food ordering system for students to browse menus, place orders, and track deliveries from their hostel rooms.*

---

## Overview  
**Byte Me!** is a command-line application designed to simplify food ordering for students. It allows users to view menus, place and cancel orders, and track delivery status — all from the comfort of their hostel rooms. The system simulates a backend process where eateries process orders and delivery personnel handle logistics.

---

## Features  
- View available eateries and menus  
- Add items to the cart and place orders  
- View current and past orders
- Track delivery status  
- Cancel orders that haven't been delivered
- Customers can write reviews for the admin to view
- Admin can view analytics of orders
- Refunds are processed for canceled orders

---

## Object-Oriented Concepts Used  
The system applies OOP principles to build a modular, extensible design:

- **Modular Class Design**: Core entities like `User`, `Order`, `Item`, and `Admin` are modeled as independent classes, improving clarity and reusability.
- **Role-Based Behavior**: `Customer` and `Admin` extend a common `User` class, each with custom actions and overridden methods.
- **Exception Handling**: Custom exceptions handle edge cases like invalid orders or unavailable items, improving robustness and user feedback.
- **Service Abstraction**: Components like `OrderQueue` and `Refunds` encapsulate business logic, separating it from the user interface and models.

---
```
   _________
  | _______ |
 / \         \
/___\_________\
|   | \       |
|   |  \      |
|   |   \     |
|   | M  \    |
|   |     \   |
|   |\  I  \  |
|   | \     \ |
|   |  \  L  \|
|   |   \     |
|   |    \  K |
|   |     \   |
|   |      \  |
|___|_______\_|
```
