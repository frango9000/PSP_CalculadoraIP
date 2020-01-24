package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final String IP = "192.168.0.1";
    public static final int PORT = 5555;

    private static ServerSocket serverSocket;


    public static void main(String[] args) {
        try {
            System.out.println("Creando socket servidor calculadora");

            serverSocket = new ServerSocket();

            System.out.println("Realizando el bind");

            InetSocketAddress addr = new InetSocketAddress(IP, PORT);
            serverSocket.bind(addr);

            System.out.println("Aceptando conexiones");

            while (true) {
                final Socket clientSocket1 = serverSocket.accept();
                new Thread(new Runnable() {
                    Socket clientSocket = clientSocket1;

                    @Override
                    public void run() {
                        String id = clientSocket.getInetAddress() + ":" + clientSocket.getPort() + ": ";
                        System.out.println(id + "Conexion iniciada");
                        try {
                            while (clientSocket.isConnected()) {
                                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                                System.out.println(id + "Enviando señal preparado");
                                out.writeBoolean(true);

                                String expresion = in.readUTF();
                                System.out.println(id + "Expresion recibida: " + expresion);

                                try {
                                    double resp = Calculadora.evaluarExpresion(expresion);
                                    System.out.println(id + "Enviando señal OK");
                                    out.writeBoolean(true);
                                    System.out.println(id + "Enviando Respuesta : " + resp);
                                    out.writeDouble(resp);
                                } catch (Exception e) {
                                    System.err.println(id + "No se pueden procesar los datos recibidos");
                                    out.writeBoolean(false);
                                }
                                System.out.println(id + "Esperando orden de continuar o cerrar.");
                                boolean repeat = in.readBoolean();
                                if (!repeat)
                                    break;
                            }
                        } catch (IOException e) {
                            System.out.println(id + "Cliente deconectado");
                            //e.printStackTrace();
                        } finally {
                            System.out.println(id + "Cerrando el socket cliente");
                            try {
                                clientSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Cerrando el socket servidor");
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Terminado");
                System.exit(0);
            }
        }
    }
}


