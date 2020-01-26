package psp.c_calc2.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import psp.c_calc2.Cliente;
import psp.c_calc2.Servidor;

public class ClientControl extends Pane implements IClientStatusListener {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabConnect;

    @FXML
    private TextField txtFieldPort;

    @FXML
    private TextField txtFieldIP;

    @FXML
    private Button btnConnect;

    @FXML
    private Circle circleClientStatus;

    @FXML
    private Button btnDisconnect;

    @FXML
    private TextArea txtAreaClientLog;

    @FXML
    private Button btnExit;

    @FXML
    private Tab tabClient;

    private CalculadoraControl calculadoraControl;

    @FXML
    void initialize() {
        loadClientPane("/fxml/CalculadoraPane.fxml");
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert tabConnect != null : "fx:id=\"tabConnect\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert txtFieldPort != null : "fx:id=\"txtFieldPort\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert txtFieldIP != null : "fx:id=\"txtFieldIP\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert btnConnect != null : "fx:id=\"btnConnect\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert circleClientStatus != null : "fx:id=\"circleClientStatus\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert btnDisconnect != null : "fx:id=\"btnDisconnect\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert txtAreaClientLog != null : "fx:id=\"txtAreaClientLog\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'ClientPane.fxml'.";
        assert tabClient != null : "fx:id=\"tabClient\" was not injected: check your FXML file 'ClientPane.fxml'.";

        Cliente.getInstance().getClientStatusListeners().add(this);
        txtFieldIP.setText(Servidor.DEFAULT_HOSTNAME);
        txtFieldPort.setText(Servidor.DEFAULT_PORT + "");
    }

    private void loadClientPane(String name) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(name));
        try {
            tabClient.setContent(loader.load());
            calculadoraControl = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
            tabClient.setDisable(true);
        }
    }

    @FXML
    void btnConnectAction(ActionEvent event) {
        if (Servidor.isValidPort(txtFieldPort.getText())) {
            Cliente.getInstance().setServerHostname(txtFieldIP.getText());
            Cliente.getInstance().setServerPort(Integer.parseInt(txtFieldPort.getText()));
            Cliente.getInstance().connect();

        } else
            onLogOutput("Puerto invalido");
    }

    @FXML
    void btnDisconnectAction(ActionEvent event) {
        Cliente.getInstance().disconnect();

    }

    @FXML
    void btnExitAction(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void onStatusChanged() {
        if (Cliente.getInstance().isConnected()) {
            circleClientStatus.setFill(Color.LIMEGREEN);
            btnConnect.setDisable(true);
        } else {
            circleClientStatus.setFill(Color.ORANGERED);
            btnConnect.setDisable(false);
        }
    }

    @Override
    public void onLogOutput(String string) {
        txtAreaClientLog.appendText(string + "\n");
    }


    @Override
    public void onResultReceived(String expression, boolean valid, double result) {
        Platform.runLater(() -> calculadoraControl.txtAreaClienteLog.appendText(expression + "\n" + (valid ? result : "ERROR") + "\n"));

    }
}
