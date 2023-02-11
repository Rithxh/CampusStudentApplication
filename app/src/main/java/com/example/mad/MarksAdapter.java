package com.example.mad;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MarksAdapter extends BaseAdapter {

    Context context;
    String[] courses,marks1,marks2,marks3;
    LayoutInflater inflater;

    public MarksAdapter(Context applicationContext,ArrayList<String> courses,ArrayList<String> marks1,ArrayList<String> marks2,ArrayList<String> marks3)
    {
        this.context=applicationContext;
        this.courses=courses.toArray(new String[courses.size()]);
        this.marks1=marks1.toArray(new String[marks1.size()]);
        this.marks2=marks2.toArray(new String[marks2.size()]);
        this.marks3=marks3.toArray(new String[marks3.size()]);
        inflater=(LayoutInflater.from(applicationContext));
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

        view=inflater.inflate(R.layout.marks_list_item,null);
        TextView courseView=view.findViewById(R.id.courseCodeMarks);
        TextView cie1View=view.findViewById(R.id.cie1);
        TextView cie2View=view.findViewById(R.id.cie2);
        TextView cie3View=view.findViewById(R.id.cie3);

        courseView.setText(courses[i]);
        cie1View.setText(marks1[i]);
        cie2View.setText(marks2[i]);
        cie3View.setText(marks3[i]);
        return view;
    }
}
