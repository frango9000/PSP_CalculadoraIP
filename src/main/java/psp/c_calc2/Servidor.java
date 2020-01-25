package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import psp.c_calc2.ui.ServerStatusListener;
import psp.z_misc.Asserts;

public class Servidor {

    private static Servidor instance;

    private Servidor() {
    }

    public static Servidor getInstance() {
        if (instance == null) {
            synchronized (Servidor.class) {
                if (instance == null) {
                    instance = new Servidor();
                }
            }
        }
        return instance;
    }

    //    public final String HOSTNAME = "192.168.0.1";
    public String hostname = "192.168.1.100";
    public int port = 5555;

    public static boolean isValidPort(String string) {
        int port = -1;
        return Asserts.isInteger(string) && (port = Integer.parseInt(string)) < 65536 && port > 1024;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    private ServerThread serverThread;

    public ServerThread getServerThread() {
        return serverThread;
    }

    private List<ServerStatusListener> listeners = new ArrayList<>();

    public List<ServerStatusListener> getServerStatusListeners() {
        return listeners;
    }

    private void notifyLogChangeToListeners(String msg) {
        listeners.forEach(serverStatusListener -> serverStatusListener.onLogOutput(msg));
    }

    private void notifyServerStatusToListeners() {
        listeners.forEach(ServerStatusListener::onServerStatusChanged);
    }

    private void notifyActiveClientsToListeners(int activeClients) {
        listeners.forEach(serverStatusListener -> serverStatusListener.onActiveClientsChange(activeClients));
    }

    private void log(String msg) {
        System.out.println(msg);
        notifyLogChangeToListeners(msg);
    }

    public void startServer() {
        stopServer();
        serverThread = new ServerThread();
        serverThread.start();
    }

    public void stopServer() {
        if (serverThread != null) {
            try {
                serverThread.getServerSocket().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverThread.pool.shutdownNow();
            serverThread.interrupt();
            serverThread = null;
        }
    }

    public boolean isServerAlive() {
        return getServerThread() != null && getServerThread().getServerSocket() != null && getServerThread().getServerSocket().isBound();
    }

    private class ServerThread extends Thread {

        private int cores = Runtime.getRuntime().availableProcessors();
        private ExecutorService pool = Executors.newFixedThreadPool(cores);
        private int activeClients = 0;

        private ServerSocket serverSocket = null;

        public ServerSocket getServerSocket() {
            return serverSocket;
        }

        private InetSocketAddress inetSocketAddress;

        @Override
        public void run() {
            try {
                log("Creando socket servidor calculadora");

                serverSocket = new ServerSocket();

                log("Realizando el bind");

                if (hostname.length() > 6 && InetAddress.getByName(hostname).isReachable(100)) {
                    inetSocketAddress = new InetSocketAddress(hostname, port);
                } else {
                    inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
                }

                serverSocket.bind(inetSocketAddress);
                notifyServerStatusToListeners();

                log("Aceptando conexiones: " + serverSocket.getLocalSocketAddress().toString());

                while (true) {
                    final Socket clientSocket = serverSocket.accept();
                    pool.execute(new WorkerThread(clientSocket));
                }
            } catch (BindException be) {
                log("Unbindable Socket Address" + inetSocketAddress);
            } catch (IllegalArgumentException iae) {
                log("Puerto Invalido");
            } catch (UnknownHostException uhe) {
                log("Hostname invalido");
            } catch (IOException ioe) {
//                ioe.printStackTrace();
                log("Deteniendo Servidor");
            } finally {
                log("Cerrando el socket servidor");
                try {
                    if (serverSocket != null) {
                        serverSocket.close();
                        log("Socket Cerrado");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                notifyServerStatusToListeners();
                log("Server Thread Terminado");
            }
        }

        private class WorkerThread implements Runnable {

            private final Socket clientSocket;

            public WorkerThread(Socket clientSocket) {
                this.clientSocket = clientSocket;
            }

            @Override
            public void run() {
                notifyActiveClientsToListeners(++activeClients);

                String id = clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": ";
                log(id + "Conexion iniciada");
                try {
                    while (clientSocket.isConnected()) {
                        DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                        log(id + "Enviando señal preparado");
                        out.writeBoolean(true);                                         //O1

                        String expresion = in.readUTF();                                   //I2
                        log(id + "Expresion recibida: " + expresion);

                        try {
                            double resp = Calculadora.evaluarExpresion(expresion);
                            log(id + "Enviando señal OK");
                            out.writeBoolean(true);                                     //O3a1
                            log(id + "Enviando Respuesta : " + resp);
                            out.writeDouble(resp);                                         //O3a2
                        } catch (Exception e) {
                            log(id + "No se pueden procesar los datos recibidos");
                            out.writeBoolean(false);                                    //O3b
                        }
                        log(id + "Esperando orden de continuar o cerrar.");
                        boolean repeat = in.readBoolean();                                 //I4
                        if (!repeat)
                            break;
                    }
                } catch (IOException e) {
                    log(id + "Cliente deconectado");
                    //e.printStackTrace();
                } finally {
                    log(id + "Cerrando el socket cliente");
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    notifyActiveClientsToListeners(activeClients--);
                }
            }
        }
    }
}


