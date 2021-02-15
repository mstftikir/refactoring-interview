package com.company.interview.javafx;

import com.company.interview.InterviewApplication;
import com.company.interview.javafx.events.StageReadyEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() {
        ApplicationContextInitializer<GenericApplicationContext> initializer =
                genericApplicationContext -> {
                    genericApplicationContext.registerBean(Application.class, () -> JavaFxApplication.this);
                    genericApplicationContext.registerBean(Parameters.class, this::getParameters);
                };
        this.context = new SpringApplicationBuilder()
                .sources(InterviewApplication.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
    }

    @Override
    public void start(Stage stage) {
        this.context.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        this.context.close();
        Platform.exit();
    }
}