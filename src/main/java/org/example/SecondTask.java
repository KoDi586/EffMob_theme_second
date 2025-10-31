package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SecondTask {

    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Thread serverTCPThread = serverTCPThreadCreator(5000, executor);
        Thread customerTCPThread = customerTCPThreadCreator(5000);

        serverTCPThread.start();

        // время чтобы сервер поднялся и не словить ConnectionRefused
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        customerTCPThread.start();
    }

    private Thread customerTCPThreadCreator(int port) {

        return new Thread(() -> {

            String host = "localhost";

            try (Socket socket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

                System.out.println("Подключено к серверу. Напиши что-нибудь:");

                String text;
                while ((text = userInput.readLine()) != null) {
                    out.println(text);
                    String response = in.readLine();
                    System.out.println("Сервер ответил: " + response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private Thread serverTCPThreadCreator(int port, ExecutorService executor) {

        return new Thread(() -> {

            System.out.println("Echo сервер запущен на порту " + port);

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Клиент подключился: " + clientSocket.getInetAddress());

                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String line;
                    while ((line = in.readLine()) != null) {
                        String currentLine = line;
                        executor.execute(() -> {
                            System.out.println("Получено: " + currentLine);
                            out.println(currentLine);
                        });
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
