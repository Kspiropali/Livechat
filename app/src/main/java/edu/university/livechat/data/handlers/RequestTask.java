package edu.university.livechat.data.handlers;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class RequestTask {
    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private static final String REGISTER_URL = "user/register";
    private static final String LOGIN_URL = "user/login";
    private static final String CHAT_URL = "chat/sendMessage";

    private static final String GET_REGISTERED_USERS = "download/chat/registeredUsers";
    private static final MediaType jsonType = MediaType.parse("application/json");

    // register a user api call
    public String registerPostRequest(String email, String username, String firstName, String lastName, String password) throws IOException {
        Response response;

        RequestBody requestBody = RequestBody.create(jsonType, "{\"email\":\"" + email
                + "\",\"username\":\""
                + username + "\",\"name\":\""
                + firstName + "\",\"surname\":\""
                + lastName + "\",\"password\":\""
                + password + "\"}");

        System.out.printf("Request body: %s\n", requestBody.contentType());
        Request request = new Request.Builder()
                .url(BASE_URL + REGISTER_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Android")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "GET, POST")
                .addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .addHeader("Access-Control-Allow-Credentials", "true")
                .addHeader("Access-Control-Max-Age", "3600")
                .build();

        Call call = client.newCall(request);

        try {
            response = call.execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            return "Server is down";
        } catch (Exception e) {
            return "Something went wrong";
        }

        assert response.body() != null;
        return response.body().string();
    }

    // login with a user api call
    public String loginPostRequest(String username, String password) throws IOException {
        // convert username:password to base64
        Response response;
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
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            return "Server is down";
        } catch (Exception e) {
            return "Something went wrong";
        }

        assert response.body() != null;
        return response.body().string();
    }

    public String loginByToken(String token) throws IOException {
        // convert username:password to base64
        Response response;

        Request request = new Request.Builder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Android")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "GET, POST")
                .addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .addHeader("Access-Control-Allow-Credentials", "true")
                .addHeader("Access-Control-Max-Age", "3600")
                .addHeader("Authorization", "Basic " + token)
                .url(BASE_URL + LOGIN_URL)
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            return "Server is down";
        } catch (Exception e) {
            return "Something went wrong";
        }

        assert response.body() != null;
        return response.body().string();
    }

    public String getRegisteredUsers(String token) throws IOException {
        Response response;
        String encoded = android.util.Base64.encodeToString((token).getBytes(), android.util.Base64.NO_WRAP);

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
                .url(BASE_URL + GET_REGISTERED_USERS)
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            return "Server is down";
        } catch (Exception e) {
            return "Something went wrong";
        }

        assert response.body() != null;
        return response.body().string();
    }


    public String sendMessage(String message, String destination) throws IOException {
        Response response;

        RequestBody requestBody = new FormBody.Builder()
                .add("message", message)
                .add("destination", destination)

                .build();

        Request request = new Request.Builder()
                .url(BASE_URL + CHAT_URL)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        response = call.execute();

        return "returnStatus";
    }
}