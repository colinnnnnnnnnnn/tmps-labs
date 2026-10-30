package com.tmps.lab1;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("TMPS Lab 1: Creational Design Patterns Demonstration (Music Record Store)\n");

        RecordStoreConfig config = RecordStoreConfig.getInstance();

        // 1. Singleton Pattern Demonstration
        System.out.println("[1] Singleton Pattern: RecordStoreConfig");
        System.out.println("Store Name: " + config.getStoreName());
        System.out.println("Active Currency: " + config.getDefaultCurrency());
        System.out.printf(Locale.US, "Default VAT Rate: %.1f%%%n", config.getStandardTaxRate() * 100);
        System.out.println("Next Order ID generated: " + config.nextOrderId() + "\n");

        // 2. Interactive Order Creation via Builder Pattern
        Scanner scanner = new Scanner(System.in);
        System.out.println("[2] Builder Pattern: Constructing RecordOrder");
        System.out.print("Enter customer name (press Enter for default 'Miles Davis'): ");
        String customerInput = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        String customerName = customerInput.isEmpty() ? "Miles Davis" : customerInput;

        System.out.print("Is this a priority order? (y/N): ");
        String priorityInput = scanner.hasNextLine() ? scanner.nextLine().trim().toLowerCase() : "";
        boolean isPriority = priorityInput.equals("y") || priorityInput.equals("yes");

        System.out.print("Shipping destination (press Enter for 'Self-pickup'): ");
        String shippingInput = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
        String shippingAddress = shippingInput.isEmpty() ? null : shippingInput;

        RecordOrderBuilder orderBuilder = new RecordOrderBuilder()
                .withCustomer(customerName)
                .asPriority(isPriority)
                .withShippingAddress(shippingAddress);

        System.out.println("Enter record order items (press Enter on item name to finish):");
        int itemCount = 0;
        while (true) {
            System.out.print("  Item " + (itemCount + 1) + " name: ");
            String itemName = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            if (itemName.isEmpty()) {
                if (itemCount > 0) break;
                System.out.println("  Adding default vinyl album: 'Pink Floyd - The Dark Side of the Moon (Vinyl LP)' x1 @ 35.00");
                orderBuilder.addItem("Pink Floyd - The Dark Side of the Moon (Vinyl LP)", 1, 35.0);
                itemCount++;
                break;
            }

            System.out.print("  Quantity (default 1): ");
            String qtyInput = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            int qty = 1;
            try {
                if (!qtyInput.isEmpty()) {
                    int parsed = Integer.parseInt(qtyInput);
                    if (parsed > 0) qty = parsed;
                }
            } catch (NumberFormatException ignored) {}

            System.out.print("  Unit price (default 30.0): ");
            String priceInput = scanner.hasNextLine() ? scanner.nextLine().trim() : "";
            double price = 30.0;
            try {
                if (!priceInput.isEmpty()) {
                    double parsed = Double.parseDouble(priceInput);
                    if (parsed >= 0.0) price = parsed;
                }
            } catch (NumberFormatException ignored) {}

            orderBuilder.addItem(itemName, qty, price);
            itemCount++;
        }

        RecordOrder order = orderBuilder.build();
        System.out.printf(Locale.US, "%nOrder #%d constructed successfully for %s.%n%n", order.getId(), order.getCustomerName());

        // 3. Abstract Factory Pattern Demonstration
        System.out.println("[3] Abstract Factory Pattern: Checkout Processing");
        System.out.print("Select checkout channel [1: Retail, 2: Wholesale B2B] (default 1): ");
        String channelChoice = scanner.hasNextLine() ? scanner.nextLine().trim() : "1";

        CheckoutFactory factory;
        if ("2".equals(channelChoice)) {
            factory = new WholesaleCheckoutFactory();
        } else {
            factory = new RetailCheckoutFactory();
        }

        OrderCheckoutService checkoutService = new OrderCheckoutService(factory);
        double total = checkoutService.processCheckout(order);

        System.out.printf(Locale.US, "Execution completed. Total charged: %.2f %s%n", total, config.getDefaultCurrency());
    }
}
