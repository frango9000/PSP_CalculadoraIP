package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import psp.c_calc2.ui.ClientStatusListener;

public class Cliente extends Thread {

    private static Cliente instance;

    private Cliente() {
    }

    public static Cliente getInstance() {
        if (instance == null) {
            synchronized (Servidor.class) {
                if (instance == null) {
                    instance = new Cliente();
                }
            }
        }
        return instance;
    }

    private String serverHostname = Servidor.DEFAULT_HOSTNAME;
    private int serverPort = Servidor.DEFAULT_PORT;

    public String getServerHostname() {
        return serverHostname;
    }

    public void setServerHostname(String serverHostname) {
        this.serverHostname = serverHostname;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    private List<ClientStatusListener> listeners = new ArrayList<>();

    public List<ClientStatusListener> getClientStatusListeners() {
        return listeners;
    }

    private void notifyLogChangeToListeners(String msg) {
        listeners.forEach(serverStatusListener -> serverStatusListener.onLogOutput(msg));
    }

    private ClienteThread clienteThread;

    public ClienteThread getClienteThread() {
        return clienteThread;
    }

    private void log(String msg) {
        System.out.println(msg);
        notifyLogChangeToListeners(msg);
    }

    public FutureTask<Boolean> connect() {
        return new FutureTask<>(() -> {
            clienteThread = new ClienteThread();
            clienteThread.start();
            int timeoutMs = 10000;
            while (!clienteThread.isConnected() && timeoutMs > 0) {
                timeoutMs -= 100;
                Thread.sleep(100);
            }
            return clienteThread.isConnected();
        });
    }

    private class ClienteThread extends Thread {

        private AtomicBoolean connected = new AtomicBoolean(false);

        public boolean isConnected() {
            return connected.get();
        }

        private Socket clienteSocket = null;

        @Override
        public void run() {
            try {
                log("Creando socket cliente");
                clienteSocket = new Socket();
                log("Estableciendo la conexion");

                InetSocketAddress addr = new InetSocketAddress(serverHostname, serverPort);
                clienteSocket.connect(addr);
                log("Conectado");
                connected.set(true);

                DataInputStream in = new DataInputStream(clienteSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clienteSocket.getOutputStream());

                while (true) {
                    boolean preparado = in.readBoolean();                               //I1
                    if (preparado) {
                        System.out.print("Introduce la expresion a calcular: ");
                        String expresion = getExpresion();
                        out.writeUTF(expresion);                                        //O2

                        boolean validacion = in.readBoolean();                          //I3a1
                        log("Status Recibido: " + validacion);
                        if (validacion) {
                            double resp = in.readDouble();                              //I3a2
                            log("Respuesta recibida : " + resp);
                        } else                                                          //I3b
                            log("Error en los datos enviados.");
                        out.writeBoolean(true);                                      //O4
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                log("Error");
            } finally {
                log("Cerrando el socket cliente");
                try {
                    clienteSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connected.set(false);
                log("Terminando hilo cliente");
            }
        }

        private String getExpresion() {
            return new Scanner(System.in).nextLine();
        }

    }
}