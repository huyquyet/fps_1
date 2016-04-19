package framgia.vn.photo_sketch.asynctask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import framgia.vn.photo_sketch.R;
import framgia.vn.photo_sketch.bitmaputil.BitmapUtil;
import framgia.vn.photo_sketch.models.Effect;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 19/04/2016.
 */
public class ApplyEffectAsync extends AsyncTask<Effect, Void, Bitmap> {
    private Activity mContext;
    private ImageView mImageView;
    private Bitmap mBitmap;

    public ApplyEffectAsync(Activity context, Bitmap bitmap) {
        this.mContext = context;
        this.mBitmap = bitmap;
        mImageView = (ImageView) mContext.findViewById(R.id.source_image);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(Effect... params) {
        return BitmapUtil.hue(mBitmap, params[0].getValue());
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        }
        super.onPostExecute(bitmap);
    }
}
