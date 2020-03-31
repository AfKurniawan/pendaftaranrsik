package id.rsi.klaten.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.rsik.R;


public class NotifikasiActivity extends AppCompatActivity {

    private static final String TAG = NotifikasiActivity.class.getSimpleName();
    private TextView txtTitle, txtMessage;
    SharedPreferences sp;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);


        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp = getSharedPreferences("rsiklaten", Context.MODE_PRIVATE);
        mEditor = sp.edit();


        txtTitle = findViewById(R.id.tvTitle);
        txtMessage = findViewById(R.id.tvMessages);

//        String message = getIntent().getStringExtra("message");
//        mEditor.putString("messages", message);
//        mEditor.commit();
//
//
//        String title = getIntent().getStringExtra("title");
//        mEditor.putString("title", title);
//        mEditor.commit();


        String judul = sp.getString("title", "");
        txtTitle.setText(judul);

        String pesan = sp.getString("messages", "");
        txtMessage.setText(pesan);

        Log.e("Message", pesan);
        Log.e("Judul", judul);



/*



        txtMessage.setText(message);
        txtTitle.setText(title);
*/

        initToolbar();

        initSharedPref();





    }

    public void initSharedPref(){



    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.green_700);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

