package group8.lab1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toolbar;

public class ShowProfile extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText biography;
    String stringName;
    String stringEmail;
    String stringBiography;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        name = (EditText) findViewById(R.id.name);
        name.setText(stringName);
        email = (EditText) findViewById(R.id.email);
        biography = (EditText) findViewById(R.id.bio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, EditProfile.class);
        startActivityForResult(intent, 0);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 0) {

            String stringName = data.getStringExtra("name");
            String stringEmail = data.getStringExtra("email");
            String stringBiography = data.getStringExtra("biography");
            if(!stringName.isEmpty()) {
                name.setText(stringName);
            }
            if(!stringEmail.isEmpty()) {
                email.setText(stringEmail);
            }
            if(!stringBiography.isEmpty()) {
                biography.setText(stringBiography);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        stringName = savedInstanceState.getString("name");
        name.setText(stringName);
    }
}
