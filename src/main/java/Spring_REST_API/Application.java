package Spring_REST_API;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = "Spring_REST_API")
public class Application {

    @Autowired

    public static void main(String[] args) {

        ConfigurableApplicationContext appContext = SpringApplication.run(Application.class, args);

    }
}
