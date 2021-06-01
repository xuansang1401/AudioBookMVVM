package com.t3h.basemvvm.ui.account;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.databinding.AccountFragmentBinding;

public class AccountFragment extends Fragment {
    private AccountViewModel mViewModel;
    private AccountFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= AccountFragmentBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        binding.btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections  directions= AccountFragmentDirections.Companion.actionAccountFragmentToCategoryDetailFragment(
                        101,"Lịch sử"
                );
                Navigation.findNavController(v).navigate(directions);
            }
        });
        binding.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections  directions= AccountFragmentDirections.Companion.actionAccountFragmentToCategoryDetailFragment(
                        201,"Yêu thích"
                );
                Navigation.findNavController(v).navigate(directions);
            }
        });
    }

}