package org.example.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public abstract class Client {
    private final String host;
    private final int port;
    private SocketChannel socketChannel;
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }
    public void connect() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
    }

    public void disconnect() throws IOException {
        socketChannel.close();
    }
    public String sendAndRead(String text) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(text.getBytes());
        connect();
        socketChannel.write(byteBuffer);

        byteBuffer = ByteBuffer.allocate(2056);
        socketChannel.read(byteBuffer);
        disconnect();

        return new String(byteBuffer.array(), StandardCharsets.UTF_8).trim();
    }
}
