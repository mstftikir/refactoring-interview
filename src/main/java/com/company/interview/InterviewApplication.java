package com.company.interview;

import com.company.interview.javafx.JavaFxApplication;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class InterviewApplication {
    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }
}
