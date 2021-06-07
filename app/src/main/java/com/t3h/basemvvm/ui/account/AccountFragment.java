package com.t3h.basemvvm.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.t3h.basemvvm.R;
import com.t3h.basemvvm.databinding.AccountFragmentBinding;
import com.t3h.basemvvm.ui.login.LoginActivity;
import com.t3h.basemvvm.ui.login.LoginViewModel;
import com.t3h.basemvvm.ui.main.MainActivityJava;
import com.t3h.basemvvm.ui.main.SplashActivity;
import com.t3h.basemvvm.util.AppUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AccountFragment extends Fragment {
    private LoginViewModel mViewModel;
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
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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
        binding.tvNameUser.setText(mViewModel.getAppConfig().getNameUser());
        AppUtil.imageFromUrl(binding.imageUser, mViewModel.getAppConfig().getAvatarUser());
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.getAppConfig().setIsLogin(false);
                FirebaseAuth.getInstance().signOut();
                openLoginActivity();
            }
        });
    }

    private void openLoginActivity() {
        Intent intent= new Intent(getContext(), LoginActivity.class);
        startActivity(intent);

    }

}