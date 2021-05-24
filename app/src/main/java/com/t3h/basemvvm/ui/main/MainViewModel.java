package com.t3h.basemvvm.ui.main;

import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.ViewModel;

import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.room.History;
import com.t3h.basemvvm.db.DatabaseDao;
import com.t3h.basemvvm.netword.AudioBookRequest;

import java.util.Date;

public class MainViewModel extends ViewModel {
    private AudioBookRequest request;
    private DatabaseDao databaseRetrofit;
    public @ViewModelInject MainViewModel(
            AudioBookRequest audioBookRequest,
            DatabaseDao databaseRetrofit) {
        this.request = audioBookRequest;
        this.databaseRetrofit=databaseRetrofit;
    }

    public String milisToString(long milis) {
        long min = milis / 60000;
        long second = (milis - min * 60000) / 1000;
        long hour = min / 60;
        min = min - (hour * 60);

        if (min < 10) {
            if (hour > 0) {
                if (second < 10) {
                    return hour + ":0" + min + ":0" + second;
                } else {
                    return hour + ":0" + min + ":" + second;
                }
            } else {
                if (second < 10) {
                    return "0" + min + ":0" + second;
                } else {
                    return "0" + min + ":" + second;
                }
            }


        } else {
            if (hour > 0) {
                if (second < 10) {
                    return hour + ":" + min + ":0" + second;
                } else {
                    return hour + ":" + min + ":" + second;
                }
            } else {
                if (second < 10) {
                    return min + ":0" + second;
                } else {
                    return min + ":" + second;
                }
            }
        }
    }

    public void addHistoryBook(Book book) {
        History history= new History("H"+book.getId(),book,new Date().getTime());
        databaseRetrofit.addDbHis(history);
    }
}