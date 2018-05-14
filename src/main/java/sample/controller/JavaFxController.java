package sample.controller;

import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.Main;

import java.util.HashMap;
import java.util.Map;

public class JavaFxController {
    public static Map<String, String> fieldsNameInRussian = new HashMap<>();
    static {
        fieldsNameInRussian.put("name", "Название организации");
        fieldsNameInRussian.put("address", "Адрес");
        fieldsNameInRussian.put("checking", "Расчетный счет");
        fieldsNameInRussian.put("email", "Почта");
        fieldsNameInRussian.put("KPP", "КПП");
        fieldsNameInRussian.put("owner", "Владелец");
        fieldsNameInRussian.put("phone", "Телефон");
        fieldsNameInRussian.put("name", "Название банка");
        fieldsNameInRussian.put("BIC", "BIC");
        fieldsNameInRussian.put("account", "Аккаунт");
        fieldsNameInRussian.put("name", "Название курса");
        fieldsNameInRussian.put("classNumber", "Номер класса");
        fieldsNameInRussian.put("price", "Цена");
        fieldsNameInRussian.put("during", "Продолжительность");
        fieldsNameInRussian.put("fullName", "ФИО");
        fieldsNameInRussian.put("eKey", "E-Key");
    }
    
    
    public static void alert(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void alert(String title, String text, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }

    public static void changing(Parent parent, double width, double height) {
        Stage stage = (Stage) Main.scenes.get("root");
        stage.getScene().setRoot(parent);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.centerOnScreen();
    }

    public static void changing(Parent parent) {
        Stage stage = (Stage) Main.scenes.get("root");
        stage.getScene().setRoot(parent);
        stage.centerOnScreen();
    }
}
