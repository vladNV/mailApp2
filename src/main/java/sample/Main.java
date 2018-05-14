package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sample.controller.ControllerConfiguration;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class Main extends Application {
    public static Map<String, Object> scenes = new HashMap<>();
    private static String[] savedArgs;
    private ConfigurableApplicationContext context;

    public static final double SCENE_DEFAULT_WIDTH = 1000;
    public static final double SCENE_DEFAULT_HEIGHT = 800;

    @Qualifier("login")
    @Autowired
    private ControllerConfiguration.View view;

    @Override
    public void init() throws Exception {
        context = SpringApplication.run(getClass(), savedArgs);
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Mail");
        primaryStage.setScene(new Scene(view.getView(), SCENE_DEFAULT_WIDTH, SCENE_DEFAULT_HEIGHT));
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
        primaryStage.show();
        scenes.put("root", primaryStage);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        context.close();
    }

    public static void main(String[] args) {
        savedArgs = args;
        launch(Main.class, args);
    }
}
