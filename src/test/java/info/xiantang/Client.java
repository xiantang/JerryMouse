package info.xiantang;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public void start() throws UnknownHostException, IOException
    {
        Socket socket=new Socket("127.0.0.1",8080);
        System.out.println("客户端连接成功");
        Scanner scanner=new Scanner(System.in);
        BufferedWriter write=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));     //可用PrintWriter
        BufferedReader in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String readline;
        while(true)                                                 //循环发消息
        {
            readline=scanner.nextLine();
            write.write(readline+'\n');                            //write()要加'\n'
            write.flush();
//			socket.shutdownOutput();
            System.out.println(in.readLine());
        }
//        write.close();
//        in.close();
//        socket.close();
    }
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        new Client().start();
    }

}
