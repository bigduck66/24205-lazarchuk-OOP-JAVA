package com.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {
    
    @Test
    void testAllBulls() {
        CountResult result = BullsAndCowsCounter.count(1234, 1234);
        assertEquals(4, result.getBulls(), "need 4 bulls");
        assertEquals(0, result.getCows(), "need 0 cows");
    }

    @Test
    void testMixedCase() {
        CountResult result = BullsAndCowsCounter.count(3219, 2310);
        assertEquals(1, result.getBulls(), "need 1 bull");
        assertEquals(2, result.getCows(), "need 2 cows");
    }

    @Test
    void testMultipleCalls() {
        CountResult result1 = BullsAndCowsCounter.count(1234, 4321);
        assertEquals(0, result1.getBulls());
        assertEquals(4, result1.getCows());
        
        CountResult result2 = BullsAndCowsCounter.count(1234, 1234);
        assertEquals(4, result2.getBulls(), "new bulls");
        assertEquals(0, result2.getCows(), "new cows");
    }

    @Test
    void testEdgeCases() {
        CountResult result1 = BullsAndCowsCounter.count(1000, 1000);
        assertTrue(result1.getBulls() >= 1);
        
        CountResult result2 = BullsAndCowsCounter.count(9999, 9999);
        assertTrue(result2.getBulls() >= 1);
    }   

    @Test
    void testDifferentLengths() {
        CountResult result = BullsAndCowsCounter.count(123, 1234);
        System.out.println("123 vs 1234: Bulls=" + result.getBulls() + ", Cows=" + result.getCows());
        
        assertNotNull(result);
    }
    
    @Test
    void testZeroBullsAndCows() {
        CountResult result = BullsAndCowsCounter.count(1234, 5678);
        assertEquals(0, result.getBulls());
        assertEquals(0, result.getCows());
    }
    
    @Test
    void testCountResultConstructor() {
        CountResult result = new CountResult(2, 1);
        assertEquals(2, result.getBulls());
        assertEquals(1, result.getCows());
    }
    
    @Test
    void testCountResultToString() {
        CountResult result = new CountResult(3, 1);
        String expected = "Bulls: 3, Cows: 1";
        assertEquals(expected, result.toString());
    }
}