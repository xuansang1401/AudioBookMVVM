package com.t3h.basemvvm.ui.category;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.databinding.CategoryFragmentBinding;
import com.t3h.basemvvm.databinding.CatgoryDetailFragmentBinding;
import com.t3h.basemvvm.ui.adapter.BookAdapter;
import com.t3h.basemvvm.ui.adapter.TrendingAdapter;
import com.t3h.basemvvm.ui.audiobook.BookFragmentArgs;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CategoryDetailFragment extends Fragment {

    private CategoryDetailFragmentArgs args;
    private CategoryViewModel mViewModel;
    private CatgoryDetailFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CatgoryDetailFragmentBinding.inflate(inflater, container, false);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        binding.rcvBook.setLayoutManager(
                new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        args= CategoryDetailFragmentArgs.fromBundle(getArguments());
        mViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        binding.tvTitle.setText(args.getNameCategory());
        int id= args.getIdCategory()-1;
        if (id==100){
            mViewModel.getAllHistory();
            mViewModel.historyAll.observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
                @Override
                public void onChanged(List<Book> histories) {
                    setAdapterList(histories);
                }
            });
        } if (id==200){
            mViewModel.getAllFavorite();
            mViewModel.favoriteAll.observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
                @Override
                public void onChanged(List<Book> favorite) {
                    setAdapterList(favorite);
                }
            });
        } else{
            mViewModel.getBookByCategoryId(id);
            mViewModel.book.observe(getViewLifecycleOwner(), new Observer<List<Book>>() {
                @Override
                public void onChanged(List<Book> books) {
                    setAdapterList(books);
                }
            });
        }
    }


    private void setAdapterList(List<Book> books){
        binding.rcvBook.setAdapter(new BookAdapter(books,getContext(),1));
    }

    @Override
    public void onDestroyView() {
        mViewModel.setDisposable();
        super.onDestroyView();
    }
}