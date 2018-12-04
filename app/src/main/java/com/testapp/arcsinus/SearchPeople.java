package com.testapp.arcsinus;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.testapp.models.MyAdapter;
import com.testapp.models.PeopleResults;
import com.testapp.models.RecyclerItem;
import com.testapp.retrofit.APIClient;
import com.testapp.sqlite.DBHelper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPeople extends AppCompatActivity implements View.OnClickListener {
    EditText edSearch;
    TextView txtResult;
    Button btnSearch;
    Button btnRead;
    Button btnSave;
    Button btnDel;
    Toolbar toolbar;
    DBHelper dbHelper;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<RecyclerItem> dataList;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);
        initToolbar(true);
        setToolbarTitle(getString(R.string.poisk));
        initView();
        initDb();
        dataList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(dataList, this);
        recyclerView.setAdapter(adapter);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        edSearch = (EditText) findViewById(R.id.edSearch);
        txtResult = (TextView) findViewById(R.id.txtResult);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnRead = (Button) findViewById(R.id.btnRead);
        btnDel = (Button) findViewById(R.id.btnDel);

        btnSearch.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnRead.setOnClickListener(this);
        btnDel.setOnClickListener(this);
    }

    public void initToolbar(boolean isTitleEnabled) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(isTitleEnabled);
    }

    public void setToolbarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private void initDb() {
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    private void loadFromDb() {
        Cursor cursor = database.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            dataList.clear();
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            do {
                dataList.add(new RecyclerItem(cursor.getString(nameIndex)));
            } while (cursor.moveToNext());
            adapter.refreshList(dataList);
            cursor.close();
        } else
            dataList = new ArrayList<>();
    }

    private void saveToDb(String text) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAME, text);
        database.insert(DBHelper.TABLE_NAME, null, contentValues);
    }

    private void searchPeople(String word) {
        Call<PeopleResults> call = APIClient.get().searchPeople(word);
        call.enqueue(new Callback<PeopleResults>() {

            @Override
            public void onResponse(Call<PeopleResults> call, Response<PeopleResults> response) {
                Log.d("APIPlug", "Successfully response fetched");
                edSearch.setText("");
                PeopleResults people = response.body();

                ///если по запросу пользователя был хоть что то найдено,то это выводится на экран
                if (people.results.size() > 0)
                    txtResult.setText(people.results.get(0).toString());
                else
                    txtResult.setText("По вашему запросу ничего не найдено");
            }

            @Override
            public void onFailure(Call<PeopleResults> call, Throwable t) {
                txtResult.setText("Ошибка: " + t.toString());
                edSearch.setText("");
            }
        });
    }

    private void onSearchClicked(){
        searchPeople(edSearch.getText().toString());
        txtResult.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    private void onSaveClicked(){
        String text = txtResult.getText().toString();
        if (!text.equals("")) {
            saveToDb(text);
            showToast("Сохранено");
        } else {
            showToast("Мне нечего сохранять.Введите запрос");
        }
    }

    private void onReadClicked(){
        loadFromDb();
        recyclerView.setVisibility(View.VISIBLE);
        txtResult.setVisibility(View.GONE);
    }

    private void onDeleteClicked(){
        database.delete(DBHelper.TABLE_NAME, null, null);
        dataList = new ArrayList<>();
        adapter.refreshList(dataList);
        showToast("Удалено");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                onSearchClicked();
                break;
            case R.id.btnSave:
                onSaveClicked();
                break;
            case R.id.btnRead:
                onReadClicked();
                break;
            case R.id.btnDel:
                onDeleteClicked();
                break;
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
