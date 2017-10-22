package ness.com.etw.notes.views.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import ness.com.etw.R;
import ness.com.etw.common.constant.IntegerConstant;
import ness.com.etw.common.constant.StringConstant;

public class NotesHeaderItem extends AbstractHeaderItem<NotesHeaderItem.HeaderViewHolder> {

    private String id;
    private String strCreateDate;
    private  int isArchiveVisible=1;

    public NotesHeaderItem(String id) {
        super();
        this.id = id;
    }

    @Override
    public boolean equals(Object inObject) {
        if (inObject instanceof NotesHeaderItem) {
            NotesHeaderItem inItem = (NotesHeaderItem) inObject;
            return this.getStrCreateDate().equals(inItem.getStrCreateDate());
        }
        return false;
    }


    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public HeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new HeaderViewHolder(view, adapter);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.notes_header_item_row;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HeaderViewHolder holder, int position, List payloads) {

        holder.txtCreatedDate.setText(getStrCreateDate());

        if(isArchiveVisible==1)
        {
            holder.imgArchive.setVisibility(View.VISIBLE);
        }else {
            holder.imgArchive.setVisibility(View.GONE);
        }


        /*List<ISectionable> sectionableList = adapter.getSectionItems(this);
        String subTitle = (sectionableList.isEmpty() ? "Empty section" :
                sectionableList.size() + " section items");*/
        /*holder.txtCreatedDate.setText(subTitle);*/

    }

    static class HeaderViewHolder extends FlexibleViewHolder {

        TextView txtCreatedDate;
        ImageView imgArchive;

        HeaderViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter, true);
            txtCreatedDate = view.findViewById(R.id.txt_notes_header_item_row_current_date);
            imgArchive = view.findViewById(R.id.img_notes_header_item_row_archive_current_date_notes);

        }
    }


    private String getStrCreateDate() {
        return strCreateDate;
    }

    public void setStrCreateDate(String strCreateDate) {
        this.strCreateDate = strCreateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNotesArchiveVisibility(int visibility)
    {
        isArchiveVisible =visibility;

    }

    @Override
    public String toString() {
        return "NotesHeaderItem[" + strCreateDate + "]";
    }
}
