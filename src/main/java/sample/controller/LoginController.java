package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import sample.controller.exception.ClientException;
import sample.domain.Client;
import sample.repository.ClientRepository;
import sample.session.ClientDetails;

import static sample.Main.SCENE_DEFAULT_HEIGHT;
import static sample.Main.SCENE_DEFAULT_WIDTH;
import static sample.Main.scenes;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class LoginController {

    @Autowired private ClientRepository repository;

    @Autowired private ClientDetails clientDetails;

    @Qualifier("writing")
    @Autowired
    private ControllerConfiguration.View writing;

    @Qualifier("registration")
    @Autowired
    private ControllerConfiguration.View registration;

    @FXML private Button signin;
    @FXML private Button signup;
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Label error;


    @FXML
    @SneakyThrows(value = Exception.class)
    public void initialize() {
        // buttons controllers
        signin.setOnAction(event -> {
            shouldNotBeNull();
            try {
                Client client = login(username.getText(), password.getText());
                clientDetails.extractClient(client);
                scenes.put("client", clientDetails);
                JavaFxController.changing(writing.getView(), SCENE_DEFAULT_WIDTH, SCENE_DEFAULT_HEIGHT);
            } catch (ClientException e) {
                error.setText(e.getMessage());
            }
        });
        signup.setOnAction(event -> JavaFxController.changing(registration.getView(), SCENE_DEFAULT_WIDTH,
                SCENE_DEFAULT_HEIGHT));
    }

    private Client login(@NonNull String username,
                         @NonNull String password) throws ClientException {
        Client client = repository.findByLogin(username);
        if (client != null && client.getLogin().equals(username)
                && client.getPassword().equals(password)) {
            return client;
        }
        throw new ClientException("Неправильный пароль или логин");
    }

    private void shouldNotBeNull() {
        Assert.notNull(username, "username field should not be null");
        Assert.notNull(password, "password field should not be null");
        Assert.notNull(error, "label error should not be null");
    }

}
