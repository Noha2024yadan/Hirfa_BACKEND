package com.HIRFA.HIRFA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(basePackages = "com.HIRFA.HIRFA", excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.HIRFA.HIRFA.service.backup.*"))
public class HirfaApplication {

	public static void main(String[] args) {
		SpringApplication.run(HirfaApplication.class, args);
	}

}
