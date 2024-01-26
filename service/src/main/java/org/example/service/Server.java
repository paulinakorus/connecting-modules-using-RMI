package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.model.enums.Method;
import org.example.service.model.Payload;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public abstract class Server {
    private String host = "localhost";
    private int port = 2137;
    private ByteBuffer buffer = ByteBuffer.allocate(2056);          // przechowalnia danych, (wielkosc)
    protected ObjectMapper objectMapper = new ObjectMapper();

    public void start(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        start();
    }
    public void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel channel = ServerSocketChannel.open();
        channel.bind(new InetSocketAddress(host, port));
        channel.configureBlocking(false);                           // nie blokuje wątku tylko czeka
        channel.register(selector, SelectionKey.OP_ACCEPT);

        while(true){
            selector.select();
            var keySet = selector.selectedKeys();
            var iterator = keySet.iterator();

            while(iterator.hasNext()){
                var key = iterator.next();
                if(key.isAcceptable()){
                    var client = channel.accept();
                    if(client != null) {
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                    }
                }
                if(key.isReadable()){
                    try(var clientChannel = (SocketChannel)key.channel()){
                        buffer = ByteBuffer.allocate(2056);
                        if(clientChannel.read(buffer) == -1){       // klient sie rozłączył
                            key.cancel();
                            //clientChannel.close();
                            return;
                        }
                        buffer.flip();                  // ustawia wskaznik na poczatek i usatwia buffera na to ile wczytał
                        String data = new String(buffer.array(), StandardCharsets.UTF_8).trim();        // usuwa spacje i biale znaki
                        Payload payload = objectMapper.readValue(data, Payload.class);

                        Object argument = null;
                        String agument = payload.getArgument();
                        if(payload.getArgument() != null)
                            argument = objectMapper.readValue(payload.getArgument(), payload.getMethod().getType());

                        var response = execute(payload.getMethod(), argument);
                        buffer = ByteBuffer.wrap(response.getBytes());
                        clientChannel.write(buffer);
                    }
                }
            }
        }
    }

    protected abstract String execute(Method method, Object obj);

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
