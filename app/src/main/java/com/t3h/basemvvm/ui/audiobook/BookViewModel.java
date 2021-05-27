package com.t3h.basemvvm.ui.audiobook;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.netword.AudioBookRequest;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookViewModel extends ViewModel {
    public CompositeDisposable disposable= new CompositeDisposable();
    private AudioBookRequest request;
    public  @ViewModelInject BookViewModel(AudioBookRequest request) {
        this.request = request;
    }
    private MutableLiveData<Book> bookData=new MutableLiveData<>();
    public ObservableBoolean isLoad = new ObservableBoolean(false);

    public void getBookByID(String id) {
        disposable.add(
                request.getBookById(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
//                .timeout(10, TimeUnit.SECONDS)
                .subscribe(data ->{
                            bookData.setValue(data);
                            isLoad.set(true);
                        }
                        , error -> {
                    Log.e("sangg", "Loi: " + error);
                    isLoad.set(true);
                        }
                ));

    }

    public MutableLiveData<Book> getData(){
        return bookData;
    }

    public void  setDisposable(){
        disposable.clear();
    }
}