package com.example.manas.tnp.tpo;

import android.os.AsyncTask;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient().newBuilder()
            .addInterceptor(new RevApiInterceptor())
            .build();

    private class GetTranslationResponse extends AsyncTask<List<String>, Void, List<String>> {

        @Override
        protected List<String> doInBackground(List<String>... lists) {

            HttpUrl.Builder urlBuilder = HttpUrl.parse(revApiBase.concat("nmt")).newBuilder();
            final String url = urlBuilder.build().toString();
            Request reqObject = null;
            Response response = null;
            String res = null;
            List<String> lst = new ArrayList<>();
            try {
                reqObject = getStringTranslateRequest(url, lists[0]);
                response = client.newCall(reqObject).execute();
                res = response.body().string();
                JSONObject jobj = new JSONObject(res);
                JSONObject dt = jobj.getJSONObject("data");
                JSONArray jarr = dt.getJSONArray("result");
                for (int i = 0; i < jarr.length(); i++) {
                    lst.add(i, jarr.getJSONArray(i).get(0).toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return lst;
        }

        @Override
        protected void onPostExecute(List<String>list){
            System.out.println(list);
           super.onPostExecute(list);
        }

    }


    private Request getStringTranslateRequest(String url, List<String> list) throws JSONException {

        JSONObject requestBody = new JSONObject();
        requestBody.put("tgt", "hi");
        requestBody.put("src", "en");
        JSONArray data = new JSONArray(list);
        requestBody.put("data", data);

        return new Request.Builder()
                .url(url)
                .post(RequestBody.create(requestBody.toString(), JSON))
                .build();
    }
   // private List<String> returnString(List<String> result){
    //    return result;
   // }

    public void getTranslatedList(List<String> list) {
        //return new GetTranslationResponse().doInBackground();
        new GetTranslationResponse().execute(list);
    }
}
