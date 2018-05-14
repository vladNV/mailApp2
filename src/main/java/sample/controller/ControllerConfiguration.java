package sample.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class ControllerConfiguration {


    @Bean(name = "login")
    public View getLogin() {
        return loadView("fxml/login.fxml");
    }

    @Bean(name = "form")
    public View getForm() {
        return loadView("fxml/form.fxml");
    }

    @Bean(name = "registration")
    public View getRegistration() {
        return loadView("fxml/registration.fxml");
    }

    @Bean(name = "crud")
    public View getCrud() {
        return loadView("fxml/crud.fxml");
    }

    @Bean(name = "box")
    public View getBox() {
        return loadView("fxml/box.fxml");
    }

    @Bean(name = "writing")
    public View getWriting() { return loadView("fxml/letter.fxml"); }

    @Bean
    public LoginController getLoginController() {
        return (LoginController) getLogin().getController();
    }

    @Bean
    public FormController getFormController() {
        return (FormController) getForm().getController();
    }

    @Bean
    public RegistrationController getRegistrationController() {
        return (RegistrationController) getRegistration().getController();
    }

    @Bean
    public CrudController getCrudController() {
        return (CrudController) getCrud().getController();
    }

    @Bean
    public MailboxController getMailBoxController() {
        return (MailboxController) getBox().getController();
    }

    @Bean
    public WritingController getWritingController() {
        return (WritingController) getWriting().getController();
    }

    @SneakyThrows(Exception.class)
    private View loadView(String url) {
        InputStream fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
        FXMLLoader loader = new FXMLLoader();
        loader.load(fxmlStream);
        return new View(loader.getRoot(), loader.getController());
    }


    public class View {
        private Parent view;
        private Object controller;

        public View(Parent view, Object controller) {
            this.view = view;
            this.controller = controller;
        }

        public Parent getView() {
            return view;
        }

        public void setView(Parent view) {
            this.view = view;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }

}
