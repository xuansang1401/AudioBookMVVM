package com.t3h.basemvvm.ui.category;

import android.util.Log;

import androidx.databinding.ObservableBoolean;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.model.api.CateModel;
import com.t3h.basemvvm.db.DatabaseDao;
import com.t3h.basemvvm.netword.AudioBookRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryViewModel extends ViewModel {
    private AudioBookRequest request;
    private DatabaseDao databaseDao;
    public @ViewModelInject CategoryViewModel(AudioBookRequest request,DatabaseDao databaseDao) {
        this.request = request;
        this.databaseDao=databaseDao;
    }

    public ObservableBoolean isLoad = new ObservableBoolean(false);

    public MutableLiveData<List<CateModel>> category = new MutableLiveData<>();//LiveData dữ liệu chạy theo vòng đơi của android
    public MutableLiveData<List<Book>> book = new MutableLiveData<>();
    public LiveData<List<Book>> historyAll = new MutableLiveData<>();
    private CompositeDisposable disposable= new CompositeDisposable();
    // gọi api lấy DL sử dụng LiveData
    public void getCategoryData() {
        disposable.add(request.getCategory()
                .subscribeOn(Schedulers.newThread())// chạy trên 1 thread mới
                .observeOn(AndroidSchedulers.mainThread())// chạy xong về main Thread
                .subscribe(data -> {
                            category.setValue(data);
                            isLoad.set(true);
                        }
                        , error -> {
                            Log.e("sangg", "Loi: " + error);
                            isLoad.set(true);
                        }
                ));

    }

    public void getBookByCategoryId(int id) {
        disposable.add( request.getCategory()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS)
                .subscribe(data -> book.setValue(data.get(id).getBooks())
                        , error -> Log.e("sangg", "Loi: " + error)
                ));
    }

    public void  getAllHistory(){
        historyAll=databaseDao.getAllHistory();

    }

    public void setDisposable(){
        disposable.clear();
    }
}