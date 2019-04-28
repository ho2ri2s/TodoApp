package android.lifeistech.com.memo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.lifeistech.com.memo.R;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    Realm realm;

    EditText titleEditText;
    EditText contentEditText;
    TextView dateText;
    TextView timeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = Realm.getDefaultInstance();

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText) findViewById(R.id.contentEditText);
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);

        dateText.setOnClickListener(this);
        timeText.setOnClickListener(this);
        showData();
    }

    public void showData() {

        final Task task = realm.where(Task.class).equalTo("updateDate",
                getIntent().getStringExtra("updateDate")).findFirst();

        titleEditText.setText(task.title);
        contentEditText.setText(task.content);
        dateText.setText(task.dateDeadline);
        timeText.setText(task.timeDeadline);
    }

    public void update(View view) {

        final Task task = realm.where(Task.class).equalTo("updateDate",
                getIntent().getStringExtra("updateDate")).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                task.title = titleEditText.getText().toString();
                task.content = contentEditText.getText().toString();
                task.dateDeadline = dateText.getText().toString();
                task.timeDeadline = timeText.getText().toString();
            }
        });

        //更新したら画面を閉じる
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_todo:
                //ミスタッチ防止
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("本当に削除していいですか？")
                        .setPositiveButton("削除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //メモ削除
                                delete();
                                Toast.makeText(DetailActivity.this, "削除しました", Toast.LENGTH_SHORT).show();
                                //メモを消したらMainActivityへ戻る
                                finish();
                            }
                        })
                        .setNegativeButton("キャンセル", null)
                        .show();
                return true;
            case android.R.id.home:
                //マニフェストに戻り先（親Activity）を記述する必要がある
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void delete() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Task task = realm.where(Task.class).equalTo("updateDate",
                        getIntent().getStringExtra("updateDate")).findFirst();

                task.deleteFromRealm();

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dateText:
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DetailActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                dateText.setText(year + "/" + month + "/" + day);
                            }
                        },
                        date.get(Calendar.YEAR),
                        date.get(Calendar.MONTH),
                        date.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
                break;
            case R.id.timeText:
                final Calendar time = Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        DetailActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                timeText.setText(hour + " : " + minute);
                            }
                        },
                        time.get(Calendar.HOUR_OF_DAY),
                        time.get(Calendar.MINUTE),
                        true);

                timePickerDialog.show();
                break;
        }
    }
}