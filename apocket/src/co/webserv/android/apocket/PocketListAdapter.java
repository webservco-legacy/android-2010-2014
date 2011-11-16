package co.webserv.android.apocket;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class PocketListAdapter extends SimpleAdapter {

	private List<Pocket> mPockets;

	@SuppressWarnings("unchecked")
	public PocketListAdapter(Context context,
			List<? extends Map<String, String>> pockets, int resource,
			String[] from, int[] to) {
		super(context, pockets, resource, from, to);
		mPockets = (List<Pocket>) pockets;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return super.getView(position, convertView, parent);
	}

	@Override
	public long getItemId(int position) {
		return mPockets.get(position).mId;
	}
}
/* $Id$ */