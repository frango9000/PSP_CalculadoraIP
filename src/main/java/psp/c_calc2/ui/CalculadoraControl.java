package psp.c_calc2.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import psp.c_calc2.Cliente;

public class CalculadoraControl extends VBox {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    protected TextArea txtAreaClienteLog;

    @FXML
    protected TextField txtFieldInput;

    @FXML
    private Button btn7;

    @FXML
    private Button btn8;

    @FXML
    private Button btn9;

    @FXML
    private Button btnDivide;

    @FXML
    private Button btn4;

    @FXML
    private Button btn5;

    @FXML
    private Button btn6;

    @FXML
    private Button btnMultiply;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btnMinus;

    @FXML
    private Button btnEqual;

    @FXML
    private Button btn0;

    @FXML
    private Button btnComa;

    @FXML
    private Button btnPlus;

    @FXML
    private Button btnCE;

    @FXML
    private Button btnC;

    @FXML
    private Button btnBackSpace;

    @FXML
    private Button btnExit;

    @FXML
    protected Circle circleClientStatus;


    @FXML
    void anyButtonAction(ActionEvent event) {
        txtFieldInput.setText(txtFieldInput.getText() + (((Button) event.getSource()).getText().charAt(0)));
    }

    @FXML
    void btnBackSpaceAction(ActionEvent event) {
        txtFieldInput.setText(txtFieldInput.getText().substring(0, txtFieldInput.getText().length() - 1));
    }
    @FXML
    void btnCAction(ActionEvent event) {
        txtFieldInput.setText("");
    }
    @FXML
    void btnCEAction(ActionEvent event) {
        txtFieldInput.setText("");
        txtAreaClienteLog.setText("");
    }

    @FXML
    void btnEqualAction(ActionEvent event) {
        if (Cliente.getInstance().isConnected())
            Cliente.getInstance().setExpression(txtFieldInput.getText());
    }

    @FXML
    void txtFieldInputAction(ActionEvent event) {
        if (Cliente.getInstance().isConnected())
            Cliente.getInstance().setExpression(txtFieldInput.getText());
    }

    @FXML
    void btnExitAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void initialize() {

    }
}
