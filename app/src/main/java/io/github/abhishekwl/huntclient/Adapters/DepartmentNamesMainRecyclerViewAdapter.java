package io.github.abhishekwl.huntclient.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.abhishekwl.huntclient.R;

public class DepartmentNamesMainRecyclerViewAdapter extends RecyclerView.Adapter<DepartmentNamesMainRecyclerViewAdapter.DepartmentNameViewHolder> {

    private String[] departmentNamesArrayList;
    private Context context;
    private int colorAccent;

    public DepartmentNamesMainRecyclerViewAdapter(String[] departmentNamesArrayList, Context context) {
        this.departmentNamesArrayList = departmentNamesArrayList;
        this.context = context;
        this.colorAccent = ContextCompat.getColor(context, R.color.colorAccent);
    }

    @NonNull
    @Override
    public DepartmentNamesMainRecyclerViewAdapter.DepartmentNameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_department_list_item, parent, false);
        return new DepartmentNameViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DepartmentNamesMainRecyclerViewAdapter.DepartmentNameViewHolder holder, int position) {
        String departmentName = departmentNamesArrayList[position];
        holder.render(departmentName);
    }

    @Override
    public int getItemCount() {
        return departmentNamesArrayList.length;
    }

    class DepartmentNameViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.departmentListItemDepartmentNameTextView)
        TextView departmentNameTextView;

        DepartmentNameViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void render(String departmentName) {
            departmentNameTextView.setText(departmentName);
        }
    }
}
