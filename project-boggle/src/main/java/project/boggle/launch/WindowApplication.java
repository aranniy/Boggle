package project.boggle.launch;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import project.boggle.SpringBootExampleApplication;


public class WindowApplication extends Application {
    private ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(WindowApplication.class, args);

        launch();
    }

    @Override
    public void init() {
        this.applicationContext = new SpringApplicationBuilder(SpringBootExampleApplication.class).run();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

}

