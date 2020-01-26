package psp.c_calc2.ui;

public interface IServerStatusListener extends IStatusListener {

     void onActiveClientsChange(int activeClients);
}
