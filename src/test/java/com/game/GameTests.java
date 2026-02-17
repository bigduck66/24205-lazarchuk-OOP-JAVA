package com.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

public class GameTests {
    private CheckingMatches checker;

    @BeforeEach
    void setUp(){
        checker = new CheckingMatches();
    }

    @Test
    void testConstructor(){
        assertEquals(0, checker.getBulls(), "need 0 bulls");
        assertEquals(0, checker.getCows(), "need 0 cows");
    }

    @Test
    void testAllBulls(){
        checker.count(1234, 1234);
        assertEquals(4, checker.getBulls(), "need 4 bulls");
        assertEquals(0, checker.getCows(), "need 0 cows");
    }

    @Test
    void testMixedCase(){
        checker.count(3219, 2310);
        assertEquals(1, checker.getBulls(), "need 1 bull");
        assertEquals(2, checker.getCows(), "need 2 cows");
    }

    @Test
    void testMultipleCalls() {
        checker.count(1234, 4321);
        assertEquals(0, checker.getBulls());
        assertEquals(4, checker.getCows());
        
        checker.count(1234, 1234);
        assertEquals(4, checker.getBulls(), "new bulls");
        assertEquals(0, checker.getCows(), "new cows");
    }

    @Test
    void testEdgeCases() {
        checker.count(1000, 1000);
        assertTrue(checker.getBulls() >= 1);
        checker.count(9999, 9999);
        assertTrue(checker.getBulls() >= 1);
    }   

    @Test
    void testDifferentLengths() {
        checker.count(123, 1234);
        System.out.println("123 vs 1234: Bulls=" + checker.getBulls() + ", Cows=" + checker.getCows());
    }
}