package psp.c_calc2.ui;

public interface ServerStatusListener {

     void onServerStatusChanged();

     void onActiveClientsChange(int activeClients);

     void onLogOutput(String string);

}
