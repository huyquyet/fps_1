package framgia.vn.photo_sketch.library;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import framgia.vn.photo_sketch.bitmaputil.BitmapUtil;
import framgia.vn.photo_sketch.models.Photo;

/**
 * Created by hoada921 on 2016-04-25.
 */
public class LoadPhoto {
    private static List<Photo> mListPhoto;

    public static List<Photo> loadPhotoAll() {
        mListPhoto = new ArrayList<>();
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BitmapUtil.FOLDER_NAME;
        File file = new File(dirPath);
        if (file != null && file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                mListPhoto.add(new Photo(listFile[i].getAbsolutePath()));
            }
        }
        return mListPhoto;
    }

    public static List<Photo> loadPhotoPaths() {
        mListPhoto = new ArrayList<>();
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + BitmapUtil.FOLDER_NAME;
        File file = new File(dirPath);
        if (file != null && file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                mListPhoto.add(new Photo(listFile[i].getAbsolutePath()));
            }
        }
        return mListPhoto;
    }
}
