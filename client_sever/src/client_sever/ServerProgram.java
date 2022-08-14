/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_sever;

/**
 *
 * @author Hoang.HN
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerProgram {

    public static void main(String args[]) throws IOException {

        ServerSocket listener = null;

        System.out.println("doi client ket noi....");
        int clientNumber = 0;

        // Mở một ServerSocket tại cổng 7234.
        try {
            listener = new ServerSocket(7234);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(1);
        }

        try {
            while (true) {
                // Chấp nhận một yêu cầu kết nối từ phía Client.                
                Socket socketOfServer = listener.accept();
                new ServiceThread(socketOfServer, clientNumber++).start();
            }
        } finally {
            listener.close();
        }

    }

    private static class ServiceThread extends Thread {

        private final int clientNumber;
        private final Socket socketOfServer;

        public ServiceThread(Socket socketOfServer, int clientNumber) {
            this.clientNumber = clientNumber;
            this.socketOfServer = socketOfServer;
            // Log
            System.out.println("co mot ket noi moi voi client# " + this.clientNumber + " o " + socketOfServer);
        }

        @Override
        public void run() {
            try {
                // Mở luồng vào ra trên Socket tại Server.
                BufferedReader is = new BufferedReader(new InputStreamReader(socketOfServer.getInputStream()));
                BufferedWriter os = new BufferedWriter(new OutputStreamWriter(socketOfServer.getOutputStream()));
                
                while (true) {
                    // Đọc dữ liệu tới server (Do client gửi tới).
                    String line = is.readLine();
                    // Nếu người dùng gửi tới QUIT (Muốn kết thúc trò chuyện).
                    if (line.equals("QUIT")) {
                        os.write(">> OK");
                        os.newLine();
                        os.flush();
                        System.out.println("da ngat dong ket noi voi# " + this.clientNumber + " at " + socketOfServer);
                        break;
                    }
                    // Ghi vào luồng đầu ra của Socket tại Server.
                    // Nghĩa là gửi tới Client.
                    os.write(">> " + line.toUpperCase());
                  
                    // Kết thúc dòng
                    os.newLine();
                    // Đẩy dữ liệu đi
                    os.flush();
                }

            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }
}
