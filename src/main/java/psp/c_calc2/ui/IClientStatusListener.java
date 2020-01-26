package psp.c_calc2.ui;

public interface IClientStatusListener extends IStatusListener {

    void onResultReceived(String expression, boolean valid, double result);
}
