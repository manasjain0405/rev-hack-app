package com.example.manas.tnp.tpo;

import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

class RevApiInterceptor implements Interceptor {

    final static String accessToken = "963a5a83b3f8b7f838b6f86160debb2ad74e91a2";
    final static String AUTHORIZATION = "token";

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request originalRequest = chain.request();

        // Add authorization header with updated authorization value to intercepted request
        Request authorisedRequest = originalRequest.newBuilder()
                .header(AUTHORIZATION, accessToken)
                .build();
        return chain.proceed(authorisedRequest);
    }
}

public class RevHackApiUtils {

    public static final String revApiBase = "https://hackapi.reverieinc.com/";

    private void getClient(final String ext, JSONObject req) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(revApiBase.concat(ext)).newBuilder();
        final String url = urlBuilder.build().toString();

        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(req.toString(), JSON))
                .build();

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new RevApiInterceptor())
                .build();

//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                call.cancel();
//            }
//
//            @Override
//            public void onResponse(Call call, final Response response) throws IOException {
//
//                final String myResponse = response.body().string();
//
//                EditDetailsActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });
//
//            }
//        });

    }

    private JSONObject getStringTranslateRequest(List<String> list) throws JSONException {
        JSONObject requestBody = new JSONObject();
        requestBody.put("tgt", "hi");
        requestBody.put("src", "en");
        JSONArray data = new JSONArray(list);
        requestBody.put("data", data);
        return requestBody;
    }

    public List<String> getHindiStringArray(List<String> list) throws JSONException {

        getStringTranslateRequest(list);

        return null;
    }
}




//String json = "{\"data\":[\"Hello World\"],\"tgt\":\"hi\",\"src\":\"en\"}";
//                JSONObject j= new JSONObject();
//                try {
//                    j = new JSONObject(json);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                //JSONObject js = new
//                RecipeDetails student_det = new RecipeDetails(recipeName.getText().toString());
//                HttpUrl.Builder urlBuilder = HttpUrl.parse("https://hackapi.reverieinc.com/nmt").newBuilder();
//                String url = urlBuilder.build().toString();
//                final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//                //final JSONObject j = new JSONObject();
//                Request request = new Request.Builder()
//                        .url(url)
//                        .post(RequestBody.create(j.toString(), JSON))
//                        .build();
//
//                OkHttpClient client = new OkHttpClient().newBuilder()
//                        .addInterceptor(new ApiInterceptor())
//                        .build();
//
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        call.cancel();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, final Response response) throws IOException {
//
//                        final String myResponse = response.body().string();
//
//                        EditDetailsActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                    Toast.makeText(EditDetailsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        });
//
//                    }
//                });