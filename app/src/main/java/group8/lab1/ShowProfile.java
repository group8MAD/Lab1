package group8.lab1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import java.net.URI;

public class ShowProfile extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText biography;
    ImageView image;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        image = (ImageView) findViewById(R.id.image);
        name = (EditText) findViewById(R.id.name);
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
            String imageUriString = data.getStringExtra("imageUri");

            if(imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
                image.setImageURI(imageUri);
            }
            if(stringName != null) {
                name.setText(stringName);
            }
            if(stringEmail != null) {
                email.setText(stringEmail);
            }
            if(stringBiography != null) {
                biography.setText(stringBiography);
            }


        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", name.getText().toString());
        outState.putString("email", email.getText().toString());
        outState.putString("biography", biography.getText().toString());
        if (imageUri != null)
            outState.putString("imageUri", imageUri.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        name.setText(savedInstanceState.getString("name"));
        email.setText(savedInstanceState.getString("email"));
        biography.setText(savedInstanceState.getString("biography"));
        String imageUriString = savedInstanceState.getString("imageURi");

            if(imageUriString != null){
                imageUri = Uri.parse(imageUriString);
                image.setImageURI(imageUri);
            }

    }
}
