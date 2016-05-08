/*
 * Copyright (c) 2016 Jivan Ghadage <jivanghadage@gmail.com>.
 */

package in.co.mrfoody.mrfoody.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.co.mrfoody.mrfoody.R;

/**
 * Created by om on 7/5/16.
 */
public class RecyclerViewActivity extends Activity {

    private List<Person> persons;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recycler_view_activity);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData() {
        persons = new ArrayList<>();
        persons.add(new Person("Emma Wilson", "23 years old", R.drawable.emma));
        persons.add(new Person("Lavery Maiss", "25 years old", R.drawable.lavery));
        persons.add(new Person("Lillie Watts", "35 years old", R.drawable.lillie));
    }

    private void initializeAdapter() {
        RVAdapter adapter = new RVAdapter(persons);
        rv.setAdapter(adapter);
    }
}