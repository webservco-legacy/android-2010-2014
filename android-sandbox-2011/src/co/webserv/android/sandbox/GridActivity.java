package co.webserv.android.sandbox;

import android.os.Bundle;
import android.widget.GridView;

public class GridActivity extends MenuActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.gridview);

        GridView gridview = (GridView) findViewById(R.id.gridview);

        gridview.setAdapter(new ImageAdapterGrid(this));
    }
}
/* $Id$ */