package com.inventorydelivery;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends BaseActivty implements OnItemClickListener {

	String progress_dismiss="";
	private Timer autoUpdate;
	private ListView mOrderedProductsListView = null;
	ProgressDialog progress = null;
	public static ArrayList<Order_List> mOrderedProducts=new ArrayList<Order_List>();
	public static ArrayList<Inventory_Object> mInventoryList=new ArrayList<Inventory_Object>();
	String str_Update="";

	String empID="",wHouesID="",empType="";

	private String Webresponse = "";
	private final String NAMESPACE = "http://beverli.com";

	private final String URL = "http://27.7.112.29:8080/axis/services/OrderSummaryProcessorPort_V1.0?wsdl";

	private final String SOAP_ACTION = "http://beverli.com/OrderSummaryProcessor/OrderSummary_V1.0";


	private final String METHOD_NAME = "OrderSummary_V1.0";
	
	
	
	private final String OC_URL = "http://27.7.112.29:8080/axis/services/OrderCountProcessorPort_V1.0?wsdl";

	private final String OC_SOAP_ACTION = "http://beverli.com/OrderCountProcessor/OrderCount_V1.0";


	private final String OC_METHOD_NAME = "OrderCount_V1.0";
	//{http://beverli.com/CashCollectionSummaryProcessor/}CashCollectionSummaryProcessorPort_V1.0SoapBinding
	private final String CCURL = "http://27.7.112.29:8080/axis/services/CashCollectionSummaryProcessorPort_V1.0?wsdl";

	private final String CCSOAP_ACTION = "http://beverli.com/CashCollectionSummaryProcessor/CashCollectionSummary_V1.0";


	private final String CCMETHOD_NAME = "CashCollectionSummary_V1.0";


	//http://27.7.112.29:8080/axis/services/InventorySummaryProcessorPort_V1.0?wsdl
	private final String ISURL = "http://27.7.112.29:8080/axis/services/InventorySummaryProcessorPort_V1.0?wsdl";

	private final String ISSOAP_ACTION = "http://beverli.com/InventorySummaryProcessor/InventorySummary_V1.0";


	private final String ISMETHOD_NAME = "InventorySummary_V1.0";


	// http://27.7.112.29:8080/axis/services/CashCollectionSummaryProcessorPort_V1.0?wsdl
	private Thread thread;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_home);

		
		
		Bundle b=getIntent().getExtras();

		if(b!=null){
			wHouesID=b.getString("wHouseID");//KA53A8280
			empID=b.getString("empID");
			empType=b.getString("empType");
		}

		initViews();

		initListeners();


		if(empType.equalsIgnoreCase("3")){
			myOrderSUmmary();
			((TextView)findViewById(R.id.title)).setText("Orders Received");
		}else if(empType.equalsIgnoreCase("4")){
			((TextView)findViewById(R.id.title)).setText("Inventory Stock");
			((TextView)findViewById(R.id.title)).setTextSize(22);
			stock_inventory();
		}else if(empType.equalsIgnoreCase("5")){
			cashCollectionSummary();
			((TextView)findViewById(R.id.title)).setText("Cash Collection Summary");
			((TextView)findViewById(R.id.title)).setTextSize(22);
		}

  
	}



	public void myOrderSUmmary(){
		mOrderedProducts.clear();
		
		progress = new ProgressDialog(HomeActivity.this);
		progress.setCancelable(false);
		String plz_craetedb = "Please Wait... while loading Orders List";
		progress.setMessage(plz_craetedb);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setProgress(0);
		progress.setMax(100);
		progress.show();
		
	
			
		
		thread = new Thread(){
			public void run(){
				try{
					SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
					request.addProperty("wareHouseID", wHouesID);  //  EMP100001
					request.addProperty("Emp_ID", empID);         //   EMP100001


					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				// envelope.dotNet = true;
					
					envelope.setOutputSoapObject(request);
					AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
					Order_List orderdata;

					androidHttpTransport.call(SOAP_ACTION, envelope);
					SoapObject object=(SoapObject)envelope.bodyIn;

					mOrderedProducts.clear();
					if(object.getPropertyCount()!=0){
					
					for(int i=0;i<object.getPropertyCount();i++){
						SoapObject newObj=(SoapObject) object.getProperty(i);

						System.out.println("TEST nnn "+newObj.getProperty("cust_ID"));
						String custid=newObj.getProperty("cust_ID").toString();
						String custadd=newObj.getProperty("cust_Address").toString();
						String custdeldate=newObj.getProperty("delivery_Date").toString();
						String custfuturedate=newObj.getProperty("future_1").toString();

						orderdata=new Order_List(custid,custadd,custdeldate,custfuturedate);//newObj.getProperty("cust_ID").toString(), newObj.getProperty("cust_Address").toString(), newObj.getProperty("delivery_Date").toString());

						mOrderedProducts.add(orderdata);
					}
					}else{
						Toast.makeText(getApplicationContext(), "No Records Available!", Toast.LENGTH_SHORT).show();
					}
					
				}

				catch(Exception e){
					e.printStackTrace();
				}
				
			
				handler.post(createUI);
			}
		};
		

		thread.start();
	}
	
	
	

	///////////////////////////////cash collection summary///////////////////////////


	public void cashCollectionSummary(){
		mOrderedProducts.clear();

		progress = new ProgressDialog(HomeActivity.this);
		progress.setCancelable(false);
				
			String plz_craetedb = "Please Wait... while loading Cash Collection Details";
		
		
		progress.setMessage(plz_craetedb);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setProgress(0);
		progress.setMax(100);
		progress.show();
		thread = new Thread(){
			public void run(){
				try{
					SoapObject request = new SoapObject(NAMESPACE, CCMETHOD_NAME);


					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					//envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(CCURL);
					Order_List orderdata;

					androidHttpTransport.call(CCSOAP_ACTION, envelope);
					SoapObject object=(SoapObject)envelope.bodyIn;
					
					if(object.getPropertyCount()!=0){
					
					for(int i=0;i<object.getPropertyCount();i++){
						SoapObject newObj=(SoapObject) object.getProperty(i);
						String bank_accno=newObj.getProperty("Bank_ACNO_Str").toString();
						String bank_branch=newObj.getProperty("Bank_Branch_Str").toString();
						String bank_balance=newObj.getProperty("Bank_Balance_Int").toString(); 

						orderdata=new Order_List(bank_accno,bank_branch,bank_balance,"");//newObj.getProperty("cust_ID").toString(), newObj.getProperty("cust_Address").toString(), newObj.getProperty("delivery_Date").toString());

						mOrderedProducts.add(orderdata);
					}
					}else{
						Toast.makeText(getApplicationContext(), "No Records Available!", Toast.LENGTH_SHORT).show();
					}



				}

				catch(Exception e){
					e.printStackTrace();
				}

				handler.post(createUI);
			}
		};

		thread.start();
	}

	final Runnable createUI = new Runnable() {
		public void run(){


			OrderedProductsAdapter orderedProductsAdapter=new OrderedProductsAdapter(ACTIVTY_INSTANCE, mOrderedProducts);
			
			//orderedProductsAdapter.notifyDataSetChanged();
			mOrderedProductsListView.setAdapter(orderedProductsAdapter);
		
			
			progress.dismiss();
		}
	};
	
	


	/////////////////////////////stock inventory

	public void stock_inventory(){
		mOrderedProducts.clear();

		progress = new ProgressDialog(HomeActivity.this);
		progress.setCancelable(false);
		String plz_craetedb = "Please Wait... while loading Stock Details";
		progress.setMessage(plz_craetedb);
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setProgress(0);
		progress.setMax(100);
		progress.show();
		thread = new Thread(){
			public void run(){
				try{
					SoapObject request = new SoapObject(NAMESPACE, ISMETHOD_NAME);


					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
					//envelope.dotNet = true;
					envelope.setOutputSoapObject(request);
					AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(ISURL);
					Order_List orderdata;

					Inventory_Object inventory_Object=null;
					androidHttpTransport.call(ISSOAP_ACTION, envelope);
					SoapObject object=(SoapObject)envelope.bodyIn;
					mOrderedProducts.clear();
					if(object.getPropertyCount()!=0){
					
					for(int i=0;i<object.getPropertyCount();i++){
						SoapObject newObj=(SoapObject) object.getProperty(i);
						String wh_Name_Str=newObj.getProperty("wh_Name_Str").toString();
						String Full_Stock_int=newObj.getProperty("Full_Stock_int").toString();
						String Empty_Stock_int=newObj.getProperty("Empty_Stock_int").toString();
						
						String rol_int=newObj.getProperty("rol_int").toString();
						String required_Stock_int=newObj.getProperty("required_Stock_int").toString();
						
						String wh_ID_Str=newObj.getProperty("wh_ID_Str").toString();

						orderdata=new Order_List(wh_Name_Str,Full_Stock_int,Empty_Stock_int,"");//newObj.getProperty("cust_ID").toString(), newObj.getProperty("cust_Address").toString(), newObj.getProperty("delivery_Date").toString());

						inventory_Object=new Inventory_Object(wh_ID_Str,wh_Name_Str, Full_Stock_int, Empty_Stock_int, rol_int, required_Stock_int);
						mInventoryList.add(inventory_Object);
						mOrderedProducts.add(orderdata);
					}
					}else{
						Toast.makeText(getApplicationContext(), "No Records Available", Toast.LENGTH_LONG).show();
						finish();
					}



				}

				catch(Exception e){
					e.printStackTrace();
				}

				handler.post(createUI);
			}
		};

		thread.start();
	}

	
	private void initViews() {
		mOrderedProductsListView = (ListView) findViewById(R.id.listView_ordered_products);
	}

	private void initListeners() {
		mOrderedProductsListView.setOnItemClickListener(this);
	}
	String emapname;
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		if(empType.equalsIgnoreCase("3")){
			Intent detailsIntent=new Intent(ACTIVTY_INSTANCE,DetailsActivity.class);
			detailsIntent.putExtra("mOrderedcustid", mOrderedProducts.get(arg2).cust_ID);
			detailsIntent.putExtra("mOrderedcustadd", mOrderedProducts.get(arg2).cust_Address);
			detailsIntent.putExtra("mOrderedcustdeldate", mOrderedProducts.get(arg2).cust_del_date);//empID //wHouesID
			detailsIntent.putExtra("mEmpID", empID);
			detailsIntent.putExtra("mWHID", wHouesID);
			startActivity(detailsIntent);
			
		}
		else if(empType.equalsIgnoreCase("5"))
		{
			//Cash collection 

			String tEmp_old_bal = null;

			try {
				for(int i=0;i<=mOrderedProducts.size();i++){
					if(empID.equalsIgnoreCase(mOrderedProducts.get(i).cust_ID)){
						emapname= mOrderedProducts.get(i).cust_Address;

						tEmp_old_bal= mOrderedProducts.get(i).cust_del_date;
					}


				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			Intent detailsIntent=new Intent(ACTIVTY_INSTANCE,Cash_DetailsActivity.class);
			detailsIntent.putExtra("fCust_id", mOrderedProducts.get(arg2).cust_ID);
			detailsIntent.putExtra("fCust_name", mOrderedProducts.get(arg2).cust_Address);
			detailsIntent.putExtra("fOld_bal", mOrderedProducts.get(arg2).cust_del_date);//empID //wHouesID
			detailsIntent.putExtra("mEmpID", empID);
			detailsIntent.putExtra("mEmpname", emapname);
			detailsIntent.putExtra("empType", empType);
			detailsIntent.putExtra("mWHID", wHouesID);
			detailsIntent.putExtra("tOld_bal", tEmp_old_bal);
			startActivity(detailsIntent);

		}else if(empType.equalsIgnoreCase("4")){
			

			
			try {
				for(int i=0;i<=mInventoryList.size();i++){
					if(wHouesID.equalsIgnoreCase(mInventoryList.get(i).wh_ID_Str)){

						fWh_name=mInventoryList.get(i).wh_Name_Str;
						fOld_stock=mInventoryList.get(i).Full_Stock_int;
						fEmpt_stock=mInventoryList.get(i).Empty_Stock_int;
					}


				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(Integer.parseInt(mInventoryList.get(arg2).Full_Stock_int)==0  && wHouesID.equalsIgnoreCase(mInventoryList.get(arg2).wh_ID_Str)){
				Intent inventoryIntent=new Intent(ACTIVTY_INSTANCE,Inv_StockFillActivity.class);
				inventoryIntent.putExtra("emp_id", empID);

				inventoryIntent.putExtra("cust_name", wHouesID);//mOrderedProducts.get(arg2).cust_ID);
				
				
				startActivity(inventoryIntent);
			}else{
			
			Intent inventoryIntent=new Intent(ACTIVTY_INSTANCE,InventoryActivity.class);
			inventoryIntent.putExtra("required_Stock_int", mInventoryList.get(arg2).required_Stock_int);

			inventoryIntent.putExtra("Empty_Stock_int", mInventoryList.get(arg2).Empty_Stock_int);
			inventoryIntent.putExtra("Full_Stock_int", mInventoryList.get(arg2).Full_Stock_int);
			inventoryIntent.putExtra("rol_int", mInventoryList.get(arg2).rol_int);
			inventoryIntent.putExtra("wh_Name_Str", mInventoryList.get(arg2).wh_Name_Str);
			inventoryIntent.putExtra("fEmp_id", empID);
			inventoryIntent.putExtra("wh_ID_Str", mInventoryList.get(arg2).wh_ID_Str);
			inventoryIntent.putExtra("twh_id", wHouesID);
			
			inventoryIntent.putExtra("fWh_name", fWh_name);
			inventoryIntent.putExtra("fOld_stock", fOld_stock);
			inventoryIntent.putExtra("fEmpt_stock", fEmpt_stock);
			
			inventoryIntent.putExtra("WH_ID", wHouesID);
			
			startActivity(inventoryIntent);
			}
		}
	}
	String fWh_name="",fOld_stock="",fEmpt_stock="";
	public class Order_List{
		String cust_ID;
		String cust_Address;
		String cust_del_date;
		String future_Date;
		public Order_List(String custid,String custaddress,String custdeldate, String cust_furedate){
			cust_ID=custid;
			cust_Address=custaddress;
			cust_del_date=custdeldate;
			future_Date=cust_furedate;
		}



	}
	
	
	
	public class Inventory_Object{
		String wh_Name_Str,Full_Stock_int,Empty_Stock_int,rol_int,required_Stock_int,wh_ID_Str;        
        
		public Inventory_Object(String wh_ID_Str,String wh_Name_Str,String Full_Stock_int,String Empty_Stock_int,String rol_int,String required_Stock_int){
			 this.wh_Name_Str=wh_Name_Str;
			 this.Full_Stock_int=Full_Stock_int;
			 this.Empty_Stock_int=Empty_Stock_int;
			 this.rol_int=rol_int;
			 this.required_Stock_int=required_Stock_int;
			 this.wh_ID_Str=wh_ID_Str;
		}



	}


	public class OrderedProductsAdapter extends BaseAdapter {

		private ArrayList<Order_List> mOrderedProducts = null;
		private Context mContext = null;

		public OrderedProductsAdapter(Context context,ArrayList<Order_List> mOrderedProducts2) {
			mContext = context;
			mOrderedProducts = mOrderedProducts2;
		}

		@Override
		public int getCount() {


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

			if(empType.equalsIgnoreCase("3")){	
				holder.txtViewOrderedProduct.setText("Customer ID: "+mOrderedProducts.get(arg0).cust_ID+"\n"+"Customer Address: "+mOrderedProducts.get(arg0).cust_Address+"\n"+"Delivery Date :"+mOrderedProducts.get(arg0).cust_del_date+"\n"+"Customer Type :"+mOrderedProducts.get(arg0).future_Date);
			}else if(empType.equalsIgnoreCase("4")){
				
				holder.txtViewOrderedProduct.setText("Name: "+mOrderedProducts.get(arg0).cust_ID+"\n"+"Full Stock: "+mOrderedProducts.get(arg0).cust_Address+"\n"+"Empty Stock :"+mOrderedProducts.get(arg0).cust_del_date);
			}else if(empType.equalsIgnoreCase("5")){	
				holder.txtViewOrderedProduct.setText("Employee Id: "+mOrderedProducts.get(arg0).cust_ID+"\n"+"Name: "+mOrderedProducts.get(arg0).cust_Address+"\n"+"Balance :"+mOrderedProducts.get(arg0).cust_del_date);
			}
			
			//showNotification("total arders"+mOrderedProducts.size());//+"newly added"+(mOrderedProducts.size()-Integer.parseInt(orderListSize)));
			
			return arg1;
		}

		private class ViewHolder {
			TextView txtViewOrderedProduct = null;
			TextView txtViewOrderedProductTimeLeft = null;
		}
	}
	
	/* @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if(resultCode==1){
         Intent refresh = new Intent(this, HomeActivity.class);
         startActivity(refresh);
        // this.finish();
      }
	 }
	*/
	
	
	@Override
	 public void onResume() {
	  super.onResume();
	  autoUpdate = new Timer();
	  autoUpdate.schedule(new TimerTask() {
	   @Override
	   public void run() {
	    runOnUiThread(new Runnable() {
	     public void run() {
	    	 
	    	 
	    	 
	      updateHTML();
	      
	      
	     }
	    });
	   }
	  }, 0, 120000);
	 }

	 private void updateHTML(){
	  // your logic here
		 mInventoryList.clear();
			initViews();

			initListeners();


			if(empType.equalsIgnoreCase("3")){
				progress.dismiss();  
				
				str_Update="order_details";
				  myOrderSUmmary();
					
			
				//myOrderSUmmary();
				((TextView)findViewById(R.id.title)).setText("Orders Received");
				((TextView)findViewById(R.id.title)).setTextSize(22);
			//	}
				}else if(empType.equalsIgnoreCase("4")){
					str_Update="inventory_stock";
				progress.dismiss();
				((TextView)findViewById(R.id.title)).setText("Inventory Stock");
				((TextView)findViewById(R.id.title)).setTextSize(22);
				stock_inventory();
			}else if(empType.equalsIgnoreCase("5")){
				str_Update="cash_Collection";
				progress.dismiss();
				cashCollectionSummary();
				((TextView)findViewById(R.id.title)).setText("Cash Collection Summary");
				((TextView)findViewById(R.id.title)).setTextSize(22);
			}
	 }
	 
	 @SuppressLint("NewApi")
		public void showNotification(){

	        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

	        PendingIntent pIntent=PendingIntent.getActivity(this, 0,new Intent(this, HomeActivity.class),0);
			
	        NotificationCompat.Builder mNotification = new NotificationCompat.Builder(this)

	            .setContentTitle("New order added")
	            .setContentText("Orders added now")
	            .setSmallIcon(R.drawable.ic_launcher)
	            .setContentIntent(pIntent)
	            .setSound(soundUri);
	            //.build();

	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	        // If you want to hide the notification after it was selected, do the code below
	        // my Notification.flags |= Notification.FLAG_AUTO_CANCEL;

	        notificationManager.notify(0, mNotification.build());
	    }


	 @Override
	 public void onPause() {
	  autoUpdate.cancel();
	  super.onPause();
	 }

}
