package com.t3h.basemvvm.ui.audiobook;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.model.api.CateModel;
import com.t3h.basemvvm.databinding.BookFragmentBinding;
import com.t3h.basemvvm.databinding.HomeFragmentBinding;
import com.t3h.basemvvm.ui.adapter.CategoryAdapter;
import com.t3h.basemvvm.ui.home.HomeFragment;
import com.t3h.basemvvm.ui.main.MainActivityJava;
import com.t3h.basemvvm.util.AppUtil;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookFragment extends Fragment {

    private BookViewModel mViewModel;

    private BookFragmentArgs args;
    private BookFragmentBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding= BookFragmentBinding.inflate(inflater,container,false);
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BookViewModel.class);
        args=BookFragmentArgs.fromBundle(getArguments());
        String id=args.getIdBook();
        Log.e("sang", "Id: "+id);
        mViewModel.getBookByID(id);

        mViewModel.getData().observe(getViewLifecycleOwner(), new Observer<Book>() {
            @Override
            public void onChanged(Book book) {
                initDataView(book);
            }
        });
        binding.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivityJava)getActivity()).playEXOVideo(mViewModel.getData().getValue());
            }
        });
        binding.setIsLoading(mViewModel.isLoad);

        binding.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book book= mViewModel.getData().getValue();
                if (book == null){
                    Toast.makeText(v.getContext(),"Đang load dữ liệu", Toast.LENGTH_LONG);
                    return;
                }
                if (!mViewModel.setIsFavorite("F"+book.getId())){
                   mViewModel.addFav(book);
                    binding.favorite.setImageResource(R.drawable.baseline_favorite_red_600_24dp);

                }else {
                    mViewModel.deleteFav(book);
                    binding.favorite.setImageResource(R.drawable.baseline_favorite_border_white_24dp);

                }
            }
        });
    }

    private void initDataView(Book book) {
        binding.tvContent.setText(book.getIntro());
        binding.tvAuther.setText(book.getAuthor());
        binding.tvTitle.setText(book.getTitle());
        binding.tvTitle1.setText(book.getTitle());
        AppUtil.imageFromUrl(binding.image, book.getCoverImage());
        if (mViewModel.setIsFavorite("F"+book.getId())){
            binding.favorite.setImageResource(R.drawable.baseline_favorite_red_600_24dp);
        }else {
            binding.favorite.setImageResource(R.drawable.baseline_favorite_border_white_24dp);
        }
    }

    @Override
    public void onDestroyView() {
        mViewModel.setDisposable();
        super.onDestroyView();
    }
}