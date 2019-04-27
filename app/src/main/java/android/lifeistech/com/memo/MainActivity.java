package android.lifeistech.com.memo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public Realm realm;
    public ListView listView;
    RealmResults<Memo> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //realmを開く
        realm = Realm.getDefaultInstance();
        listView = (ListView)findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        results = realm.where(Memo.class).findAll();
        setMemoList(results);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void create(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }

    public void setMemoList(RealmResults<Memo> results){
        //realmから読み取る
        List<Memo> items = realm.copyFromRealm(results);
        MemoAdapter adapter = new MemoAdapter(getApplicationContext(), R.layout.layout_item_memo, items);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filtering_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.all:
                results = realm.where(Memo.class).findAll();
                setMemoList(results);
                return true;
            case R.id.compleated:
                results = realm.where(Memo.class).equalTo("isCompleted", true).findAll();
                setMemoList(results);
                return true;
            case R.id.uncompleated:
                results = realm.where(Memo.class).equalTo("isCompleted", false).findAll();
                setMemoList(results);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
