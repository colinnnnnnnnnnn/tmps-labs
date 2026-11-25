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
import com.tmps.lab2.domain.UserSession;
import com.tmps.lab2.facade.CheckoutFacade;
import com.tmps.lab2.facade.CheckoutRequest;
import com.tmps.lab2.facade.CheckoutResponse;
import com.tmps.lab2.facade.InvoiceAddOn;
import com.tmps.lab2.facade.StoreCheckoutFacade;
import com.tmps.lab2.proxy.OrderFulfillmentService;
import com.tmps.lab2.proxy.SecuredOrderFulfillmentProxy;
import com.tmps.lab2.proxy.StandardOrderFulfillmentService;

import java.util.EnumSet;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // If explicit --demo flag is provided, run automated showcase immediately
        if (args.length > 0 && (args[0].equalsIgnoreCase("--demo") || args[0].equalsIgnoreCase("-d") || args[0].equalsIgnoreCase("--automated"))) {
            runFullDemonstration();
            return;
        }

        // If stdin is interactive (or has user input), launch the Interactive CLI
        Scanner scanner = new Scanner(System.in);
        try {
            // Check if there is interactive input available
            if (System.console() != null || System.in.available() > 0) {
                InteractiveCli cli = new InteractiveCli(scanner);
                cli.start();
                return;
            }
        } catch (Exception ignored) {
            // Fall back to automated demo on environments where interactive stream is not supported
        }

        // Default: If no console or piped input without terminal, run the full demonstration
        runFullDemonstration();
    }

    public static void runFullDemonstration() {
        System.out.println("================================================================================");
        System.out.println("   TMPS Laboratory Work 2: Structural Design Patterns Demonstration");
        System.out.println("             Domain: Vintage Groove Records (Music Store)");
        System.out.println("================================================================================\n");

        // -------------------------------------------------------------------------
        // 1. COMPOSITE PATTERN DEMONSTRATION
        // -------------------------------------------------------------------------
        System.out.println(">>> [1] STRUCTURAL PATTERN: COMPOSITE (Uniform Catalog Hierarchy)");
        System.out.println("Creating individual vinyl records (Leaves) and a collector box set (Composite):");

        SingleRecordItem record1 = new SingleRecordItem("Kind of Blue", "Miles Davis", 38.00, "180g Vinyl LP");
        SingleRecordItem record2 = new SingleRecordItem("A Love Supreme", "John Coltrane", 35.00, "180g Vinyl LP");
        SingleRecordItem record3 = new SingleRecordItem("Time Out", "Dave Brubeck Quartet", 32.00, "180g Vinyl LP");

        VinylBoxSet jazzClassicsBox = new VinylBoxSet("Essential Jazz Giants 1959 Box Set", 15.0); // 15% bundle discount
        jazzClassicsBox.addItem(record1);
        jazzClassicsBox.addItem(record2);
        jazzClassicsBox.addItem(record3);

        SingleRecordItem standAlone = new SingleRecordItem("The Dark Side of the Moon", "Pink Floyd", 42.00, "Remastered LP");

        System.out.println(standAlone.getFormattedDetails("  "));
        System.out.println(jazzClassicsBox.getFormattedDetails("  "));
        System.out.printf(Locale.US, "  >> Leaf count: %d, Box set total vinyl count: %d (discounted bundle price: $%.2f)%n%n",
                standAlone.getItemCount(), jazzClassicsBox.getItemCount(), jazzClassicsBox.getPrice());

        // -------------------------------------------------------------------------
        // 2. DECORATOR PATTERN DEMONSTRATION
        // -------------------------------------------------------------------------
        System.out.println(">>> [2] STRUCTURAL PATTERN: DECORATOR (Dynamic Value-Added Services)");
        System.out.println("Constructing base order with creational Builder and dynamically decorating invoice:");

        RecordOrder sampleOrder = new RecordOrderBuilder()
                .withCustomer("Herbie Hancock")
                .addItem(standAlone)
                .addItem(jazzClassicsBox)
                .withShippingAddress("Blue Note Avenue, Chicago, IL")
                .build();

        OrderInvoice invoice = new BaseOrderInvoice(sampleOrder);
        System.out.printf(Locale.US, "  - Base Invoice Total (incl 10%% tax): %.2f USD%n", invoice.calculateTotal());

        // Stacking Decorators
        invoice = new UltrasonicCleaningDecorator(invoice, sampleOrder.getTotalRecordCount());
        System.out.printf(Locale.US, "  - After Ultrasonic Cleaning Decorator: %.2f USD%n", invoice.calculateTotal());

        invoice = new VinylInsuranceDecorator(invoice, "VG-COV-88219");
        System.out.printf(Locale.US, "  - After Transit Loss/Damage Insurance Decorator: %.2f USD%n", invoice.calculateTotal());

        invoice = new GiftWrapDecorator(invoice, "Happy Birthday from Blue Note Records!");
        System.out.printf(Locale.US, "  - After Luxury Obi Gift-Wrap Decorator: %.2f USD%n%n", invoice.calculateTotal());

        // -------------------------------------------------------------------------
        // 3. PROXY PATTERN DEMONSTRATION
        // -------------------------------------------------------------------------
        System.out.println(">>> [3] STRUCTURAL PATTERN: PROXY (Access Control, Security & Audit)");
        OrderFulfillmentService realService = new StandardOrderFulfillmentService();
        SecuredOrderFulfillmentProxy secureProxy = new SecuredOrderFulfillmentProxy(realService);

        System.out.println("Attempt 1: Guest user attempting priority order fulfillment...");
        RecordOrder priorityOrder = new RecordOrderBuilder()
                .withCustomer("Anonymous Collector")
                .addItem(standAlone)
                .asPriority(true)
                .build();
        OrderInvoice guestInvoice = new BaseOrderInvoice(priorityOrder);

        try {
            secureProxy.fulfillOrder(priorityOrder, guestInvoice, UserSession.guest());
        } catch (AccessDeniedException ex) {
            System.out.println("  [BLOCKED BY PROXY] Expected security interception: " + ex.getMessage());
        }

        System.out.println("Attempt 2: Authenticated Staff user fulfilling the same order...");
        UserSession staffSession = UserSession.staff("sarah_manager");
        secureProxy.fulfillOrder(priorityOrder, guestInvoice, staffSession);
        System.out.println("  [ALLOWED BY PROXY] Execution successfully delegated to Real Fulfillment Service!");
        System.out.println("  Audit Trail recorded by Proxy:");
        for (String logEntry : secureProxy.getAuditLog()) {
            System.out.println("    " + logEntry);
        }
        System.out.println();

        // -------------------------------------------------------------------------
        // 4. FACADE PATTERN DEMONSTRATION (End-to-End Integrated Interaction)
        // -------------------------------------------------------------------------
        System.out.println(">>> [4] STRUCTURAL PATTERN: FACADE (Unified Subsystem Orchestration)");
        System.out.println("Execution path: Client -> Facade -> Proxy -> Real Subject & Decorators & Composite");

        CheckoutFacade checkoutFacade = new StoreCheckoutFacade(secureProxy);

        CheckoutRequest request = new CheckoutRequest.Builder()
                .customer("Thelonious Monk")
                .destination("125th Street, Harlem, NY")
                .priority(true)
                .addItem(standAlone)
                .addItem(jazzClassicsBox)
                .addOn(InvoiceAddOn.ULTRASONIC_CLEANING)
                .addOn(InvoiceAddOn.TRANSIT_INSURANCE)
                .addOn(InvoiceAddOn.GIFT_WRAPPING)
                .giftMessage("Keep swingin' with Vintage Groove Records!")
                .build();

        UserSession customerSession = UserSession.customer("monk_vip");

        CheckoutResponse response = checkoutFacade.processCheckout(request, customerSession);

        System.out.println("Checkout Response received from Facade:");
        System.out.printf("  Success: %b | Order ID: %d | Tracking: %s%n",
                response.isSuccess(), response.getOrderId(), response.getTrackingNumber());
        System.out.printf(Locale.US, "  Total Settled: %.2f USD%n", response.getTotalAmount());
        System.out.println("\nGenerated Itemized Fiscal Receipt from Subsystem:");
        System.out.println(response.getReceipt());
        System.out.println("\n================================================================================");
        System.out.println("   All 4 Structural Design Patterns successfully demonstrated!");
        System.out.println("================================================================================");
    }
}
