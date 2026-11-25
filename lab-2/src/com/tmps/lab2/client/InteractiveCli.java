package com.tmps.lab2.client;

import com.tmps.lab2.composite.OrderItemComponent;
import com.tmps.lab2.composite.SingleRecordItem;
import com.tmps.lab2.composite.VinylBoxSet;
import com.tmps.lab2.decorator.BaseOrderInvoice;
import com.tmps.lab2.decorator.GiftWrapDecorator;
import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.decorator.UltrasonicCleaningDecorator;
import com.tmps.lab2.decorator.VinylInsuranceDecorator;
import com.tmps.lab2.domain.AccessDeniedException;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.RecordOrderBuilder;
import com.tmps.lab2.domain.UserRole;
import com.tmps.lab2.domain.UserSession;
import com.tmps.lab2.facade.CheckoutFacade;
import com.tmps.lab2.facade.CheckoutRequest;
import com.tmps.lab2.facade.CheckoutResponse;
import com.tmps.lab2.facade.InvoiceAddOn;
import com.tmps.lab2.facade.StoreCheckoutFacade;
import com.tmps.lab2.proxy.OrderFulfillmentService;
import com.tmps.lab2.proxy.SecuredOrderFulfillmentProxy;
import com.tmps.lab2.proxy.StandardOrderFulfillmentService;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class InteractiveCli {

    private final Scanner scanner;
    private final OrderFulfillmentService realService;
    private final SecuredOrderFulfillmentProxy secureProxy;
    private final CheckoutFacade checkoutFacade;

    // Cart state
    private final List<OrderItemComponent> cart = new ArrayList<>();
    private final Set<InvoiceAddOn> selectedAddOns = EnumSet.noneOf(InvoiceAddOn.class);
    private String giftMessage = "Enjoy your vintage vinyl selection!";
    private UserSession currentSession = UserSession.customer("john_collector");

    public InteractiveCli(Scanner scanner) {
        this.scanner = scanner;
        this.realService = new StandardOrderFulfillmentService();
        this.secureProxy = new SecuredOrderFulfillmentProxy(realService);
        this.checkoutFacade = new StoreCheckoutFacade(secureProxy);
    }

    public void start() {
        boolean running = true;
        while (running) {
            printMainMenu();
            System.out.print("Select an option [0-9]: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    browseCatalogAndAddToCart();
                    break;
                case "2":
                    buildCustomBoxSet();
                    break;
                case "3":
                    configureAddOns();
                    break;
                case "4":
                    switchUserSession();
                    break;
                case "5":
                    performCheckout();
                    break;
                case "6":
                    viewAuditLog();
                    break;
                case "7":
                    viewCart();
                    break;
                case "8":
                    clearCart();
                    break;
                case "9":
                    runAutomatedDemo();
                    break;
                case "0":
                    running = false;
                    System.out.println("\nThank you for visiting Vintage Groove Records! Keep spinning vinyl!");
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option between 0 and 9.");
            }
        }
    }

    private void printMainMenu() {
        double subtotal = 0.0;
        int discCount = 0;
        for (OrderItemComponent item : cart) {
            subtotal += item.getPrice();
            discCount += item.getItemCount();
        }

        System.out.println("\n================================================================================");
        System.out.println("      VINTAGE GROOVE RECORDS — Interactive Terminal POS System");
        System.out.println("================================================================================");
        System.out.printf("Active Session : [%s] %s (Role: %s)%n",
                currentSession.isAuthenticated() ? "AUTHENTICATED" : "ANONYMOUS",
                currentSession.getUsername(),
                currentSession.getRole());
        System.out.printf("Current Cart   : %d items (%d vinyl discs) | Merchandise Subtotal: $%.2f USD%n",
                cart.size(), discCount, subtotal);
        System.out.printf("Active Add-Ons : %s%n",
                selectedAddOns.isEmpty() ? "None" : selectedAddOns.toString());
        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("  [1] Browse Catalog & Add Items to Cart      (Composite: Leaves & Bundles)");
        System.out.println("  [2] Build a Custom Box Set Interactively    (Composite: Part-Whole Hierarchy)");
        System.out.println("  [3] Configure Value-Added Services          (Decorator: Dynamic Wrapping)");
        System.out.println("  [4] Switch User Login / Roles               (Proxy: Authentication & AuthZ)");
        System.out.println("  [5] Checkout & Dispatch Order               (Facade: 4-Pattern Pipeline)");
        System.out.println("  [6] View Security Proxy Audit Log           (Proxy: Audit Trail Invariant)");
        System.out.println("  [7] View Detailed Cart Contents             (Composite: Recursive Inspector)");
        System.out.println("  [8] Clear Cart");
        System.out.println("  [9] Run Full Automated 4-Pattern Showcase   (Full Static Demo)");
        System.out.println("  [0] Exit");
        System.out.println("================================================================================");
    }

    private void browseCatalogAndAddToCart() {
        System.out.println("\n--- VINTAGE GROOVE STORE CATALOG ---");
        System.out.println("  [1] [Leaf] Miles Davis - 'Kind of Blue' (180g LP)                  - $38.00");
        System.out.println("  [2] [Leaf] John Coltrane - 'A Love Supreme' (180g LP)              - $35.00");
        System.out.println("  [3] [Leaf] Dave Brubeck Quartet - 'Time Out' (180g LP)             - $32.00");
        System.out.println("  [4] [Leaf] Pink Floyd - 'The Dark Side of the Moon' (Remastered)   - $42.00");
        System.out.println("  [5] [Leaf] Rare Acetate Master Pressing (High-Value Wholesale)     - $1200.00 (Triggers Proxy $1000 Guard!)");
        System.out.println("  [6] [Composite] 'Essential Jazz Giants 1959 Box Set' (3 LPs, 15% off) - $89.25");
        System.out.println("  [0] Back to Main Menu");
        System.out.print("Select item to add [0-6]: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1":
                cart.add(new SingleRecordItem("Kind of Blue", "Miles Davis", 38.00, "180g Vinyl LP"));
                System.out.println("Added 'Kind of Blue' to cart.");
                break;
            case "2":
                cart.add(new SingleRecordItem("A Love Supreme", "John Coltrane", 35.00, "180g Vinyl LP"));
                System.out.println("Added 'A Love Supreme' to cart.");
                break;
            case "3":
                cart.add(new SingleRecordItem("Time Out", "Dave Brubeck Quartet", 32.00, "180g Vinyl LP"));
                System.out.println("Added 'Time Out' to cart.");
                break;
            case "4":
                cart.add(new SingleRecordItem("The Dark Side of the Moon", "Pink Floyd", 42.00, "Remastered LP"));
                System.out.println("Added 'The Dark Side of the Moon' to cart.");
                break;
            case "5":
                cart.add(new SingleRecordItem("Rare Acetate Master Pressing", "Miles Davis", 1200.00, "12\" Studio Acetate"));
                System.out.println("Added 'Rare Acetate Master Pressing' ($1,200.00) to cart. Note: Checkout will require STAFF or ADMIN!");
                break;
            case "6":
                VinylBoxSet box = new VinylBoxSet("Essential Jazz Giants 1959 Box Set", 15.0);
                box.addItem(new SingleRecordItem("Kind of Blue", "Miles Davis", 38.00));
                box.addItem(new SingleRecordItem("A Love Supreme", "John Coltrane", 35.00));
                box.addItem(new SingleRecordItem("Time Out", "Dave Brubeck Quartet", 32.00));
                cart.add(box);
                System.out.println("Added Composite 'Essential Jazz Giants 1959 Box Set' (3 records @ 15% discount) to cart.");
                break;
            case "0":
                break;
            default:
                System.out.println("Unknown item selection.");
        }
    }

    private void buildCustomBoxSet() {
        System.out.println("\n--- BUILD A CUSTOM VINYL BOX SET (Interactive Composite) ---");
        System.out.print("Enter box set title: ");
        String title = scanner.nextLine().trim();
        if (title.isEmpty()) title = "Custom Collector Bundle";

        System.out.print("Enter bundle discount percent (e.g. 10 for 10% off): ");
        double discount = 10.0;
        try {
            String discountInput = scanner.nextLine().trim();
            if (!discountInput.isEmpty()) {
                discount = Double.parseDouble(discountInput);
            }
        } catch (NumberFormatException ignored) {}

        VinylBoxSet customBox = new VinylBoxSet(title, discount);

        boolean adding = true;
        while (adding) {
            System.out.printf("%nBox Set '%s' currently has %d records (Price with discount: $%.2f USD)%n",
                    customBox.getTitle(), customBox.getItemCount(), customBox.getPrice());
            System.out.println("Add a record to this box set:");
            System.out.println("  [1] Miles Davis - 'Kind of Blue' ($38.00)");
            System.out.println("  [2] John Coltrane - 'A Love Supreme' ($35.00)");
            System.out.println("  [3] Dave Brubeck - 'Time Out' ($32.00)");
            System.out.println("  [4] Custom Record (Enter title, artist, price)");
            System.out.println("  [5] Finish & Add Box Set to Cart");
            System.out.print("Choice [1-5]: ");

            String ch = scanner.nextLine().trim();
            switch (ch) {
                case "1":
                    customBox.addItem(new SingleRecordItem("Kind of Blue", "Miles Davis", 38.00));
                    break;
                case "2":
                    customBox.addItem(new SingleRecordItem("A Love Supreme", "John Coltrane", 35.00));
                    break;
                case "3":
                    customBox.addItem(new SingleRecordItem("Time Out", "Dave Brubeck Quartet", 32.00));
                    break;
                case "4":
                    System.out.print("Album title: ");
                    String customTitle = scanner.nextLine().trim();
                    System.out.print("Artist name: ");
                    String customArtist = scanner.nextLine().trim();
                    System.out.print("Price (USD): ");
                    double customPrice = 25.0;
                    try {
                        customPrice = Double.parseDouble(scanner.nextLine().trim());
                    } catch (Exception ignored) {}
                    customBox.addItem(new SingleRecordItem(customTitle, customArtist, customPrice));
                    break;
                case "5":
                    if (customBox.getItemCount() == 0) {
                        System.out.println("Cannot add an empty box set to cart. Add at least one record.");
                    } else {
                        cart.add(customBox);
                        System.out.printf("Success! Added custom Composite '%s' (%d discs, $%.2f) to cart.%n",
                                customBox.getTitle(), customBox.getItemCount(), customBox.getPrice());
                        adding = false;
                    }
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void configureAddOns() {
        System.out.println("\n--- CONFIGURE VALUE-ADDED SERVICES (Decorator Pattern) ---");
        System.out.println("Decorators dynamically wrap the base invoice at runtime without class explosion.");
        System.out.printf("  [1] Toggle Ultrasonic Deep Bath Cleaning ($4.00 per record) - Currently: %s%n",
                selectedAddOns.contains(InvoiceAddOn.ULTRASONIC_CLEANING) ? "ON [Active]" : "OFF");
        System.out.printf("  [2] Toggle Collector Transit Loss Insurance ($7.50 flat)     - Currently: %s%n",
                selectedAddOns.contains(InvoiceAddOn.TRANSIT_INSURANCE) ? "ON [Active]" : "OFF");
        System.out.printf("  [3] Toggle Luxury Japanese Obi Gift Ribbon ($5.00 flat)      - Currently: %s%n",
                selectedAddOns.contains(InvoiceAddOn.GIFT_WRAPPING) ? "ON [Active]" : "OFF");
        System.out.println("  [4] Change Custom Gift Card Message");
        System.out.println("  [0] Back to Main Menu");
        System.out.print("Toggle option [0-4]: ");

        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1":
                toggleAddOn(InvoiceAddOn.ULTRASONIC_CLEANING);
                break;
            case "2":
                toggleAddOn(InvoiceAddOn.TRANSIT_INSURANCE);
                break;
            case "3":
                toggleAddOn(InvoiceAddOn.GIFT_WRAPPING);
                break;
            case "4":
                System.out.print("Enter custom message for gift card: ");
                String msg = scanner.nextLine().trim();
                if (!msg.isEmpty()) {
                    this.giftMessage = msg;
                    System.out.println("Updated gift card message.");
                }
                break;
            case "0":
                break;
            default:
                System.out.println("Invalid selection.");
        }
    }

    private void toggleAddOn(InvoiceAddOn addOn) {
        if (selectedAddOns.contains(addOn)) {
            selectedAddOns.remove(addOn);
            System.out.println("Removed " + addOn + " from invoice decorators.");
        } else {
            selectedAddOns.add(addOn);
            System.out.println("Attached " + addOn + " decorator to invoice stack.");
        }
    }

    private void switchUserSession() {
        System.out.println("\n--- SWITCH USER AUTHENTICATION & ROLE (Proxy Pattern) ---");
        System.out.println("The SecuredOrderFulfillmentProxy validates roles before delegating execution:");
        System.out.println("  [1] Guest / Anonymous User (Unauthenticated -> Proxy BLOCKS checkout or priority!)");
        System.out.println("  [2] Regular Customer ('miles_collector' -> Can order up to $1,000)");
        System.out.println("  [3] Store Staff Manager ('sarah_manager' -> Authorized for wholesale > $1,000)");
        System.out.println("  [4] System Administrator ('admin_dave' -> Full system privileges)");
        System.out.print("Select session [1-4]: ");

        String ch = scanner.nextLine().trim();
        switch (ch) {
            case "1":
                currentSession = UserSession.guest();
                System.out.println("Switched to GUEST session. Note: Proxy will intercept transactional orders!");
                break;
            case "2":
                currentSession = UserSession.customer("miles_collector");
                System.out.println("Switched to CUSTOMER session ('miles_collector').");
                break;
            case "3":
                currentSession = UserSession.staff("sarah_manager");
                System.out.println("Switched to STAFF session ('sarah_manager'). Authorized for wholesale!");
                break;
            case "4":
                currentSession = UserSession.admin("admin_dave");
                System.out.println("Switched to ADMIN session ('admin_dave').");
                break;
            default:
                System.out.println("Invalid selection.");
        }
    }

    private void performCheckout() {
        if (cart.isEmpty()) {
            System.out.println("\n[!] Cart is empty! Please add some records or box sets before checkout.");
            return;
        }

        System.out.println("\n================================================================================");
        System.out.println(">>> INITIATING CHECKOUT THROUGH FACADE (Integrated 4-Pattern Pipeline)");
        System.out.println("================================================================================");
        System.out.println("Execution Path: Client -> Facade -> Proxy -> Real Subject & Decorators & Composite");

        System.out.print("Enter recipient customer name [Default: " + currentSession.getUsername() + "]: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) name = currentSession.getUsername();

        System.out.print("Enter delivery destination address: ");
        String address = scanner.nextLine().trim();
        if (address.isEmpty()) address = "742 Evergreen Terrace, Springfield";

        System.out.print("Expedited Courier Priority Air Dispatch? (y/N): ");
        boolean isPriority = scanner.nextLine().trim().equalsIgnoreCase("y");

        // Assemble Facade Request DTO
        CheckoutRequest.Builder reqBuilder = new CheckoutRequest.Builder()
                .customer(name)
                .destination(address)
                .priority(isPriority)
                .giftMessage(giftMessage);

        for (OrderItemComponent item : cart) {
            reqBuilder.addItem(item);
        }
        for (InvoiceAddOn addOn : selectedAddOns) {
            reqBuilder.addOn(addOn);
        }

        CheckoutRequest request = reqBuilder.build();

        try {
            System.out.println("\nDispatching request to StoreCheckoutFacade.processCheckout(...)...");
            CheckoutResponse response = checkoutFacade.processCheckout(request, currentSession);

            System.out.println("\n[SUCCESS] Checkout Completed Successfully through Facade!");
            System.out.printf("Order ID        : #%d%n", response.getOrderId());
            System.out.printf("Tracking Number : %s%n", response.getTrackingNumber());
            System.out.printf(Locale.US, "Final Settled   : $%.2f USD%n", response.getTotalAmount());
            System.out.println("\n--- ITEMIZIED FISCAL RECEIPT ---");
            System.out.println(response.getReceipt());

            // Clear cart on successful checkout
            cart.clear();
            selectedAddOns.clear();

        } catch (AccessDeniedException ex) {
            System.out.println("\n********************************************************************************");
            System.out.println("[BLOCKED BY PROXY] Security Interception Detected!");
            System.out.println("Reason: " + ex.getMessage());
            System.out.println("The SecuredOrderFulfillmentProxy successfully guarded the subsystem.");
            System.out.println("Switch your session role (Option 4) to an authorized user to complete this order.");
            System.out.println("********************************************************************************");
        }
    }

    private void viewAuditLog() {
        System.out.println("\n--- SECURED PROXY AUDIT TRAIL ---");
        List<String> logs = secureProxy.getAuditLog();
        if (logs.isEmpty()) {
            System.out.println("No security events recorded yet in this session.");
        } else {
            for (int i = 0; i < logs.size(); i++) {
                System.out.printf("%2d. %s%n", i + 1, logs.get(i));
            }
        }
    }

    private void viewCart() {
        if (cart.isEmpty()) {
            System.out.println("\nCart is empty.");
            return;
        }

        System.out.println("\n--- CURRENT CART ITEMS (Polymorphic Composite Tree) ---");
        double subtotal = 0.0;
        int discCount = 0;
        for (int i = 0; i < cart.size(); i++) {
            OrderItemComponent item = cart.get(i);
            System.out.printf("[%d] %s%n", i + 1, item.getFormattedDetails("    "));
            subtotal += item.getPrice();
            discCount += item.getItemCount();
        }
        System.out.printf("%nTotal Items: %d | Total Vinyl Discs: %d | Merchandise Subtotal: $%.2f USD%n",
                cart.size(), discCount, subtotal);
    }

    private void clearCart() {
        cart.clear();
        selectedAddOns.clear();
        System.out.println("\nCart and add-on selections have been cleared.");
    }

    private void runAutomatedDemo() {
        System.out.println("\nRunning full automated structural pattern demonstration...\n");
        Main.runFullDemonstration();
    }
}
