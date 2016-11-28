package me.instabattle.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.instabattle.app.Battle;
import me.instabattle.app.Entry;
import me.instabattle.app.R;
import me.instabattle.app.State;
import me.instabattle.app.activities.BattleActivity;
import me.instabattle.app.activities.BattleListActivity;
import me.instabattle.app.activities.MapActivity;
import me.instabattle.app.activities.MyProfileActivity;

/**
 * Created by wackloner on 28.11.2016.
 */

public class UserEntryListAdapter extends BaseAdapter {
    private List<Entry> entries;
    private Context context;
    private LayoutInflater inflater;

    public UserEntryListAdapter(Context context, List<Entry> entries) {
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
            res = inflater.inflate(R.layout.user_entry_list_item, parent, false);
        }

        Entry entry = entries.get(position);
        final Battle battle = entry.getBattle();

        ((ImageView) res.findViewById(R.id.userEntryListItemImage)).setImageBitmap(entry.getPhoto());
        ((TextView) res.findViewById(R.id.userEntryListItemTitle)).setText(battle.getName());
        ((TextView) res.findViewById(R.id.userEntryListItemDate)).setText("Posted on 28.11.16");
        ((TextView) res.findViewById(R.id.userEntryListItemUpvotes)).setText(entry.getUpvotes() + " upvotes");
        res.findViewById(R.id.userEntryListItemViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.chosenBattle = battle;
                BattleActivity.gotHereFrom = MyProfileActivity.class;
                Intent viewBattle = new Intent(context, BattleActivity.class);
                context.startActivity(viewBattle);
            }
        });
        res.findViewById(R.id.userEntryListItemViewOnMapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.viewPoint = battle.getLocation();
                MapActivity.gotHereFrom = MyProfileActivity.class;
                Intent viewMap = new Intent(context, MapActivity.class);
                context.startActivity(viewMap);
            }
        });
        return res;
    }
}
