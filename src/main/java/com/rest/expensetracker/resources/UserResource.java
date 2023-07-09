package com.rest.expensetracker.resources;

import com.rest.expensetracker.Constraints;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rest.expensetracker.domain.User;
import com.rest.expensetracker.services.UserService;

import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody Map<String,Object> userMap){
        String email=(String) userMap.get("email");
        String password=(String) userMap.get("password");
        User user=userService.validateUser(email,password);

        return new ResponseEntity<>(generateJWTToken(user),HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody Map<String, Object> userMap) {

        String firstName = (String) userMap.get("firstName");
        String lastName = (String) userMap.get("lastName");
        String email = (String) userMap.get("email");
        String password = (String) userMap.get("password");

        User user = userService.registerUser(firstName, lastName, email, password);

        return new ResponseEntity<>(generateJWTToken(user), HttpStatus.OK);
    }

    private Map<String,String> generateJWTToken(User user){
        long timeStamp=System.currentTimeMillis();
        String token = Jwts.builder().signWith(SignatureAlgorithm.HS256, Constraints.API_SERECT_KEY).setIssuedAt(new Date(timeStamp))
                .setExpiration(new Date(timeStamp+Constraints.TOKEN_VALIDITY))
                .claim("userId",user.getUserId())
                .claim("email",user.getEmail())
                .claim("firstName",user.getFirstName())
                .claim("lastName",user.getLastName())
                .compact();

        Map<String,String> map=new HashMap<>();
        map.put("token",token);
        return map;
    }
}
