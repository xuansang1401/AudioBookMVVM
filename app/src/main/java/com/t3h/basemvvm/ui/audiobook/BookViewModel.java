package com.t3h.basemvvm.ui.audiobook;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.room.Favorite;
import com.t3h.basemvvm.data.room.History;
import com.t3h.basemvvm.db.DatabaseDao;
import com.t3h.basemvvm.netword.AudioBookRequest;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BookViewModel extends ViewModel {
    public CompositeDisposable disposable= new CompositeDisposable();
    private AudioBookRequest request;
    private DatabaseDao dao;
    public  @ViewModelInject BookViewModel(
            AudioBookRequest request,
            DatabaseDao dao) {
        this.request = request;
        this.dao= dao;
    }
    private MutableLiveData<Book> bookData=new MutableLiveData<>();
    public ObservableBoolean isLoad = new ObservableBoolean(false);
    public void addFav(Book book){
        dao.addFav(new Favorite("F"+book.getId(),book, new Date().getTime()));
    }
    public boolean setIsFavorite(String id){
        List<Favorite> list=dao.findFavById(id);
        if (!list.isEmpty()) return true;
        return false;
    }
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

    public void deleteFav(Book book) {
        dao.deleteFav("F"+book.getId());
    }
}