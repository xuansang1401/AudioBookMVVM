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
    private CategoryFragmentBinding binding;
    private CategoryAdapter adapter;
    private CompositeDisposable compositeDisposable;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CategoryFragmentBinding.inflate(inflater, container, false);
        binding.rcvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rcvCategory1.setLayoutManager(
                new StaggeredGridLayoutManager(3, RecyclerView.HORIZONTAL)
        );
        compositeDisposable = new CompositeDisposable();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        compositeDisposable.add(viewModel.getCategoryData());
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
        super.onDestroyView();
        compositeDisposable.dispose();
    }
}