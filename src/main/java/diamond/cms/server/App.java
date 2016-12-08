package diamond.cms.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({ "diamond.cms.server.*" })
public class App {
    public static void main(String[] args) {
        // auto build - 2016-12-8 13:58:57
        SpringApplication.run(App.class);
    }
}
