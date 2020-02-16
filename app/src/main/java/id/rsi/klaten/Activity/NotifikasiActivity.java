package id.rsi.klaten.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import id.rsi.klaten.rsik.R;


public class NotifikasiActivity extends AppCompatActivity {

    private static final String TAG = NotifikasiActivity.class.getSimpleName();
    private TextView txtTitle, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);

        txtTitle = findViewById(R.id.tvTitle);
        txtMessage = findViewById(R.id.tvMessages);

        String message = getIntent().getStringExtra("message");
        String title = getIntent().getStringExtra("title");

        txtMessage.setText(message);
        txtTitle.setText(title);



    }
}

