package com.example.client;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;

public class ClientHighOrLow {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public ClientHighOrLow(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            System.out.println("Error creating client");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMsgToServer(String msgToServer){
        try{
            bufferedWriter.write(msgToServer);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e){
            System.out.println("Error sending message to the client");
            e.printStackTrace();
            closeEverything(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    public void receiveMsgFromClient(VBox vBox){
        new Thread(new Runnable() {
            public void run() {
                while (socket.isConnected()){
                    try {
                        String msgFromClient = bufferedReader.readLine();
                        HelloController.addLabel(msgFromClient, vBox);
                    } catch (IOException e){
                        System.out.println("Error receiving message from the client");
                        e.printStackTrace();
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }

                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if(socket != null){
                socket.close();
            }
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
        } catch (IOException e) {
            System.out.println("Error creating client");
            e.printStackTrace();
        }

    }
}
