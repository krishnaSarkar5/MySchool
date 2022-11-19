package com.MySchool.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class Demo {

	@GetMapping("/test")
	private String test(@RequestHeader("Authorization") String Authorization) {
		return "Hello World!";
	}
}
