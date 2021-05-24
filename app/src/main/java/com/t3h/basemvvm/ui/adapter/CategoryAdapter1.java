package com.t3h.basemvvm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.t3h.basemvvm.R;
import com.t3h.basemvvm.data.model.api.CateModel;
import com.t3h.basemvvm.ui.category.CategoryFragmentDirections;

import java.util.List;

public class CategoryAdapter1 extends RecyclerView.Adapter<CategoryAdapter1.MyViewHolder> {
    private Context context;
    private List<CateModel> list;


    public CategoryAdapter1(List<CateModel> list, Context context) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CategoryAdapter1.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_1, parent, false);
        return new CategoryAdapter1.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CateModel cate = list.get(position);
        initView(cate, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections dir= CategoryFragmentDirections.Companion.actionCategoryFragmentToCategoryDetailFragment(
                        cate.getId(),cate.getName()
                );
                Navigation.findNavController(v).navigate(dir);
            }
        });
    }

    private void initView(CateModel cate, MyViewHolder view) {
        view.textViewTitle.setText(cate.getName());
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle;
        private RecyclerView recyclerView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.tv_name);
        }
    }


}
