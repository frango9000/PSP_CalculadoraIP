package psp.c_calc2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import psp.z_misc.Asserts;

public class Cliente {

    public static final String SERV_IP = "localhost";
    public static final int SERV_PORT = 5555;


    public static void main(String[] args) {
        boolean calcRepeat = true;
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("Creando socket cliente");
            Socket clienteSocket = new Socket();
            System.out.println("Estableciendo la conexion");

            InetSocketAddress addr = new InetSocketAddress(SERV_IP, SERV_PORT);
            clienteSocket.connect(addr);
            System.out.println("Conectado");

            DataInputStream in = new DataInputStream(clienteSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clienteSocket.getOutputStream());

            while (calcRepeat) {
                boolean preparado = in.readBoolean();
                if (preparado) {
                    System.out.print("Introduce la expresion a calcular: ");
                    String expresion = scanner.nextLine();
                    out.writeUTF(expresion);

                    boolean validacion = in.readBoolean();
                    System.out.println("Status Recibido: " + validacion);
                    if (validacion) {
                        double resp = in.readDouble();
                        System.out.println("Respuesta recibida : " + resp);
                    } else
                        System.out.println("Error en los datos enviados.");

                    System.out.println("1 - Nuevo Calculo\n0 - Cerrar");
                    String nloop = scanner.nextLine();
                    calcRepeat = Asserts.isInteger(nloop) && Integer.parseInt(nloop) == 1;
                    System.out.println("Enviando señal de continuar o cerrar: " + calcRepeat);
                    out.writeBoolean(calcRepeat);
                }
            }

            System.out.println("Cerrando el socket cliente");

            clienteSocket.close();

            System.out.println("Terminado");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}