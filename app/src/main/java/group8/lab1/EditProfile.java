package group8.lab1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;


import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

public class EditProfile extends AppCompatActivity {

    public static final int REQUEST_GALLERY_PICTURE = 100;
    public static final int REQUEST_CAMERA_PICTURE = 101;
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

    }

    public void showPopup(View view){
        PopupMenu popupMenu = new PopupMenu(EditProfile.this, view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.show();
        
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_gallery:
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, REQUEST_GALLERY_PICTURE);
                        break;

                    case R.id.menu_camera:
                        Intent takePictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, REQUEST_CAMERA_PICTURE);
                        }
                }
                return true;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == REQUEST_GALLERY_PICTURE ) {
                Uri imageUri = data.getData();
                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .setMinCropResultSize(512,512)
                        .start(this);
            }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri imageUri = result.getUri();
                imageCacheFile = new File(imageUri.getPath());
                image.setImageURI(imageUri);
            }else if (requestCode == REQUEST_CAMERA_PICTURE){

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
