package sample.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import sample.Main;
import sample.domain.*;
import sample.repository.*;
import sample.session.ClientDetails;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

public class CrudController {
    private static final double INPUT_WIDTH = 275;
    private static final double INPUT_HEIGHT = 30;
    private static final double SIZE_BETWEEN_LABEL_AND_INPUT = 20;
    private static final double X_LAYOUT = 100;
    private static final double Y_LAYOUT = 30;
    private static final double DY_LAYOUT = 70;

    private static final String ORGANIZATION = "Организация";
    private static final String BANK = "Банк";
    private static final String SIGNATURE = "Подпись";
    private static final String COURSE = "Курс";

    @FXML private TableView<Object> data;
    @FXML private TableColumn<Object, Object> columnName;
    @FXML private AnchorPane dataPane;
    @FXML private Button commit;
    @FXML private Button add;
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
    @Autowired private ClientRepository clientRepository;

    @Autowired
    @Qualifier("login")
    private ControllerConfiguration.View login;

    @Autowired
    @Qualifier("writing")
    private ControllerConfiguration.View writing;

    private Map<String, TextField> fields = new HashMap<>();
    private Map<String, ComboBox> comboBoxMap = new HashMap<>();
    private String selectedEntity;


    @FXML
    public void initialize() {
        organization.setOnAction(event -> setTable(repositoryOrg));
        course.setOnAction(event -> setTable(courseRepository));
        signature.setOnAction(event -> setTable(signatureRepository));
        bank.setOnAction(event -> setTable(bankRepository));

        incoming.setOnAction(event -> {});
        sent.setOnAction(event -> {});
        write.setOnAction(event -> JavaFxController.changing(writing.getView()));

        changing.setOnAction(event -> changeEmail());
        about.setOnAction(event -> JavaFxController.alert("Mail program 1.0v", "Автор: ",
                "Программа для рассылки писем"));
        logout.setOnAction(event -> JavaFxController.changing(login.getView()));

        add.setOnAction(event -> addNewEntity());

        // crud controller

        commit.setVisible(false);
        commit.setOnAction(event -> {
            if (!fields.isEmpty()) {
                addToTable();
            }
            commit.setVisible(false);
        });

        data.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        columnName.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));

        data.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEntityDetails(newValue));
    }


    void setTable(JpaRepository<?, Long> repository) {
        ObservableList<Object> objects = FXCollections.observableArrayList();
        List<Object> names = repository.findAll().stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        objects.addAll(names);
        data.setItems(objects);
    }

    void changeEmail() {
        ClientDetails clientDetails = (ClientDetails) Main.scenes.get("client");
        Dialog<String> dialog = new TextInputDialog(clientDetails.getEmail());
        dialog.setTitle("Сменить почту");
        dialog.setContentText("Ваш текущий email: " + clientDetails.getEmail());
        dialog.setHeaderText("Введите новый email");
        Optional<String> response = dialog.showAndWait();
        response.ifPresent(s -> updateEmailInDatabase(s, clientDetails));
    }

    private void updateEmailInDatabase(String newEmail, ClientDetails clientDetails) {
        Client client = clientDetails.buildClient();
        client.setEmail(newEmail);
        clientRepository.save(client);
        clientDetails.setEmail(newEmail);
        JavaFxController.alert("Изменение почты",
                "Ваша почта была успешно изменена",
                "Ваша новая почта " + newEmail);
    }

    private void addNewEntity() {
        List<String> choices = new ArrayList<>();
        choices.add(ORGANIZATION);
        choices.add(BANK);
        choices.add(COURSE);
        choices.add(SIGNATURE);
        Dialog<String> dialog = new ChoiceDialog(choices.get(0), choices);
        dialog.setTitle("Выбор");
        dialog.setContentText("Что вы хотите добавить ?");
        dialog.setHeaderText("Добавление");
        Optional<String> response = dialog.showAndWait();
        response.ifPresent(this::chooseEntity);
    }

    private void chooseEntity (String entityName) {
        HashMap<String,String> map = new HashMap<>();
        Runnable runnable = this::addToTable;
        this.selectedEntity = entityName;
        switch (entityName) {
            case ORGANIZATION:
                map.put("name", "Название организации");
                map.put("address", "Адрес");
                map.put("checking", "Расчетный счет");
                map.put("email", "Почта");
                map.put("KPP", "КПП");
                map.put("owner", "Владелец");
                map.put("phone", "Телефон");
//                map.put("bankName", "Введите имя банка");
                createInputs(map, "Добавить организацию", runnable);
                createCombobox("bankName", FXCollections.observableArrayList(
                        bankRepository
                                .findAll()
                                .stream()
                                .filter(Objects::nonNull)
                                .map(Bank::getName)
                                .collect(Collectors.toList())));

                break;
            case BANK:
                map.put("name", "Название банка");
                map.put("BIC", "BIC");
                map.put("account", "Аккаунт");
                createInputs(map, "Добавить банк", runnable);
                break;
            case COURSE:
                map.put("name", "Название курса");
                map.put("classNumber", "Номер класса");
                map.put("price", "Цена");
                map.put("during", "Продолжительность");
                createInputs(map, "Добавить курс", runnable);
                break;
            case SIGNATURE:
                map.put("fullName", "ФИО");
                map.put("eKey", "E-Key");
                createInputs(map, "Добавить подпись", runnable);
                break;
            default:
                break;
        }
    }

    private void showEntityDetails(Object object) {
        if (object != null) {
            initInputsAndLabels(object, "Обновить", true, this::updateEntity);
        } else {
            defaultValues();
        }
    }

    private void updateEntity() {
        Object obj = data.getSelectionModel().getSelectedItem();
        Class<?> selectedModelClass = obj.getClass();
        if (selectedModelClass == Organization.class) {
            Organization org = (Organization) obj;
            fillOrganization(org);
            repositoryOrg.save(org);
        } else if (selectedModelClass == Bank.class) {
            Bank bank = (Bank) obj;
            fillBank(bank);
            bankRepository.save(bank);
        } else if (selectedModelClass == Signature.class) {
            Signature signature = (Signature) obj;
            fillSignature(signature);
            signatureRepository.save(signature);
        } else if (selectedModelClass == Course.class) {
            Course course = (Course) obj;
            fillCourse(course);
            courseRepository.save(course);
        } else {
            throw new RuntimeException("unknown entity");
        }
        JavaFxController.alert("Редактирование",
                "Данные отредактированы",
                "Данные успешно отредактированы!");
    }

    private void addToTable() {
        ClientDetails clientDetails = (ClientDetails) Main.scenes.get("client");
        switch (selectedEntity) {
            case ORGANIZATION:
                addOrganizationToBase(clientDetails);
                break;
            case BANK:
                addBankToBase(clientDetails);
                break;
            case COURSE:
                addCourseToBase(clientDetails);
                break;
            case SIGNATURE:
                addSignatureToBase(clientDetails);
                break;
            default:
                break;
        }
        JavaFxController.alert("Добавление " + selectedEntity,selectedEntity + " была успешно добавлена",
                "Данные добавлены и сохранены!");
    }

    private void addBankToBase(ClientDetails clientDetails) {
        Bank bank = new Bank();
        fillBank(bank);
        bank.setClient(clientDetails.buildClient());
        bankRepository.save(bank);
    }

    private void fillBank(Bank bank) {
        bank.setName(fields.get("name").getText());
        bank.setBIC(fields.get("BIC").getText());
        bank.setAccount(fields.get("account").getText());
    }

    private void addOrganizationToBase(ClientDetails clientDetails) {
        Organization org = new Organization();
        fillOrganization(org);
        org.setBank(bankRepository.findByName((String) comboBoxMap.get("bankName").getSelectionModel().getSelectedItem()));
        org.setClient(clientDetails.buildClient());
        repositoryOrg.save(org);
    }

    private void fillOrganization(Organization org) {
        org.setAddress(fields.get("address").getText());
        org.setChecking(fields.get("checking").getText());
        org.setEmail(fields.get("email").getText());
        org.setKPP(fields.get("KPP").getText());
        org.setName(fields.get("name").getText());
        org.setOwner(fields.get("owner").getText());
        org.setPhone(Long.valueOf(fields.get("phone").getText()));
    }

    private void addSignatureToBase(ClientDetails clientDetails) {
        Signature signature = new Signature();
        fillSignature(signature);
        signature.setClient(clientDetails.buildClient());
        signatureRepository.save(signature);
    }

    private void fillSignature(Signature signature) {
        signature.setEKey(fields.get("eKey").getText());
        signature.setFullName(fields.get("fullName").getText());
    }

    private void addCourseToBase(ClientDetails clientDetails) {
        Course course = new Course();
        fillCourse(course);
        course.setClient(clientDetails.buildClient());
        courseRepository.save(course);
    }

    private void fillCourse(Course course) {
        course.setClassNumber(fields.get("classNumber").getText());
        course.setName(fields.get("name").getText());
        // TO DO CONVERTER
        course.setPrice(Long.valueOf(fields.get("price").getText()));
        // TO DO CONVERTER
        course.setDuring(Long.valueOf(fields.get("during").getText()));
    }

    @SneakyThrows(Exception.class)
    private void initInputsAndLabels(Object object, String buttonName,
                                     boolean filled, Runnable runnable) {
        dataPane.getChildren().clear();
        double y = 30;

        Class entityMetaData = object.getClass();
        Field[] fields = entityMetaData.getDeclaredFields();
        for (Field field : fields) {
            if (!Modifier.isPrivate(field.getModifiers()) ||
                    (filled && field.getName().equals("id"))) {
                continue;
            }
            Label l = new Label(JavaFxController.fieldsNameInRussian.get(field.getName()));
            l.setLayoutX(X_LAYOUT);
            l.setLayoutY(y);

            TextField tf = new TextField();
            tf.setPrefWidth(INPUT_WIDTH);
            tf.setPrefHeight(INPUT_HEIGHT);
            tf.setLayoutX(X_LAYOUT);
            tf.setLayoutY(y + SIZE_BETWEEN_LABEL_AND_INPUT);

            this.fields.put(field.getName(), tf);

            if (filled) {
                field.setAccessible(true);
                field.get(object);
                tf.setText(field.get(object).toString());
            }

            dataPane.getChildren().addAll(l, tf);
            y += DY_LAYOUT;
        }
        commit.setVisible(true);
        commit.setText(buttonName);
        commit.setOnAction(event -> runnable.run());
        dataPane.getChildren().add(commit);
    }

    private void createInputs(Map<String, String> map, String buttonName,
                              Runnable runnable) {
        dataPane.getChildren().clear();
        double y = 30;
        for (Map.Entry<String, String> e : map.entrySet()) {
            Label l = new Label(e.getValue());
            l.setLayoutX(X_LAYOUT);
            l.setLayoutY(y);

            TextField tf = new TextField();
            tf.setPrefWidth(INPUT_WIDTH);
            tf.setPrefHeight(INPUT_HEIGHT);
            tf.setLayoutX(X_LAYOUT);
            tf.setLayoutY(y + SIZE_BETWEEN_LABEL_AND_INPUT);

            this.fields.put(e.getKey(), tf);

            dataPane.getChildren().addAll(l, tf);
            y += DY_LAYOUT;
        }
        commit.setOnAction(event -> runnable.run());
        commit.setText(buttonName);
        commit.setVisible(true);
        dataPane.getChildren().add(commit);
    }

    private void createCombobox(String name, ObservableList<String> values) {
        Label l = new Label("Введите имя банка");
        l.setLayoutX(X_LAYOUT);
        l.setLayoutY(520);

        ComboBox<String> comboBox = new ComboBox<>(values);
        comboBox.setPrefWidth(INPUT_WIDTH);
        comboBox.setPrefHeight(INPUT_HEIGHT);
        comboBox.setLayoutX(X_LAYOUT);
        comboBox.setLayoutY(520 + SIZE_BETWEEN_LABEL_AND_INPUT);
        comboBoxMap.put(name, comboBox);
        dataPane.getChildren().addAll(comboBox, l);
    }

    private void defaultValues() { }
}
