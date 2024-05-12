package com.apartment.house.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

  @GetMapping("/update")
  public ResponseEntity<?> update() {
    return ResponseEntity.ok("response");
  }
}
