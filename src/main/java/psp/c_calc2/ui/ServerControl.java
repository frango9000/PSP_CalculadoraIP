package psp.c_calc2.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ServerControl extends VBox {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtFieldHostname;

    @FXML
    private TextField txtFieldPort;

    @FXML
    private Button btnBroadcastStart;

    @FXML
    private Button btnBroadcastStop;

    @FXML
    private Button btnServerStart;

    @FXML
    private Button btnServerStop;

    @FXML
    private TextArea txtAreaServerLog;

    @FXML
    private Button btnExit;

    @FXML
    void btnBroadcastStartAction(ActionEvent event) {

    }

    @FXML
    void btnBroadcastStopAction(ActionEvent event) {

    }

    @FXML
    void btnExitAction(ActionEvent event) {

    }

    @FXML
    void btnServerStartAction(ActionEvent event) {

    }

    @FXML
    void btnServerStopAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert txtFieldHostname != null : "fx:id=\"txtFieldHostname\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert txtFieldPort != null : "fx:id=\"txtFieldPort\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnBroadcastStart != null : "fx:id=\"btnBroadcastStart\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnBroadcastStop != null : "fx:id=\"btnBroadcastStop\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnServerStart != null : "fx:id=\"btnServerStart\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnServerStop != null : "fx:id=\"btnServerStop\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert txtAreaServerLog != null : "fx:id=\"txtAreaServerLog\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'ServidorPane.fxml'.";

    }
}
