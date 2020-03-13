package me.viewmodelpass;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.viewmodelpass.databinding.FragmentBlankBinding;


public class BlankFragment extends Fragment {
    private static final String ARG_NAME = "name";
    private static final String ARG_CONTENT = "content";

    private String name = "";
    private String content = "";
    private FragmentBlankBinding binding;
    MeViewModel viewModel;
    private int visibleCount = 0;

    public BlankFragment() {
        // Required empty public constructor
    }


    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, param1);
        args.putString(ARG_CONTENT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            content = getArguments().getString(ARG_CONTENT);
        }
        if (getActivity()!=null) {
            viewModel = new ViewModelProvider(getActivity()).get(MeViewModel.class);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        visibleCount++;
        postCount();
    }


    private void postCount(){
        if (viewModel!=null){
            viewModel.content.postValue(name.concat(" ").concat(String.valueOf(visibleCount)));
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_blank,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.centerTv.setText(content);
        binding.topTv.setText(name);
    }
}
