package com.rameshmklll.church;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;


public class Gallery extends Fragment {

    GridView androidGridView;
    View mv;
    Integer[] imageIDs = {
            R.drawable.c1, R.drawable.c2, R.drawable.c1,
            R.drawable.c1, R.drawable.c2, R.drawable.c1,
            R.drawable.c1, R.drawable.c2, R.drawable.c2,
            R.drawable.c1, R.drawable.c2, R.drawable.c2,
            R.drawable.c1, R.drawable.c2, R.drawable.c2,
            R.drawable.c1, R.drawable.c2, R.drawable.c2,
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Gallery");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mv=inflater.inflate(R.layout.fragment_gallery, container, false);;
        // Inflate the layout for this fragment


        androidGridView = (GridView)mv. findViewById(R.id.gridview);
        androidGridView.setAdapter(new ImageAdapterGridView(getActivity()));

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Toast.makeText(getActivity(), "Grid Item " + (position ) + " Selected", Toast.LENGTH_LONG).show();
                Intent intent =  new Intent(getActivity(), ImageFullView.class);
                intent.putExtra("pos", position);
                getActivity().startActivity(intent);

            }
        });
        return mv;
    }




    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);
                mImageView.setLayoutParams(new GridView.LayoutParams(130, 130));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }

}


