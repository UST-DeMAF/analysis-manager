package ust.tad.analysismanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.shell.interactive.enabled=false"
})
class AnalysisManagerApplicationTests {

	@Test
	void contextLoads() {
	}

}
