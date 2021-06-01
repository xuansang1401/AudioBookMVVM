package com.t3h.basemvvm.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.databinding.FragmentSearchBinding;
import com.t3h.basemvvm.ui.adapter.CategoryAdapter;

import com.t3h.basemvvm.ui.adapter.TrendingAdapter;

import com.t3h.basemvvm.ui.trending.TrendingViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchFragment extends Fragment {
    private TrendingViewModel viewModel;
    private FragmentSearchBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);// khởi tạo layout

        //List item
        binding.rcvSearch.setLayoutManager(new LinearLayoutManager(getContext()));//Item dọc thẳng xuống
        binding.searchView.setFocusable(true);
        binding.searchView.requestFocus();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TrendingViewModel.class);// khởi tạo VM
        //láy dữ liệu từ server
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.isEmpty()){
                    return false;
                }
                Log.e("Sang", "Search: "+query);
                onSearchText(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    return false;
                }
                Log.e("Sang", "Search: "+newText);
                onSearchText(newText);
                return false;
            }
        });

        // chuyển dữ liệu từ LiveData sang View
        viewModel.searchData.observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
            @Override
            public void onChanged(List<Book> categoryModels) {
                binding.rcvSearch.setAdapter(new TrendingAdapter(categoryModels,getContext()));
                binding.rcvSearch.getAdapter().notifyDataSetChanged();
            }
        });
   }

    private void onSearchText(String query) {
        viewModel.searchData(query);
    }
}
