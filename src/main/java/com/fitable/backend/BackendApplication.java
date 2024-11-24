package com.fitable.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.fitable.backend.facilitytraining.mapper",
		"com.fitable.backend.user.mapper", "com.fitable.backend.hometraining.mapper", "com.fitable.backend.calendar.mapper"
		})
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
