package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import psp.c_calc2.ui.IClientStatusListener;

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

    private volatile ClienteThread clienteThread;
    private List<IClientStatusListener> listeners = new ArrayList<>();


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

    public List<IClientStatusListener> getClientStatusListeners() {
        return listeners;
    }

    public ClienteThread getClienteThread() {
        return clienteThread;
    }

    private void notifyClientStatusToListeners() {
        listeners.forEach(IClientStatusListener::onStatusChanged);
    }

    private void notifyLogChangeToListeners(String msg) {
        listeners.forEach(serverStatusListener -> serverStatusListener.onLogOutput(msg));
    }

    private void notifyResultReceivedToListeners(String expression, boolean valid, double result) {
        listeners.forEach(listener -> listener.onResultReceived(expression, valid, result));
    }

    private void log(String msg) {
        System.out.println(msg);
        notifyLogChangeToListeners(msg);
    }

//    public FutureTask<Boolean> connect() {
//        return new FutureTask<>(() -> {
//            clienteThread = new ClienteThread();
//            clienteThread.start();
//            int timeoutMs = 10000;
//            while (!isConnected() && timeoutMs > 0) {
//                timeoutMs -= 100;
//                Thread.sleep(100);
//            }
//            return isConnected();
//        });
//    }

    public void connect() {
        clienteThread = new ClienteThread();
        clienteThread.start();
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                clienteThread.clienteSocket.setSoTimeout(0);
                clienteThread.clienteSocket.close();
                clienteThread.clienteSocket = null;
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (clienteThread != null)
            clienteThread.interrupt();
        clienteThread = null;

    }

    public boolean setExpression(String expression) {
        if (isConnected()) {
            try {
                clienteThread.blockingQueue.put(expression);
                return true;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    public boolean isConnected() {
        return clienteThread != null && clienteThread.clienteSocket != null && clienteThread.clienteSocket.isConnected();
    }


    private class ClienteThread extends Thread {

        private Socket clienteSocket = null;


        private Object lock = new Object();

        private BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);


        @Override
        public void run() {

            DataInputStream in = null;
            DataOutputStream out = null;
            try {
                clienteSocket = new Socket();

                InetSocketAddress addr = new InetSocketAddress(serverHostname, serverPort);
                log("Conectando: " + addr.getAddress());
                clienteSocket.connect(addr);
                log("Conectado: " + addr.getAddress());
                notifyClientStatusToListeners();
                in  = new DataInputStream(clienteSocket.getInputStream());
                out = new DataOutputStream(clienteSocket.getOutputStream());

                while (true) {
                    boolean preparado = in.readBoolean();                               //I1
                    if (preparado) {
                        log("Cliente preparado.");
                        String expression = blockingQueue.take();
                        log("Esperando peticion.");
                        out.writeUTF(expression);                                       //O2
                        log("Peticion enviada, esperando respuesta.");
                        boolean valid = in.readBoolean();                               //I3a1
                        log("Peticion valida ? " + valid);
                        double result = 0;
                        if (valid) {
                            result = in.readDouble();                                   //I3a2
                            log("Resultado: " + result);
                        }
                        notifyResultReceivedToListeners(expression, valid, result);
                        out.writeBoolean(true);                                      //O4
                    }
                }

            } catch (ConnectException ce) {
                log("Host no responde");
            } catch (IOException e) {
                e.printStackTrace();
                log("Error");
            } catch (InterruptedException ie) {
                log("Cliente Interrumpido");
            } finally {
                try {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                    if (clienteSocket != null)
                        clienteSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyClientStatusToListeners();
                log("Cliente terminado");
            }
        }
    }
}