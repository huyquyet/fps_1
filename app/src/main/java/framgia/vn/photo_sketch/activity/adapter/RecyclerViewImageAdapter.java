package framgia.vn.photo_sketch.activity.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import framgia.vn.photo_sketch.R;
import framgia.vn.photo_sketch.models.Photo;

/**
 * Created by hoada921 on 2016-04-25.
 */
public class RecyclerViewImageAdapter extends RecyclerView.Adapter<RecyclerViewImageAdapter.ViewHolder> {
    private List<Photo> mListPhoto;
    private Context mContext;
    private int mCountChecked = 0;

    public RecyclerViewImageAdapter(List<Photo> listPhoto) {
        mListPhoto = listPhoto;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_fragment_view_image, null);
        ViewHolder holder = new ViewHolder(mContext,view);
//        holder.checkBoxImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (mCountChecked > 6)
////                    Toast.makeText(parent.getContext(), "", Toast.LENGTH_SHORT).show();
////                else
////                    mCountChecked++;
//                Toast.makeText(parent.getContext(), "select" + v.getId(), Toast.LENGTH_SHORT).show();
//                Log.d("check", String.valueOf(v.getId()));
//            }
//        });
//        holder.imageViewImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(parent.getContext(), "image" + v.getId(), Toast.LENGTH_SHORT).show();
//                Log.d("image", String.valueOf(v.getId()));
//            }
//        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Photo photo = mListPhoto.get(position);
        Uri uri = Uri.fromFile(new File(photo.getUri()));
        Picasso.with(mContext).load(uri)
                .resize(240, 240)
                .centerCrop()
                .into(holder.imageViewImage);
    }

    @Override
    public int getItemCount() {
        return mListPhoto.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageViewImage;
        private CheckBox checkBoxImage;
        private Context context;

        public ViewHolder(Context context,View itemView) {
            super(itemView);
            imageViewImage = (ImageView) itemView.findViewById(R.id.imageView_item_fragment_view_image);
            checkBoxImage = (CheckBox) itemView.findViewById(R.id.checkBox_item_fragment_view_image);
            // Store the context
            this.context = context;
            // Attach a click listener to the entire row view
            imageViewImage.setOnClickListener(this);
            checkBoxImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
//            Toast.makeText(context, "image" + v.getId(), Toast.LENGTH_SHORT).show();
            switch (v.getId()) {
                case R.id.imageView_item_fragment_view_image:
                    Toast.makeText(context, "image" + position, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.checkBox_item_fragment_view_image:
                    Toast.makeText(context, "select" + position, Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }
}
