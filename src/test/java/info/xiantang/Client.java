package info.xiantang;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    public void start() throws UnknownHostException, IOException, InterruptedException {

    public void start() throws UnknownHostException, IOException
    {


        write.write("GET /login?api=jdk-zh HTTP/1.1\n" +
                "Host: tool.oschina.net\n" +
                "Connection: keep-alive\n" +
                "Upgrade-Insecure-Requests: 1\n" +
                "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36\n" +
                "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                "Accept-Encoding: gzip, deflate\n" +
                "Accept-Language: zh-CN,zh;q=0.9\n" +
                "Cookie: bad_id8387c580-a888-11e5-bc38-bb63a4ea0854=47c190f1-aa9f-11e8-953b-219cef9748c6; _user_behavior_=e5abca29-5e58-4622-adb1-cb6b2a9bbeca; oscid=XOS5dgOgEZTLstnEj4MnpIrto%2BBjsgkg4QJ35ebherslh54Qm6WuZTsYgqSlW5wbGXVWTCKE1xLMHWLYN7%2FGXImIwFVCXT4IoAsxwuPaVWEyZ0wMeMBFpRM3Uuo3y0B5%2BIwfLuJQLcXrCMvUVOA7Rw%3D%3D; Hm_lvt_a411c4d1664dd70048ee98afe7b28f0b=1537442124,1538308081,1539186448" +
                "\n\r\n\r");
        write.flush();
//        String readline;
//        while(true)                                                 //循环发消息
//        {
//            readline=scanner.nextLine();
//            write.write(readline+'\n');                            //write()要加'\n'
//            write.flush();
//        }

        Thread.sleep(30000);
        System.out.println("ok 开读");
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());
        System.out.println(in.readLine());

        while (true);
    }
    public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

        new Client().start();
    }

}
