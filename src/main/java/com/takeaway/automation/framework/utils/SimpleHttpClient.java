package com.takeaway.automation.framework.utils;

import com.takeaway.automation.framework.logger.LoggerFactory;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class SimpleHttpClient {

    private final OkHttpClient httpClient = new OkHttpClient();

    Headers.Builder builder = new Headers.Builder();
    public String sendGetRequest(String url, HashMap<String, String> headersMap) throws IOException {
        Headers headers = addHeaders(headersMap);
        Request request = new Request.Builder()
                .url(url).headers(headers).build();
        return getResponse(request);
    }

    @NotNull
    private String getResponse(Request request) throws IOException {
        try (Response response = httpClient.newCall(request).execute()) {
            final String responseBody = response.body().string();
            if (!response.isSuccessful()) {
                LoggerFactory.LOG.error("Request failed. Returning response for further analysis");
            }
            LoggerFactory.LOG.info(String.format("Response data is: =[%s]", responseBody));
            return responseBody;
        }
    }

    private Headers addHeaders(HashMap<String, String> headersMap){
        headersMap.forEach((key, value) -> builder.add(key, value));
        return builder.build();
    }

    public String sendPostRequest(String url, String body, HashMap<String, String> headersMap) throws IOException {
        Headers headers = addHeaders(headersMap);
        RequestBody requestBody = RequestBody.create(
                body, MediaType.parse("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(url).headers(headers).post(requestBody).build();
        return getResponse(request);
    }

    public String sendDeleteRequest(String url, HashMap<String, String> headersMap) throws IOException {
        Headers headers = addHeaders(headersMap);
        Request request = new Request.Builder().url(url).headers(headers).delete().build();
        return getResponse(request);
    }

}
