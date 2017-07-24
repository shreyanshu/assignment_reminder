package class1.dwit.com.assignmentreminder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import class1.dwit.com.assignmentreminder.Assignment.AssignmentDetail;
import class1.dwit.com.assignmentreminder.adapter.ListAdapter;
import class1.dwit.com.assignmentreminder.database.DatabaseHelper;
import class1.dwit.com.assignmentreminder.domain.Assignment;

public class ClassOf2020 extends AppCompatActivity {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_of2020);

        ListView listView = (ListView) findViewById(R.id.list_2020);
        List<Assignment> assList = databaseHelper.fetchBatchAssignment("2020");
        ListAdapter adapter=new ListAdapter(this, assList);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Assignment ass = (Assignment)parent.getAdapter().getItem(position);
                Intent inte = new Intent(getApplicationContext() ,AssignmentDetail.class);
//                    inte.putExtra("key",);
                Bundle b = new Bundle();
//                    b.putParcelable();
                inte.putExtra("title",ass.getAssignment_name());
                inte.putExtra("name",ass.getName());
                inte.putExtra("deadline",ass.getDeadline());
                inte.putExtra("batch",ass.getBatch());
                startActivity(inte);

            }
        });
    }
}
