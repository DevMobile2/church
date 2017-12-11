 package com.rameshmkll.church;

 import android.content.Intent;
 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.view.View;
 import android.widget.TextView;

 public class MainActivity extends AppCompatActivity {
     TextView skip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
skip=findViewById(R.id.tvSkip);
skip.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MainActivity.this,DashBoard.class);
        startActivity(intent);
    }
});

    }
}
