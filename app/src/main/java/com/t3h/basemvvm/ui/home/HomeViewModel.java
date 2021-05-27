package com.t3h.basemvvm.ui.home;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.room.History;
import com.t3h.basemvvm.db.DatabaseDao;
import com.t3h.basemvvm.netword.AudioBookRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {
    private AudioBookRequest request;
    private DatabaseDao dao;

    public @ViewModelInject
    HomeViewModel(AudioBookRequest audioBookRequest, DatabaseDao dao) {
        this.request = audioBookRequest;
        this.dao = dao;
    }

    public LiveData<List<History>> history = new MutableLiveData<>();
    private MutableLiveData<List<Book>> listMutableLiveData = new MutableLiveData<>();
    public ObservableBoolean isLoad = new ObservableBoolean(false);
    public LiveData<List<Book>> historyAll = new MutableLiveData<>();
    private CompositeDisposable disposable= new CompositeDisposable();
    public void setDisposable(){
        disposable.clear();
    }
    public void getList() {
        disposable.add( request.getListBook()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .timeout(10, TimeUnit.SECONDS)
                .subscribe(data -> {
                            listMutableLiveData.setValue(data);
                            isLoad.set(true);
                        }
                        , error -> {
                            Log.e("sangg", "Loi: " + error);
                            isLoad.set(true);
                        }
                ));
    }

    public MutableLiveData<List<Book>> getData() {
        return listMutableLiveData;
    }

    public void getHistory() {
        history = dao.getThreeHistory();
    }


}