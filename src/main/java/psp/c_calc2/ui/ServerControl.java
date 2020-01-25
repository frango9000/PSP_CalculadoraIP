package psp.c_calc2.ui;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import psp.c_calc2.Servidor;
import psp.z_misc.Asserts;

public class ServerControl extends VBox implements ServerStatusListener {

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
    private Circle circleBroadcastStatus;

    @FXML
    private Button btnServerStart;

    @FXML
    private Button btnServerStop;

    @FXML
    private Circle circleServerStatus;

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
    void initialize() {
        assert txtFieldHostname != null : "fx:id=\"txtFieldHostname\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert txtFieldPort != null : "fx:id=\"txtFieldPort\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnBroadcastStart != null : "fx:id=\"btnBroadcastStart\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnBroadcastStop != null : "fx:id=\"btnBroadcastStop\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert circleBroadcastStatus != null : "fx:id=\"circleBroadcastStatus\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnServerStart != null : "fx:id=\"btnServerStart\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnServerStop != null : "fx:id=\"btnServerStop\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert circleServerStatus != null : "fx:id=\"circleServerStatus\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert txtAreaServerLog != null : "fx:id=\"txtAreaServerLog\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        assert btnExit != null : "fx:id=\"btnExit\" was not injected: check your FXML file 'ServidorPane.fxml'.";
        Servidor.getInstance().getServerStatusListeners().add(this);
        try {
            txtFieldHostname.setText(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            onLogOutput("Cant find default localhost IP");
        }
        txtFieldPort.setText("5555");

    }

    @FXML
    void btnServerStartAction(ActionEvent event) {
        if (Asserts.isInteger(txtFieldPort.getText())) {
            int port = Integer.parseInt(txtFieldPort.getText());
            if (port > 1024 && port < 65536) {
                btnServerStart.setDisable(true);
                Servidor.getInstance().setHostname(txtFieldHostname.getText());
                Servidor.getInstance().setPort(port);
                Servidor.getInstance().startServer();
            } else
                onLogOutput("Puerto fuera de rango");
        } else
            onLogOutput("Puerto invalido");
    }

    @FXML
    void btnServerStopAction(ActionEvent event) {
        Servidor.getInstance().stopServer();
    }


    @Override
    public synchronized void onServerStatusChanged() {
        Platform.runLater(() -> {
            if (Servidor.getInstance().isServerAlive()) {
                circleServerStatus.setFill(Color.LIMEGREEN);
                btnServerStart.setDisable(true);
            } else {
                circleServerStatus.setFill(Color.ORANGERED);
                btnServerStart.setDisable(false);
                btnServerStart.setText("Start");
            }
        });
    }

    @Override
    public void onActiveClientsChange(int activeClients) {
        btnServerStart.setText(Integer.toString(activeClients));
    }

    @Override
    public void onLogOutput(String string) {
        txtAreaServerLog.appendText(string + "\n");
    }
}
