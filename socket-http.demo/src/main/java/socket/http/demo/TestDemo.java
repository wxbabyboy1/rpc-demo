package socket.http.demo;

import socket.http.demo.utils.SimpleOneceAgent;

import java.io.IOException;

public class TestDemo {

    public static void main(String[] args) throws IOException {
        String url = "http://www.mtime.com";
        String content = "";
        try{
            content = new SimpleOneceAgent().ReadUrlContent(url);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        System.in.read();
    }

}
