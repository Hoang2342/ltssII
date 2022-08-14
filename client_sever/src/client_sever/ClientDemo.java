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

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.*;


public class ClientDemo {

    public static void main(String[] args) throws UnknownHostException {
        // Địa chỉ máy chủ.
        String serverHost = null;

        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface n = (NetworkInterface) e.nextElement();
                Enumeration ee = n.getInetAddresses();
                while (ee.hasMoreElements()) {
                    InetAddress ip = (InetAddress) ee.nextElement();
                    if(ip instanceof Inet6Address){
                        serverHost = ip.getHostAddress();
                        break;
                    }
                }   
            break;
            }
        } catch (SocketException ex) {
            Logger.getLogger(ClientDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 
        Socket socketOfClient;
        BufferedWriter os;
        BufferedReader is;
        Scanner sc = new Scanner(System.in);
        try {
            // Gửi yêu cầu kết nối tới Server đang lắng nghe
            // trên máy 'ServerHost' cổng 7234.
            socketOfClient = new Socket(serverHost, 7234);

            // Tạo luồng đầu ra tại client (Gửi dữ liệu tới server)
            os = new BufferedWriter(new OutputStreamWriter(socketOfClient.getOutputStream()));

            // Luồng đầu vào tại Client (Nhận dữ liệu từ server).
            is = new BufferedReader(new InputStreamReader(socketOfClient.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("khong ket noi duoc voi  " + serverHost);
            return;
        } catch (IOException e) {
            System.err.println("khong I/O duoc voi " + serverHost);
            return;
        }

        try {
            String reLine;
            // Ghi dữ liệu vào luồng đầu ra của Socket tại Client.
            while (true) {
                os.write(sc.nextLine());
                os.newLine();
                os.flush();  
                
                // Đọc dữ liệu trả lời từ phía server
                // Bằng cách đọc luồng đầu vào của Socket tại Client.
                if ((reLine = is.readLine()) != null) {
                    System.out.println("Server: " + reLine);
                    if (reLine.contains("OK")) {
                    break;
                    }
                }
            }
         
            os.close();
            is.close();
            socketOfClient.close();
        } catch (UnknownHostException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println("IOException:  " + e);
        }
    }
}
