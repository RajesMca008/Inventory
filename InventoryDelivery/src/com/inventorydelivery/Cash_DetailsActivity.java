package com.inventorydelivery;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


public class Cash_DetailsActivity extends BaseActivty implements OnClickListener,Serializable {

	TextView mTvProductName = null;
	ProgressDialog progress = null;
	int amt_to_pay,amt_due,pending_jars,order_status;
	TextView custr_Name,cust_name,cust_Loc;
	EditText amount_to_collect;
	CheckBox cb_order_status;
	Button mBtnSubmit = null;
	RadioButton paytype;	
	String tEmpType="",tName="",tOld_bal="0",payType_str,result_ID,whouse_ID,fCust_id="",fCust_name="",fOld_bal="",cust_loc,tEmp_ID,cust_Jars_Regd,cust_Jar_Cost,cust_Balance,cust_Jars_Return,pay_mode;
	String result_str,cust_Name,cust_Number,cust_Place;
	 Boolean isInternetPresent = false;
	 
	 Double latitude;
     Double longitude;
	 int jars_delivered;
	 GPSTracker gps;
     
	  private final String NAMESPACE = "http://beverli.com";
	    
	    private final String URL = "http://27.7.112.29:8080/axis/services/CashCollectionProcessorPort_V1.0?wsdl";
	    
	    private final String SOAP_ACTION = "http://beverli.com/CashCollectionProcessor/CashCollection_V1.0";
	    
	    
	    private final String METHOD_NAME = "CashCollection_V1.0";
    
	  
	    
	    // Connection detector class
	    ConnectionDetector cd;
	    private Thread thread;
	    private Handler handler = new Handler();  
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cashcollection_details);
		Bundle b=getIntent().getExtras();
		if(b!=null){
			fCust_id=b.getString("fCust_id");
			fCust_name=b.getString("fCust_name");
			fOld_bal=b.getString("fOld_bal");
			tEmp_ID=b.getString("mEmpID");
			whouse_ID=b.getString("mWHID");
			tEmpType=b.getString("empType");
			tName=b.getString("mEmpname");
			tOld_bal=b.getString("tOld_bal");
			
			
			
		}
		
		initViews();
	
		custr_Name.setText(fCust_id);
    	  cust_name.setText(fCust_name);
    	  cust_Loc.setText(fOld_bal);
		
		
		amount_to_collect=(EditText)findViewById(R.id.amount_to_collect);
			initListeners();
		}

	 
	
	
	private void initViews() {
		mTvProductName = (TextView) findViewById(R.id.title);
		mBtnSubmit =     (Button)   findViewById(R.id.btn_submit);
		custr_Name=      (TextView) findViewById(R.id.tv_name);
		cust_name=  (TextView) findViewById(R.id.tv_contact_number);
		cust_Loc=        (TextView) findViewById(R.id.tv_place);
		
	}

	private void initListeners() {
		mBtnSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
			jars_delivered=Integer.parseInt(amount_to_collect.getText().toString());
			validateCredentials();
			break;

		default:
			break;
		}

	}
	
	public void validateCredentials(){
		
		   progress = new ProgressDialog(Cash_DetailsActivity.this);
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
        	  DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
              //get current date time with Date()
              Date date = new Date();
              System.out.println(dateFormat.format(date));
        	  
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("transfer_Date",dateFormat.format(date));//EMP100001
            request.addProperty("fEmp_Name_Str",fCust_name );//EMP100001
            request.addProperty("fEmp_ID_Str",fCust_id);
            request.addProperty("fEmp_oldbal_int",Integer.parseInt(fOld_bal));
            request.addProperty("fEmp_newbal_int",Integer.parseInt(fOld_bal)-Integer.parseInt(amount_to_collect.getText().toString()));
            request.addProperty("tEmp_Name_Str",tName );
            request.addProperty("tEmp_ID_Str",tEmp_ID);
            request.addProperty("tEmp_oldbal_int",Integer.parseInt(tOld_bal));
            request.addProperty("tEmp_newbal_int",Integer.parseInt(tOld_bal)+Integer.parseInt(amount_to_collect.getText().toString()));
            request.addProperty("amt_Transferred_int",Integer.parseInt(amount_to_collect.getText().toString()));
            
            
              SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
               //envelope.dotNet = true;
               envelope.setOutputSoapObject(request);
               AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
              androidHttpTransport.call(SOAP_ACTION, envelope);
                  SoapObject object=(SoapObject)envelope.bodyIn;
                  
                 // System.out.println("my result set value:"+object.getProperty(0).toString());
      
        	   
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
        	Toast.makeText(getApplicationContext(), "Cash Recieved Succesfullt", Toast.LENGTH_SHORT).show();
        	
       		finish();
      	 
      	progress.dismiss();
  	     
       }
       };
       
   
       
       

}
