package android.lifeistech.com.memo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class CreateActivity extends AppCompatActivity {

    public EditText titleEditText;
    public EditText contentEditText;

    public Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        //realmを開く
        realm = Realm.getDefaultInstance();

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText)findViewById(R.id.contentEditText);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //realmを閉じる
        realm.close();
    }

    //データをRealmに保存する
    public void save(final String title, final String updateDate, final String content){

        //メモを保存する
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Memo memo = realm.createObject(Memo.class);
                memo.title = title;
                memo.updateDate = updateDate;
                memo.content = content;
                memo.isCompleted = false;   //最初はタスク未完了
            }
        });
    }

    public void create(View view){
        String title = titleEditText.getText().toString();

        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.JAPANESE);
        String updateDate = simpleDateFormat.format(date);

        String content = contentEditText.getText().toString();

        save(title, updateDate, content);

        finish();
    }


}
