package swipe.android.berkeleyfacial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.edbert.library.utils.OptionsManager;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import swipe.android.berkeleyfacial.R;
import swipe.android.berkeleyfacial.model.Data;

public class FacesAdapter extends BaseAdapter {


    public List<Data> parkingList;
    public Context context;

    public FacesAdapter(List<Data> apps, Context context) {
        this.parkingList = apps;
        this.context = context;
    }

    @Override
    public int getCount() {
        return parkingList.size();
    }

    @Override
    public Object getItem(int position) {
        return parkingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = convertView;

        ViewHolder viewHolder;
        if (rowView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);//context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
            viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //  viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");

//            Glide.with(SwipeActivity.this).load(parkingList.get(position)).into(viewHolder.cardImage);
        ImageLoader.getInstance().displayImage(
                parkingList.get(position).getImagePath(), viewHolder.cardImage,
                OptionsManager.getDefaultOptions());

        return rowView;
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;
    }

}