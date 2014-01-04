package com.inventorydelivery;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsActivity extends BaseActivty implements OnClickListener,Serializable{

	TextView mTvProductName = null;
	ProgressDialog progress = null;
	int amt_to_pay=0,amt_due,pending_jars=0,order_status;
	TextView custr_Name,contact_Number,cust_Loc,cust_jarcost,due_amt,jars_return;
	EditText et_jars_delivered, et_amount_to_pay,et_amount_paid,et_due_amount,et_jars_returned,et_jars_pending;
	CheckBox cb_order_status;
	Button mBtnSubmit = null;
	RadioButton paytype;	
	String empType="",payType_str,result_ID,whouse_ID,cust_ID="",cust_Add="",cust_del_date="",cust_loc,emp_ID,cust_Jars_Regd,cust_Jar_Cost,cust_Balance,cust_Jars_Return,pay_mode;
	String result_str,cust_Name,cust_Number,cust_Place;
	 Boolean isInternetPresent = false;
	 
	 Double latitude;
     Double longitude;
	 int jars_delivered;
	 GPSTracker gps;
     
	  private final String NAMESPACE = "http://beverli.com";
	    
	    private final String URL = "http://27.7.112.29:8080/axis/services/OrderDetailProcessorPort_V1.0?wsdl";
	    
	    private final String SOAP_ACTION = "http://beverli.com/OrderDetailProcessor/OrderDetail_V1.0";
	    
	    
	    private final String METHOD_NAME = "OrderDetail_V1.0";
    
	    private final String URL_OCR = "http://27.7.112.29:8080/axis/services/OrderCloseProcessorPort_V1.0?wsdl";
	   
	    private final String SOAP_ACTION_OCR = "http://beverli.com/OrderCloseProcessor/OrderClose_V1.0";
	    
	    
	    private final String METHOD_NAME_OCR = "OrderClose_V1.0";
	    
	    // Connection detector class
	    ConnectionDetector cd;
	    private Thread thread;
	    private Handler handler = new Handler();  
	    RadioGroup rg_payment_type;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_details);
		Bundle b=getIntent().getExtras();
		if(b!=null){
			cust_ID=b.getString("mOrderedcustid");
			cust_Add=b.getString("mOrderedcustadd");
			cust_del_date=b.getString("mOrderedcustdeldate");
			emp_ID=b.getString("mEmpID");
			whouse_ID=b.getString("mWHID");
			empType=b.getString("empType");
			
		}
		
		
		rg_payment_type=(RadioGroup)findViewById(R.id.rg_payment_type);
		initViews();
		
		et_jars_delivered=(EditText)findViewById(R.id.et_jars_delivered);
		et_amount_to_pay=(EditText)findViewById(R.id.et_amount_to_pay);
		et_amount_paid=(EditText) findViewById(R.id.et_ammount_to_paid);
		et_due_amount=(EditText)findViewById(R.id.et_due_ammount);
		et_jars_returned=(EditText)findViewById(R.id.et_jars_returned);
		et_jars_pending=(EditText)findViewById(R.id.et_jars_pending);

		validateCredentials();


		initListeners();

		// amt_to_pay=(Integer.parseInt(cust_Balance)+((Integer.parseInt(cust_Jar_Cost)*(Integer.parseInt(cust_Jars_Regd))*Integer.parseInt(et_jars_delivered.getText().toString()))));
		   
	     //   et_amount_to_pay.setText(""+amt_to_pay);
		
		
		
		et_jars_delivered.addTextChangedListener(new TextWatcher() {
			  
			  @Override
			    public void afterTextChanged(Editable arg0) {
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			    	
			    }

			    @Override
			    public void onTextChanged(CharSequence txtWatcherStr, int start, int before, int count) {

			        String outputedText = txtWatcherStr.toString();
			        
			      
			        if(!outputedText.equalsIgnoreCase("")){
			        	 try {
			        		// int cust_jar_register= Integer.parseInt(cust_Jars_Regd);  cust_Jars_Return
						     //  cust_jar_register=Integer.parseInt(outputedText);
							amt_to_pay=(Integer.parseInt(cust_Balance)+(((Integer.parseInt(cust_Jar_Cost)*Integer.parseInt(outputedText)))));
							   int pjars=Integer.parseInt(cust_Jars_Return)+(Integer.parseInt(outputedText)-Integer.parseInt(et_jars_returned.getText().toString()));
							et_amount_to_pay.setText(""+amt_to_pay);
							et_amount_paid.setText(""+amt_to_pay);
							et_jars_pending.setText(""+pjars);
							et_jars_returned.setText(outputedText);
			        	 } catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 	       
			        }else{
			        	//amt_to_pay=(Integer.parseInt(cust_Balance)+(((Integer.parseInt(cust_Jar_Cost)))));
						
			        	et_amount_to_pay.setText(""+Integer.parseInt(cust_Balance));
			        	et_amount_paid.setText(""+Integer.parseInt(cust_Balance));
			        	//amt_due=(amt_to_pay-Integer.parseInt(outputedText));
						et_due_amount.setText(""+(Integer.parseInt(et_amount_to_pay.getText().toString())-Integer.parseInt(et_amount_paid.getText().toString())));
						et_jars_returned.setText("0");
			        }
			        
			       
			    }
		});
		
		
		
		et_jars_returned.addTextChangedListener(new TextWatcher() {
			
			  @Override
			    public void afterTextChanged(Editable arg0) {
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			    	
			    }

			    @Override
			    public void onTextChanged(CharSequence txtWatcherStr, int start, int before, int count) {

			        String outputedText = txtWatcherStr.toString();
			        if(!outputedText.equalsIgnoreCase("")){
			        	 try {
						int pjars=(Integer.parseInt(et_jars_delivered.getText().toString())-Integer.parseInt(et_jars_returned.getText().toString())+Integer.parseInt(cust_Jars_Return));
			        		et_jars_pending.setText(""+pjars);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			 	       
			        }else{
			        	et_jars_pending.setText(cust_Jars_Return);
			        }
			        
			       
			    }
		});
		
		et_amount_paid.addTextChangedListener(new TextWatcher() {
			
			 @Override
			    public void afterTextChanged(Editable arg0) {
			    }

			    @Override
			    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			    	//et_due_amount.setText(""+amt_due);
			    }

			    @Override
			    public void onTextChanged(CharSequence txtWatcherStr, int start, int before, int count) {

			        String outputedText = txtWatcherStr.toString();
			        if(!outputedText.equalsIgnoreCase("")){
			     		amt_due=(amt_to_pay-Integer.parseInt(outputedText));
						et_due_amount.setText(""+amt_due);
					
					 	       
			        }else{
			        	et_due_amount.setText("0");
			        }
			        
			       
			    }
		});
		cb_order_status=(CheckBox)findViewById(R.id.cb_to_ord_status);
		payType_str="CASH";
		rg_payment_type.setOnCheckedChangeListener(new OnCheckedChangeListener() 
	    {
	        public void onCheckedChanged(RadioGroup group, int checkedId) {
	        	
	        	
	        	int selectedId = rg_payment_type.getCheckedRadioButtonId();

	        	paytype = (RadioButton) findViewById(selectedId);
	        	payType_str=paytype.getText().toString();
	        	if(payType_str.equalsIgnoreCase("By Cash")){
	        		payType_str="CASH";
	        	}else{
	        		payType_str="SODEXHO";
	        	}
	            Toast.makeText(DetailsActivity.this,
	            		paytype.getText(), Toast.LENGTH_SHORT).show();
	        }
	    });
		
		
		et_due_amount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				amt_due=(amt_to_pay-Integer.parseInt(et_amount_paid.getText().toString()));
				et_due_amount.setText(""+amt_due);
			}
		});
		
		cb_order_status.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(cb_order_status.isChecked()==true){
					order_status=2;
				}
				
			}
		});
		
		
		
	}
	

	private void initViews() {
		mTvProductName = (TextView) findViewById(R.id.title);
		mBtnSubmit =     (Button)   findViewById(R.id.btn_submit);
		custr_Name=      (TextView) findViewById(R.id.tv_name);
		contact_Number=  (TextView) findViewById(R.id.tv_contact_number);
		cust_Loc=        (TextView) findViewById(R.id.tv_place);
		cust_jarcost= (TextView) findViewById(R.id.tv_jarcost);
		due_amt=(TextView)findViewById(R.id.tv_custbal);
		jars_return=(TextView)findViewById(R.id.tv_jarreturn);
		
	}

	private void initListeners() {
		mBtnSubmit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_submit:
		//	jars_delivered=Integer.parseInt(et_jars_delivered.getText().toString());
			if(et_jars_delivered.getText().toString().length()!=0){
				
				 gps = new GPSTracker(DetailsActivity.this);
				 
	             // check if GPS enabled     
	             if(gps.canGetLocation()){
	                  
	                  latitude = gps.getLatitude();
	                  longitude = gps.getLongitude();
	                  
	                  orderCloseRequest(latitude,longitude);
	              //    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
	             }else{
	                  
	                 gps.showSettingsAlert();
	             }


			//jars_delivered=Integer.parseInt(et_jars_delivered.getText().toString());
			}else{
				Toast.makeText(getApplicationContext(), "Please enter Delivered Jars", Toast.LENGTH_SHORT).show();
			}
			break;

		default:
			break;
		}

	}
	
	public void validateCredentials(){
		
		   progress = new ProgressDialog(DetailsActivity.this);
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
        	  
        	  
            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
            request.addProperty("cust_ID",cust_ID);//EMP100001
              SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
               //envelope.dotNet = true;
               envelope.setOutputSoapObject(request);
               AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
              androidHttpTransport.call(SOAP_ACTION, envelope);
                  SoapObject object=(SoapObject)envelope.bodyIn;
                  
                  
            
            for(int i=0;i<object.getPropertyCount();i++){
                	  SoapObject newobj=(SoapObject)object.getProperty(i);
                	  cust_Name=newobj.getProperty("cust_Name").toString();
                	  cust_Number=newobj.getProperty("cust_ID").toString();
                	  cust_del_date=newobj.getProperty("order_Date").toString();
                	  cust_loc=newobj.getProperty("cust_Address").toString();
                	  cust_Jars_Regd=newobj.getProperty("cust_Jars_Regd").toString();
                	  cust_Jar_Cost=newobj.getProperty("cust_Jar_Cost").toString();
                	  cust_Balance=newobj.getProperty("cust_Balance").toString();
                	  cust_Jars_Return=newobj.getProperty("cust_Jars_Return").toString();
                	  
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
        	
        	custr_Name.setText(cust_Name);
      	  contact_Number.setText(cust_Number);
      	  cust_Loc.setText(cust_loc);
      	cust_jarcost.setText(cust_Jar_Cost);
      	due_amt.setText(cust_Balance);
      	jars_return.setText(cust_Jars_Return);
      	
      	//cust_jarcost,due_amt,jars_return
      	
      	  
      	  et_jars_delivered.setText(""+cust_Jars_Regd);   
          // et_jars_delivered.setCursorVisible(inv)
           et_jars_returned.setText(""+cust_Jars_Regd);
           
           amt_to_pay=(Integer.parseInt(cust_Balance)+(((Integer.parseInt(cust_Jar_Cost)*(Integer.parseInt(cust_Jars_Regd))))));
          
           int pjars=(Integer.parseInt(et_jars_delivered.getText().toString())-Integer.parseInt(et_jars_returned.getText().toString())+Integer.parseInt(cust_Jars_Return));
           et_jars_pending.setText(""+pjars);
			
	        et_amount_to_pay.setText(""+amt_to_pay);
	        et_amount_paid.setText(""+amt_to_pay);
	        et_amount_to_pay.setEnabled(false);
	        et_jars_pending.setEnabled(false);
	        et_due_amount.setEnabled(false);
      	 
      	progress.dismiss();
  	     
       }
       };
       
     
	public void orderCloseRequest(Double lat,Double longt){
    	   latitude=new Double(lat);
    	//   latitude = lat;
    	   longitude=new Double(longt);
    	   progress = new ProgressDialog(DetailsActivity.this);
			progress.setCancelable(false);
			String plz_craetedb = "Please Wait... ";
			progress.setMessage(plz_craetedb);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setProgress(0);
			progress.setMax(100);
			progress.show();
			
		
			final GregorianCalendar gcalendar = new GregorianCalendar();

			thread = new Thread(){
            public void run(){
             try{
            	 
            	 java.util.Date date= new java.util.Date();
                 Timestamp currentTimestamp= new Timestamp(date.getTime());
               SoapObject close_request = new SoapObject(NAMESPACE, METHOD_NAME_OCR);
               close_request.addProperty("cust_ID_str",cust_ID);
               close_request.addProperty("cust_WHID_str",whouse_ID);  
               close_request.addProperty("emp_ID_str",emp_ID);
//               request.addProperty("delivered_Date_Str",cust_del_date+" "+gcalendar.get(Calendar.HOUR) + ":"+gcalendar.get(Calendar.MINUTE) + ":"+gcalendar.get(Calendar.SECOND)+":"+gcalendar.get(Calendar.MILLISECOND));
               close_request.addProperty("delivered_Date_Str", currentTimestamp.toString());
               close_request.addProperty("jars_Delivered_int",Integer.parseInt(et_jars_delivered.getText().toString()));
               close_request.addProperty("pay_Type_str",payType_str);
               close_request.addProperty("amt_To_Pay_int", amt_to_pay);
               close_request.addProperty("amt_Paid_int", Integer.parseInt(et_amount_paid.getText().toString()));
               close_request.addProperty("due_Amt_int", amt_due);   
               close_request.addProperty("jars_Returned_int",Integer.parseInt(et_jars_returned.getText().toString()));
               close_request.addProperty("jars_Pending_int", Integer.parseInt(et_jars_pending.getText().toString()));
               close_request.addProperty("latitude_Double", ""+latitude);//latitude);//latitude_Double
               close_request.addProperty("longitude_Double", ""+longitude);//longitude);//longitude_Double
               close_request.addProperty("order_Status_Int",order_status);//order_Status_Int  
               
               
               SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
               envelope.dotNet = false;
               envelope.setOutputSoapObject(close_request);
              
              
         
               AndroidHttpTransport androidHttpTransport_ocr = new AndroidHttpTransport(URL_OCR);
               androidHttpTransport_ocr.call(SOAP_ACTION_OCR, envelope);
	                  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	                  SoapObject object=(SoapObject)envelope.bodyIn;  
	                  
	                if(object!=null){
                 	 result_ID=object.getProperty(0).toString();
	                  
	                 System.out.println("result id...."+result_ID);
	                  }
	                 
              }
              
             catch(Exception e){
              e.printStackTrace();
             }
              
             handler.post(createUI_OCR);
            }
           };
            
           thread.start();
          }
           
           
          final Runnable createUI_OCR = new Runnable() {
           public void run(){
        
           		Toast.makeText(getApplicationContext(), "Order Closed Sucessfully", Toast.LENGTH_SHORT).show();
           		
           		/*Intent i=new Intent(getApplicationContext(),HomeActivity.class);
           		i.putExtra("data", "Data");
           		startActivityForResult(i, 1);*/
           		
           		
           		finish();
           		
           		
           		
           		progress.dismiss();
        
           
     	     
          }
          };


       

}
