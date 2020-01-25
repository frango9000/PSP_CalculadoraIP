package psp.c_calc2.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ConnectControl {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnect;

    @FXML
    private TextField txtFieldPort;

    @FXML
    private TextField txtFieldIP;

    @FXML
    void btnConnectAction(ActionEvent event) {

    }

    @FXML
    void txtFieldIPAction(ActionEvent event) {

    }

    @FXML
    void txtFieldPortAction(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert btnConnect != null : "fx:id=\"btnConnect\" was not injected: check your FXML file 'ConnectPane.fxml'.";
        assert txtFieldPort != null : "fx:id=\"txtFieldPort\" was not injected: check your FXML file 'ConnectPane.fxml'.";
        assert txtFieldIP != null : "fx:id=\"txtFieldIP\" was not injected: check your FXML file 'ConnectPane.fxml'.";

    }
}
