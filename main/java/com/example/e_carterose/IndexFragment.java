package com.example.e_carterose;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.e_carterose.databinding.FragmentIndexBinding;

public class IndexFragment extends Fragment {

    private FragmentIndexBinding binding;

    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentIndexBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.ButtonEleveur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Créer une instance de ConnexionEleveurFragment
                ConnexionEleveurFragment connexionEleveurFragment = ConnexionEleveurFragment.newInstance();

                // Remplacer le fragment actuel par le fragment ConnexionEleveurFragment
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, connexionEleveurFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }


}