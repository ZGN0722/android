package com.example.firstapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String tag="test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //简单登录

        AlertDialog.Builder Builder = new AlertDialog.Builder(MainActivity.this);
        TextView textView=findViewById(R.id.text);
        Button button=findViewById(R.id.Button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                final View viewDialog = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null,false);

                builder.setTitle("自定义对话框")
                        .setView(viewDialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                EditText editTextID = viewDialog.findViewById(R.id.text1);
                                EditText editTextPass = viewDialog.findViewById(R.id.text2);
                                if(editTextID.getText().toString().equals("abc") && editTextPass.getText().toString().equals("123")) {
                                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                                    Log.i(tag,"登录成功");
                                }
                                else{
                                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                                    Log.i(tag,"登录失败");
                                }
                            }
                        }).create().show();
            }
        });

        //传递参数
        Intent intent = getIntent();
        String str1= intent.getStringExtra("name");
        String str2= intent.getStringExtra("password");
        if(str1!=null && str2!=null)
        Toast.makeText(MainActivity.this, str1+" "+str2, Toast.LENGTH_LONG).show();
        Button button2=findViewById(R.id.Button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnothActivity.class);
                intent.putExtra("name", "Zhangsan");
                intent.putExtra("password", "3");
                startActivity(intent);
            }

        });

        //简单聊天
        Button button3 =(Button) findViewById(R.id.Button3);
       button3.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(MainActivity.this, ChatMainActivity.class);
               startActivity(intent);
           }
       });
    }
}