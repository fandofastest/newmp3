package uiux.design.bottomnavigation.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.utils.Tools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context ctx;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ctx=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // display image
        Tools.displayImageOriginal(ctx, (ImageView)view.findViewById(R.id.image_1), R.drawable.image_8);
        Tools.displayImageOriginal(ctx, (ImageView) view.findViewById(R.id.image_2), R.drawable.image_9);
        Tools.displayImageOriginal(ctx, (ImageView) view.findViewById(R.id.image_3), R.drawable.image_15);
        Tools.displayImageOriginal(ctx, (ImageView) view.findViewById(R.id.image_4), R.drawable.image_14);
        Tools.displayImageOriginal(ctx, (ImageView) view.findViewById(R.id.image_5), R.drawable.image_12);
        Tools.displayImageOriginal(ctx, (ImageView) view.findViewById(R.id.image_6), R.drawable.image_2);
        Tools.displayImageOriginal(ctx, (ImageView) view.findViewById(R.id.image_7), R.drawable.image_5);
    }
}