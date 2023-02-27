package edu.university.livechat;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class RequestTask {
    private final OkHttpClient client = new OkHttpClient();
    private String returnStatus;
    private static final String BASE_URL = "http://10.0.2.2";

    public String run(String url) {
        Request request = new Request.Builder()
                .url("http://10.0.2.2/")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                returnStatus = "Error";
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful())
                        returnStatus = String.valueOf(response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    assert responseBody != null;
                    System.out.println(responseBody.string());
                    //redirect to chat activity

                }
            }
        });

        return returnStatus;
    }

    public String registerPost(String url, String email, String username, String password, RequestBody body) throws IOException {
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("username", username)
                .add("password", password)
                .build();
        if (url.endsWith("/"))
            url = url.substring(0, url.length() - 1);

        Request request = new Request.Builder()
                .url(BASE_URL+url)
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();
        System.out.println(response.body().string());

        return returnStatus;
    }
}