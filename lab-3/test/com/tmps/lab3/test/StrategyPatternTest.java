package com.tmps.lab3.test;

import com.tmps.lab3.domain.RecordOrder;
import com.tmps.lab3.domain.RecordOrderBuilder;
import com.tmps.lab3.state.VinylOrderContext;
import com.tmps.lab3.strategy.ExpressCourierShippingStrategy;
import com.tmps.lab3.strategy.InternationalAirShippingStrategy;
import com.tmps.lab3.strategy.ShippingStrategy;
import com.tmps.lab3.strategy.StandardGroundShippingStrategy;

public class StrategyPatternTest {

    /**
     * Requirement B Test:
     * Each Strategy produces its expected result according to its distinct pricing algorithm.
     */
    public void testEachStrategyProducesExpectedResult() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("McCoy Tyner")
                .addItem("The Real McCoy", "McCoy Tyner", 35.00)
                .addItem("Inception", "McCoy Tyner", 30.00)
                .build();
        // 2 records, subtotal = 65.00

        ShippingStrategy ground = new StandardGroundShippingStrategy();
        // Ground: base 5.00 + (2 * 1.00) = 7.00
        double groundCost = ground.calculateShippingCost(order);
        if (Math.abs(groundCost - 7.00) > 0.001) {
            throw new AssertionError(String.format("Ground strategy expected 7.00, got %.2f", groundCost));
        }
        if (ground.getEstimatedDeliveryDays() != 5) {
            throw new AssertionError("Ground strategy delivery days mismatch");
        }

        ShippingStrategy express = new ExpressCourierShippingStrategy();
        // Express: base 15.00 + (2 * 2.50) = 20.00
        double expressCost = express.calculateShippingCost(order);
        if (Math.abs(expressCost - 20.00) > 0.001) {
            throw new AssertionError(String.format("Express strategy expected 20.00, got %.2f", expressCost));
        }
        if (express.getEstimatedDeliveryDays() != 1) {
            throw new AssertionError("Express strategy delivery days mismatch");
        }

        ShippingStrategy air = new InternationalAirShippingStrategy();
        // Air: base 35.00 + (2 * 5.00) = 45.00
        double airCost = air.calculateShippingCost(order);
        if (Math.abs(airCost - 45.00) > 0.001) {
            throw new AssertionError(String.format("Air strategy expected 45.00, got %.2f", airCost));
        }
        if (air.getEstimatedDeliveryDays() != 8) {
            throw new AssertionError("Air strategy delivery days mismatch");
        }
    }

    /**
     * Requirement B & Advanced Requirement A Test:
     * Strategies can be swapped at runtime, altering context calculation dynamically.
     */
    public void testStrategiesCanBeSwappedAtRuntime() {
        RecordOrder order = new RecordOrderBuilder()
                .withCustomer("Wayne Shorter")
                .addItem("Speak No Evil", "Wayne Shorter", 40.00)
                .build();
        // 1 record, subtotal = 40.00

        VinylOrderContext context = new VinylOrderContext(order, new StandardGroundShippingStrategy());
        // Initial Grand Total: 40.00 + 6.00 = 46.00
        if (Math.abs(context.calculateGrandTotal() - 46.00) > 0.001) {
            throw new AssertionError("Initial ground calculation error: " + context.calculateGrandTotal());
        }

        // Swap to Express Courier at runtime
        context.setShippingStrategy(new ExpressCourierShippingStrategy());
        // New Grand Total: 40.00 + (15.00 + 2.50) = 57.50
        if (Math.abs(context.calculateGrandTotal() - 57.50) > 0.001) {
            throw new AssertionError("Runtime swap to express calculation error: " + context.calculateGrandTotal());
        }
        if (!context.getShippingStrategy().getCarrierName().contains("Sonic Courier Express")) {
            throw new AssertionError("Carrier name not updated after runtime swap");
        }

        // Swap to International Air at runtime
        context.setShippingStrategy(new InternationalAirShippingStrategy());
        // New Grand Total: 40.00 + (35.00 + 5.00) = 80.00
        if (Math.abs(context.calculateGrandTotal() - 80.00) > 0.001) {
            throw new AssertionError("Runtime swap to air calculation error: " + context.calculateGrandTotal());
        }
    }
}
