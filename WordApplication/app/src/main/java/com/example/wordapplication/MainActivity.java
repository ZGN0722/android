package com.example.wordapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    WordsDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //为ListView注册上下文菜单
        ListView list = (ListView) findViewById(R.id.IstWords);
        registerForContextMenu(list);

        //创建SQLiteOpenHelper对象，注意第一次运行时，此时数据库并没有被创建
        mDbHelper = new WordsDBHelper(this);

        //在列表显示全部单词
        final ArrayList<Map<String, String>> items = getAll();
        setWordsListView(items);

        // 点击事件
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(Words.Word.COLUMN_NAME_WORD,items.get(position).get(Words.Word.COLUMN_NAME_WORD));
                    bundle.putSerializable(Words.Word.COLUMN_NAME_MEANING,items.get(position).get(Words.Word.COLUMN_NAME_MEANING));
                    bundle.putSerializable(Words.Word.COLUMN_NAME_SAMPLE,items.get(position).get(Words.Word.COLUMN_NAME_SAMPLE));
                    Intent intent=new Intent(MainActivity.this,FrameActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView LandWord = findViewById(R.id.LandWord);
                    TextView LandMeaning = findViewById(R.id.LandMeaning);
                    TextView LandSqmple = findViewById(R.id.LandSample);

                    LandWord.setText(items.get(position).get(Words.Word.COLUMN_NAME_WORD));
                    LandMeaning.setText(items.get(position).get(Words.Word.COLUMN_NAME_MEANING));
                    LandSqmple.setText(items.get(position).get(Words.Word.COLUMN_NAME_SAMPLE));
                    //Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private ArrayList<Map<String, String>> getAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words";
        Cursor c=db.rawQuery(sql,new String[]{});
        return ConvertCursor2List(c);
    }

    private ArrayList<Map<String, String>> ConvertCursor2List(Cursor c) {
        ArrayList<Map<String, String>> items = new ArrayList<Map<String, String>>();
        c.moveToNext();
            while (true) {
                Map<String,String> map =new HashMap<>();
                if(c.isLast()){
                    map.put("id", c.getString(0));
                    map.put(Words.Word.COLUMN_NAME_WORD, c.getString(1));
                    map.put(Words.Word.COLUMN_NAME_MEANING, c.getString(2));
                    map.put(Words.Word.COLUMN_NAME_SAMPLE, c.getString(3));
                    items.add(map);
                    break;
                }
                else {
                    map.put("id", c.getString(0));
                    map.put(Words.Word.COLUMN_NAME_WORD, c.getString(1));
                    map.put(Words.Word.COLUMN_NAME_MEANING, c.getString(2));
                    map.put(Words.Word.COLUMN_NAME_SAMPLE, c.getString(3));
                    items.add(map);
                    c.moveToNext();
                }
            }
        return items;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mDbHelper.close();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_search:
                //查找
                SearchDialog();
                return true;
            case R.id.action_insert:
                //新增单词
                InsertDialog();
                return true;
            case R.id.action_help:
                Toast.makeText(MainActivity.this, "你点击了帮助", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
//使用Sql语句查找
    private ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="select * from words where word like ? order by word desc";
        Cursor c=db.rawQuery(sql,new String[]{"%"+strWordSearch+"%"});
        return ConvertCursor2List(c);
    }
//使用query方法查找
    private ArrayList<Map<String, String>> Search(String strWordSearch) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE
        };
        String sortOrder =
                Words.Word.COLUMN_NAME_WORD + " DESC";
        String selection = Words.Word.COLUMN_NAME_WORD + " LIKE ?";
        String[] selectionArgs = {"%"+strWordSearch+"%"};
        Cursor c = db.query(
                Words.Word.TABLE_NAME,  // The table to query
                           projection,                               // The columns to return
                            selection,                                // The columns for the WHERE clause
                      selectionArgs,                            // The values for the WHERE clause
                            null,                                     // don't group the rows
                            null,                                     // don't filter by row groups
                           sortOrder                                 // The sort order
                 );
                    return ConvertCursor2List(c);
    }

    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")//标题
                    .setView(tableLayout)//设置视图
        // 确定按钮及其动作
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String txtSearchWord=((EditText)tableLayout.findViewById(R.id.txtSearchWord)).getText().toString();
                                    ArrayList<Map<String, String>> items=null;
                                    //既可以使用Sql语句查询，也可以使用方法查询
                                    items=SearchUseSql(txtSearchWord);
                                     //items=Search(txtSearchWord);
                                    if(items.size()>0) {
                                        Bundle bundle=new Bundle();
                                        bundle.putSerializable("result",items);
                                        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }else
                                        Toast.makeText(MainActivity.this,"没有找到", Toast.LENGTH_LONG).show();
                                }
                    })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
         }
                })
                .create()//创建对话框
                    .show();//显示对话框
    }

    //新增对话框
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this)
                .setTitle("新增单词")//标题
                .setView(tableLayout)//设置视图
        // 确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String strWord=((EditText)tableLayout.findViewById(R.id.txtWord)).getText().toString();
                                String strMeaning=((EditText)tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                                String strSample=((EditText)tableLayout.findViewById(R.id.txtSample)).getText().toString();
                                //既可以使用Sql语句插入，也可以使用使用insert方法插入
                                InsertUserSql(strWord, strMeaning, strSample);
                                //Insert(strWord, strMeaning, strSample);
                                ArrayList<Map<String, String>> items = getAll();
                                setWordsListView(items);

                            }
                })
    //取消按钮及其动作
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .create()//创建对话框
                .show();//显示对话框
    }
    //使用Sql语句插入单词
    private void InsertUserSql(String strWord, String strMeaning, String strSample){
        String sql="insert into  words(word,meaning,sample) values(?,?,?)";
        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }

    //使用insert方法增加单词
    private void Insert(String strWord, String strMeaning, String strSample) {
        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);
        // Insert the new row, returning the primary key value of the new row
        long newRowId;
        newRowId = db.insert(Words.Word.TABLE_NAME,null,values);
    }


    //设置适配器，在列表中显示单词
    private void setWordsListView(ArrayList<Map<String, String>> items){
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.item,
        new String[]{"id",
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE,
                },
        new int[]{R.id.textId,R.id.textViewWord, R.id.textViewMeaning, R.id.textViewSample});

        ListView list = (ListView) findViewById(R.id.IstWords);
        list.setAdapter(adapter);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.contextmenu_wordslistview, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId=null;
        TextView textWord=null;
        TextView textMeaning=null;
        TextView textSample=null;
        AdapterView.AdapterContextMenuInfo info=null;
        View itemView=null;
        switch (item.getItemId()){
            case R.id.action_delete:
                //删除单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                if(textId!=null){
                    String strId=textId.getText().toString();
                    DeleteDialog(strId);
                    }
                break;
            case R.id.action_update:
                //修改单词
                info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                itemView=info.targetView;
                textId =(TextView)itemView.findViewById(R.id.textId);
                textWord =(TextView)itemView.findViewById(R.id.textViewWord);
                textMeaning =(TextView)itemView.findViewById(R.id.textViewMeaning);
                textSample =(TextView)itemView.findViewById(R.id.textViewSample);
                if(textId!=null && textWord!=null && textMeaning!=null && textSample!=null){
                    String strId=textId.getText().toString();
                    String strWord=textWord.getText().toString();
                    String strMeaning=textMeaning.getText().toString();
                    String strSample=textSample.getText().toString();
                    UpdateDialog(strId, strWord, strMeaning, strSample);
                }
                break;
        }
        return true;
    }
