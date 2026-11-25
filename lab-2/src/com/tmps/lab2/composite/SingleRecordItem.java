package com.tmps.lab2.composite;

import java.util.Locale;

public class SingleRecordItem implements OrderItemComponent {
    private final String title;
    private final String artist;
    private final double price;
    private final String format;

    public SingleRecordItem(String title, String artist, double price, String format) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Record title cannot be blank");
        }
        if (price < 0.0) {
            throw new IllegalArgumentException("Record price cannot be negative");
        }
        this.title = title.trim();
        this.artist = (artist == null || artist.trim().isEmpty()) ? "Unknown Artist" : artist.trim();
        this.price = price;
        this.format = (format == null || format.trim().isEmpty()) ? "12\" Vinyl LP" : format.trim();
    }

    public SingleRecordItem(String title, String artist, double price) {
        this(title, artist, price, "12\" Vinyl LP");
    }

    @Override
    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public String getFormattedDetails(String indent) {
        return String.format(Locale.US, "%s• [%s] \"%s\" by %s - %.2f USD",
                indent, format, title, artist, price);
    }
}
