package com.csye6225.assignment1;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

@Controller
@RequestMapping(path="/")
public class MainController {
    @GetMapping(path="/")
    public @ResponseBody String getCurrentTime(@RequestHeader String email,@RequestHeader String pwd) {
        // This returns a JSON or XML with the users
        if(email==null || email.equals("")){
            return "Email is invalid";
        }
        if(pwd==null || pwd.equals("")){
            return "pwd is invalid";
        }
        //String encryptPwd=BCrypt.hashpw(pwd,BCrypt.gensalt(12));
        //boolean validPwd=BCrypt.checkpw(pwd,encryptPwd);
        //#TBD validate email and pwd from database


        Date date=new Date();
        String strDateFormat= "hh:mm:ss a";
        DateFormat dateFormat=new SimpleDateFormat(strDateFormat);
        String formattedDate=dateFormat.format(date);
        return formattedDate;
        //#TBD return in json with http code
    }

}
