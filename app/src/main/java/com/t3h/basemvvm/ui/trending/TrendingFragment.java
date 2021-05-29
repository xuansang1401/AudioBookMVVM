package com.t3h.basemvvm.ui.trending;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.databinding.TrendingFragmentBinding;
import com.t3h.basemvvm.ui.adapter.BookAdapter;
import com.t3h.basemvvm.ui.adapter.TrendingAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrendingFragment extends Fragment {

    private TrendingViewModel mViewModel;

    private TrendingFragmentBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // xác định view
        binding=TrendingFragmentBinding.inflate(inflater, container,false);
        // xác định layout cảu RecyclerView
        binding.rcvTrending.setLayoutManager(new LinearLayoutManager(getContext()));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //load data

        //Khởi tạo ViewmOdel
        mViewModel = new ViewModelProvider(this).get(TrendingViewModel.class);

        //lay dữ liệu từ server
        mViewModel.getCategoryData();
        // hiện thị lên view
        mViewModel.trendingData.observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {

                binding.rcvTrending.setAdapter(new TrendingAdapter(books,getContext()));
            }
        });
        binding.setIsLoading(mViewModel.isLoad);
    }

    @Override
    public void onDestroyView() {
        mViewModel.setDisposable();
        super.onDestroyView();
    }
}