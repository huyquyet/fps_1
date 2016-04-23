package framgia.vn.photo_sketch.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import framgia.vn.photo_sketch.R;
import framgia.vn.photo_sketch.bitmaputil.BitmapUtil;
import framgia.vn.photo_sketch.constants.ConstActivity;

public class ViewListPhotoActivity extends AppCompatActivity implements ConstActivity {
    private int count;
    private Bitmap[] thumbnails;
    private boolean[] thumbnailsselection;
    private String[] arrPath;
    GridView imageGrid;
    private ImageAdapter imageAdapter;
    List<String> f = new ArrayList<String>();// list of file paths
    File[] listFile;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_photo);
//        getFromSdcard();
        imageGrid = (GridView) findViewById(R.id.PhoneImageGrid);
        imageAdapter = new ImageAdapter();
        imageGrid.setAdapter(imageAdapter);
        AsyncDisplayImage loadfile = new AsyncDisplayImage();
        loadfile.execute();
    }

    public class ImageAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ImageAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public int getCount() {
            return f.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gelleryitem, null);
                holder.imageview = (ImageView) convertView.findViewById(R.id.thumbImage);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            Bitmap myBitmap = BitmapFactory.decodeFile(f.get(position));
            Bitmap myBitmap = null;
            try {
                myBitmap = BitmapUtil.createThumbnail(ViewListPhotoActivity.this, f.get(position), 80, 80);
            } catch (Exception e) {
                e.printStackTrace();
            }
            holder.imageview.setImageBitmap(myBitmap);
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageview;
    }

    private class AsyncDisplayImage extends AsyncTask<Void, String, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            File file = new File(android.os.Environment.getExternalStorageDirectory(), FOLDER_NAME);
            if (file.isDirectory()) {
                listFile = file.listFiles();
                for (File list : listFile) {
                    publishProgress(list.getAbsolutePath());
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            f.add(values[0]);
            imageAdapter.notifyDataSetChanged();
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
