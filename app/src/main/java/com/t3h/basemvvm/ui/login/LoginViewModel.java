package com.t3h.basemvvm.ui.login;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.room.History;
import com.t3h.basemvvm.db.DatabaseDao;
import com.t3h.basemvvm.netword.AudioBookRequest;
import com.t3h.basemvvm.util.AppConfig;

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private AudioBookRequest request;
    private AppConfig appConfig;

    public @ViewModelInject
    LoginViewModel(AudioBookRequest audioBookRequest, AppConfig appConfig) {
        this.request = audioBookRequest;
        this.appConfig = appConfig;
    }



    public AppConfig getAppConfig(){
        return appConfig;
    }

    public void addUserGoogle(FirebaseUser user) {

        appConfig.setNameUser(Objects.requireNonNull(user.getDisplayName()));
        appConfig.setAvatarUser(Objects.requireNonNull(user.getPhotoUrl()).toString());
        appConfig.setIsLogin(true);
    }
}