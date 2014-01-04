package com.inventorydelivery.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.inventorydelivery.HomeActivity.Order_List;
import com.inventorydelivery.R;

public class OrderedProductsAdapter2 extends BaseAdapter {

	private ArrayList<Order_List> mOrderedProducts = null;
	private Context mContext = null;

	public OrderedProductsAdapter2(Context context,ArrayList<Order_List> mOrderedProducts2) {
		mContext = context;
		mOrderedProducts = mOrderedProducts2;
	}

	@Override
	public int getCount() {
		
		System.out.println("size of the order"+mOrderedProducts.size());

		return mOrderedProducts.size();
	}

	@Override
	public Object getItem(int arg0) {

		return mOrderedProducts.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {

		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		ViewHolder holder;
		if (arg1 == null) {
			holder = new ViewHolder();
			arg1 = View.inflate(mContext, R.layout.ordered_product, null);
			holder.txtViewOrderedProduct = (TextView) arg1
					.findViewById(R.id.txtView_OrderedProduct);
			holder.txtViewOrderedProductTimeLeft = (TextView) arg1
					.findViewById(R.id.txtView_OrderedProduct_timeleft);
			arg1.setTag(holder);

		} else {
			holder = (ViewHolder) arg1.getTag();
		}
		
		
		String res_data=mOrderedProducts.get(arg0).toString();
		
		holder.txtViewOrderedProduct.setText(res_data);
		//holder.txtViewOrderedProductTimeLeft.setText("00:00:00");

		return arg1;
	}

	private class ViewHolder {
		TextView txtViewOrderedProduct = null;
		TextView txtViewOrderedProductTimeLeft = null;
	}
}
