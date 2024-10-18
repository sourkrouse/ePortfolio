package com.zybooks.weighttracker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/*
Last Updated 10/6/2024, Laura Brooks
Used in RegisterActivity file when inserting a new record. Checking database constraints.


 */
public class AuthenticateUser {


    public static boolean checkEmail(String emailToCheck) {

        if(!(emailToCheck.contains(" "))){
            String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9-]+.+$";
            //initialize the Pattern object
            Pattern pattern = Pattern.compile(regex);
            //searching for occurrences of regex
            Matcher isEmail = pattern.matcher(emailToCheck);
            return isEmail.find();
        } else
        {
            return false;
        }


    }
    /*
    CHECKING THE PASSWORD IS VALID
    Argument - string containing the user entered password
    Returns - true if passed, false is default
    Summary - checking that the password is between 6-25 characters,
    at least 1 letter,
    at least 1 number,
    at least 1 special character
     */
    public static boolean checkPassword(String valueToCheck){

        if(valueToCheck.length()>=6 && valueToCheck.length()<=25)
        {
            Pattern letter = Pattern.compile("[a-zA-Z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile ("[!@#$%&*_+=|?{}\\[\\]~-]");

            Matcher hasLetter = letter.matcher(valueToCheck);
            Matcher hasDigit = digit.matcher(valueToCheck);
            Matcher hasSpecial = special.matcher(valueToCheck);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        }
        else
            return false;
    }
    /*
    CHECKING THE NAME USER FIELDS ARE CORRECT LENGTH
    Arguments - string containing the value of the first/last name fields
        int containing the limit of characters
    Returns - false if more than limitToCheck characters.
    Summary - can be used on multiple string length checks by changing out the limit.
     */
    public static boolean checkStringLength(String valueToCheck, int limitToCheck){
        if(valueToCheck.length()>=limitToCheck)
            return false;

        return true;
    }
}
