package com.inventorydelivery;

import java.io.Serializable;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InventoryActivity extends Activity implements Serializable{

	private final String NAMESPACE = "http://beverli.com";

	private final String URL = "http://27.7.112.29:8080/axis/services/StockTransferProcessorPort_V1.0?wsdl";

	private final String SOAP_ACTION = "http://beverli.com/StockTransferProcessor/StockTransfer_V1.0";


	private final String METHOD_NAME = "StockTransfer_V1.0";

	private Button mBtnSubmit=null;
	ProgressDialog progress = null;
	
	String Empty_Stock_int,Full_Stock_int,rol_int,wh_Name_Str,fEmp_id,fWh_ID_Str,twh_id,fWh_name,fOld_stock,fEmpt_stock,wh_id;
	  
	String stock_transfer="",empty_jars;
	
	// Connection detector class
    ConnectionDetector cd;
    private Thread thread;
    private Handler handler = new Handler();  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.stock_transfer);
		
		Bundle b=getIntent().getExtras();
		if(b!=null){
			Empty_Stock_int=b.getString("Empty_Stock_int");
			Full_Stock_int=b.getString("Full_Stock_int");
			rol_int=b.getString("rol_int");
			wh_Name_Str=b.getString("wh_Name_Str");
			fEmp_id=b.getString("fEmp_id");
			fWh_ID_Str=b.getString("wh_ID_Str");
			twh_id=b.getString("twh_id");
			fWh_name=b.getString("fWh_name");
			fOld_stock=b.getString("fOld_stock");
			fEmpt_stock=b.getString("fEmpt_stock");
			wh_id=b.getString("WH_ID");
		}
		TextView wh_name=      (TextView) findViewById(R.id.tv_name);
		TextView wh_id=  (TextView) findViewById(R.id.tv_contact_number);
		TextView balence=        (TextView) findViewById(R.id.tv_place);
		TextView emptyjars=        (TextView) findViewById(R.id.tv_emptyjars);
		
		
		wh_name.setText(wh_Name_Str);
  	  wh_id.setText(fWh_ID_Str);
  	  balence.setText(Full_Stock_int);
  	emptyjars.setText(Empty_Stock_int);
		
		mBtnSubmit =     (Button)   findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				
				
				stock_transfer=((EditText)findViewById(R.id.stock_transfer)).getText().toString();
			     empty_jars=((EditText)findViewById(R.id.empty_jars)).getText().toString();
			     
			     if(stock_transfer.trim().length()>0)
			    	 
			    	 if(stock_transfer.equalsIgnoreCase("") && !empty_jars.equalsIgnoreCase("")){
			    		 Toast.makeText(getApplicationContext(), "Please Enter the stock Transfer", Toast.LENGTH_LONG).show();
			    		 
			    	 }else if(!stock_transfer.equalsIgnoreCase("") && empty_jars.equalsIgnoreCase("")){
			    		 Toast.makeText(getApplicationContext(), "Please Enter the stock Transfer", Toast.LENGTH_LONG).show();
			    	 }else{
			    		 validateCredentials();
			    	 }
			     
			     

			}
		});
		
		
		
		
		
		
		
	}
	
	
	public void validateCredentials(){
		
		   progress = new ProgressDialog(InventoryActivity.this);
					progress.setCancelable(false);
					String plz_craetedb = "Please Wait...";
					progress.setMessage(plz_craetedb);
					progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progress.setProgress(0);
					progress.setMax(100);
					progress.show();
		    	   
     thread = new Thread(){
      public void run(){
       try{
           //get current date time with Date()
            
          
     	  
         SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
         request.addProperty("fWH_ID",twh_id);// ok
         request.addProperty("fWH_Name",fWh_name);// ok
         request.addProperty("emp_ID",fEmp_id); // ok
         request.addProperty("tWH_ID",fWh_ID_Str);//ok
         request.addProperty("tWH_Name",wh_Name_Str);//ok
         request.addProperty("Stock_Transferred_int",Integer.parseInt(stock_transfer));//ok
         request.addProperty("fNew_Stock",0);//Integer.parseInt(stock_transfer));//
         request.addProperty("femt_new",0);//(Integer.parseInt(empty_jars)));
        
         
        // Empty_Stock_int=b.getString("Empty_Stock_int");
		//	Full_Stock_int=b.getString("Full_Stock_int");
         
         request.addProperty("tOld_Stock",(Integer.parseInt(Full_Stock_int))); // select fullstock
         request.addProperty("tNew_Stock",0);//Integer.parseInt(stock_transfer));
         request.addProperty("temt_Trans",(Integer.parseInt(empty_jars)));//ok
         request.addProperty("temt_old",Integer.parseInt(Empty_Stock_int)); // select fempty stock
         request.addProperty("temt_new", 0);//Integer.parseInt(stock_transfer));
         
         if(fOld_stock.equalsIgnoreCase("") && fEmpt_stock.equalsIgnoreCase("")){
        	 request.addProperty("fOld_Stock",0);//Integer.parseInt(fOld_stock)); //ok
             request.addProperty("femt_old",0);//(Integer.parseInt(fEmpt_stock)));// Empty_Stock_int;
            
         }else{
        	 request.addProperty("fOld_Stock",Integer.parseInt(fOld_stock)); //ok
             request.addProperty("femt_old",(Integer.parseInt(fEmpt_stock)));// Empty_Stock_int;
            
         }
         
         
       
         
        
         
         
           SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
           // envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
           androidHttpTransport.call(SOAP_ACTION, envelope);
              // SoapObject object=(SoapObject)envelope.bodyIn;
               
             //  System.out.println("my result set value:"+object.getProperty(0).toString());
           
          /* if (envelope.bodyIn instanceof SoapFault) {
               String str= ((SoapFault) envelope.bodyIn).faultstring;
               Log.i("", str);
           } else {*/
               SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
               Log.d("WS", String.valueOf(resultsRequestSOAP));
           //}
   
     	   
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
	        	
	        	/*custr_Name.setText(cust_Name);
	      	  contact_Number.setText(cust_Number);
	      	  cust_Loc.setText(cust_loc);*/
	        	Toast.makeText(getApplicationContext(), "Stock Recieved Succesfully", Toast.LENGTH_SHORT).show();
	        	
           		finish();
	      	 
	      	progress.dismiss();
	  	     
	       }
	       };
 
}
