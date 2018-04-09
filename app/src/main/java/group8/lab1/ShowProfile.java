package group8.lab1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import java.io.File;
import java.net.URI;

public class ShowProfile extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String PROFILE_PICTURE = "ProfilePicture";

    EditText name;
    EditText email;
    EditText biography;
    ImageView image;
    File directory;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        image = (ImageView) findViewById(R.id.image);
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        biography = (EditText) findViewById(R.id.bio);

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String name = prefs.getString("name", null);
        String email = prefs.getString("email",null);
        String biography = prefs.getString("biography", null);
        directory = getFilesDir();
        imageFile = new File(directory, PROFILE_PICTURE);

        if (name != null) {
             this.name.setText(name);
        }
        if (email != null){
            this.email.setText(email);
        }
        if (biography!=null){
            this.biography.setText(biography);
        }

        if (imageFile.exists()){
            this.image.setImageURI(Uri.fromFile(imageFile));
        }
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
            String imageUriString = data.getStringExtra("imageUri");

            if(stringName != null ) {
                name.setText(stringName);
            }
            if(stringEmail != null) {
                email.setText(stringEmail);
            }
            if(stringBiography != null) {
                biography.setText(stringBiography);
            }
            if(imageUriString!=null && imageUriString.equals("OK")) {
                recreate();
            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        if(!name.getText().toString().isEmpty()){
            editor.putString("name", name.getText().toString());
        }
        if(!email.getText().toString().isEmpty()){
            editor.putString("email", email.getText().toString());
        }
        if(!biography.getText().toString().isEmpty()){
            editor.putString("biography", biography.getText().toString());
        }
        editor.apply();
    }
}
