package com.t3h.basemvvm.ui.trending;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.netword.AudioBookRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrendingViewModel extends ViewModel {
    private AudioBookRequest request;
    public ObservableBoolean isLoad = new ObservableBoolean(false);

    public @ViewModelInject
    TrendingViewModel(AudioBookRequest audioBookRequest) {
        this.request = audioBookRequest;
    }

    public MutableLiveData<List<Book>> trendingData = new MutableLiveData<>();//LiveData
    private CompositeDisposable disposable= new CompositeDisposable();
    public void setDisposable(){
        disposable.clear();
    }
    public void getCategoryData() {
        disposable.add( request.getTrending()//rx android
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .timeout(10, TimeUnit.SECONDS)
                .subscribe(data -> {
                            trendingData.postValue(data);// add data vào LiveData trending
                            isLoad.set(true);
                        }
                        , error -> {
                            Log.e("sangg", "Loi: " + error);
                            isLoad.set(true);
                        }
                ));

    }

}