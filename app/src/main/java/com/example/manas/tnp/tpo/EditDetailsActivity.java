package com.example.manas.tnp.tpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Logger;
import com.google.firebase.storage.FirebaseStorage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Callback;


public class EditDetailsActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListner;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private EditText recipeName;
    private EditText email;
    private EditText address;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);

        Intent mainActivity = getIntent();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference();
        recipeName = findViewById(R.id.edit_description);
        submit = findViewById(R.id.submit);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String json = "{\"data\":[\"Hello World\"],\"tgt\":\"hi\",\"src\":\"en\"}";

                ArrayList<String> x=new ArrayList<String>();
                x.add("Hello");
                x.add("Bye");
                new RevHackApiUtils().getTranslatedList(x);
                System.out.println("llo");


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
//                                Toast.makeText(EditDetailsActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        });
//                        return lst;
//                    }

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
                //try {
//                    Response response = client.newCall(request).execute();
//                    Toast.makeText(EditDetailsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
//                } catch (IOException e) {
//                    Toast.makeText(EditDetailsActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
//                }
                //Requeststudent_det.getName();

//                Intent intent = new Intent(EditDetailsActivity.this, MainActivity.class);
//                startActivity(intent);
            }
        });
    }
}





