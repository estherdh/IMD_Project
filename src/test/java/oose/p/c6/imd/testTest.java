package oose.p.c6.imd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class testTest {
    @Test
    public void firstTest(){
        test t = new test();
        String s = t.getHelloWorld();
        assertEquals("Hello World", s);
    }
}
