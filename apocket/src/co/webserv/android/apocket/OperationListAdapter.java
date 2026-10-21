package co.webserv.android.apocket;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class OperationListAdapter extends SimpleAdapter {

    private List<Operation> mOperations;

    @SuppressWarnings("unchecked")
    public OperationListAdapter(Context context,
            List<? extends Map<String, String>> operations, int resource,
            String[] from, int[] to) {
        super(context, operations, resource, from, to);
        mOperations = (List<Operation>) operations;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public long getItemId(int position) {
        return mOperations.get(position).mId;
    }
}
/* $Id$ */