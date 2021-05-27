package com.t3h.basemvvm.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.room.History;
import com.t3h.basemvvm.databinding.HomeFragmentBinding;
import com.t3h.basemvvm.ui.adapter.HistoryAdapter;
import com.t3h.basemvvm.ui.adapter.HomeAdapter;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    private HomeFragmentBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= HomeFragmentBinding.inflate(inflater,container,false);
        binding.rcvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvHistory.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        mViewModel.getHistory();
        mViewModel.history.observe(getViewLifecycleOwner(), new Observer<List<History>>() {
            @Override
            public void onChanged(List<History> histories) {
                if (!histories.isEmpty()){
                    binding.tvHis.setVisibility(View.VISIBLE);
                    binding.rcvHistory.setVisibility(View.VISIBLE);
                    binding.rcvHistory.setAdapter(new HistoryAdapter(histories, getContext()));
                }else {
                    binding.tvHis.setVisibility(View.GONE);
                    binding.rcvHistory.setVisibility(View.GONE);
                }
            }
        });
        mViewModel.getList();
        mViewModel.getData().observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> books) {
                binding.rcvHome.setAdapter(new HomeAdapter(books,getContext()));
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