package tddKoans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class GreetingKata{
    @Test
    public void greetTest(){
        String expected = "Hello, David.";

        String actual = TDDMain.greet("David");

        assertEquals(expected, actual);
    }

    @Test
    public void nullGreeting(){
        String expected = "Hello, my friend.";

        String input = null;
        String actual = TDDMain.greet(input);
        
        assertEquals(expected, actual);
    }

    @Test
    public void shoutGreeting(){
        String expected = "HELLO JERRY!";
        
        String actual = TDDMain.greet("JERRY");

        assertEquals(expected, actual);
    }

    @Test
    public void arrayGreeting(){
        String expected = "Hello, Jill and Jane.";
        
        String actual = TDDMain.greet(new String[]{"Jill", "Jane"});

        assertEquals(expected, actual);
    }

    @Test
    public void greetMoreThan2(){
        String expected = "Hello, Amy, Brian, and Charlotte.";

        String actual = TDDMain.greet(new String[]{"Amy", "Brian", "Charlotte"});

        assertEquals(expected, actual);
    }

    @Test
    public void greetMoreThan2WithShouting(){
        String expected = "Hello, Amy and Charlotte. AND HELLO BRIAN!";

        String actual = TDDMain.greet(new String[]{"Amy", "BRIAN", "Charlotte"});

        assertEquals(expected, actual);
    }

    
    @Test
    public void greetMorethan2InOneString(){
        String expected = "Hello, Bob, Charlie, and Dianne.";
        
        String actual = TDDMain.greet(new String[]{"Bob", "Charlie, Dianne"});
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void greetWithExeapes(){
        String expected = "Hello, Bob and Charlie, Dianne.";

        String actual = TDDMain.greet(new String[]{"Bob", "\"Charlie, Dianne\""});

        assertEquals(expected, actual);
    }

}