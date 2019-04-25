package android.lifeistech.com.memo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import io.realm.Realm;

public class DetailActivity extends AppCompatActivity {

    Realm realm;

    EditText titleEditText;
    EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        realm = Realm.getDefaultInstance();

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        contentEditText = (EditText)findViewById(R.id.contentEditText);

        showData();
    }

    public void showData(){

        final Memo memo = realm.where(Memo.class).equalTo("updateDate",
                getIntent().getStringExtra("updateDate")).findFirst();

        titleEditText.setText(memo.title);
        contentEditText.setText(memo.content);
    }

    public void update(View view){

        final Memo memo = realm.where(Memo.class).equalTo("updateDate",
                getIntent().getStringExtra("updateDate")).findFirst();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                memo.title = titleEditText.getText().toString();
                memo.content = contentEditText.getText().toString();
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
        switch(item.getItemId()){
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void delete(){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                Memo memo = realm.where(Memo.class).equalTo("updateDate",
                        getIntent().getStringExtra("updateDate")).findFirst();

                memo.deleteFromRealm();

            }
        });

    }
}