//package com.example.otwAppservice.controller;
//
//import com.example.otwAppservice.Security.JwtTokenUtil;
//import com.example.otwAppservice.dto.JwtRequest;
//import com.example.otwAppservice.entity.User;
//import com.example.otwAppservice.service.userService.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//
//
//@RestController
//@RequestMapping("api/authentication")
//public class JwtAuthenticationController {
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//    @Autowired
//    private UserService userDetailsService;
//
//    @RequestMapping(value = "/generateOuath", method = RequestMethod.POST)
//    public String createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//
//        final User user = userDetailsService.getUserByPhoneNumber(authenticationRequest.getUsername());
//
//        final String token = jwtTokenUtil.generateToken(user.getPhoneNumber());
//
//        return token;
//    }
//
//    private void authenticate(String username, String password) {
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//    }
//}
