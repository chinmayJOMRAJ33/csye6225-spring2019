package com.csye6225.assignment1;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainControllerTest {

    MainController mainController = new MainController();
    @Test
    public void validateEmail() {
        String email;
        email = "abcd@gmail.com";
        //  boolean validEmail = mainController.validateEmail(email);

        assertTrue("Correct Email Format",mainController.validateEmail(email));

    }

    @Test
    public void validatePwd() {
        String password;
        password = "aBCD1@gh";

        System.out.println(mainController.validatePwd(password));

        //  boolean validEmail = mainController.validateEmail(email);
        assertFalse("Correct Password",mainController.validatePwd(password));
    }
    }
