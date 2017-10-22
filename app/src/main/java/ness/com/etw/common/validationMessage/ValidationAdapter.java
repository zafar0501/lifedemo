package ness.com.etw.common.validationMessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ness.com.etw.R;
import ness.com.etw.common.presenter.IResultView;

public class ValidationAdapter extends RecyclerView.Adapter<ValidationAdapter.ValidationViewHolder> {

    private List<String> errorList;
    private IResultView showErrorMessageListener;

    public ValidationAdapter(List<String> errorList, IResultView showErrorMessage) {
        this.errorList = errorList;
        this.showErrorMessageListener = showErrorMessage;
    }

    @Override
    public ValidationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.validation_recycler_view_item_row, parent, false);

        ValidationViewHolder holder = new ValidationViewHolder(view);
        holder.textView_closeoption.setTag(holder);


        return holder;
    }

    @Override
    public void onBindViewHolder(ValidationViewHolder holder, int position) {
        String errorMessage = errorList.get(position);
        holder.textView_errormessage.setText(errorMessage);


    }

    @Override
    public int getItemCount() {
        return errorList.size();
    }

    class ValidationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView_errormessage, textView_closeoption;

        ValidationViewHolder(View view) {
            super(view);
            textView_errormessage = view.findViewById(R.id.txt_validation_item_row_title);
            textView_closeoption = view.findViewById(R.id.txt_validation_item_row_close);
            textView_closeoption.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            errorList.remove(getAdapterPosition());
            showErrorMessageListener.displayErrorList(errorList);
        }
    }

}

