package com.tmps.lab3.domain;

public class VinylRecord {
    private final String title;
    private final String artist;
    private final double price;

    public VinylRecord(String title, String artist, double price) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Record title cannot be blank");
        }
        if (price < 0.0) {
            throw new IllegalArgumentException("Record price cannot be negative");
        }
        this.title = title.trim();
        this.artist = (artist == null || artist.trim().isEmpty()) ? "Unknown Artist" : artist.trim();
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return String.format("\"%s\" by %s ($%.2f)", title, artist, price);
    }
}
