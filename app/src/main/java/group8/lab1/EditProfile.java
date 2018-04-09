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

public class EditProfile extends AppCompatActivity {
    public static final int REQUEST_CAMERA_PERMISSION = 200;
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
        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        biography = (EditText) findViewById(R.id.bio);
        image = (ImageButton) findViewById(R.id.image);

        directory = getFilesDir();
        image.setOnClickListener(myListener);
    }

    private View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (ContextCompat.checkSelfPermission(EditProfile.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(EditProfile.this,new String[]{Manifest.permission.CAMERA},REQUEST_CAMERA_PERMISSION);
            }else{
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(EditProfile.this);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getApplicationContext().getString(R.string.grantedPermission), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, getApplicationContext().getString(R.string.deniedPermission), Toast.LENGTH_LONG).show();
            }
        }
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
