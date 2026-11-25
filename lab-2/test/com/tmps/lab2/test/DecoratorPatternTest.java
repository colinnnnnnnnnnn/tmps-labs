package com.tmps.lab2.test;

import com.tmps.lab2.composite.SingleRecordItem;
import com.tmps.lab2.decorator.BaseOrderInvoice;
import com.tmps.lab2.decorator.GiftWrapDecorator;
import com.tmps.lab2.decorator.OrderInvoice;
import com.tmps.lab2.decorator.UltrasonicCleaningDecorator;
import com.tmps.lab2.decorator.VinylInsuranceDecorator;
import com.tmps.lab2.domain.RecordOrder;
import com.tmps.lab2.domain.RecordOrderBuilder;

public class DecoratorPatternTest {

    /**
     * Requirement B Test:
     * Decorators can be stacked in any order and still produce the expected result.
     */
    public void testDecoratorsCanBeStackedInAnyOrderWithIdenticalTotal() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Bill Evans")
                .addItem(new SingleRecordItem("Waltz for Debby", "Bill Evans", 40.00))
                .addItem(new SingleRecordItem("Portrait in Jazz", "Bill Evans", 40.00))
                .build();
        // Subtotal = 80.00, Tax (10%) = 8.00 => Base Invoice = 88.00
        // Ultrasonic cleaning for 2 records @ 4.00 = 8.00
        // Vinyl Insurance = 7.50
        // Gift Wrap = 5.00
        // Expected Final Total = 88.00 + 8.00 + 7.50 + 5.00 = 108.50

        // Permutation 1: Base -> Cleaning -> Insurance -> GiftWrap
        OrderInvoice inv1 = new BaseOrderInvoice(order);
        inv1 = new UltrasonicCleaningDecorator(inv1, order.getTotalRecordCount());
        inv1 = new VinylInsuranceDecorator(inv1, "POL-111");
        inv1 = new GiftWrapDecorator(inv1, "Jazz gift note");

        // Permutation 2: Base -> GiftWrap -> Insurance -> Cleaning
        OrderInvoice inv2 = new BaseOrderInvoice(order);
        inv2 = new GiftWrapDecorator(inv2, "Jazz gift note");
        inv2 = new VinylInsuranceDecorator(inv2, "POL-111");
        inv2 = new UltrasonicCleaningDecorator(inv2, order.getTotalRecordCount());

        // Permutation 3: Base -> Insurance -> GiftWrap -> Cleaning
        OrderInvoice inv3 = new BaseOrderInvoice(order);
        inv3 = new VinylInsuranceDecorator(inv3, "POL-111");
        inv3 = new GiftWrapDecorator(inv3, "Jazz gift note");
        inv3 = new UltrasonicCleaningDecorator(inv3, order.getTotalRecordCount());

        double total1 = inv1.calculateTotal();
        double total2 = inv2.calculateTotal();
        double total3 = inv3.calculateTotal();

        double expected = 108.50;

        if (Math.abs(total1 - expected) > 0.001) {
            throw new AssertionError(String.format("Permutation 1 expected %.2f, got %.2f", expected, total1));
        }
        if (Math.abs(total2 - expected) > 0.001) {
            throw new AssertionError(String.format("Permutation 2 expected %.2f, got %.2f", expected, total2));
        }
        if (Math.abs(total3 - expected) > 0.001) {
            throw new AssertionError(String.format("Permutation 3 expected %.2f, got %.2f", expected, total3));
        }

        // Check that details retain all decorated services regardless of order
        for (OrderInvoice inv : new OrderInvoice[]{inv1, inv2, inv3}) {
            String details = inv.getInvoiceDetails();
            if (!details.contains("Ultrasonic Deep Cleaning")) {
                throw new AssertionError("Invoice details missing Ultrasonic Cleaning service");
            }
            if (!details.contains("Collector Damage/Loss Transit Insurance")) {
                throw new AssertionError("Invoice details missing Transit Insurance");
            }
            if (!details.contains("Luxury Japanese Obi & Gift Ribbon")) {
                throw new AssertionError("Invoice details missing Gift Wrapping");
            }
        }
    }

    /**
     * Requirement B Test:
     * Each concrete decorator accurately adds its specific surcharge and description.
     */
    public void testIndividualDecoratorsAddExpectedSurchargeAndDetails() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Chet Baker")
                .addItem(new SingleRecordItem("Chet", "Chet Baker", 50.00))
                .build();
        // Base: 50.00 + 5.00 tax = 55.00

        BaseOrderInvoice base = new BaseOrderInvoice(order);
        if (Math.abs(base.calculateTotal() - 55.00) > 0.001) {
            throw new AssertionError("Base invoice calculation error: expected 55.00, got " + base.calculateTotal());
        }

        // Test Ultrasonic Cleaning standalone
        UltrasonicCleaningDecorator cleaning = new UltrasonicCleaningDecorator(base, 1);
        if (Math.abs(cleaning.calculateTotal() - 59.00) > 0.001) {
            throw new AssertionError("Cleaning decorator fee mismatch: expected 59.00, got " + cleaning.calculateTotal());
        }
        if (!cleaning.getInvoiceDetails().contains("Ultrasonic Deep Cleaning")) {
            throw new AssertionError("Missing cleaning description");
        }

        // Test Insurance standalone
        VinylInsuranceDecorator insurance = new VinylInsuranceDecorator(base, "TEST-POL-99");
        if (Math.abs(insurance.calculateTotal() - 62.50) > 0.001) {
            throw new AssertionError("Insurance decorator fee mismatch: expected 62.50, got " + insurance.calculateTotal());
        }
        if (!insurance.getInvoiceDetails().contains("TEST-POL-99")) {
            throw new AssertionError("Missing policy number in insurance description");
        }

        // Test Gift Wrap standalone
        GiftWrapDecorator giftWrap = new GiftWrapDecorator(base, "Special message for Chet");
        if (Math.abs(giftWrap.calculateTotal() - 60.00) > 0.001) {
            throw new AssertionError("Gift wrap decorator fee mismatch: expected 60.00, got " + giftWrap.calculateTotal());
        }
        if (!giftWrap.getInvoiceDetails().contains("Special message for Chet")) {
            throw new AssertionError("Missing custom card text in gift wrap description");
        }
    }
}
