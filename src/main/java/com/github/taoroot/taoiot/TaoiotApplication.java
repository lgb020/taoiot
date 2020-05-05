package com.github.taoroot.taoiot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TaoiotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaoiotApplication.class, args);
	}

}
