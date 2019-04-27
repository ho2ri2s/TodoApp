package android.lifeistech.com.memo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class CreateActivity extends AppCompatActivity implements View.OnClickListener{

    public EditText titleEditText;
    public EditText contentEditText;
    public TextView dateText;
    public TextView timeText;
    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //realmを開く
        realm = Realm.getDefaultInstance();

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText)findViewById(R.id.contentEditText);
        dateText = (TextView) findViewById(R.id.dateText);
        timeText = (TextView) findViewById(R.id.timeText);

        dateText.setOnClickListener(this);
        timeText.setOnClickListener(this);
        //Upナビゲーション
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //realmを閉じる
        realm.close();
    }

    //データをRealmに保存する
    public void save(final String title, final String updateDate, final String content, final String dateDeadline, final String timeDeadline){

        //メモを保存する
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.createObject(Memo.class);
                memo.title = title;
                memo.updateDate = updateDate;
                memo.content = content;
                memo.isCompleted = false;   //最初はタスク未完了
                memo.dateDeadline = dateDeadline;
                memo.timeDeadline = timeDeadline;
            }
        });
    }

    public void create(View view){
        String title = titleEditText.getText().toString();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        String updateDate = simpleDateFormat.format(date);

        String content = contentEditText.getText().toString();

        String dateDeadline = dateText.getText().toString();
        String timeDeadline = timeText.getText().toString();

        save(title, updateDate, content, dateDeadline, timeDeadline);

        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                //マニフェストに戻り先（親Activity）を記述する必要がある
                NavUtils.navigateUpFromSameTask(this);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dateText:
                final Calendar date = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateActivity.this,
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
                        CreateActivity.this,
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
