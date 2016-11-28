package me.instabattle.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.instabattle.app.Entry;
import me.instabattle.app.R;

/**
 * Created by wackloner on 28.11.2016.
 */

public class EntryListAdapter extends BaseAdapter {
    private List<Entry> entries;
    private Context context;
    private LayoutInflater inflater;

    public EntryListAdapter(Context context, List<Entry> entries) {
        this.context = context;
        this.entries = entries;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View res = convertView;
        if (res == null) {
            res = inflater.inflate(R.layout.battle_entry_item, parent, false);
        }
        Entry entry = entries.get(position);
        ((ImageView) res.findViewById(R.id.listEntryImage)).setImageBitmap(entry.getPhoto());
        ((TextView) res.findViewById(R.id.listEntryAuthor)).setText(entry.getAuthor().getNickname());
        ((TextView) res.findViewById(R.id.listEntryUpvotes)).setText(entry.getUpvotes() + " upvotes");
        return res;
    }
}
