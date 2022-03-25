package ust.tad.analysismanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnalysisManagerApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(AnalysisManagerApplication.class);
		app.setBannerMode(Mode.OFF);
		//app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}

}
