package org.example.BarnesAndNoble;

public interface BookDatabase {
        Book findByISBN(String ISBN);
}