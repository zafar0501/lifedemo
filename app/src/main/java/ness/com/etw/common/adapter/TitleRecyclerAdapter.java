package ness.com.etw.common.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ness.com.etw.R;
import ness.com.etw.common.model.TitleDO;
import ness.com.etw.common.presenter.IItemViewAction;

public class TitleRecyclerAdapter<T> extends RecyclerView.Adapter<TitleRecyclerAdapter.TitleRecyclerAdapterView> {

    private Context context;
    private List<T> adapterList;
    private IItemViewAction iItemViewAction;


    public TitleRecyclerAdapter(Context context, List<T> adapterList, IItemViewAction iItemViewAction) {
        this.context = context;
        this.adapterList = adapterList;
        this.iItemViewAction = iItemViewAction;
    }

    @Override
    public TitleRecyclerAdapterView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_detail_item_row, parent, false);
        return new TitleRecyclerAdapterView(view);

    }

    @Override
    public void onBindViewHolder(TitleRecyclerAdapter.TitleRecyclerAdapterView holder, int position) {

        String strTitle = null, strSubTitle = null;
        Drawable imgThumbnail = null;
        T adapterContent = adapterList.get(0);

        if (adapterContent instanceof TitleDO) {

            TitleDO titleDO = (TitleDO) adapterList.get(position);
            strTitle = titleDO.getTitle();
            strSubTitle = titleDO.getSubTitle();
            imgThumbnail = titleDO.getImgThumbnail();
        }


        holder.txtTitle.setText(strTitle);
        holder.txtSubTitle.setText(strSubTitle);
        holder.imgThumbnail.setImageDrawable(imgThumbnail);

    }

    @Override
    public int getItemCount() {
        return adapterList.size();
    }

    class TitleRecyclerAdapterView extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtTitle;
        TextView txtSubTitle;
        ImageView imgThumbnail;

        public TitleRecyclerAdapterView(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_home_detail_title);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txt_home_detail_subTitle);
            imgThumbnail = (ImageView) itemView.findViewById(R.id.img_home_detail_thumbnail);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            iItemViewAction.onItemRowClick(adapterList.get(getAdapterPosition()), getAdapterPosition());

        }
    }
}
