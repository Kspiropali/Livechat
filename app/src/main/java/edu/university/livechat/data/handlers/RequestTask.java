package edu.university.livechat.data.handlers;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.university.livechat.data.model.Message;
import edu.university.livechat.data.model.User;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class RequestTask {
    private final OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = "http://192.168.0.127:8080/";
    private static final String REGISTER_URL = "user/register";
    private static final String LOGIN_URL = "user/login";
    private static final String CHAT_URL = "chat/sendMessage";
    private static final String GET_REGISTERED_USERS = "download/chat/registeredUsers";
    private static final String GET_MESSAGES = "download/chat/";
    private static final MediaType jsonType = MediaType.parse("application/json");
    private static final String UPDATE_URL = "user/update";
    private static final String GET_USER_DETAILS = "user/details";

    // register a user api call
    @SuppressWarnings("deprecation")
    public String registerPostRequest(String email, String username, String firstName, String lastName, String password) throws IOException {
        Response response;

        RequestBody requestBody = RequestBody.create(jsonType, "{\"email\":\"" + email
                + "\",\"username\":\""
                + username + "\",\"name\":\""
                + firstName + "\",\"surname\":\""
                + lastName + "\",\"password\":\""
                + password + "\"}");

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

    // login by one time token instead of username password
    public String loginByToken(String token) throws IOException {
        // convert username:password to base64
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

    // get active users
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

    // download messages from server for a specific user in a room
    public ArrayList<Message> downloadMessages(String destination, String token) throws IOException {
        Response response;
        if (token == null) {
            return null;
        }
        String encoded = android.util.Base64.encodeToString((token).getBytes(), android.util.Base64.NO_WRAP);
        ArrayList<Message> messages = new ArrayList<>();

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
                .url(BASE_URL + GET_MESSAGES + destination + "/messages")
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                assert response.body() != null;

                //parse response to Message Arraylist
                JSONArray jsonArray = new JSONArray(response.body().string());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    messages.add(new Message(jsonObject.getString("content"), jsonObject.getString("sender"), jsonObject.getString("destination"), jsonObject.getString("time"), jsonObject.getString("type")));
                }

                return messages;
            }
        } catch (IOException e) {
            messages.add(new Message("Server is down"));
            return messages;
        } catch (Exception e) {
            messages.add(new Message(e.getMessage()));
            return messages;
        }

        assert response.body() != null;
        messages.add(new Message("unauthorized"));

        return messages;
    }

    // sends message to servers dedicated socket
    @SuppressWarnings("unused")
    public String sendMessage(String token, Message message) throws IOException {
        Response response;
        RequestBody requestBody = RequestBody.create(jsonType, new Gson().toJson(message));
        String encoded = android.util.Base64.encodeToString((token).getBytes(), android.util.Base64.NO_WRAP);
        Request request = new Request.Builder()
                .header("Authorization", "Basic " + encoded)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Android")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "GET, POST")
                .addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .addHeader("Access-Control-Allow-Credentials", "true")
                .addHeader("Access-Control-Max-Age", "3600")
                .url(BASE_URL + CHAT_URL)
                .post(requestBody)
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

    // update the user's details
    @SuppressWarnings("deprecation")
    public String update(String token, User user) throws IOException {
        Response response;
        RequestBody userToUpdate = RequestBody.create(jsonType, new Gson().toJson(user));
        //RequestBody requestBody = RequestBody.create(jsonType, new Gson().toJson(message));
        String encoded = android.util.Base64.encodeToString((token).getBytes(), android.util.Base64.NO_WRAP);
        Request request = new Request.Builder()
                .header("Authorization", "Basic " + encoded)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Android")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "GET, POST")
                .addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .addHeader("Access-Control-Allow-Credentials", "true")
                .addHeader("Access-Control-Max-Age", "3600")
                .url(BASE_URL + UPDATE_URL)
                .post(userToUpdate)
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

    @SuppressWarnings("deprecation")
    public User getUserDetails(String token) throws IOException {
        Response response;
        String encoded = android.util.Base64.encodeToString((token).getBytes(), android.util.Base64.NO_WRAP);
        Request request = new Request.Builder()
                .header("Authorization", "Basic " + encoded)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("User-Agent", "Android")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Methods", "GET, POST")
                .addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")
                .addHeader("Access-Control-Allow-Credentials", "true")
                .addHeader("Access-Control-Max-Age", "3600")
                .url(BASE_URL + GET_USER_DETAILS)
                .post(RequestBody.create(jsonType, ""))
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return new Gson().fromJson(response.body().string(), User.class);
            }
        } catch (Exception e) {
            return new User("Server is down");
        }

        assert response.body() != null;
        return new User(response.body().string());
    }

}