package com.example.mad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceAdapter extends BaseAdapter {

    Context context;
    String[] courses,attended,total,eligible;
    LayoutInflater inflater;

    public AttendanceAdapter(Context applicationContext, ArrayList<String> courses, ArrayList<String> attended, ArrayList<String> total)
    {
        this.context=applicationContext;
        this.courses=courses.toArray(new String[courses.size()]);
        this.attended=attended.toArray(new String[attended.size()]);
        this.total=total.toArray(new String[total.size()]);
        inflater=(LayoutInflater.from(applicationContext));
        checkEligibility();
    }

    public void checkEligibility(){

        eligible=new String[courses.length];
        eligible[0]="Eligibility";
        for(int i=1;i< courses.length;i++)
        {
            float val=Integer.parseInt(attended[i])/Integer.parseInt(total[i]);
            if(val>=0.85)
            {
                eligible[i]="Eligible";
            }
            else if(val>=0.75)
            {
                eligible[i]="Condonation";
            }
            else
            {
                eligible[i]="Not eligible";
            }
        }
    }

    @Override
    public int getCount() {
        return courses.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.attendance_list_item,null);
        TextView courseView=view.findViewById(R.id.courseCodeAttendance);
        TextView attendedView=view.findViewById(R.id.attended);
        TextView totalView=view.findViewById(R.id.total);
        TextView eligibleView=view.findViewById(R.id.eligible);

        courseView.setText(courses[i]);
        attendedView.setText(attended[i]);
        totalView.setText(total[i]);
        eligibleView.setText(eligible[i]);
        return view;
    }
}
