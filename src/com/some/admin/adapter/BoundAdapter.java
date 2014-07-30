package com.some.admin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.some.admin.R;
import com.some.admin.model.Bound;

public class BoundAdapter extends BaseAdapter{
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Bound> mListBound;
	public BoundAdapter(Context context) {
		super();
		this.mContext = context;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setListBound(List<Bound> listBound){
		if(listBound != null && !listBound.isEmpty()){
			mListBound = listBound;
		}else{
			mListBound = null;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListBound != null ? mListBound.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListBound.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.layout_item_bound, null);
			holder = new ViewHolder();
			holder.mNamePlayer = (TextView)convertView.findViewById(R.id.tvNameBound);
			holder.mMoneyPlayer = (TextView)convertView.findViewById(R.id.tvMoneyBound);
			holder.mDate = (TextView)convertView.findViewById(R.id.tvDateBound);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final Bound item = mListBound.get(position);
		holder.mNamePlayer.setText(item.getUname());
		holder.mMoneyPlayer.setText(item.getMoney());
		holder.mDate.setText(item.getCtime());
		return convertView;
	}
	
	private class ViewHolder{
		private TextView mNamePlayer;
		private TextView mMoneyPlayer;
		private TextView mDate;
	}

}
