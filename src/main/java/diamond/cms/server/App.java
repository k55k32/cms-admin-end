package diamond.cms.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "diamond.cms.server.*" })
public class App {
    public static void main(String[] args) {
        // auto build
        SpringApplication.run(App.class);
    }
}
