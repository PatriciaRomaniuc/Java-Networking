package controllers;

import domain.Excursie;
import domain.Rezervare;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import observer.applicationObserver;
import persistance.PersistanceException;
import service.IService;


import javax.swing.text.TableView;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class AppController implements applicationObserver {
    IService service;

    @FXML
    javafx.scene.control.TableView excursiiTable;

    @FXML
    TableColumn obiectivColumn;
    @FXML
    TableColumn idColumn;
    @FXML
    TableColumn idColumn1;
    @FXML
    TableColumn transportColumn;
    @FXML
    TableColumn oraColumn;
    @FXML
    TableColumn pretColumn;
    @FXML
    TableColumn locuriColumn;
    @FXML
    javafx.scene.control.TableView excursiiFiltratTable;
    @FXML
    TableColumn obiectivColumn1;
    @FXML
    TableColumn transportColumn1;
    @FXML
    TableColumn oraColumn1;
    @FXML
    TableColumn pretColumn1;
    @FXML
    TableColumn locuriColumn1;
    @FXML
    TextField obiectivField;
    @FXML
    TextField inainteDeField;
    @FXML
    TextField dupaOraField;
    @FXML
    Button cautaButton;
    @FXML
    private Button addRezervare;
    @FXML
    private TextField fieldNumeClient;
    @FXML
    private TextField fieldTelefonClient;
    @FXML
    private TextField fieldLocuri;
    @FXML
    private TextField fieldIdExcursie;
private LoginController loginController;
    Stage mainStage;

    public void set(Stage mainStage, IService service, LoginController loginController) throws PersistanceException {
        this.service=service;
        this.mainStage = mainStage;
        this.loginController = loginController;
        init();
    }

    public void init() throws PersistanceException {
        List<Excursie> list = service.findAllExcursii();
        ObservableList<Excursie> excursieObservableList = FXCollections.observableArrayList(list);

        excursiiTable.setItems(excursieObservableList);
        excursiiTable.setRowFactory(x -> new TableRow<Excursie>() {
            @Override
            protected void updateItem(Excursie item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null)
                    setStyle("");
                else if (item.getNrLocuriDisponibile() == 0)
                    setStyle("-fx-background-color: red;");
                else
                    setStyle("");
            }
        });
    }

    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<Excursie,Integer>("id"));
        obiectivColumn.setCellValueFactory(new PropertyValueFactory<Excursie, String>("numeObiectiv"));
        transportColumn.setCellValueFactory(new PropertyValueFactory<Excursie, String>("numeFirmaTransport"));
        oraColumn.setCellValueFactory(new PropertyValueFactory<Excursie, LocalTime>("oraPlecarii"));
        pretColumn.setCellValueFactory(new PropertyValueFactory<Excursie, Float>("pret"));
        locuriColumn.setCellValueFactory(new PropertyValueFactory<Excursie, Integer>("nrLocuriDisponibile"));
        idColumn1.setCellValueFactory(new PropertyValueFactory<Excursie,Integer>("id"));
        obiectivColumn1.setCellValueFactory(new PropertyValueFactory<Excursie, String>("numeObiectiv"));
        transportColumn1.setCellValueFactory(new PropertyValueFactory<Excursie, String>("numeFirmaTransport"));
        oraColumn1.setCellValueFactory(new PropertyValueFactory<Excursie, LocalTime>("oraPlecarii"));
        pretColumn1.setCellValueFactory(new PropertyValueFactory<Excursie, Float>("pret"));
        locuriColumn1.setCellValueFactory(new PropertyValueFactory<Excursie, Integer>("nrLocuriDisponibile"));
        tableSelection();
    }

    @FXML
    public void cautaExcursii() {
        String obiectiv = obiectivField.getText();
        try {
            String dupa = dupaOraField.getText();
            String inainte = inainteDeField.getText();
            excursiiFiltratTable.getItems().clear();
            List<Excursie> list=service.getExcursiiTableFiltru(obiectiv, dupa, inainte);
            ObservableList<Excursie> observableList= FXCollections.observableList(list);
            excursiiFiltratTable.setItems(observableList);
            excursiiFiltratTable.refresh();

            excursiiFiltratTable.setRowFactory(x -> new TableRow<Excursie>() {
                @Override
                protected void updateItem(Excursie item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null)
                        setStyle("");
                    else if (item.getNrLocuriDisponibile() == 0)
                        setStyle("-fx-background-color: red;");
                    else
                        setStyle("");
                }
            });
        } catch (DateTimeParseException | PersistanceException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ora gresita");
            alert.show();
        }
    }

    public void tableSelection() {
        excursiiTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Excursie>() {
            @Override
            public void changed(ObservableValue<? extends Excursie> observableValue, Excursie excursie, Excursie t1) {
                if (excursiiTable.getSelectionModel().getSelectedItem() != null && ((Excursie) excursiiTable.getSelectionModel().getSelectedItem()).getNrLocuriDisponibile() != 0) {
                    Excursie excursie1 = ((Excursie) excursiiTable.getSelectionModel().getSelectedItem());
                    fieldIdExcursie.setText(excursie1.getId().toString());
                } else {
                    fieldIdExcursie.setText("");
                }
            }
        });
    }

    @FXML
    public void adaugaRezervare() {
        try {
            List<Excursie> list=service.findAllExcursii();
            int id=list.size()+1;
            service.addRezervare(new Rezervare(id,fieldNumeClient.getText(), fieldTelefonClient.getText(), Integer.parseInt(fieldLocuri.getText()), Integer.parseInt(fieldIdExcursie.getText())));
            Excursie excursie = service.findOneExcursie(Integer.parseInt(fieldIdExcursie.getText()));
            excursie.setNrLocuriDisponibile(excursie.getNrLocuriDisponibile() - Integer.parseInt(fieldLocuri.getText()));
            service.updateExcursie(excursie);
            init();
            fieldIdExcursie.clear();
            fieldNumeClient.clear();
            fieldTelefonClient.clear();
            fieldLocuri.clear();

        } catch (NumberFormatException | PersistanceException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Numarul de locuri trebuie sa fie int");
            alert.show();
        }
    }

    @FXML
    public void setTable() throws PersistanceException {
        List<Excursie> list=service.findAllExcursii();
        ObservableList<Excursie> excursies= FXCollections.observableArrayList(list);
        excursiiTable.setItems(excursies);
        excursiiTable.refresh();
        excursiiTable.getSelectionModel().select(null);

    }

    @FXML
    public void logout() throws IOException, PersistanceException {
        mainStage.close();
        service.logout(loginController.getUser());
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/loginView.fxml"));
        AnchorPane root = loader.load();
        LoginController ctrl = loader.getController();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        ctrl.setService(stage, mainStage,service,this);
        stage.setScene(scene);
        stage.setTitle("Login");
        stage.show();
    }


    @Override
    public void updateTrips(List<Excursie> excursies) throws PersistanceException {
        excursiiTable.getItems().clear();
        ObservableList<Excursie> all = FXCollections.observableArrayList(excursies);
        excursiiTable.setItems(all);
        excursiiTable.refresh();
        excursiiTable.getSelectionModel().select(null);

    }

}