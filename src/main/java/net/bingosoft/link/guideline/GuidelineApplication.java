package net.bingosoft.link.guideline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(
		scanBasePackages = {"net.bingosoft.link.guideline"},exclude = DataSourceAutoConfiguration.class
)
public class GuidelineApplication {

	public static void main(String[] args) {
		SpringApplication.run(GuidelineApplication.class, args);
	}

}


