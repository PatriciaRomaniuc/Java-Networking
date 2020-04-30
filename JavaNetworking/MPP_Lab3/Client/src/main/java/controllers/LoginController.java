package controllers;

import domain.Excursie;
import domain.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import persistance.PersistanceException;
import service.IService;

import java.io.IOException;
import java.util.ArrayList;

public class LoginController {
    @FXML
    public Button signIn;
    IService service;
    private String user;

    private Stage loginStage;
    @FXML
    TextField usernameField;
    @FXML
    public AnchorPane pane;
    @FXML
    PasswordField passwordField;

    private Alert eroare;

    private AppController appController;
    Stage mainStage;

    public void setService(Stage loginStage, Stage mainStage, IService service, AppController appController) {
        this.loginStage = loginStage;
        this.service = service;
        this.mainStage=mainStage;
        this.appController=appController;
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        pane = new AnchorPane();
        usernameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                pane.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
    }

    @FXML
    public void signInAction(javafx.event.ActionEvent actionEvent) throws IOException {
        eroare = new Alert(Alert.AlertType.ERROR);
        eroare.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            String username = usernameField.getText();
            String passwd = passwordField.getText();
            try {
                service.login(new User(username, passwd), appController);
                System.out.println("login succes");
                user = usernameField.getText();
                loginStage.close();
                appController.set( mainStage,service,this);
                appController.setTable();
                mainStage.setTitle("Meniu");
                mainStage.show();


            } catch (PersistanceException ex) {
                ex.printStackTrace();
                eroare.setContentText("Date gresite! Username sau parola incorecte!");
                eroare.showAndWait();
            }
        }
    }

    public String getUser() {
        return user;

    }

    @FXML
    public void clear() {
        usernameField.clear();
        passwordField.clear();
        pane.requestFocus();
    }
}



