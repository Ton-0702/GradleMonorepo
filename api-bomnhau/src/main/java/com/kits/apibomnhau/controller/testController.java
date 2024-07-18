package com.kits.apibomnhau.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api/v1/test")
public class testController {

    @GetMapping("")
    public void getTest() {   //@RequestParam(defaultValue="1") String page
        System.out.println("Check");
    }
}
