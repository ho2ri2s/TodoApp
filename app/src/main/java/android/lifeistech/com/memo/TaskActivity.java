package android.lifeistech.com.memo;

import android.content.Intent;
import android.lifeistech.com.memo.R;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

import io.realm.Realm;

public class TaskActivity extends AppCompatActivity implements View.OnClickListener{

    TextView deadlineText;
    TextView titleText;
    TextView contentText;
    CheckBox checkBox;
    FloatingActionButton fab;
    Realm realm;
    Task task;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        deadlineText = (TextView)findViewById(R.id.deadlineText);
        titleText = (TextView)findViewById(R.id.titleText);
        contentText = (TextView)findViewById(R.id.contentText);
        checkBox = (CheckBox)findViewById(R.id.checkBox);
        fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        fab.setOnClickListener(this);

        showData();

    }

    public void showData(){
        realm = Realm.getDefaultInstance();
        task = realm
                .where(Task.class)
                .equalTo("updateDate", getIntent().getStringExtra("updateDate"))
                .findFirst();

        titleText.setText(task.title);
        contentText.setText(task.content);
        deadlineText.setText(task.dateDeadline + " " + task.timeDeadline);
    }

    @Override
    public void onClick(View view) {
        if(checkBox.isChecked()){
            Intent intent = new Intent(TaskActivity.this, DetailActivity.class);
            intent.putExtra("updateDate", task.updateDate);
            startActivity(intent);
        }else{
            Snackbar.make(coordinatorLayout, "チェックしてね", Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
                default:
                    return super.onOptionsItemSelected(item);

        }
    }
}
