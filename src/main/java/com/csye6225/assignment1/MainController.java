package com.csye6225.assignment1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
//import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.*;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(path="/")
public class MainController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    public static final Pattern VALID_PWD_REGEX =
             Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[@#$%])(?=.*[A-Z]).{8,30}$");

    @PostMapping(path = "/user/register")
    public @ResponseBody
    JEntity addNewUser(@RequestBody User user, HttpServletResponse response) {
        JEntity jEntity = new JEntity();

        if (validateEmail(user.getEmail()) == false) {
            jEntity.setMsg("Please enter a valid email id");

            jEntity.setStatuscode(HttpStatus.FORBIDDEN);
            jEntity.setCode(HttpStatus.FORBIDDEN.value());
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setHeader("status", HttpStatus.FORBIDDEN.toString());
            return jEntity;
        }

         if (validatePwd(user.getpwd())==false){
             jEntity.setMsg("Password should atleast have 1 Lower case, 1 upper case, 1 digit and 1 special character ");

             jEntity.setStatuscode(HttpStatus.EXPECTATION_FAILED);
             jEntity.setCode(HttpStatus.EXPECTATION_FAILED.value());
             response.setStatus(HttpStatus.EXPECTATION_FAILED.value());
             response.setHeader("status",HttpStatus.EXPECTATION_FAILED.toString());
             return jEntity;
        }

        User user1 = userRepository.findByEmail(user.getEmail());
        if (user1 == null) {
            user1 = new User();
            String encryptedPwd = BCrypt.hashpw(user.getpwd(), BCrypt.gensalt(12));
            user1.setpwd(encryptedPwd);
            user1.setEmail(user.getEmail());
            userRepository.save(user1);


            jEntity.setMsg("User account created successfully!");

            jEntity.setStatuscode(HttpStatus.CREATED);
            jEntity.setCode(HttpStatus.CREATED.value());
            response.setStatus(HttpStatus.CREATED.value());
            response.setHeader("status",HttpStatus.CREATED.toString());
            return jEntity;

        } else {
            jEntity.setMsg("User account already exist!");

            jEntity.setStatuscode(HttpStatus.BAD_REQUEST);
            jEntity.setCode(HttpStatus.BAD_REQUEST.value());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setHeader("status",HttpStatus.BAD_REQUEST.toString());

            return jEntity;

        }



    }

    @GetMapping(path = "/")
    public @ResponseBody
    JEntity getCurrentTime(HttpServletRequest httpServletRequest,HttpServletResponse response) {

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

                    j.setStatuscode(HttpStatus.NOT_ACCEPTABLE);
                    j.setCode(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
                    response.setHeader("status",HttpStatus.NOT_ACCEPTABLE.toString());

                    return j;
                } else {

                    if (!BCrypt.checkpw(pwd, u.getpwd())) {
                        j.setMsg("Please enter valid password!");

                        j.setStatuscode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
                        j.setCode(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
                        response.setStatus(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.value());
                        response.setHeader("status",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.toString());
                        return j;
                    }
                    Date date=new Date();
                    String strDateFormat= "hh:mm:ss a";
                    DateFormat dateFormat=new SimpleDateFormat(strDateFormat);
                    String formattedDate=dateFormat.format(date);
                    j.setMsg("User is logged in! "+formattedDate);
                    j.setStatuscode(HttpStatus.OK);
                    j.setCode(HttpStatus.OK.value());
                    response.setStatus(HttpStatus.OK.value());
                    response.setHeader("status",HttpStatus.OK.toString());

                    return j;
                }
            }
            else{
                j.setMsg("User is not authorized!");

                j.setStatuscode(HttpStatus.UNAUTHORIZED);
                j.setCode(HttpStatus.UNAUTHORIZED.value());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setHeader("status",HttpStatus.UNAUTHORIZED.toString());

                return j;
            }


        }
        j.setMsg("User is not logged in!");

        j.setStatuscode(HttpStatus.NOT_FOUND);
        j.setCode(HttpStatus.NOT_FOUND.value());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setHeader("status",HttpStatus.NOT_FOUND.toString());

        return j;
    }

    @PostMapping(path="/note")
    public @ResponseBody Note createNote(@RequestBody Note note,HttpServletRequest httpServletRequest,HttpServletResponse response){
        return saveNote(note,httpServletRequest,response);
    }


    public Note saveNote(Note note,HttpServletRequest httpServletRequest,HttpServletResponse response){
        String auth=httpServletRequest.getHeader("Authorization");
        StringBuffer msg=new StringBuffer();
        Note n=null;
        if (auth != null && !auth.isEmpty() && auth.toLowerCase().startsWith("basic")) {
            String base64Credentials = auth.substring("Basic".length()).trim();
            if (!base64Credentials.isEmpty() && base64Credentials!=null &&Base64.isBase64(base64Credentials)) {
                byte[] credDecoded = Base64.decodeBase64(base64Credentials);
                String credentials = new String(credDecoded, StandardCharsets.UTF_8);
                String[] values = credentials.split(":", 2);
                String email = values[0];
                String pwd = values[1];
                // request.
                User u = userRepository.findByEmail(email);


                if (u == null) {
                    msg.append("Email is invalid");
                    setResponse(HttpStatus.NOT_ACCEPTABLE,response,msg);
                    return n;

                } else {


                    if (!BCrypt.checkpw(pwd, u.getpwd())) {
                        msg.append("Password is incorrect");
                        setResponse(HttpStatus.UNAUTHORIZED,response,msg);
                        return n;

                    }
                    if (note==null){
                        msg.append("Please enter title and content for note");
                        setResponse(HttpStatus.NOT_FOUND,response,msg);
                        return n;
                    }
                    n=createNote(u,note);
                    noteRepository.save(n);
                    setResponse(HttpStatus.CREATED,response);

                    return n;

                }
            }
            else{

                msg.append("User is not logged in");
                setResponse(HttpStatus.UNAUTHORIZED,response,msg);
                return n;
            }


        }
        msg.append("User is not logged in");
        setResponse(HttpStatus.UNAUTHORIZED,response,msg);
        return n;
    }


    public Note createNote(User u,Note note){
        Note n=new Note();
        Instant ins=Instant.now();
        n.setId(UUID.randomUUID().toString());
        n.setCreated_on(ins.toString());
        n.setUpdated_on(ins.toString());
        n.setTitle(note.getTitle());
        n.setContent(note.getContent());
        n.setUser(u);
        return n;
    }

    public void setResponse(HttpStatus hs,HttpServletResponse response){

        response.setStatus(hs.value());
        response.setHeader("status", hs.toString());
    }

    public void setResponse(HttpStatus hs,HttpServletResponse response,StringBuffer message){
        try {
            response.sendError(hs.value(),message.toString());
        }
        catch(Exception e){

        }
        response.setStatus(hs.value());
        response.setHeader("status", hs.toString());

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
