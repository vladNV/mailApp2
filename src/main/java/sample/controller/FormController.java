package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sample.repository.*;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class FormController {
    @FXML private MenuItem organization;
    @FXML private MenuItem course;
    @FXML private MenuItem signature;
    @FXML private MenuItem bank;
    @FXML private MenuItem about;
    @FXML private MenuItem changing;
    @FXML private MenuItem sent;
    @FXML private MenuItem write;
    @FXML private MenuItem incoming;
    @FXML private MenuItem logout;

    @Autowired private OrganizationRepository repositoryOrg;
    @Autowired private BankRepository bankRepository;
    @Autowired private CourseRepository courseRepository;
    @Autowired private SignatureRepository signatureRepository;

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

        incoming.setOnAction(event -> {});
        sent.setOnAction(event -> {});
        write.setOnAction(event -> JavaFxController.changing(writing.getView()));

        changing.setOnAction(event -> {});
        about.setOnAction(event -> JavaFxController.alert("Mail program 1.0v", "Автор: ", "Программа для рассылки писем"));
        logout.setOnAction(event -> JavaFxController.changing(login.getView()));
    }



    private CrudController crudController() {
        return (CrudController) crud.getController();
    }
}
