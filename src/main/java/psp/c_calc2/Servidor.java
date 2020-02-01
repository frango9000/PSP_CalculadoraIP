package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import psp.c_calc2.ui.IServerStatusListener;
import psp.z_misc.Asserts;

public class Servidor {

    public static final String DEFAULT_HOSTNAME = "192.168.1.100";
    //    private static String DEFAULT_HOSTNAME = "192.168.0.1";
    public static final int DEFAULT_PORT = 5555;

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

    private String hostname = DEFAULT_HOSTNAME;
    private int port = DEFAULT_PORT;

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

    private List<IServerStatusListener> listeners = new ArrayList<>();

    public List<IServerStatusListener> getServerStatusListeners() {
        return listeners;
    }

    private void notifyLogChangeToListeners(String msg) {
        listeners.forEach(IServerStatusListener -> IServerStatusListener.onLogOutput(msg));
    }

    private void notifyServerStatusToListeners() {
        listeners.forEach(IServerStatusListener::onStatusChanged);
    }

    private void notifyActiveClientsToListeners(int activeClients) {
        listeners.forEach(IServerStatusListener -> IServerStatusListener.onActiveClientsChange(activeClients));
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
            } finally {
                serverThread = null;
            }
        }
        notifyServerStatusToListeners();
    }

    public boolean isServerAlive() {
        return getServerThread() != null && getServerThread().getServerSocket() != null && getServerThread().getServerSocket().isBound();
    }

    private class ServerThread extends Thread {

        private boolean active = false;

        //        private int maxActiveClients = Runtime.getRuntime().availableProcessors();
        private int maxActiveClients = 4;
        private ExecutorService pool = Executors.newFixedThreadPool(maxActiveClients);
        private int activeClients = 0;
        private List<WorkerThread> boundClients = new ArrayList<>();

        private ServerSocket serverSocket = null;
        private InetSocketAddress inetSocketAddress;

        public ServerSocket getServerSocket() {
            return serverSocket;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public boolean isActive() {
            return active;
        }

        @Override
        public void run() {
            try {
                log("Creando socket servidor calculadora. Clientes Max: " + maxActiveClients);
                serverSocket = new ServerSocket();

                inetSocketAddress = (hostname.length() > 6 && InetAddress.getByName(hostname).isReachable(100))
                                    ? new InetSocketAddress(hostname, port)
                                    : new InetSocketAddress(InetAddress.getLocalHost(), port);

                log("Realizando el bind: " + inetSocketAddress.getAddress() + ":" + inetSocketAddress.getPort());
                serverSocket.bind(inetSocketAddress);
                setActive(true);
                notifyServerStatusToListeners();
                notifyActiveClientsToListeners(activeClients);

                log("Aceptando conexiones: " + serverSocket.getLocalSocketAddress().toString());
                while (isActive()) {
                    Socket clientSocket = serverSocket.accept();
                    clientSocket.setKeepAlive(true);
                    WorkerThread workerThread = new WorkerThread(clientSocket);
                    boundClients.add(workerThread);
                    pool.execute(workerThread);
                }
            } catch (BindException be) {
                log("Unbindable Socket Address: " + inetSocketAddress);
            } catch (IllegalArgumentException iae) {
                log("Puerto Invalido");
            } catch (SocketException se) {
                log("Interrumpiendo Bind");
            } catch (UnknownHostException uhe) {
                log("Hostname invalido");
            } catch (IOException ioe) {
                log("Deteniendo Servidor");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                killServerThread();
            }
        }

        public void killServerThread() {
            log("Cerrando el socket servidor");
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                    log("Socket Cerrado");
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                setActive(false);
                notifyServerStatusToListeners();
                log("Server Thread Terminado");
            }
            boundClients.forEach(workerThread -> workerThread.setActive(false));
            pool.shutdown(); // Disable new tasks from being submitted
            try {
                // Wait a while for existing tasks to terminate
                if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                    pool.shutdownNow(); // Cancel currently executing tasks
                    // Wait a while for tasks to respond to being cancelled
                    if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                        System.err.println("Pool did not terminate");
                }
            } catch (InterruptedException ie) {
                // (Re-)Cancel if current thread also interrupted
                pool.shutdownNow();
                // Preserve interrupt status
                Thread.currentThread().interrupt();
            } finally {

            }
        }

        private class WorkerThread implements Runnable {

            private final Socket clientSocket;

            private boolean active = false;

            public WorkerThread(Socket clientSocket) {
                this.clientSocket = clientSocket;
            }

            public boolean isActive() {
                return active;
            }

            public void setActive(boolean active) {
                this.active = active;
            }

            @Override
            public void run() {

                setActive(true);
                notifyActiveClientsToListeners(++activeClients);

                String id = clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": ";
                log(id + "Conexion iniciada");

                try {
                    DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                    while (clientSocket.isConnected() && serverSocket.isBound() && isActive()) {

                        log(id + "Enviando señal preparado");
                        out.writeBoolean(true);                                         //O1

                        String expresion = in.readUTF();                                   //I2 //TODO: Heartbeat
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
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    log(id + "Cerrando el socket cliente");
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        notifyActiveClientsToListeners(--activeClients);
                        boundClients.remove(this);
                    }
                }
            }
        }
    }
}