package psp.c_calc2.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ClientControl implements ClientStatusListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnConnect;
    @FXML
    private Button btnDisconnect;

    @FXML
    private TextArea txtAreaClientLog;

    @FXML
    private Button btnExit;

    @FXML
    private TextField txtFieldPort;

    @FXML
    private TextField txtFieldIP;

    @FXML
    void initialize() {
        assert btnConnect != null : "fx:id=\"btnConnect\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert txtFieldPort != null : "fx:id=\"txtFieldPort\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert txtFieldIP != null : "fx:id=\"txtFieldIP\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert btnDisconnect != null : "fx:id=\"btnDisconnect\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert txtAreaClientLog != null : "fx:id=\"txtAreaClientLog\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'ClientPane.fxml'.";


    }

    @FXML
    void btnConnectAction(ActionEvent event) {

    }

    @FXML
    void btnDisconnectAction(ActionEvent event) {

    }

    @FXML
    void btnExitAction(ActionEvent event) {

    }

    @Override
    public void onLogOutput(String string) {
        txtAreaClientLog.appendText(string + "\n");
    }

}
