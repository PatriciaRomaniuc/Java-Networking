import controllers.*;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;
import objectProtocol.ServicesObjectProxy;
import persistance.PersistanceException;
import service.IService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Client extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        IService server = new ServicesObjectProxy("localhost",55555);
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/loginView.fxml"));
        AnchorPane root=loader.load();
        FXMLLoader loaderPagPrinc = new FXMLLoader();
        loaderPagPrinc.setLocation(getClass().getResource("/views/mainView.fxml"));
        AnchorPane rootPagPrinc=loaderPagPrinc.load();
        AppController controllerPagPrinc = loaderPagPrinc.getController();
        Scene scenePagPrinc = new Scene(rootPagPrinc);
        Stage stagePagPrinc = new Stage();
        stagePagPrinc.setTitle("Aplicatie Zboruri");
        stagePagPrinc.setScene(scenePagPrinc);

        LoginController ctrl=loader.getController();
        ctrl.setService(primaryStage,stagePagPrinc, server,controllerPagPrinc);
        Scene scene=new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");

        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                try {
                    server.logout(ctrl.getUser());
                } catch (PersistanceException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });

        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
