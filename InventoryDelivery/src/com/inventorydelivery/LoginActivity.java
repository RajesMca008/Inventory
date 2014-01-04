package com.inventorydelivery;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.AndroidHttpTransport;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivty implements OnClickListener {

	EditText mEtUsername = null;
	EditText mEtPassword = null;
	Button mBtnLogin = null;
	ProgressDialog progress = null;
	 // flag for Internet connection status
    Boolean isInternetPresent = false;
    String empType;
     
    // Connection detector class
    ConnectionDetector cd;
	
	  private final String NAMESPACE = "http://beverli.com";
	    
	    private final String URL = "http://27.7.112.29:8080/axis/services/AuthenticationProcessorPort_V1.0?wsdl";
	    
	    private final String SOAP_ACTION = "http://beverli.com/AuthenticationProcessor/Authentication_V1.0";
	    
	    
	    private final String METHOD_NAME = "Authentication_V1.0";
	    
	    private String Webresponse = "",wHouseID="";
	    private TextView textView;
	    private Thread thread;
	    private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		  cd = new ConnectionDetector(getApplicationContext());
		  
		initViews();

		initListeners();

	}
	public void showAlertDialog(Context context, String title, String message, Boolean status) {
	        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	 
	        // Setting Dialog Title
	        alertDialog.setTitle(title);
	 
	        // Setting Dialog Message
	        alertDialog.setMessage(message);
	         
	        // Setting alert dialog icon
	        alertDialog.setIcon((status) ? R.drawable.networkconnectsucess : R.drawable.networkdisconnnnect);
	 
	        // Setting OK Button
	        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	            }
	        });
	 
	        // Showing Alert Message
	        alertDialog.show();
	    }

	private void initViews() {

		mEtUsername = (EditText) findViewById(R.id.et_username);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
	}

	private void initListeners() {
		mBtnLogin.setOnClickListener(this);

	}



	public void validateCredentials(){
		
		 progress = new ProgressDialog(LoginActivity.this);
			progress.setCancelable(false);
			String plz_craetedb = "Please Wait... ";
			progress.setMessage(plz_craetedb);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setProgress(0);
			progress.setMax(100);
			progress.show();
	        thread = new Thread(){
	         public void run(){  
	          try{
	            SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
	            request.addProperty("userID", mEtUsername.getText().toString());//EMP100001
	            request.addProperty("password", mEtPassword.getText().toString());//EMP100001
	            
	               SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	               //envelope.dotNet = true;
	               envelope.setOutputSoapObject(request);
	               AndroidHttpTransport androidHttpTransport = new AndroidHttpTransport(URL);
	              androidHttpTransport.call(SOAP_ACTION, envelope);
	                  SoapPrimitive response = (SoapPrimitive)envelope.getResponse();
	                  SoapObject object=(SoapObject)envelope.bodyIn;  
	                  
	                  if(object!=null && !object.getProperty(1).toString().equalsIgnoreCase(null)){
	                	  empType=object.getProperty(1).toString();
	                	  wHouseID=object.getProperty(2).toString();
	                  
	                  System.out.println("TEST111 "+wHouseID);
	                  }else{
	                	  empType="001";
	                  }
	                  Webresponse = response.toString();
	                  
	                  System.out.println("TEST "+response.getName());
	                  
	                  
	          }
	           
	          catch(Exception e){
	           e.printStackTrace();
	          /* if(empType.equalsIgnoreCase("null")){
	        	   Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show();
	        	   finish();
	           }*/
	           
	           
	          }
	           
	          handler.post(createUI);
	         }
	        };
	         
	        thread.start();
	       }
	        
	        
	       final Runnable createUI = new Runnable() {
	        public void run(){
	  	        	isInternetPresent = cd.isConnectingToInternet();
	        	 
                // check for Internet status  
                if (isInternetPresent) {
                	
                	System.out.println("emp type test:"+empType);
                	
                	 try {
						if(empType.equalsIgnoreCase("null")){// && empType.equalsIgnoreCase("3") || empType.equalsIgnoreCase("4") || empType.equalsIgnoreCase("5")){
													 
              
               }else  if(empType.equalsIgnoreCase("3")){
            	   
            	   Intent i=new Intent(getApplicationContext(), HomeActivity.class);
	                	i.putExtra("empType", empType);
	        		  i.putExtra("wHouseID", wHouseID);
	                	i.putExtra("empID", mEtUsername.getText().toString());
	                	
            		startActivity(i);
            		progress.dismiss();
            		finish();
            	   
              finish();
               }else if(empType.equalsIgnoreCase("4")){
            	   Intent i=new Intent(getApplicationContext(), HomeActivity.class);
	                	i.putExtra("empType", empType);
	        		  i.putExtra("wHouseID", wHouseID);
	                	i.putExtra("empID", mEtUsername.getText().toString());
	                	
            		startActivity(i);
            		progress.dismiss();  
            		finish();
               }else if(empType.equalsIgnoreCase("5")){
            	   Intent i=new Intent(getApplicationContext(), HomeActivity.class);
	                	i.putExtra("empType", empType);
	        		  i.putExtra("wHouseID", wHouseID);
	                	i.putExtra("empID", mEtUsername.getText().toString());
	                	
            		startActivity(i);
            		progress.dismiss();  
            		finish();
               }
               
               else{
            	   Toast.makeText(getApplicationContext(), "You are not Authorized Person, Please contact admin", Toast.LENGTH_SHORT).show();
              
            	   progress.dismiss(); 
               }
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
						 
						 Toast.makeText(getApplicationContext(), "Invalid Credentials! Please Try Again", Toast.LENGTH_SHORT).show();
					     
					   
						 progress.dismiss(); 
					}
               
	        
               
                } else {
                   
                    showAlertDialog(LoginActivity.this, "No Internet Connection",
                            "You don't have internet connection.", false);
                    progress.dismiss();
                }
	        	
	        	
	       }
	       };
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			validateCredentials();
			break;

		default:
			break;
		}

	}

}
