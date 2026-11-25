package com.tmps.lab2.test;

import com.tmps.lab2.composite.OrderItemComponent;
import com.tmps.lab2.composite.SingleRecordItem;
import com.tmps.lab2.composite.VinylBoxSet;

import java.util.ArrayList;
import java.util.List;

public class CompositePatternTest {

    /**
     * Requirement B Test:
     * A Composite leaf and a Composite group can be used through the same interface.
     */
    public void testLeafAndCompositeCanBeUsedUniformlyThroughSameInterface() {
        OrderItemComponent leaf1 = new SingleRecordItem("Abbey Road", "The Beatles", 35.00);
        OrderItemComponent leaf2 = new SingleRecordItem("Let It Be", "The Beatles", 30.00);

        VinylBoxSet boxSet = new VinylBoxSet("Beatles 1969-1970 Duo", 10.0); // 10% discount
        boxSet.addItem(new SingleRecordItem("Yellow Submarine", "The Beatles", 25.00));
        boxSet.addItem(new SingleRecordItem("Revolver", "The Beatles", 35.00));
        // Subtotal = 60.00, with 10% off = 54.00, count = 2

        List<OrderItemComponent> catalog = new ArrayList<>();
        catalog.add(leaf1);
        catalog.add(leaf2);
        catalog.add(boxSet); // Treated uniformly as OrderItemComponent!

        double totalCatalogValue = 0.0;
        int totalVinylDiscs = 0;

        for (OrderItemComponent item : catalog) {
            // Polymorphic dispatch: no instanceof check needed!
            totalCatalogValue += item.getPrice();
            totalVinylDiscs += item.getItemCount();
            if (item.getTitle() == null || item.getFormattedDetails("").isEmpty()) {
                throw new AssertionError("Item detail formatting failed for: " + item.getTitle());
            }
        }

        // Expected total: 35.00 + 30.00 + 54.00 = 119.00
        double expectedValue = 119.00;
        if (Math.abs(totalCatalogValue - expectedValue) > 0.001) {
            throw new AssertionError(String.format("Expected total %.2f, but got %.2f", expectedValue, totalCatalogValue));
        }

        // Expected discs: 1 + 1 + 2 = 4
        if (totalVinylDiscs != 4) {
            throw new AssertionError("Expected 4 discs, but got: " + totalVinylDiscs);
        }
    }

    /**
     * Requirement B Test:
     * A nested box set calculates correct aggregate price with discount and item count recursively.
     */
    public void testNestedBoxSetCalculatesDiscountAndItemCountRecursively() {
        VinylBoxSet innerBox = new VinylBoxSet("Miles Davis 50s Trio", 10.0);
        innerBox.addItem(new SingleRecordItem("Round About Midnight", "Miles Davis", 30.00));
        innerBox.addItem(new SingleRecordItem("Miles Ahead", "Miles Davis", 30.00));
        innerBox.addItem(new SingleRecordItem("Milestones", "Miles Davis", 40.00));
        // Inner box raw: 100.00, discounted by 10%: 90.00, items: 3

        VinylBoxSet masterBox = new VinylBoxSet("Miles Davis Master Retrospective Anthology", 20.0);
        masterBox.addItem(innerBox); // Nested Composite!
        masterBox.addItem(new SingleRecordItem("Kind of Blue (Master Session)", "Miles Davis", 50.00));
        // Master raw: 90.00 + 50.00 = 140.00, discounted by 20%: 112.00, items: 3 + 1 = 4

        if (masterBox.getItemCount() != 4) {
            throw new AssertionError("Expected master box set to contain 4 items, got: " + masterBox.getItemCount());
        }

        double expectedPrice = 140.00 * 0.80; // 112.00
        if (Math.abs(masterBox.getPrice() - expectedPrice) > 0.001) {
            throw new AssertionError(String.format("Expected nested bundle price %.2f, but got %.2f",
                    expectedPrice, masterBox.getPrice()));
        }

        String details = masterBox.getFormattedDetails("");
        if (!details.contains("Miles Davis 50s Trio") || !details.contains("Kind of Blue (Master Session)")) {
            throw new AssertionError("Master box details missing child items:\n" + details);
        }
    }
}
