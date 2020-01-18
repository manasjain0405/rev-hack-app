package com.example.manas.tnp.tpo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;


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
    private ListView steps;
    private TextView ingredients;

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
                steps=(ListView)findViewById(R.id.Steps);
                RecipeDetails student_det = new RecipeDetails(recipeName.getText().toString());
                Toast.makeText(EditDetailsActivity.this, "Searching recipie", Toast.LENGTH_SHORT).show();

                //Intent intent = new Intent(EditDetailsActivity.this, MainActivity.class);
                //startActivity(intent);
            }
        });
        String ingre="Ingredients are :";
        ingredients=(TextView)findViewById(R.id.ingredients);
        ingredients.setText(ingre);
        List<String> your_array_list = new ArrayList<String>();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,your_array_list);
        steps.setAdapter(arrayAdapter);
    }
}





