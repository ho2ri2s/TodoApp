package android.lifeistech.com.memo;

import android.content.Context;
import android.content.Intent;
import android.lifeistech.com.memo.R;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;

public class TaskAdapter extends ArrayAdapter<Task> {

    private LayoutInflater layoutinflater;
    private List<Task> mTasks;
    ViewHolder viewHolder;


    TaskAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);
        layoutinflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTasks = objects;
    }

    public static class ViewHolder{
        TextView titleText;
        TextView deadlineText;
        LinearLayout linearLayout;
        CheckBox checkBox;

        public ViewHolder(View convertView){
            titleText = (TextView)convertView.findViewById(R.id.titleText);
            deadlineText = (TextView)convertView.findViewById(R.id.deadlineText);
            linearLayout = (LinearLayout)convertView.findViewById(R.id.linearLayout);
            checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        }
    }

    @Nullable
    @Override
    public Task getItem(int position) {
        return  mTasks.get(position);
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {


        if (convertView == null) {
            convertView = layoutinflater.from(getContext()).inflate(R.layout.layout_item_task_main, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        //realmに管理されていない、メモの情報を取得
        final Task task = getItem(position);

        if(task != null) {

            viewHolder.titleText.setText(task.title);
            viewHolder.deadlineText.setText(task.dateDeadline + "   " + task.timeDeadline + "まで");
            viewHolder.checkBox.setChecked(task.isCompleted);

            viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), TaskActivity.class);
                    intent.putExtra("updateDate", task.updateDate);
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
                    final Task realmTask = realm.where(Task.class)
                                           .equalTo("updateDate", task.updateDate)
                                           .findFirst();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            if(checkBoxView.isChecked()){
                                realmTask.isCompleted = true;
                                Snackbar.make(parent, "Task is Completed", Snackbar.LENGTH_SHORT).show();
                            }else{
                                realmTask.isCompleted = false;
                                Snackbar.make(parent, "Task is uncompleted", Snackbar.LENGTH_SHORT).show();
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
