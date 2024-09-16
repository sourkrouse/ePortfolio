package com.zybooks.weighttracker;

import org.junit.Test;
import com.zybooks.weighttracker.AuthenticateUser;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


public class ExampleUnitTest {

    @Test
    public void testStringLengthTrue() {
        assertTrue(AuthenticateUser.checkStringLength("laura", 50));
    }
    @Test
    public void testStringLengthFalse() {
        List<String> text = new ArrayList<String>();

        //invalid lengths
        text.add("123456789123456789123456789");
        text.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        text.add("vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");

        for(String t : text){
            assertFalse(AuthenticateUser.checkStringLength(t,25));
        }

    }

    @Test
    public void testPasswordTrue() {
        assertTrue(AuthenticateUser.checkPassword("Test12!"));
    }
    @Test
    public void testPasswordFalse() {
        List<String> pws = new ArrayList<String>();

        //invalid passwords
        pws.add("123456789123456789123456789");
        pws.add("test123");
        pws.add("&&&");
        pws.add("<script>");
        pws.add("test()");
        for(String p : pws){
            assertFalse(AuthenticateUser.checkPassword(p));
        }

    }
    @Test
    public void testEmailTrue() {
        assertTrue(AuthenticateUser.checkEmail("laura@test.com"));
    }
    @Test
    public void testEmailFalse() {
        List<String> emails = new ArrayList<String>();

        //invalid email addresses
        emails.add("laura.br.ooks.example.com");
        emails.add("laura brooks@example.com");
        //emails.add("laura@.example.me.org");
        emails.add("laura@@test.me.org");
        for(String e : emails){
            assertFalse(AuthenticateUser.checkEmail(e));
        }

    }
}