//使用Sql语句更新单词
    private void UpdateUseSql(String strId,String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql="update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample,strId});
    }
//使用方法更新
    private void Update(String strId,String strWord, String strMeaning, String strSample) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);
        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};
        int count = db.update(
                Words.Word.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private void UpdateDialog(final String strId, String strWord, String strMeaning, String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText)tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText)tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText)tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this)
                .setTitle("修改单词")
    //标题
                .setView(tableLayout)
    //设置视图
        // 确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        //既可以使用Sql语句更新，也可以使用使用update方法更新
                        UpdateUseSql(strId, strNewWord, strNewMeaning, strNewSample);
                        //  Update(strId, strNewWord, strNewMeaning, strNewSample);
                        setWordsListView(getAll());
                    }
                })
    //取消按钮及其动作
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        })
                .create()//创建对话框
                .show();//显示对话框
    }

    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //既可以使用Sql语句删除，也可以使用使用delete方法删除
                        DeleteUseSql(strId);
                        //Delete(strId);
                        setWordsListView(getAll());
                    }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }


    //删除单词
    private void DeleteUseSql(String strId) {
        String sql="delete from words where _id='"+strId+"'";
        //Gets the data repository in write mode*/
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    private void Delete(String strId) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        // 定义where子句
        String selection = Words.Word._ID + " = ?";
        // 指定占位符对应的实际参数
        String[] selectionArgs = {strId};
        // Issue SQL statement.
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }
}