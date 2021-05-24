package com.t3h.basemvvm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.Book;
import com.t3h.basemvvm.data.model.api.CateModel;
import com.t3h.basemvvm.data.model.api.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private List<CateModel> list;

    public CategoryAdapter(List<CateModel> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CateModel cate = list.get(position);
        initView(cate, holder);

    }

    private void initView(CateModel cate, MyViewHolder view) {
        view.recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        view.textViewTitle.setText(cate.getName());
        view.recyclerView.setAdapter(new BookAdapter(cate.getBooks(), context,10));
    }

    @Override
    public int getItemCount() {
        if (list!= null) {
            return list.size();
        } else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private RecyclerView recyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            recyclerView=itemView.findViewById(R.id.rcv_category_item);

        }
    }
}
