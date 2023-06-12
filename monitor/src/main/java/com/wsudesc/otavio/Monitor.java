package com.wsudesc.otavio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Monitor {
  public Monitor() {
  }
  private static int NClientes;
  private static String DbDir;

  public static void setNroClient(int nclients) throws Exception {
    NClientes = nclients;
  }

  public static int getNroClient() throws Exception {
    return NClientes;
  }

  public static void setDbDir(String dbDir) throws Exception {
    DbDir = dbDir;
  }

  public static String getdbDir() throws Exception {
    return DbDir;
  }

  public static void setServer() throws Exception {
    Scanner sc = new Scanner(System.in);
    while (true) {
      if (!sc.hasNextLine()) {
        break;
      }
      String sCurrentLine = sc.nextLine();
      String[] word = sCurrentLine.split("=");
      switch (word[0]) {
        case "NofClient":
          setNroClient(Integer.parseInt(word[1]));
          break;
        case "dbDIR":
          setDbDir(word[1]);
          break;

        default:
      }
    }

    sc.close();
    // Ler diretório de entrada
    // Montar fila de jobs (upload)
    //
  }

  static void printReport() {
    System.out.println("*** Log DESCMon File ***");
    System.out.println("****** End DESCMon ******");
  }

  public static void main(String[] args) {
    try {
      // System.out.println("Configurando servidor ...");
      setServer();
      // Instancia o objeto servidor e a sua stub
      ServerSocket welcomeSocket = new ServerSocket(4355);

      // System.out.println("Servidor pronto ...");

      List<Thread> jobThreads = new ArrayList<>();

      for (int i = 0; i < qtdFiles * 2; i++) {
        Socket connectionSocket = welcomeSocket.accept();
        // setNroClient(getNroClient() - 1);
        Thread thread = new Thread() {
          @Override
          public void run() {
            try {

              DataInputStream inFromClient = new DataInputStream(connectionSocket.getInputStream());
              DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

              // Receber job da fila
              // Caso não tenha job, espere uma nova inserção na fila.
              Job job = jobsQueue.take();
            } catch (IOException | InterruptedException exception) {
            }
          }
        };
        thread.start();
        jobThreads.add(thread);
      }

      for (int i = 0; i < NClientes; i++) {
        Socket connectionSocket = welcomeSocket.accept();
        // setNroClient(getNroClient() - 1);
        Thread thread = new Thread() {
          @Override
          public void run() {
            try {
              DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
              outToClient.writeUTF("finish");
            } catch (IOException exception) {

            }
          }
        };
        thread.start();
        jobThreads.add(thread);
      }

      for (Thread jobThread : jobThreads) {
        jobThread.join();
      }

      printReport();
      // System.out.println("Servidor finalizado com sucesso!");
      welcomeSocket.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
