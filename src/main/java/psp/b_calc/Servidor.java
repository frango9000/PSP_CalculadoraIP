package psp.b_calc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final String IP = "192.168.0.1";
    public static final int PORT = 5555;


    public static void main(String[] args) {
        try {
            System.out.println("Creando socket servidor calculadora");

            ServerSocket serverSocket = new ServerSocket();

            System.out.println("Realizando el bind");

            InetSocketAddress addr = new InetSocketAddress(IP, PORT);
            serverSocket.bind(addr);

            System.out.println("Aceptando conexiones");

            Socket newSocket = serverSocket.accept();

            System.out.println("Conexion recibida, ip: " + newSocket.getInetAddress() + ":" + newSocket.getPort());

            DataInputStream in = new DataInputStream(newSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(newSocket.getOutputStream());

            while (newSocket.isConnected()) {
                System.out.println("Enviando señal preparado (1)");
                out.writeInt(1);

                try {

                    float num1 = in.readFloat();
                    float num2 = in.readFloat();
                    int opr = in.readInt();

                    float resp = 0;
                    switch (opr) {
                        case 1:
                            resp = num1 + num2;
                            break;
                        case 2:
                            resp = num1 - num2;
                            break;
                        case 3:
                            resp = num1 * num2;
                            break;
                        case 4:
                            resp = num1 / num2;
                            break;
                    }
                    out.writeInt(1);
                    out.writeFloat(resp);
                } catch (Exception e) {
                    System.err.println("No se pueden procesar los datos recibidos");
                    out.writeInt(0);
                }

            }

            System.out.println("Cerrando el nuevo socket");

            newSocket.close();

            System.out.println("Cerrando el socket servidor");

            serverSocket.close();

            System.out.println("Terminado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
