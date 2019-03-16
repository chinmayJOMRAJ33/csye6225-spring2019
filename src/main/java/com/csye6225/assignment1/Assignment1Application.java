package com.csye6225.assignment1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


//extends SpringBootServletInitializer
@SpringBootApplication
public class Assignment1Application {

    public static void main(String[] args) {
        SpringApplication.run(Assignment1Application.class, args);
    }


   // @Override
   // protected SpringApplicationBuilder configure (SpringApplicationBuilder application){
//
  //      return application.sources(Assignment1Application.class);
   // }
}

