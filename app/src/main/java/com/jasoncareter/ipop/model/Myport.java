package com.jasoncareter.ipop.model;

public class Myport {
    private static int port = 50555;

    public Myport(){}

    public  void setPort(int port) {
        Myport.port = port;
    }

    public  int getPort() {
        return port;
    }
}
