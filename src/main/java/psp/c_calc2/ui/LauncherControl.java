package psp.c_calc2.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import psp.z_misc.fx.FxApplication;

public class LauncherControl extends VBox {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnServidor;

    @FXML
    private Button btnCliente;

    @FXML
    void initialize() {
        assert btnServidor != null : "fx:id=\"btnServidor\" was not injected: check your FXML file 'LauncherPane.fxml'.";
        assert btnCliente != null : "fx:id=\"btnCliente\" was not injected: check your FXML file 'LauncherPane.fxml'.";

    }

    @FXML
    void btnClienteAction(ActionEvent event) {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/fxml/ConnectPane.fxml"));
            FxApplication.getMainStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnServidorAction(ActionEvent event) {
        try {
            Pane root = FXMLLoader.load(getClass().getResource("/fxml/ServidorPane.fxml"));
            FxApplication.getMainStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
