package psp.c_calc2.ui;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import psp.c_calc2.Cliente;
import psp.c_calc2.Servidor;
import psp.z_misc.fx.FXMLStage;

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
        Cliente.getInstance().getClientStatusListeners().add(this);
        txtFieldIP.setText(Servidor.DEFAULT_HOSTNAME);
        txtFieldPort.setText(Servidor.DEFAULT_PORT + "");

    }

    @FXML
    void btnConnectAction(ActionEvent event) {
        if (Servidor.isValidPort(txtFieldPort.getText())) {
            Cliente.getInstance().setServerHostname(txtFieldIP.getText());
            FutureTask<Boolean> connection = Cliente.getInstance().connect();
            Thread thread = new Thread(connection);
            thread.start();
            Platform.runLater(() -> {
                boolean connectionAcquired = false;
                try {
                    onLogOutput("Conectando...");
                    connectionAcquired = connection.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                if (connectionAcquired) {
                    onLogOutput("Conectado!");
                    FXMLStage calcWindow = new FXMLStage("/fxml/CalculadoraPane.fxml", "Calculadora Remota");
                    calcWindow.show();
                } else
                    onLogOutput("Conexion Rechazada");
            });
        } else
            onLogOutput("Puerto invalido");
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
