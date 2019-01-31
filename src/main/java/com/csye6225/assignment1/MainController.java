package com.csye6225.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.*;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path="/")
public class MainController {
    @Autowired
    private UserRepository userRepository;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static final Pattern VALID_PWD_REGEX =
             Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[@#$%])(?=.*[A-Z]).{8,30}$");

    @PostMapping(path = "/user/register")
    public @ResponseBody
    JEntity addNewUser(@RequestParam String email
            , @RequestParam String pwd) {
        JEntity jEntity = new JEntity();

        if (validateEmail(email) == false) {
            jEntity.setMsg("Please enter a valid email id");
            return jEntity;
        }

         if (validatePwd(pwd)==false){
             jEntity.setMsg("Password should atleast have 1 Lower case, 1 upper case, 1 digit and 1 special character ");
             return jEntity;
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = new User();
            String encryptedPwd = BCrypt.hashpw(pwd, BCrypt.gensalt(12));
            user.setpwd(encryptedPwd);
            user.setEmail(email);
            userRepository.save(user);
            jEntity.setMsg("User account created successfully!");
            return jEntity;

        } else {
            jEntity.setMsg("User account already exist!");
            return jEntity;

        }



    }

    @GetMapping(path = "/")
    public @ResponseBody
    JEntity getCurrentTime(HttpServletRequest httpServletRequest) {

        JEntity j = new JEntity();
        String auth=httpServletRequest.getHeader("Authorization");
        if (auth != null && !auth.isEmpty() && auth.toLowerCase().startsWith("basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            if (!base64Credentials.isEmpty() && base64Credentials!=null &&Base64.isBase64(base64Credentials)) {
                byte[] credDecoded = Base64.decodeBase64(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);
                String[] values = credentials.split(":", 2);
                String email = values[0];
                String pwd = values[1];

                User u = userRepository.findByEmail(email);


                if (u == null) {
                    j.setMsg("Please enter a valid email!");
                    return j;
                } else {

                    if (!BCrypt.checkpw(pwd, u.getpwd())) {
                        j.setMsg("Please enter valid password!");
                        return j;
                    }
                    Date date=new Date();
                    String strDateFormat= "hh:mm:ss a";
                    DateFormat dateFormat=new SimpleDateFormat(strDateFormat);
                    String formattedDate=dateFormat.format(date);
                    j.setMsg("User is logged in! "+formattedDate);
                    return j;
                }
            }
            else{
                j.setMsg("User is not authorized!");
                return j;
            }


        }
        j.setMsg("User is not logged in!");
        return j;
    }



    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
    public static boolean validatePwd(String pwdStr) {
        Matcher matcher = VALID_PWD_REGEX.matcher(pwdStr);
        return matcher.find();
    }

}
