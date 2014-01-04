package com.inventorydelivery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Inv_StockFillActivity extends Activity{

	//private final String NAMESPACE = "http://beverli.com";

	/*private final String URL = "http://27.7.112.29:8080/axis/services/StockTransferProcessorPort_V1.0?wsdl";

	private final String SOAP_ACTION = "http://beverli.com/StockTransferProcessor/StockTransfer_V1.0";


	private final String METHOD_NAME = "StockTransfer_V1.0";
	*/
	
	private final String NAMESPACE = "http://beverli.com";

	private final String URL = "http://27.7.112.29:8080/axis/services/StockProcProcessorPort_V1.0?wsdl";

	private final String SOAP_ACTION = "http://beverli.com/StockProcProcessor/StockProc_V1.0";
										//http://beverli.com/StockProcProcessor/StockProc_V1.0

	private final String METHOD_NAME = "StockProc_V1.0";

	private Button mBtnSubmit=null;
	ProgressDialog progress = null;
	
	  String emp_id,emp_name;
	
	EditText ven_Id,Veh_No,proc_Qty,empty_given,empty_taken,amt_paid,amt_paiddetails;
	TextView emp_Id,emp_Name;
	
	// Connection detector class
    ConnectionDetector cd;
    private Thread thread;
    private Handler handler = new Handler();  
    
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		setContentView(R.layout.stock_fill);
		
		Bundle b=getIntent().getExtras();
		if(b!=null){
			emp_id=b.getString("emp_id");
			emp_name=b.getString("cust_name");
		
		}
		
		// ven_Id,Veh_No,proc_Qty,empty_given,empty_taken,amt_paid,amt_paiddetails;
		ven_Id=(EditText)findViewById(R.id.et_venId);
		
		Veh_No=(EditText)findViewById(R.id.et_vehNO);
		proc_Qty=(EditText)findViewById(R.id.et_procQty);
		empty_given=(EditText)findViewById(R.id.et_emptyGiven);
		empty_taken=(EditText)findViewById(R.id.et_emptyTaken);
		amt_paid=(EditText)findViewById(R.id.et_amtPaid);
		amt_paiddetails=(EditText)findViewById(R.id.et_amtPaiddetails);
		emp_Id=(TextView)findViewById(R.id.et_EmpID);
		emp_Name=(TextView)findViewById(R.id.et_EmpName);
		emp_Id.setText(emp_id);
		emp_Name.setText(emp_name);
	
		
		
		mBtnSubmit =     (Button)   findViewById(R.id.btn_submit);
		mBtnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
							     
			     validateCredentials();

			}
		});
			
	}
	
	
	public void validateCredentials(){
		
		   progress = new ProgressDialog(Inv_StockFillActivity.this);
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
         request.addProperty("Vendor_ID_str",ven_Id.getText().toString());
         request.addProperty("Vehicle_No_str",Veh_No.getText().toString());
         request.addProperty("Proc_Qty_int",Integer.parseInt(proc_Qty.getText().toString()));
         
         request.addProperty("Empty_Given_int",Integer.parseInt(empty_given.getText().toString()));
         request.addProperty("Empty_Taken_int",Integer.parseInt(empty_taken.getText().toString()));
         request.addProperty("Amt_Paid_int",Integer.parseInt(amt_paid.getText().toString()));

         
         request.addProperty("Amt_Paid_Details",amt_paiddetails.getText().toString());
         request.addProperty("Emp_ID",emp_id);
         request.addProperty("Emp_Name",emp_name);
        
         
        
         
         
           SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            //envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
           androidHttpTransport.call(SOAP_ACTION, envelope);
               SoapObject object=(SoapObject)envelope.bodyIn;
               
               System.out.println("my result set value:"+object.getProperty(0).toString());
   
     	   
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
	        	Toast.makeText(getApplicationContext(), "Stock filled Succesfully", Toast.LENGTH_SHORT).show();
	        	
           		finish();
	      	 
	      	progress.dismiss();
	  	     
	       }
	       };
 
}
