package com.visao.exemplocrud.resources;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeResources {
	@GetMapping
	public String getHello() {
		return "Ola Spring Boot";
	}
}
