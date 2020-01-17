package psp.b_calc;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import psp.z_misc.Asserts;

public class Cliente {

    public static final String SERV_IP = "192.168.0.1";
    public static final int SERV_PORT = 5555;


    public static void main(String[] args) {

        try {
            System.out.println("Creando socket cliente");
            Socket clienteSocket = new Socket();
            System.out.println("Estableciendo la conexi�n");

            InetSocketAddress addr = new InetSocketAddress(SERV_IP, SERV_PORT);
            clienteSocket.connect(addr);

            DataInputStream in = new DataInputStream(clienteSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clienteSocket.getOutputStream());

            while (clienteSocket.isConnected()) {
                Scanner scanner = new Scanner(System.in);
                int servidorDisponible = in.readInt();
                if (servidorDisponible == 1) {
                    String ns1 = scanner.next();
                    String ns2 = scanner.next();
                    String op = scanner.next();

                    if (Asserts.isDouble(ns1) && Asserts.isDouble(ns2) && Asserts.isInteger(op)) {
                        out.writeFloat(Float.parseFloat(ns1));
                        out.writeFloat(Float.parseFloat(ns1));
                        out.writeInt(Integer.parseInt(ns1));
                    }

                    int status = scanner.nextInt();
                    if (status == 1) {
                        float resp = in.readFloat();
                        System.out.println("Respuesta recibida : " + resp);
                    } else
                        System.out.println("Error en los datos enviados.");
                    System.out.println("1 - Nuevo Calculo\n0 - Cerrar");

                    String nloop = "";
                    while (!Asserts.isInteger(nloop) || Integer.parseInt(nloop) > 1 || Integer.parseInt(nloop) < 0) {
                        nloop = scanner.next();
                    }
                    if (Integer.parseInt(nloop) == 0)
                        break;

                }
            }

            String mensaje = "mensaje desde el cliente";
            out.write(mensaje.getBytes());

            System.out.println("Mensaje enviado");

            System.out.println("Cerrando el socket cliente");

            clienteSocket.close();

            System.out.println("Terminado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}