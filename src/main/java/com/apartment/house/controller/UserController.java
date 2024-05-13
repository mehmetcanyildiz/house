package com.apartment.house.controller;

import io.jsonwebtoken.ExpiredJwtException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @GetMapping("/update")
  public ResponseEntity<?> update(){
    return ResponseEntity.ok("response");
  }
}
