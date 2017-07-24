package class1.dwit.com.assignmentreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import class1.dwit.com.assignmentreminder.R;
import class1.dwit.com.assignmentreminder.domain.Assignment;


/**
 * Created by sghatuwa on 3/10/17.
 */

public class ListAdapter extends BaseAdapter {

    Context activity;
    List<Assignment> assignmentList;
    LayoutInflater inflater;
    public ListAdapter(Context act, List<Assignment> list){
        this.activity = act;
        this.assignmentList = list;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return assignmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return assignmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.custom_list, null);
        TextView name = (TextView)vi.findViewById(R.id.list_assignment_name);
        TextView deadline = (TextView)vi.findViewById(R.id.list_deadline);
        name.setText(assignmentList.get(position).getName());

        long epoch = Long.parseLong(assignmentList.get(position).getDeadline())*1000;
        Date date = new Date(epoch); // 'epoch' in long
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = formatter.format(date);
        formatter = new SimpleDateFormat("hh:mm a"); //The "a" is the AM/PM marker
        String time = formatter.format(date);
        deadline.setText(dateString + " " + time);
        return vi;
    }
}
