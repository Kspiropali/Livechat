package edu.university.livechat;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class RequestTask {
    private final OkHttpClient client = new OkHttpClient();
    private String returnStatus;
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String REGISTER_URL = "user/register";
    private static final String LOGIN_URL = "user/login";
    private static final String CHAT_URL = "chat/sendMessage";

    // register a user api call
    public String registerPostRequest(String email, String username, String password) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("username", username)
                .add("password", password)
                .build();


        Request request = new Request.Builder()
                .url(BASE_URL + REGISTER_URL)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        if (response.isSuccessful()) {
            assert response.body() != null;
            returnStatus = response.body().string();
        } else {
            returnStatus = "Error";
        }

        return returnStatus;
    }

    // login with a user api call
    public String loginPostRequest(String username, String password) throws IOException {
        // convert username:password to base64
        String encoded = android.util.Base64.encodeToString((username + ":" + password).getBytes(), android.util.Base64.NO_WRAP);
        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Android")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "GET, POST")
                .addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .addHeader("Access-Control-Allow-Credentials", "true")
                .addHeader("Access-Control-Max-Age", "3600")
                .addHeader("Authorization", "Basic " + encoded)
                .url(BASE_URL + LOGIN_URL)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        return response.body().string();
    }

    public String sendMessage(String message, String destination) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("message", message)
                .add("destination", destination)

                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + CHAT_URL)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        return returnStatus;
    }
}