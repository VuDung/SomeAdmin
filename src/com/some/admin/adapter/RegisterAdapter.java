package com.some.admin.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.some.admin.R;
import com.some.admin.model.Register;

public class RegisterAdapter extends BaseAdapter{

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Register> mListRegister;
	public RegisterAdapter (Context context) {
		super();
		this.mContext = context;
		mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setListRegister(List<Register> listRegister){
		if(listRegister != null && !listRegister.isEmpty()){
			mListRegister = listRegister;
		}else{
			mListRegister = null;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mListRegister != null ? mListRegister.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mListRegister.get(position);
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
			convertView = mInflater.inflate(R.layout.layout_item_register, null);
			holder = new ViewHolder();
			holder.mNameRegister = (TextView)convertView.findViewById(R.id.tvNameRegister);
			holder.mDateRegister = (TextView)convertView.findViewById(R.id.tvDateRegister);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		final Register item = mListRegister.get(position);
		holder.mNameRegister.setText(item.getUserRegister());
		holder.mDateRegister.setText(item.getCtime());
		return convertView;
	}
	
	private class ViewHolder{
		private TextView mNameRegister;
		private TextView mDateRegister;
	}

}
