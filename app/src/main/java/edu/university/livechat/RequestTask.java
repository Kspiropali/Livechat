package edu.university.livechat;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class RequestTask {
  private final OkHttpClient client = new OkHttpClient();

  public void run() {
    Request request = new Request.Builder()
        .url("http://10.0.2.2")
        .build();

    client.newCall(request).enqueue(new Callback() {
      @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
        e.printStackTrace();
      }

      @Override public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try (ResponseBody responseBody = response.body()) {
          if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

          Headers responseHeaders = response.headers();
          for (int i = 0, size = responseHeaders.size(); i < size; i++) {
            System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
          }

          System.out.println(responseBody.string());
        }
      }
    });
  }
}