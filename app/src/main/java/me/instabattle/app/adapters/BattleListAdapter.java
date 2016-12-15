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

import me.instabattle.app.models.Battle;
import me.instabattle.app.R;
import me.instabattle.app.settings.State;
import me.instabattle.app.activities.BattleActivity;
import me.instabattle.app.activities.BattleListActivity;
import me.instabattle.app.activities.MapActivity;
import me.instabattle.app.models.Entry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BattleListAdapter extends BaseAdapter {
    private List<Battle> battles;
    private Context context;
    private LayoutInflater inflater;

    public BattleListAdapter(Context context, List<Battle> battles) {
        this.context = context;
        this.battles = battles;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return battles.size();
    }

    @Override
    public Object getItem(int position) {
        return battles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View res = convertView != null ? convertView :
                inflater.inflate(R.layout.battle_list_item, parent, false);

        final Battle battle = battles.get(position);

        battle.getWinnerAndDo(new Callback<Entry>() {
            @Override
            public void onResponse(Call<Entry> call, Response<Entry> response) {
                Entry winner = response.body();
                ((ImageView) res.findViewById(R.id.battleListItemImage)).setImageBitmap(winner.getPhoto());
            }

            @Override
            public void onFailure(Call<Entry> call, Throwable t) {

            }
        });

        ((TextView) res.findViewById(R.id.battleListItemTitle)).setText(battle.getName());
        ((TextView) res.findViewById(R.id.battleListItemDate)).setText("Created on 28.11.16");
        ((TextView) res.findViewById(R.id.battleListItemCount)).setText(battle.getEntriesCount() + " photos");
        res.findViewById(R.id.battleListItemViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                State.chosenBattle = battle;
                BattleActivity.gotHereFrom = BattleListActivity.class;
                Intent viewBattle = new Intent(context, BattleActivity.class);
                context.startActivity(viewBattle);
            }
        });
        res.findViewById(R.id.battleListItemViewOnMapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapActivity.viewPoint = battle.getLocation();
                MapActivity.gotHereFrom = BattleListActivity.class;
                Intent viewMap = new Intent(context, MapActivity.class);
                context.startActivity(viewMap);
            }
        });
        return res;
    }
}