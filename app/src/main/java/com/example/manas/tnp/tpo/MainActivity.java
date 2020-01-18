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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.example.manas.tnp.tpo.dtos.EquipmentsDto;
import com.example.manas.tnp.tpo.dtos.FinalResponse;
import com.example.manas.tnp.tpo.dtos.IngredientsDto;
import com.example.manas.tnp.tpo.dtos.Instruction;
import com.example.manas.tnp.tpo.dtos.RecipeSearchResponseDto;
import com.example.manas.tnp.tpo.dtos.SearchResponse;
import com.example.manas.tnp.tpo.dtos.StepsDto;

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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    public SearchResponse searchResponse;
    public RecipeSearchResponseDto recipeSearchResponseDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //spoonacular
        callService();


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
                                    .setLogo(R.drawable.foodlogo)
                                    .build(), RC_SIGN_IN);
                }
            }
        };

        editButton = findViewById(R.id.find_recipies);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editIntent = new Intent(MainActivity.this, EditDetailsActivity.class);
                editIntent.putExtra("user_email",user_email_id);
                startActivity(editIntent);
            }
        });


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
            //TODO Not in Use
            /**
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
            **/
        }



    }

    public void callService(){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spoonacular.com/recipes/search").newBuilder();
        urlBuilder.addQueryParameter("apiKey", "8917e952b5074395856aea4402376c8a");
        urlBuilder.addQueryParameter("query", "bhindi");
        urlBuilder.addQueryParameter("number","1");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error : ", "error while making call to api 1");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                  //  Log.e("Response 1",response.body().string());
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Gson gson = new Gson();
                         searchResponse = gson.fromJson(jsonObject.toString(),SearchResponse.class);
                         callReceipeService(searchResponse.getResults().get(0).getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void callReceipeService(int id){
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.spoonacular.com/recipes/"+String.valueOf(id)+"/analyzedInstructions").newBuilder();
        urlBuilder.addQueryParameter("apiKey", "8917e952b5074395856aea4402376c8a");
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error : ", "error while making call to api 2");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    //Log.e("Response 2",response.body().string());
                    try {
                        JSONArray jsonarray = new JSONArray(response.body().string());
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            Gson gson = new Gson();
                            recipeSearchResponseDto = gson.fromJson(jsonobject.toString(), RecipeSearchResponseDto.class);
                            FinalResponse finalResponse = getFinalResponse(recipeSearchResponseDto);
                            Log.e("Test: ", finalResponse.getInstructions().get(0).getIngredients());
                        }
                    } catch (JSONException e) {
                        Log.e("Error  ", e.getMessage());
                    }

                }
            }
        });
    }

    //TODO not in use
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
/**
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

    private FinalResponse getFinalResponse(RecipeSearchResponseDto recipeSearchResponseDto) {

        Log.e("First :", recipeSearchResponseDto.toString());
        FinalResponse finalResponse = new FinalResponse();
        finalResponse.setReadyInMinutes(searchResponse.getResults().get(0).getReadyInMinutes());
        Log.e("Second :", String.valueOf(finalResponse.getReadyInMinutes()));
        finalResponse.setSteps(getSteps(recipeSearchResponseDto));
        finalResponse.setInstructions(getInstructions(recipeSearchResponseDto));
        finalResponse.setIngredients(getFinalIngredients(finalResponse.getInstructions()));
        Log.e("thrid: ", finalResponse.getIngredients());
        return finalResponse;
    }


    private List<String> getSteps(RecipeSearchResponseDto recipeSearchResponseDto) {
        List<String> steps = new ArrayList<>();
        for (StepsDto step: recipeSearchResponseDto.getSteps()) {
            steps.add(step.getStep());
        }
        return steps;
    }

    private List<Instruction> getInstructions(RecipeSearchResponseDto recipeSearchResponseDto) {
        List<Instruction> instructionList = new ArrayList<>();
        for (StepsDto step: recipeSearchResponseDto.getSteps()) {
            Instruction instruction = new Instruction();
            instruction.setStep(step.getStep());
            instruction.setLength(step.getLength().getNumber()  + " " + step.getLength().getUnit());
            instruction.setIngredients(getIngredients(step.getIngredients()));
            instruction.setEquipments(getEquipments(step.getEquipment()));
            instructionList.add(instruction);
        }
        return instructionList;
    }

    private String getIngredients(List<IngredientsDto> ingredients) {
        String response = "";
        for (IngredientsDto i: ingredients) {
            response = response.concat(i.getName()+ ", ");
        }
        return response.length()>1? response.substring(0, response.length()-2): response;
    }

    private String getEquipments(List<EquipmentsDto> equipments) {
        String response = "";
        for(EquipmentsDto e: equipments) {
            response = response.concat(e.getName()+ ", ");
        }
        return response.length()>1? response.substring(0, response.length()-2): response;
    }

    private String getFinalIngredients(List<Instruction> instructions) {
        String response = "";
        for(Instruction e: instructions) {
            response = response.concat(e.getIngredients()+ ", ");
        }
        return response.length()>1? response.substring(0, response.length()-2): response;
    }

 **/
}