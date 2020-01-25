package psp.c_calc2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Test {

    private static ServerSocket serverSocket;


    public static void main(String[] args) throws IOException {

        System.out.println("Creando socket servidor calculadora");

        serverSocket = new ServerSocket();

        System.out.println("Realizando el bind");

        InetSocketAddress addr = new InetSocketAddress(5555);

        System.out.println(addr.toString());
        serverSocket.bind(addr);
        serverSocket.close();
    }

}
