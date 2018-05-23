package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sample.domain.Client;
import sample.repository.ClientRepository;

import static sample.Main.SCENE_DEFAULT_HEIGHT;
import static sample.Main.SCENE_DEFAULT_WIDTH;

public class RegistrationController {
    @FXML private Button reg;
    @FXML private TextField name;
    @FXML private TextField email;
    @FXML private TextField login;
    @FXML private TextField password;
    @FXML private TextField rePassword;
    @FXML private Label errorName;
    @FXML private Label errorEmail;
    @FXML private Label errorLogin;
    @FXML private Label errorPassword;
    @FXML private Label errorRePassword;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Autowired private ClientRepository clientRepository;
    @Autowired
    @Qualifier("login")
    private ControllerConfiguration.View loginCtrl;

    @FXML
    public void initialize() {
        reg.setOnAction(e -> {
            Client registeredClient = validate();
            if (isAllRight(registeredClient)) {
                clientRepository.save(registeredClient);
                JavaFxController.alert("Результат", "Вы успешно зарегистрировались!");
                JavaFxController.changing(loginCtrl.getView(), SCENE_DEFAULT_WIDTH, SCENE_DEFAULT_HEIGHT);
            }
        });
    }

    @FXML
    private void backToLogin() {
        JavaFxController.changing(loginCtrl.getView(), SCENE_DEFAULT_WIDTH, SCENE_DEFAULT_HEIGHT);
    }

    private Client validate() {
        Client client = new Client();

        if (name.getText() != null && !name.getText().isEmpty()) {
            client.setName(name.getText());
        } else {
            errorName.setText("Неправильное имя!");
        }

        if (login.getText() != null && !login.getText().isEmpty()) {
            client.setLogin(login.getText());
        } else {
            errorLogin.setText("Неправильный логин!");
        }

        if (email.getText() != null && !email.getText().isEmpty()) {
            try {
                client.setEmail(CrudController.validEmail(email.getText()));
            } catch (IllegalArgumentException e) {
                client.setEmail(null);
            }
        } else {
            errorEmail.setText("Неправильный email!");
        }

        if (password.getText() != null && !password.getText().isEmpty()) {
            if (rePassword.getText() != null
                    && password.getText().equals(rePassword.getText())) {
                client.setPassword(password.getText());
            } else {
                errorRePassword.setText("Пароли не совпадают");
            }
        } else {
            errorPassword.setText("Неправильный пароль!");
        }
        return client;
    }

    private boolean isAllRight(Client client) {
        return client.getName() != null && client.getEmail() != null
                && client.getLogin() != null && client.getPassword() != null;
    }

}
