package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import psp.z_misc.Asserts;

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

    public String serverHostname = "localhost";
    public int serverPort = 5555;

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

    private ClienteThread clienteThread;

    public ClienteThread getClienteThread() {
        return clienteThread;
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

    public class ClienteThread extends Thread {

        private boolean calcRepeat = true;
        Scanner scanner = new Scanner(System.in);

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

                while (calcRepeat) {
                    boolean preparado = in.readBoolean();
                    if (preparado) {
                        System.out.print("Introduce la expresion a calcular: ");
                        String expresion = getExpresion();
                        out.writeUTF(expresion);

                        boolean validacion = in.readBoolean();
                        log("Status Recibido: " + validacion);
                        if (validacion) {
                            double resp = in.readDouble();
                            log("Respuesta recibida : " + resp);
                        } else
                            log("Error en los datos enviados.");

                        log("1 - Nuevo Calculo\n0 - Cerrar");
                        String nloop = scanner.nextLine();
                        calcRepeat = Asserts.isInteger(nloop) && Integer.parseInt(nloop) == 1;
                        log("Enviando señal de continuar o cerrar: " + calcRepeat);
                        out.writeBoolean(calcRepeat);
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
            return scanner.nextLine();
        }

    }

    private void log(String message) {
        log(message);
    }
}