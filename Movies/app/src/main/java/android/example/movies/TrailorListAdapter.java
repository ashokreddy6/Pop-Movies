package android.example.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashok on 12/21/2015.
 */
public class TrailorListAdapter extends BaseAdapter{

    List<MoviesTrailorModel> trailorList=new ArrayList<MoviesTrailorModel>();

    private Context mContext;
    LayoutInflater inflater;
    public TrailorListAdapter(Context c){
        mContext = c;
    }
    public TrailorListAdapter(Context c,List<MoviesTrailorModel> list) {
        mContext = c;
        this.trailorList=list;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return trailorList.size();
    }

    @Override
    public MoviesTrailorModel getItem(int position) {
        // TODO Auto-generated method stub
        return trailorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder;
        System.out.println("in get view::"+position);
        //  System.out.println("iam in get view");
        if (convertView == null) {
            System.out.println("in ifffff");
            viewHolder=new ViewHolder();

            convertView = inflater.inflate(R.layout.trailor_details, null);

            viewHolder.imageView1 = (ImageView)convertView.findViewById(R.id.trailorID);
            viewHolder.trailorName=(TextView)convertView.findViewById(R.id.trailorname);
            System.out.println("imageView1"+viewHolder.imageView1);
            System.out.println("test1"+viewHolder.trailorName);
            convertView.setTag(viewHolder);

        } else {
            viewHolder=(ViewHolder)convertView.getTag();
            MoviesTrailorModel iModel=trailorList.get(position);
            viewHolder.imageView1.setImageResource(R.drawable.icon_play);
            viewHolder.imageView1.setContentDescription(iModel.getKey());
           viewHolder.trailorName.setText(iModel.getTrailorName());
        }


        return convertView;
    }

    static class ViewHolder{

        ImageView imageView1;
        TextView trailorName;

    }
}
