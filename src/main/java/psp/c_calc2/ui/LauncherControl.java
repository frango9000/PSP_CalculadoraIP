package psp.c_calc2.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
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

    }

    @FXML
    void btnClienteAction(ActionEvent event) {
        try {
            TabPane root = FXMLLoader.load(getClass().getResource("/fxml/ClientPane.fxml"));
            FxApplication.getMainStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnServidorAction(ActionEvent event) {
        try {
            VBox root = FXMLLoader.load(getClass().getResource("/fxml/ServerPane.fxml"));
            FxApplication.getMainStage().setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
