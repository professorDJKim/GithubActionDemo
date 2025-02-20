package org.example.BarnesAndNoble;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

class BarnesAndNobleTest {

    @Test
    @DisplayName("Null Order + IDC -> Null")
    public void testGetPriceForCart1(){

        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 25);
        Book GoT =  new Book("GameofThrones",30, 25);
        Book SpiderMan =  new Book("SpiderMan",15, 25);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        // null order
        assertThat(barnesAndNoble.getPriceForCart(null)).isEqualTo(null);

    }
    @Test
    @DisplayName("Has order + Repo contains no matching book -> PurchaseSummary")
    public void testGetPriceForCart2(){

        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 25);
        Book GoT =  new Book("GameofThrones",30, 25);
        Book SpiderMan =  new Book("SpiderMan",15, 25);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("HarryPotter2", 30);
        order.put("GameofThrones2", 30);
        order.put("HarryPotter2", 30);

        assertThrows(NullPointerException.class, ()-> barnesAndNoble.getPriceForCart(order));

    }

    @Test
    @DisplayName("Empty Order + IDC -> PurchaseSummary")
    public void testGetPriceForCart3(){

        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 25);
        Book GoT =  new Book("GameofThrones",30, 25);
        Book SpiderMan =  new Book("SpiderMan",15, 25);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        Map<String, Integer> order = new HashMap<String, Integer>();
        PurchaseSummary purchaseSummary = barnesAndNoble.getPriceForCart(order);

        // check PurchaseSummary hasn't changed at all
        assertThat(purchaseSummary.getTotalPrice()).isEqualTo(0);
        assertThat(purchaseSummary.getUnavailable()).isEmpty();

    }
    @Test
    @DisplayName("Has order + Repo contains all the books + fully in stock -> PurchaseSummary")
    public void testGetPriceForCart4(){

        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 26);
        Book GoT =  new Book("GameofThrones",30, 26);
        Book SpiderMan =  new Book("SpiderMan",15, 26);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("HarryPotter", 25);
        order.put("GameofThrones", 25);
        order.put("SpiderMan", 25);

        PurchaseSummary purchaseSummary = barnesAndNoble.getPriceForCart(order);

        // we need to check for 2 things:
        // - purchaseSummary state
        // - process.buyBook() has been invoked
        int expectedTotalPrice = (harryPotter.getPrice()*25)+
                (GoT.getPrice()*25)+(SpiderMan.getPrice()*25);

        assertThat(purchaseSummary.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(purchaseSummary.getUnavailable()).isEmpty();

        verify(bookProcess,times(1)).buyBook(harryPotter,25);
        verify(bookProcess,times(1)).buyBook(GoT,25);
        verify(bookProcess,times(1)).buyBook(SpiderMan,25);
    }

    @Test
    @DisplayName("Has order + Repo contains all the books + fully in stock -> PurchaseSummary")
    public void testGetPriceForCart5(){

        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 26);
        Book GoT =  new Book("GameofThrones",30, 26);
        Book SpiderMan =  new Book("SpiderMan",15, 26);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("HarryPotter", 25);
        order.put("GameofThrones", 25);
        order.put("SpiderMan", 25);

        PurchaseSummary purchaseSummary = barnesAndNoble.getPriceForCart(order);

        // we need to check for 2 things:
        // - purchaseSummary state
        // - process.buyBook() has been invoked
        int expectedTotalPrice = (harryPotter.getPrice()*25)+
                (GoT.getPrice()*25)+(SpiderMan.getPrice()*25);

        assertThat(purchaseSummary.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(purchaseSummary.getUnavailable()).isEmpty();

        verify(bookProcess,times(1)).buyBook(harryPotter,25);
        verify(bookProcess,times(1)).buyBook(GoT,25);
        verify(bookProcess,times(1)).buyBook(SpiderMan,25);
    }

    @Test
    @DisplayName("Has order + Repo contains all the books + partially in stock -> PurchaseSummary")
    public void testGetPriceForCart6(){
        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 26);
        Book GoT =  new Book("GameofThrones",30, 26);
        Book SpiderMan =  new Book("SpiderMan",15, 20);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("HarryPotter", 25);
        order.put("GameofThrones", 25);
        order.put("SpiderMan", 25);

        PurchaseSummary purchaseSummary = barnesAndNoble.getPriceForCart(order);

        // we need to check for 2 things:
        // - purchaseSummary state
        // - process.buyBook() has been invoked
        int expectedTotalPrice = (harryPotter.getPrice()*25)+
                (GoT.getPrice()*25)+(SpiderMan.getPrice()*20);

        assertThat(purchaseSummary.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(purchaseSummary.getUnavailable()).containsEntry(SpiderMan,5);

        verify(bookProcess,times(1)).buyBook(harryPotter,25);
        verify(bookProcess,times(1)).buyBook(GoT,25);
        verify(bookProcess,times(1)).buyBook(SpiderMan,20);
    }
    @Test
    @DisplayName("Has order + Repo contains all the books + not in stock -> PurchaseSummary")
    public void testGetPriceForCart7(){
        BookDatabase repo = mock(BookDatabase.class);
        BuyBookProcess bookProcess = mock(BuyBookProcess.class);

        Book harryPotter =  new Book("HarryPotter",25, 0);
        Book GoT =  new Book("GameofThrones",30, 0);
        Book SpiderMan =  new Book("SpiderMan",15, 0);

        when(repo.findByISBN("HarryPotter")).thenReturn(harryPotter);
        when(repo.findByISBN("GameofThrones")).thenReturn(GoT);
        when(repo.findByISBN("SpiderMan")).thenReturn(SpiderMan);

        BarnesAndNoble barnesAndNoble = new BarnesAndNoble(repo,bookProcess);

        Map<String, Integer> order = new HashMap<String, Integer>();
        order.put("HarryPotter", 25);
        order.put("GameofThrones", 25);
        order.put("SpiderMan", 25);

        PurchaseSummary purchaseSummary = barnesAndNoble.getPriceForCart(order);

        // we need to check for 2 things:
        // - purchaseSummary state
        // - process.buyBook() has been invoked
        int expectedTotalPrice = (harryPotter.getPrice()*0)+
                (GoT.getPrice()*0)+(SpiderMan.getPrice()*0);

        assertThat(purchaseSummary.getTotalPrice()).isEqualTo(expectedTotalPrice);
        assertThat(purchaseSummary.getUnavailable()).containsKeys(harryPotter,GoT,SpiderMan);

        verify(bookProcess,times(1)).buyBook(harryPotter,0);
        verify(bookProcess,times(1)).buyBook(GoT,0);
        verify(bookProcess,times(1)).buyBook(SpiderMan,0);
    }

}