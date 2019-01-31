package com.csye6225.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    // public static final Pattern VALID_PWD_REGEX =
    //        Pattern.compile("^(?=\\P{Ll}*\\p{Ll})(?=\\P{Lu}*\\p{Lu})(?=\\P{N}*\\p{N})(?=[\\p{L}\\p{N}]*[^\\p{L}\\p{N}])[\\s\\S]{8,}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PWD_REGEX =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$l", Pattern.CASE_INSENSITIVE);

    @PostMapping(path="/user/register")
   // @RequestMapping(path="/add" ,method=RequestMethod.POST)
    public @ResponseBody JEntity addNewUser (@RequestParam String pwd
            , @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        JEntity jEntity = new JEntity();

        if (validateEmail(email)==false){
            jEntity.setMsg("Email is invalid");
        }

        /*if (validateEmail(email)==false){
            return "invalid email";
        }*/
        // if (validatePwd(pwd)==false){
        //     return "invalid pwd";
        // }
        //#TBD Validate if email already exists
        //first push heta

        String encryptedPwd=BCrypt.hashpw(pwd,BCrypt.gensalt());
        User n = new User();
        n.setpwd(encryptedPwd);
        //n.setpwd(pwd);
        n.setEmail(email);
        userRepository.save(n);


        return jEntity;

    }

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



    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public static boolean validatePwd(String pwdStr) {
        Matcher matcher = VALID_PWD_REGEX.matcher(pwdStr);
        return matcher.find();
    }

    //#TBD unit/integration testing
}
