package socket.http.demo;

import socket.http.demo.utils.SimpleOneceAgent;

import java.io.IOException;

public class TestDemo {

    public static void main(String[] args) throws IOException {
        String url = "http://www.epson.jp";
        String content = "";
        try{
            content = new SimpleOneceAgent().ReadUrlContent(url);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.out.println(content);
        System.in.read();
    }

}
