package class1.dwit.com.assignmentreminder.Assignment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import class1.dwit.com.assignmentreminder.MainActivity;
import class1.dwit.com.assignmentreminder.R;
import class1.dwit.com.assignmentreminder.database.DatabaseHelper;

public class AssignmentDetail extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail);

        TextView title = (TextView) findViewById(R.id.assignment_header);
        title.setText(getIntent().getStringExtra("name"));

        TextView ques = (TextView)findViewById(R.id.question);
        ques.setText(getIntent().getCharSequenceExtra("title"));

        TextView dead = (TextView)findViewById(R.id.deadline);
        long epoch = Long.parseLong(getIntent().getCharSequenceExtra("deadline").toString())*1000;
        Date date = new Date(epoch); // 'epoch' in long
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = formatter.format(date);
        formatter = new SimpleDateFormat("hh:mm a"); //The "a" is the AM/PM marker
        String time = formatter.format(date);
        dead.setText("Deadline: "+dateString + " " + time);
        Button addRef = (Button) findViewById(R.id.reference_button);

        TextView referanceArea = (TextView)findViewById(R.id.references);
        referanceArea.setText(databaseHelper.getReferences(getIntent().getCharSequenceExtra("deadline").toString()));

        addRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EditText reference = (EditText)findViewById(R.id.add_comment);
                String refere = reference.getText().toString();
                String assId = getIntent().getCharSequenceExtra("deadline").toString();
                if(refere.length()>0)
                {
                    databaseHelper.insertReference(refere, assId);
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        Button Mark = (Button)findViewById(R.id.marked);
        Mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.setMarked(getIntent().getCharSequenceExtra("deadline").toString());
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });

        SharedPreferences sharedPref = getSharedPreferences("this_batch",Context.MODE_PRIVATE);
//        String defaultValue = "2018";
        String batch = sharedPref.getString(getString(R.string.classes), "2018");
        final String bat = batch.substring(batch.length()-4);

        if(!bat.equals(getIntent().getCharSequenceExtra("batch").toString()))
        {
            View b = findViewById(R.id.add_comment);
            b.setVisibility(b.GONE);

            View ba = findViewById(R.id.references);
            ba.setVisibility(ba.GONE);

            View baa = findViewById(R.id.marked);
            baa.setVisibility(baa.GONE);

            View v = findViewById(R.id.reference_button);
            v.setVisibility(v.GONE);

            Toast.makeText(this, bat + " " + getIntent().getCharSequenceExtra("batch").toString(), Toast.LENGTH_SHORT).show();
        }
        else
        {
            String Marks = databaseHelper.getMarkedInfo(getIntent().getCharSequenceExtra("deadline").toString());
            if(Marks.equals("True"))
            {
                View b = findViewById(R.id.add_comment);
                b.setVisibility(b.GONE);

                View baa = findViewById(R.id.marked);
                baa.setVisibility(baa.GONE);

                View v = findViewById(R.id.reference_button);
                v.setVisibility(v.GONE);

            }
        }


    }
}
