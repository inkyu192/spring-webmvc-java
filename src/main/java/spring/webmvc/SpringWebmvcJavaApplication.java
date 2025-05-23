package spring.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class SpringWebmvcJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringWebmvcJavaApplication.class, args);
    }

}
