package ex.pp_3_1_5;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Pp315Application {

    public static void main(String[] args) {
        SpringApplication.run(Pp315Application.class, args);
    }
    @Bean
    public ModelMapper modelMap() {
        return new ModelMapper();
    }
}
