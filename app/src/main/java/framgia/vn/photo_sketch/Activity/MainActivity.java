package framgia.vn.photo_sketch.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

import framgia.vn.photo_sketch.Constants.ConstActivity;
import framgia.vn.photo_sketch.R;

public class MainActivity extends AppCompatActivity implements ConstActivity {
    private Animation mAnimation;
    private RelativeLayout mRelativeCamera;
    private RelativeLayout mRelativeGallery;
    private RelativeLayout mStepNumber;
    private boolean mCheckStatus = true;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getControl();
        setEvents();
    }

    @Override
    protected void onStart() {
        overridePendingTransition(0, 0);
        flyIn();
        super.onStart();
    }

    @Override
    protected void onStop() {
        overridePendingTransition(0, 0);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void flyIn() {
        mCheckStatus = true;
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.top_holder_in);
        mRelativeCamera.startAnimation(mAnimation);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_holder_in);
        mRelativeGallery.startAnimation(mAnimation);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.step_number_in);
        mStepNumber.startAnimation(mAnimation);
    }

    private void flyOut() {
        if (!mCheckStatus) return;
        mCheckStatus = true;
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.top_holder_out);
        mRelativeCamera.startAnimation(mAnimation);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_holder_out);
        mRelativeGallery.startAnimation(mAnimation);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.step_number_out);
        mStepNumber.startAnimation(mAnimation);
    }

    private void getControl() {
        mRelativeCamera = (RelativeLayout) findViewById(R.id.relative_camera);
        mRelativeGallery = (RelativeLayout) findViewById(R.id.relative_gallery);
        mStepNumber = (RelativeLayout) findViewById(R.id.step_number);
    }

    private void setEvents() {
        mRelativeCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyOut();
                displayCamera();
            }
        });
        mRelativeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flyOut();
                displayGallery();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (!(resultCode == RESULT_OK)) {
            Toast.makeText(this, R.string.not_found_photo, Toast.LENGTH_LONG).show();
            return;
        }
        if (requestCode == REQUEST_CAMERA) displayPhotoActivity();
        else if (requestCode == REQUEST_GALLERY) {
            mUri = data.getData();
            displayPhotoActivity();
        }
    }

    private void displayPhotoActivity() {
        Intent intent = new Intent(this, PhotoActivity.class);
        intent.setData(mUri);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void displayCamera() {
        mUri = getOutputMediaFile();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void displayGallery() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_CHECKING)) {
            Intent intent = new Intent();
            intent.setType("image/jpeg");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, REQUEST_GALLERY);
        }
    }

    private Uri getOutputMediaFile() {
        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.TITLE, "Camera Pro");
//        values.put(MediaStore.Images.Media.DESCRIPTION, "www.appsroid.org");
        return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}
