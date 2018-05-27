package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sample.Main;
import sample.domain.Box;
import sample.domain.Mail;
import sample.domain.MailPattern;
import sample.domain.Organization;
import sample.model.Sender;
import sample.repository.*;
import sample.session.ClientDetails;

import java.time.LocalDateTime;
import java.util.*;

public class WritingController extends FormController {
    private static final String USERNAME = "zhigulevka.dolina@gmail.com";
    private static final String PASSWORD = "Ljkbyf123";

    @FXML private TextArea text;
    @FXML private TextField receiver;
    @FXML private Button chooseOrg;
    @FXML private Button send;
    @FXML private Button saveToPatterns;
    @FXML private Button choosePattern;
    @FXML private TextField title;

    @FXML private MenuItem organization;
    @FXML private MenuItem course;
    @FXML private MenuItem signature;
    @FXML private MenuItem bank;
    @FXML private MenuItem about;
    @FXML private MenuItem changing;
    @FXML private MenuItem write;
    @FXML private MenuItem logout;

    @Autowired private OrganizationRepository repositoryOrg;
    @Autowired private BankRepository bankRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private SignatureRepository signatureRepository;
    @Autowired private MailPatternRepository mailPatternRepository;
    @Autowired private BoxRepository boxRepository;
    @Autowired private MailRepository mailRepository;


    @Autowired
    @Qualifier("login")
    private ControllerConfiguration.View login;

    @Autowired
    @Qualifier("crud")
    private ControllerConfiguration.View crud;

    @Autowired
    @Qualifier("writing")
    private ControllerConfiguration.View writing;


    @FXML
    @SneakyThrows(Exception.class)
    public void initialize() {

        organization.setOnAction(event -> {
            crudController().setTable(repositoryOrg);
            JavaFxController.changing(crud.getView());
        });
        course.setOnAction(event -> {
            crudController().setTable(courseRepository);
            JavaFxController.changing(crud.getView());
        });
        signature.setOnAction(event -> {
            crudController().setTable(signatureRepository);
            JavaFxController.changing(crud.getView());
        });
        bank.setOnAction(event -> {
            crudController().setTable(bankRepository);
            JavaFxController.changing(crud.getView());
        });

        write.setOnAction(event -> JavaFxController.changing(writing.getView()));



        changing.setOnAction(event -> {});
        about.setOnAction(event -> JavaFxController.alert("Mail program 1.0v", "Автор: ", "Программа для рассылки писем"));
        logout.setOnAction(event -> JavaFxController.changing(login.getView()));

        changing.setOnAction(event -> crudController().changeEmail());
        /* Writing controller */
        chooseOrg.setOnAction(event -> {
            List<Organization> organizations = repositoryOrg.findAll();
            List<String> choices = new ArrayList<>();
            organizations.forEach(e -> choices.add(e.getName() + ", " + e.getEmail()));
            Dialog<String> dialog = new ChoiceDialog<>("Выбрать организацию", choices);
            dialog.setTitle("Выбор органзиации");
            Optional<String> response = dialog.showAndWait();
            response.ifPresent(this::takeEmailFromString);
        });

        saveToPatterns.setOnAction(event -> {
            if (!title.getText().isEmpty() && title.getText() != null) {
                MailPattern mailPattern = new MailPattern();
                mailPattern.setTitle(title.getText());
                mailPattern.setText(text.getText());
                mailPattern.setEmails(receiver.getText());
                mailPattern.setCreatedDate(LocalDateTime.now());
                mailPatternRepository.save(mailPattern);
                JavaFxController.alert("Сохранение","Сохранение шаблона", "Ваш шаблон был успешно сохранен!");
                return;
            }
            JavaFxController.alert("Сохранение","Сохранение шаблона",
                    "Ваш шаблон не был сохранен! " +
                    "Как минимум заполните поле, тема!");
        });

        choosePattern.setOnAction(event -> {
            MailPattern empty = new MailPattern();
            List<MailPattern> patterns = mailPatternRepository.findAll();
            Dialog<MailPattern> dialog = new ChoiceDialog<>(empty, patterns);
            dialog.setTitle("Выбор шаблона");
            Optional<MailPattern> response = dialog.showAndWait();
            response.ifPresent(this::fillLetter);
        });

        send.setOnAction(event -> {
            ClientDetails clientDetails = (ClientDetails) Main.scenes.get("client");
            String title = this.title.getText();
            String emails = this.receiver.getText();
            String context = this.text.getText();
            if (title != null && emails != null && context != null) {
                Sender sender = new Sender(USERNAME, PASSWORD);
                String[] receivers = emails.split(",");
                Mail letter = new Mail();
                letter.setMsg(context);
                letter.setTitle(title);
                List<Box> letters = new ArrayList<>();
                for (String receiver : receivers) {
                    sender.send(title, context, receiver.trim());
                    Box let = Box.builder()
                            .from(clientDetails.getEmail())
                            .to(receiver)
                            .mail(letter)
                            .sentDate(LocalDateTime.now())
                            .client(clientDetails.buildClient())
                            .build();
                    letters.add(let);
                }
                mailRepository.save(letter);
                boxRepository.saveAll(letters);
            }
            JavaFxController.alert("Отправка письма",
                    "Письмо отправлено",
                    "Письмо успешно отправлено. На указанные email адреса");
        });

    }

    private void fillLetter(MailPattern pattern) {
        receiver.setText(pattern.getEmails());
        title.setText(pattern.getTitle());
        text.setText(pattern.getText());
    }

    private void takeEmailFromString(String stringWithEmail) {
        String email = stringWithEmail.trim().split(",")[1].trim();
        String receiverEmails = receiver.getText();
        if (receiverEmails == null || receiverEmails.isEmpty()) {
            receiver.setText(email);
        } else {
            receiver.setText(receiverEmails + "," + email);
        }
    }


    private CrudController crudController() {
        return (CrudController) crud.getController();
    }

}
