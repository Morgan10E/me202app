package com.lfg.morgantenney.capsa;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by morgantenney on 5/10/16.
 */
public class GardenAdapter extends ArrayAdapter<Garden> {
    public GardenAdapter(Context context, int l, ArrayList<Garden> data) {
        super(context, l, data);
        setNotifyOnChange(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.garden_row_layout, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.gardenName = (TextView)row.findViewById(R.id.gardenName);
            holder.gardenWaterLevel = (TextView)row.findViewById(R.id.gardenWaterLevel);
            holder.gardenLightStatus = (TextView)row.findViewById(R.id.gardenLightStatus);
            holder.gardenPH = (TextView)row.findViewById(R.id.gardenPH);
            holder.gardenConduct = (TextView)row.findViewById(R.id.gardenConductivity);
            row.setTag(holder);
        }
        Garden entry = getItem(position);

        ViewHolder h = (ViewHolder)row.getTag();
        h.gardenName.setText(entry.name);
        h.gardenWaterLevel.setText(entry.status.waterLevel);
        h.gardenLightStatus.setText(entry.status.lightState);
        h.gardenPH.setText(entry.status.phValue);
        h.gardenConduct.setText(entry.status.conductivity);

//        if (entry.status.waterLevel.equals(getString(R.string.water_level_fine))) {
//            h.gardenWaterLevel.setTextColor(getResources().getColor(R.color.primary_color));
//        } else {
//            h.gardenWaterLevel.setTextColor(getResources().getColor(R.color.red));
//        }

        return row;
    }

    static class ViewHolder {
        public TextView gardenName;
        public TextView gardenWaterLevel;
        public TextView gardenLightStatus;
        public TextView gardenPH;
        public TextView gardenConduct;
    }

}
