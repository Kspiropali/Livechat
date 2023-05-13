//package edu.university.livechat;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_17;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.net.URISyntaxException;
//
//public class SocketConnector {
//    private WebSocketClient webSocketClient;
//
//    private void connectToSocket(String wsUrl) {
//        URI uri;
//
//        try {
//            uri = new URI(wsUrl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//        webSocketClient = new WebSocketClient(uri, new Draft_17()) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                System.out.printf("---------------WEBSOCKET OPENED------------------");
//            }
//
//            @Override
//            public void onMessage(String s) {
//                final String message = s;
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                System.out.printf("----------------------WEBSOCKET CLOSED------------------");
//            }
//
//            @Override
//            public void onError(Exception e) {
//                System.out.println("-----------------------------WEBSOCKET ERROR-----------------------");
//            }
//        };
//        webSocketClient.connect();
//    }
//}
