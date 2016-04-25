package framgia.vn.photo_sketch.activity.fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import framgia.vn.photo_sketch.R;
import framgia.vn.photo_sketch.activity.adapter.RecyclerViewImageAdapter;
import framgia.vn.photo_sketch.library.LoadPhoto;
import framgia.vn.photo_sketch.models.Photo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewImageFragment extends Fragment {
    private RecyclerView mRecyclerViewImage;
    private List<Photo> mListPhoto;
    private RecyclerViewImageAdapter recyclerViewImageAdapter;
    public int countImageChecked=0;
    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ViewImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewImageFragment newInstance(String param1, String param2) {
        ViewImageFragment fragment = new ViewImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_image, container, false);
        mListPhoto = LoadPhoto.loadPhotoPaths();
        mRecyclerViewImage = (RecyclerView) view.findViewById(R.id.recyclerView_image);
        mRecyclerViewImage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerViewImageAdapter = new RecyclerViewImageAdapter(mListPhoto);
        mRecyclerViewImage.setAdapter(recyclerViewImageAdapter);
//        ItemClickSupport.add(mRecyclerViewImage).set
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
