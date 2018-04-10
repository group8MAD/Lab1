package group8.lab1;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    public static final int REQUEST_PERMISSIONS = 200;
    public static final String PROFILE_PICTURE = "ProfilePicture";

    EditText name;
    EditText email;
    EditText biography;
    ImageButton image;
    File directory;
    File imageCacheFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        biography = findViewById(R.id.bio);
        image = findViewById(R.id.image);

        directory = getFilesDir();

        String nameString = getIntent().getStringExtra("name");
        String emailString = getIntent().getStringExtra("email");
        String bioString = getIntent().getStringExtra("bio");

        if(nameString!=null)
            name.setText(nameString);
        if(emailString!=null)
            email.setText(emailString);
        if(bioString!=null)
            biography.setText(bioString);

        image.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (checkAndRequestPermissions()) {

                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setMinCropResultSize(512, 512)
                        .start(EditProfile.this);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getApplicationContext().getString(R.string.grantedPermission), Toast.LENGTH_LONG).show();
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(EditProfile.this);
            } else {
                Toast.makeText(this, getApplicationContext().getString(R.string.deniedPermission), Toast.LENGTH_LONG).show();
            }
        }
    }
    private  boolean checkAndRequestPermissions() {
        int readExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<String>();

        if (readExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (writeExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri imageUri = result.getUri();
                imageCacheFile = new File(imageUri.getPath());
                image.setImageURI(imageUri);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageCacheFile!=null){
            File imageFileTmp = new File(directory,"ProfilePictureTmp");
            if(imageCacheFile.renameTo(imageFileTmp))
                outState.putString("imageSaved","YES");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString("imageSaved")!=null) {
            imageCacheFile = new File(directory, "ProfilePictureTmp");
            image.setImageURI(Uri.fromFile(imageCacheFile));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, ShowProfile.class);
        if (!name.getText().toString().isEmpty())
            intent.putExtra("name", name.getText().toString());
        if (!email.getText().toString().isEmpty())
           intent.putExtra("email", email.getText().toString());
        if (!biography.getText().toString().isEmpty())
            intent.putExtra("biography", biography.getText().toString());
        if (imageCacheFile!=null ){
            File fileDest = new File(directory, PROFILE_PICTURE);
            if (imageCacheFile.renameTo(fileDest)) {
                intent.putExtra("imageUri", "OK");
            }
        }
        setResult(RESULT_OK, intent);
        finish();
        return true;
    }

}
