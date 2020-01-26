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

    ClientControl clientControl;

    public void setClientControl(ClientControl clientControl) {
        this.clientControl = clientControl;
    }

    private void appendToInputText(char append) {
        txtFieldInput.setText(txtFieldInput.getText() + append);
    }

    @FXML
    void btn0Action(ActionEvent event) {
        appendToInputText('0');
    }

    @FXML
    void btn1Action(ActionEvent event) {
        appendToInputText('1');

    }

    @FXML
    void btn2Action(ActionEvent event) {
        appendToInputText('2');
    }

    @FXML
    void btn3Action(ActionEvent event) {
        appendToInputText('3');
    }

    @FXML
    void btn4Action(ActionEvent event) {
        appendToInputText('4');
    }

    @FXML
    void btn5Action(ActionEvent event) {
        appendToInputText('5');
    }

    @FXML
    void btn6Action(ActionEvent event) {
        appendToInputText('6');
    }

    @FXML
    void btn7Action(ActionEvent event) {
        appendToInputText('7');
    }

    @FXML
    void btn8Action(ActionEvent event) {
        appendToInputText('8');
    }

    @FXML
    void btn9Action(ActionEvent event) {
        appendToInputText('9');
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
    void btnComaAction(ActionEvent event) {
        appendToInputText('.');
    }

    @FXML
    void btnPlusAction(ActionEvent event) {
        appendToInputText('+');
    }

    @FXML
    void btnMinusAction(ActionEvent event) {
        appendToInputText('-');
    }

    @FXML
    void btnMultiplyAction(ActionEvent event) {
        appendToInputText('*');
    }

    @FXML
    void btnDivideAction(ActionEvent event) {
        appendToInputText('/');
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
