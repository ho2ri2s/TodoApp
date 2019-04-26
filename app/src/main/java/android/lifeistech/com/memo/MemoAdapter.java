package android.lifeistech.com.memo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import io.realm.Realm;

public class MemoAdapter extends ArrayAdapter<Memo> {

    private LayoutInflater layoutinflater;
    private List<Memo> mMemos;
    ViewHolder viewHolder;
    CoordinatorLayout coordinatorLayout;


    MemoAdapter(Context context, int textViewResourceId, List<Memo> objects, CoordinatorLayout coordinatorLayout) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMemos = objects;
        this.coordinatorLayout = coordinatorLayout;
    }

    public static class ViewHolder{
        TextView titleText;
        TextView contentText;
        TextView updateDateText;
        LinearLayout linearLayout;
        CheckBox checkBox;

        public ViewHolder(View convertView){
            titleText = (TextView)convertView.findViewById(R.id.titleText);
            contentText = (TextView)convertView.findViewById(R.id.contentText);
            updateDateText = (TextView)convertView.findViewById(R.id.updateDateText);
            linearLayout = (LinearLayout)convertView.findViewById(R.id.linearLayout);
            checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        }
    }

    @Nullable
    @Override
    public Memo getItem(int position) {
        return  mMemos.get(position);
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

        //realmに管理されていない、メモの情報を取得
        final Memo memo = getItem(position);

        if(memo != null) {

            viewHolder.titleText.setText(memo.title);
            viewHolder.contentText.setText(memo.content);
            viewHolder.updateDateText.setText(memo.updateDate);
            viewHolder.checkBox.setChecked(memo.isCompleted);

            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("updateDate", memo.updateDate);
                    view.getContext().startActivity(intent);

                }
            });

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    //チェックされた際にタスク完了状態。保存するためにMemoに状態を格納。
                    final CheckBox checkBoxView = (CheckBox)view;
                    //レルムを開く
                    final Realm realm = Realm.getDefaultInstance();

                    //getItem(position)で取得したメモのupdateDateを用いて、Realmで管理されているメモ情報を探す。
                    final Memo realmMemo = realm.where(Memo.class)
                                           .equalTo("updateDate", memo.updateDate)
                                           .findFirst();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if(checkBoxView.isChecked()){
                                realmMemo.isCompleted = true;
                                Snackbar.make(coordinatorLayout, "Task is Completed", Snackbar.LENGTH_SHORT).show();
                            }else{
                                realmMemo.isCompleted = false;
                                Snackbar.make(coordinatorLayout, "Task is uncompleted", Snackbar.LENGTH_SHORT).show();

                            }

                        }
                    });
                    //必ず閉める
                    realm.close();

                }

            });

        }
        return convertView;
    }
}
