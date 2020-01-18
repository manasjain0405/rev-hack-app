package com.example.manas.tnp.tpo;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
//import com.fasterxml.jackson.databind.ObjectMapper;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.revhack.spoonacular.httpService.SpoonacularHttpResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //private static final String TAG = "MainActivity";

    public static final String ANONYMOUS = "anonymous";
    public static final int RC_SIGN_IN = 1;
    public static final int RC_RESUME_PICKER = 2;

    //private ListView mMessageListView;
    //private ProgressBar mProgressBar;
    //private ImageButton mPhotoPickerButton;
    //private EditText mMessageEditText;

    private Button editButton;
    private Button pollButton;
    private Button uploadResumeButton;
    private Button downloadResumeButton;

    private String mUsername;
    private static String user_email_id;

    private FirebaseDatabase fbDB;
    private DatabaseReference dbref;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference resumeStorage;
    private static String resume_link_url;
    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RequestQueue requestQueue = Volley.newRequestQueue(this);

       // SpoonacularHttpResponse spoonacularHttpResponse = new SpoonacularHttpResponse();
        //spoonacularHttpResponse.getRecipeSearchResponse(requestQueue,324694);
        //get method for receipe
        // this method provides equiments
        //String URL = "https://api.spoonacular.com/recipes/"+"324694"+"/analyzedInstructions?apiKey=8917e952b5074395856aea4402376c8a";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//
//
//        JsonArrayRequest objectRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                URL,
//                null,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.e("Receipe details",response.toString());
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e("Error", error.toString());
//                    }
//                }
//        );
//        requestQueue.add(objectRequest);

        //http://localhost:4041/data

        String URL = "http://192.168.43.14:4041/data";
        //RequestQueue requestQueue = Volley.newRequestQueue(this);



        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Data",response.toString());
                        parseJson(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                }
        );


        requestQueue.add(objectRequest);



        mUsername = ANONYMOUS;

        fbDB = FirebaseDatabase.getInstance();
        dbref = fbDB.getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();
        resumeStorage = mFirebaseStorage.getReference().child("AllResume");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialized(user.getEmail());
                } else {
                    // User is signed out
                    onSignOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build())
                                    )
                                    .setLogo(R.drawable.sgsits)
                                    .build(), RC_SIGN_IN);
                }
            }
        };

        editButton = findViewById(R.id.edit_details);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(MainActivity.this, EditDetailsActivity.class);
                editIntent.putExtra("user_email",user_email_id);
                startActivity(editIntent);
            }
        });

        pollButton = findViewById(R.id.poll_form);
        pollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pollIntent = new Intent(MainActivity.this, PollListActivity.class);
                startActivity(pollIntent);

            }
        });

        uploadResumeButton = findViewById(R.id.upload_resume);
        uploadResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_RESUME_PICKER);
            }
        });


        downloadResumeButton = findViewById(R.id.download_resume);
        downloadResumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{resumelisten();
                if(resume_link_url!=null){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(resume_link_url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
                }else{
                    Toast.makeText(MainActivity.this, "Please Upload Resume First!", Toast.LENGTH_LONG).show();
                }
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "Firebase Server Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void parseJson(JSONArray response) {
        try {
            if(userList == null){
                userList = new ArrayList<>();
            }
            else{
                userList.clear();
            }
            JSONArray jsonarray = new JSONArray(response.toString());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                User user = new User();
                Gson gson = new Gson();
                user = gson.fromJson(jsonobject.toString(),User.class);
                Log.e("user" ,user.toString());
                userList.add(user);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("size",""+userList.size());
        for(User user : userList){
            Log.e("Converted : ",user.toString());
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Signed In Successful
                Toast.makeText(MainActivity.this, "Signed In!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in canceled by user
                Toast.makeText(MainActivity.this, "Sign In Canceled", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if(requestCode== RC_RESUME_PICKER && resultCode == RESULT_OK){
            Uri selectedResumeUri = data.getData();

            // Get a reference to store file  at chat_photo/<FILENAME>
            final StorageReference resRef = resumeStorage.child(user_email_id.replace('.',',')).child("Resume");

            // Upload file to Firebase Storage
            resRef.putFile(selectedResumeUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            String eml = user_email_id;
                            resume_link res = new resume_link(urlTask.getResult().toString());
                            dbref.child("StudentData").child(eml.replace('.',',')).child("resume").setValue(res);
                        }
                    });


        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_out_menu:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSignedInInitialized(String userid) {
        user_email_id = userid;
        //resumelisten();
        //attachDatabaseReadListner();
    }

    private void onSignOutCleanup() {
        user_email_id = null;
        //mMessageAdapter.clear();
        //detachDatabaseReadListener();

    }

    private void resumelisten(){
        String eml = user_email_id;
        dbref.child("StudentData").child(eml.replace('.',',')).child("resume").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                resume_link resl = dataSnapshot.getValue(resume_link.class);
                resume_link_url = resl.getLink();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //https://api.spoonacular.com/recipes/{id}/analyzedInstructions



}