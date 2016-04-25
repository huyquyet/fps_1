package framgia.vn.photo_sketch.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import framgia.vn.photo_sketch.R;
import framgia.vn.photo_sketch.activity.fragment.ViewImageFragment;

public class CombinePhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine_photo);
        setFragment();
    }

    private void setFragment() {
        Fragment fragmentViewImage = new ViewImageFragment();
//        Fragment fragmentViewImageCombine = new ViewImageCombineFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frameLayout_viewImage, fragmentViewImage);
//        fragmentTransaction.add(R.id.frameLayout_viewImageCombine, fragmentViewImageCombine);
        fragmentTransaction.commit();
    }
}
