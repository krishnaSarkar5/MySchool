package com.MySchool.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class Demo {

	@GetMapping("/test")
	private String test() {
		return "Hello World!";
	}
}
