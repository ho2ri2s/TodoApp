package android.lifeistech.com.memo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class MemoAdapter extends ArrayAdapter<Memo> {

    private LayoutInflater layoutinflater;
    private List<Memo> mMemos;
    ViewHolder viewHolder;



    MemoAdapter(Context context, int textViewResourceId, List<Memo> objects) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemos = objects;
    }

    public static class ViewHolder{
        TextView titleText;
        TextView contentText;
        TextView updateDateText;
        LinearLayout linearLayout;

        public ViewHolder(View convertView){
            titleText = (TextView)convertView.findViewById(R.id.titleText);
            contentText = (TextView)convertView.findViewById(R.id.contentText);
            updateDateText = (TextView)convertView.findViewById(R.id.updateDateText);
            linearLayout = (LinearLayout)convertView.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = layoutinflater.from(getContext()).inflate(R.layout.layout_item_memo, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        final Memo memo = getItem(position);

        if(memo != null) {

            viewHolder.titleText.setText(memo.title);
            viewHolder.contentText.setText(memo.content);
            viewHolder.updateDateText.setText(memo.updateDate);

            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("updateDate", memo.updateDate);
                    view.getContext().startActivity(intent);

                }
            });
        }
        return convertView;
    }
}
