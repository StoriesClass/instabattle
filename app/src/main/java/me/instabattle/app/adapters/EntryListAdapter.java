package me.instabattle.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.instabattle.app.managers.BitmapCallback;
import me.instabattle.app.models.Entry;
import me.instabattle.app.R;
import me.instabattle.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EntryListAdapter extends BaseAdapter {

    private static final String TAG = "EntryListAdapter";

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
        final View res = convertView != null ? convertView :
                inflater.inflate(R.layout.entry_list_item, parent, false);

        final ImageView listEntryImage = (ImageView) res.findViewById(R.id.listEntryImage);
        final Entry entry = entries.get(position);

        if (listEntryImage.getDrawable() == null) {
            entry.getPhotoAndDo(new BitmapCallback() {
                @Override
                public void onResponse(final Bitmap photo) {
                    ((Activity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listEntryImage.setImageBitmap(photo);
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    //TODO
                    Log.e(TAG, "Can't get entry photo");
                }
            });
        }

        ((TextView) res.findViewById(R.id.listEntryUpvotes)).setText(entry.getRating() + " points");
        entry.getAuthorAndDo(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "got author");
                ((TextView) res.findViewById(R.id.listEntryAuthor)).setText(response.body().getUsername());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //TODO
                Log.e(TAG, "cant get entries author: " + t);
            }
        });
        return res;
    }
}
