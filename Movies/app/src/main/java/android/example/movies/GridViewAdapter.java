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
 * Created by Ashok on 11/10/2015.
 */
public class GridViewAdapter extends BaseAdapter {

    List<MoviesModel> moviesList=new ArrayList<MoviesModel>();

    private Context mContext;
    LayoutInflater inflater;
    public GridViewAdapter(Context c){
        mContext = c;
    }
    public GridViewAdapter(Context c,List<MoviesModel> list) {
        mContext = c;
        this.moviesList=list;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return moviesList.size();
    }

    @Override
    public MoviesModel getItem(int position) {
        // TODO Auto-generated method stub
        return moviesList.get(position);
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

        //  System.out.println("iam in get view");
        if (convertView == null) {
            viewHolder=new ViewHolder();

            convertView = inflater.inflate(R.layout.movies_layout, null);

            viewHolder.imageView1 = (ImageView)convertView.findViewById(R.id.movie_image);
           /*viewHolder.imageView2 = (ImageView)convertView.findViewById(R.id.movie_big_image);
            viewHolder.overview=(TextView)convertView.findViewById(R.id.movie_overview);
            viewHolder.title=(TextView)convertView.findViewById(R.id.movie_title);
            viewHolder.rating=(TextView)convertView.findViewById(R.id.movie_rating);
            viewHolder.releaseDate=(TextView)convertView.findViewById(R.id.movie_releasedate);*/

            convertView.setTag(viewHolder);

        } else {
            viewHolder=(ViewHolder)convertView.getTag();
            MoviesModel iModel=moviesList.get(position);
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w500/" + moviesList.get(position).getMovieImage()).into(viewHolder.imageView1);
           /* Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + moviesList.get(position).getMovieImage()).into(viewHolder.imageView2);
            viewHolder.overview.setText(moviesList.get(position).getOverView());
            viewHolder.title.setText(moviesList.get(position).getTitle());
            viewHolder.rating.setText(moviesList.get(position).getRating());
            viewHolder.releaseDate.setText(moviesList.get(position).getReleaseDate());*/
        }


        return convertView;
    }

    static class ViewHolder{

        ImageView imageView1;
        //TextView title,overview,rating,releaseDate;

    }

}
