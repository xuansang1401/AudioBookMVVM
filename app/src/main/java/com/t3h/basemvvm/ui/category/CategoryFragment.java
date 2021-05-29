package com.t3h.basemvvm.ui.category;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.CateModel;
import com.t3h.basemvvm.data.model.api.CategoryModel;
import com.t3h.basemvvm.databinding.BookFragmentBinding;
import com.t3h.basemvvm.databinding.CategoryFragmentBinding;
import com.t3h.basemvvm.databinding.CategoryFragmentBindingImpl;
import com.t3h.basemvvm.ui.adapter.CategoryAdapter;
import com.t3h.basemvvm.ui.adapter.CategoryAdapter1;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

@AndroidEntryPoint
public class CategoryFragment extends Fragment {

    private CategoryViewModel viewModel;
    private CategoryFragmentBinding binding;// Databinding lấy layout
    private CategoryAdapter adapter;
    // láy dữ liệu từ sever về qua Retrofit trả về 1 Osevelist CateMode truyền vào LIveData để hiện thị dữ liệu bằng RCV

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CategoryFragmentBinding.inflate(inflater, container, false);// khởi tạo layout

        //List item
        binding.rcvCategory.setLayoutManager(new LinearLayoutManager(getContext()));//Item dọc thẳng xuống
        binding.rcvCategory1.setLayoutManager(
                new StaggeredGridLayoutManager(3, RecyclerView.HORIZONTAL)// định dạng RCV lướt ntn
        );
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);// khởi tạo VM
        //láy dữ liệu từ server
        viewModel.getCategoryData();

        // chuyển dữ liệu từ LiveData sang View
        viewModel.category.observe(getViewLifecycleOwner(), new Observer<List<CateModel>>() {
            @Override
            public void onChanged(List<CateModel> categoryModels) {

                binding.rcvCategory1.setAdapter(new CategoryAdapter1(categoryModels, getContext()));
                adapter = new CategoryAdapter(categoryModels, getContext());
                binding.rcvCategory.setAdapter(adapter);

            }
        });
        binding.setIsLoading(viewModel.isLoad);
    }

    @Override
    public void onDestroyView() {
        viewModel.setDisposable();
        super.onDestroyView();
    }
}