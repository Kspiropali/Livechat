//package edu.university.livechat.data.handlers;
//
//import com.here.oksse.OkSse;
//import com.here.oksse.ServerSentEvent;
//
//import okhttp3.Request;
//import okhttp3.Response;
//
//public class LiveListener {
//    private final String SSE_ENDPOINT_URL = "http://10.0.2.2:8080/latest/";
//    private final String SUFFIX = "/messages";
//    private ServerSentEvent sse;
//    public void connectToServer(String roomId, String token) {
//        String encoded = java.util.Base64.getEncoder().encodeToString((token).getBytes());
//        Request request = new Request.Builder()
//                .url(SSE_ENDPOINT_URL + roomId + SUFFIX)
//                .get()
//                .header("Accept", "*/*")
//                .header("Cache-Control", "no-cache")
//                .header("Connection", "keep-alive")
//                .header("cache-control", "no-cache")
//                .header("Access-Control-Allow-Origin", "*")
//                .header("Access-Control-Allow-Methods", "GET, POST")
//                .header("Access-Control-Allow-Headers", "Content-Type, Authorization")
//                .header("Access-Control-Max-Age", "3600")
//                .header("Access-Control-Allow-Credentials", "true")
//                .addHeader("Authorization", "Basic " + encoded)
//                .build();
//        OkSse okSse = new OkSse();
//        System.out.println("Connecting to "+ roomId);
//        //Log.d("COMMENT", comment);
//        sse = okSse.newServerSentEvent(request, new ServerSentEvent.Listener() {
//            @Override
//            public void onOpen(ServerSentEvent sse, Response response) {
//                System.out.println("Connected to server: " + request.url());
//            }
//
//            @Override
//            public void onMessage(ServerSentEvent sse, String id, String event, String message) {
//                System.out.println(message);
//            }
//
//            @Override
//            public void onComment(ServerSentEvent sse, String comment) {
//                //Log.d("COMMENT", comment);
//            }
//
//            @Override
//            public boolean onRetryTime(ServerSentEvent sse, long milliseconds) {
//                return true;
//            }
//
//            @Override
//            public boolean onRetryError(ServerSentEvent sse, Throwable throwable, Response response) {
//                return true;
//            }
//
//            @Override
//            public void onClosed(ServerSentEvent sse) {
//                System.out.println("Connection has been closed");
//            }
//            @Override
//            public Request onPreRetry(ServerSentEvent sse, Request originalRequest) {
//                return originalRequest;
//            }
//        });
//
//    }
//
//    public void disconnectFromServer() {
//        if (sse != null) {
//            sse.close();
//        }
//    }
//}