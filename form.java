package com.example.xxxx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
@SuppressLint({ "NewApi", "DefaultLocale" })
public class ActionShout extends Activity {
	
	private File destImage1, destImage2, destImage3, destImage4, f1, f2, f3, f4;
	public final static String APP_PATH_SD_CARD = "/ShoutOutLoud";
	private String cameraFile1 = null;
	private String cameraFile2 = null;
	private String cameraFile3 = null;
	private String cameraFile4 = null;
	
	// Stores the hash value of the name of the image
	private String cameraFile1Hash = null;
	private String cameraFile2Hash = null;
	private String cameraFile3Hash = null;
	private String cameraFile4Hash = null;
	
	private Bitmap largeImage1, largeImage2, largeImage3, largeImage4;

	GPSTracker gps;
	private String addressText[];
	private String city;                    //  City of News Update
	private String country;                //   Country of news update
	private ArrayList <String> addressTextList = new ArrayList<String>();
	private String addressTextNew [];
	private String addresses = "";
	private String news_update = null;             // News Update
	private String finalAddress = null;            // Address of the update
	private double latitude = 0;
	private double longitude = 0;
	private String latit = null;              // Latitude 
	private String longit = null;             // Longitude
	private String anony = null;              // 1 for anonymous | 0 for not
	private String email_id = null;           // Email ID of the User
	private String suggested_locality = null; //locality suggested by the user
	private String suggested_landmark = null; //landmark suggested by the user
	
	private String label_pronoun_text;
	private String label_questions_text;
	private String label_calamities_text;
    private String label_hot_words_text;
	private String label_question_mark_text;
	
	/* Variables to hold the filename of the images */
	private String image1;
	private String image2;
	private String image3;
	private String image4;
	
	/* Variable to hold the full path where image has to be saved */
	private String fullPath;
	
	/* Variables to check for constraints on shouting guidelines */
	
	public String[] pronouns = {"he", "his", "him", "himself", "her", "she", "herself", "i", "me", "mine",
			                 "myself", "my", "our", "ours", "ourselves", "us", "we", "you", "yours", "having",
			                 "yourselves"};
	
	int pronouns_length = pronouns.length;
	
	public String[] questions = {"who", "what", "whom", "why", "whose", "which", "where",
			                       "how"};
	
	int questions_length = questions.length;
	
	public String[] calamities = {"flood", "floods", "earthquake", "earthquakes", "quake", "quakes",
			"storm", "tornado", "tsunami", "storms", "tsunamis", "tornadoes", "cyclone", "cyclones", "blizzard",
			"snowstorms", "snowstorm", "snow", "squall", "squalls", "icestorm" ,
			"icestorms", "ice", "dustdevil", "dustdevils", "dust", "devils", "devil"};
	
	int calamities_length = calamities.length;
	
	public String[] hot_words = {"riots", "fire", "brawl", "turmoil", "uproar", "roar" ,"up", "protests",
			"protest", "breakdown", "murder", "death", "kill", "killed"};
	
	int hot_words_length = hot_words.length;
	
	/* URL handling database connection */
	private static String url_insert_data = "http://www.shoutoutloud.me/android/status_update.php";
	private static final String TAG_SUCCESS = "success";
	int flag_success;
	
	private ProgressDialog pDialog;
	
	static final int REQUEST_IMAGE_CAPTURE_ONE = 1;
	static final int REQUEST_IMAGE_CAPTURE_TWO = 2;
	static final int REQUEST_IMAGE_CAPTURE_THREE = 3;
	static final int REQUEST_IMAGE_CAPTURE_FOUR = 4;
	static final int REQUEST_IMAGE_CAPTURE_FIVE = 5;
	static final int REQUEST_IMAGE_CAPTURE_SIX = 6;
	static final int REQUEST_IMAGE_CAPTURE_SEVEN = 7;
	static final int REQUEST_IMAGE_CAPTURE_EIGHT = 8;
	
	/* height and width of the picture to be displayed in ActionShout.this activity */
	int height = 180; // height in pixels
	int width = 180; // width in pixels    
	private AsyncTask<Void, Void, Void> execute;

	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_shout);
		
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent email = getIntent();
	    Bundle b = email.getExtras();
        
	    /* Setting the Email ID if the user */
        if(b!=null)
        {
            email_id =(String) b.get("email_id");
        }
        
        /* Done setting email ID */
        
        long unixTime = System.currentTimeMillis() / 1000L;       // Current time of the user's phone
        
        image1 = email_id+unixTime+"1";                          // File name of Picture 1
        image2 = email_id+unixTime+"2";                          // File name of Picture 2
        image3 = email_id+unixTime+"3";                          // File name of Picture 3
        image4 = email_id+unixTime+"4";                          // File name of Picture 4
        
        /* Checking/Creating the directory for saving the picture taken from camera */
        
        fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_PATH_SD_CARD;
        
        File dir = new File(fullPath);
            
            if (!dir.exists())
                dir.mkdir(); 
            
            /* for first image */
            destImage1 = new File(dir, image1 + ".jpg");
            cameraFile1 = destImage1.getAbsolutePath();   
            f1 = new File(destImage1.getAbsolutePath());
            
            cameraFile1Hash = Md5.generate(cameraFile1);
            
            /* for second image */
            destImage2 = new File(dir, image2 + ".jpg");
            cameraFile2 = destImage2.getAbsolutePath();   
            f2 = new File(destImage2.getAbsolutePath());
            
            cameraFile2Hash = Md5.generate(cameraFile2);
            
            /* for third image */
            destImage3 = new File(dir, image3 + ".jpg");
            cameraFile3 = destImage3.getAbsolutePath();   
            f3 = new File(destImage3.getAbsolutePath());
            
            cameraFile3Hash = Md5.generate(cameraFile3);
            
            /* for fourth image */
            destImage4 = new File(dir, image4 + ".jpg");
            cameraFile4 = destImage4.getAbsolutePath();   
            f4 = new File(destImage4.getAbsolutePath());
            
            cameraFile4Hash = Md5.generate(cameraFile4);
            
        /* Initializing the various views in the XML file */
		
		final TextView textView1 = (TextView)findViewById(R.id.textView1);
		textView1.setTypeface(Typeface.DEFAULT_BOLD);
		final TextView textView2 = (TextView)findViewById(R.id.textView2);
		textView2.setTypeface(Typeface.DEFAULT_BOLD);
		final EditText editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setHint("Enter specific information, news updates and events. In case of spamming " +
				"your content might be blocked");
		final TextView address = (TextView)findViewById(R.id.textView3);
		
		TextView textView4 = (TextView)findViewById(R.id.textView4);
		textView4.setTypeface(Typeface.DEFAULT_BOLD);
		
		final TextView textView5 = (TextView)findViewById(R.id.textView5);
		
		final TextView textView6 = (TextView)findViewById(R.id.textView6);
		
		CheckBox checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
		
		/* Location Based Operations gies here */
		
		gps = new GPSTracker(ActionShout.this);
		if(gps.canGetLocation())
		{
			latitude = gps.getLatitude(); // returns latitude
			longitude = gps.getLongitude(); // returns longitude
			
			addressText = gps.getAddress();
			city = gps.getCity();
			country = gps.getCountry();
			
			Log.d("Address | checking if address recieved from GPS tracker class or not", addressText[1]);
			int size = addressText.length;
			Log.d("Number of addresses", Integer.toString(size));
			for(int i=0; i<size; i++){
			  if(addressText[i] != null)
				{
				  //addresses += addressText[i] + "\n";
				  addressTextList.add(addressText[i]);
				}
			  else
				  continue;
			}
			
			// Coverting the List of addresses into into the array of strings
			/*addressTextNew = new String[addressTextList.size()];
			addressTextNew = addressTextList.toArray(addressTextNew); */
			
			finalAddress = addressText[0];
			
			Log.d("Addresses made", addresses);
			if(addresses != null)
			   address.setText(finalAddress);
			else
				address.setText("Location not found. Try after turning on your GPS !");
		}
		else
		{
			gps.showSettingsAlert();
			address.setText("Can't get your Location. Enter a custom Location");
		}
		
		latit = Double.toString(latitude);
		longit = Double.toString(longitude);
		
		new Places().execute();
		
	    if(addressTextList.size() > 0)
	    {
	    	/* Show alert Dialog to select an alternate Location */
			textView5.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					   //Toast.makeText(ActionShout.this, city+country, Toast.LENGTH_SHORT).show();
					    AlertDialog.Builder builder = new AlertDialog.Builder(ActionShout.this);
					    
					    builder.setTitle("Select Alternate Location");
					    
					           builder.setItems(addressTextNew, new DialogInterface.OnClickListener() {
					               public void onClick(DialogInterface dialog, int which) {
					            	   address.setText(addressTextNew[which]);
					            	   finalAddress = addressTextNew[which];
					           }
					    }); 
					builder.show();
				}
			});
			
			textView6.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder build = new AlertDialog.Builder(ActionShout.this);
					LayoutInflater inflater = ActionShout.this.getLayoutInflater();
					View view = null;
					view = inflater.inflate(R.layout.suggest_address, null);
					build.setTitle("Suggest a better Location !");
					build.setView(view);
					
					final EditText locality = (EditText)view.findViewById(R.id.locality);
		            final EditText landmark = (EditText)view.findViewById(R.id.landmark);
		            final TextView cityCountry1 = (TextView)view.findViewById(R.id.cityCountry);
		            
		            String cityCountr = "In : "+city+", "+country;
		            
		            cityCountry1.setText(cityCountr);
					
					build.setPositiveButton("Suggest", new DialogInterface.OnClickListener()
					{
						@Override
			               public void onClick(DialogInterface dialog, int id) {
							suggested_locality = locality.getText().toString();
			                suggested_landmark = landmark.getText().toString();
			                   if((suggested_locality.length() < 2 ) ||  (suggested_landmark.length() < 2))
			                   {   Toast.makeText(ActionShout.this, "Either of the fields can " +
				                	   		"not be left blank", Toast.LENGTH_SHORT).show(); 
			                   }
			                   else
			                   {
			                	   Toast.makeText(ActionShout.this, "Your suggestion have" +
				                	   		" been saved. Thanks !", Toast.LENGTH_SHORT).show(); 
			                   }
			               }

					});
					build.setNegativeButton("Go Back", new DialogInterface.OnClickListener()
					{
						@Override
			               public void onClick(DialogInterface dialog, int id) {
							 // Do nothing !
			               }
					});
					
					build.show();
				}
			});
	    }
	    else
	    {
	    	textView5.setVisibility(View.GONE);
	    }
	    
		 
	    /* Location Operations Ends here */
	    
	    /* Checking if anonymous has been ticked or not */
	    
	    checkBox1.setOnClickListener(new View.OnClickListener() {
			 
			  @Override
			  public void onClick(View v) {
		                //is chkIos checked?
				  if (((CheckBox) v).isChecked()) {
					  anony = "1";
					}
				  else 
					  anony = "0";
			  }
			});
	    
	    /* anonymous checking finished */
		
		/* Camera Operations */
		
		ImageButton picture1 = (ImageButton) findViewById(R.id.addPicture1);
		picture1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionShout.this);
       		 
                // Setting Dialog Title
                alertDialog.setTitle("Take a Picture or Select from Gallery");
         
                // Setting Dialog Message
                alertDialog.setMessage("What would you like to do?");
         
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);
         
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Take a Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	
                    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            		    	takePictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_USER);
            		    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage1)); 
            		    	startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_ONE);
            		    }
                    }
                });
         
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    /*
                    	Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    	           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    	startActivityForResult(pickPhoto , REQUEST_IMAGE_CAPTURE_FIVE); */
                    } 
                });
         
                // Showing Alert Message
                alertDialog.show();
            }
		});
		ImageButton picture2 = (ImageButton) findViewById(R.id.addPicture2);
		picture2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionShout.this);
          		 
                // Setting Dialog Title
                alertDialog.setTitle("Take a Picture or Select from Gallery");
         
                // Setting Dialog Message
                alertDialog.setMessage("What would you like to do?");
         
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);
         
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Take a Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            		    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage2));
            		        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_TWO);
            		    }
                    }
                });
         
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { /*
                    	Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    	           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    	startActivityForResult(pickPhoto , REQUEST_IMAGE_CAPTURE_SIX);*/
                    }
                });
         
                // Showing Alert Message
                alertDialog.show();
            }
		});
		ImageButton picture3 = (ImageButton) findViewById(R.id.addPicture3);
		picture3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionShout.this);
          		 
                // Setting Dialog Title
                alertDialog.setTitle("Take a Picture or Select from Gallery");
         
                // Setting Dialog Message
                alertDialog.setMessage("What would you like to do?");
         
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);
         
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Take a Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            		    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage3));
            		        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_THREE);
            		    }
                    }
                });
         
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { /*
                    	Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    	           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    	startActivityForResult(pickPhoto , REQUEST_IMAGE_CAPTURE_SEVEN); */
                    }
                });
         
                // Showing Alert Message
                alertDialog.show();
            }
		});
		ImageButton picture4 = (ImageButton) findViewById(R.id.addPicture4);
		picture4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionShout.this);
          		 
                // Setting Dialog Title
                alertDialog.setTitle("Take a Picture or Select from Gallery");
         
                // Setting Dialog Message
                alertDialog.setMessage("What would you like to do?");
         
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.delete);
         
                // Setting Positive "Yes" Button
                alertDialog.setPositiveButton("Take a Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                    	Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            		    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            		    	takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(destImage4));
            		        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_FOUR);
            		    }
                    }
                });
         
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { /*
                    	Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                    	           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    	startActivityForResult(pickPhoto , REQUEST_IMAGE_CAPTURE_EIGHT); */
                    }
                });
         
                // Showing Alert Message
                alertDialog.show();
            }
		});
		
		
		/* Camera operations finished */
		
		/* Operations on clicking on shout button */
		
		final Button shout  = (Button) findViewById(R.id.button1);
		shout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
			public void onClick(View v) {
            	news_update = editText1.getText().toString();
            	
            	
            	/* Counting the number of words in string */
            	
            	 String trimmed = news_update.trim();
            	 int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
            	 
            	 /* splitting the string into words and save into an array */
            	 
            	 String[] words_array = news_update.split("\\s+");
            	 for (int i = 0; i < words_array.length; i++) {
            	     // You may want to check for a non-word character before blindly
            	     // performing a replacement
            	     // It may also be necessary to adjust the character class
            	     words_array[i] = words_array[i].replaceAll("[^\\p{Alnum}]+", "");
            	 }
            	 
            	 
            	 
            	 for(int i = 0; i < words_array.length; i++)
            	 {
            		 for(int j = 0; j < pronouns_length; j++)
            		 {
            			 if((words_array[i].toLowerCase()).equals(pronouns[j]))
            				 label_pronoun_text = "Your news update seems more like an opinion or a personal message.";
            		 }
            		 for(int j = 0; j < questions_length; j++)
            		 {
            			 if((words_array[i].toLowerCase()).equals(questions[j]))
            				 label_questions_text = "It seems you are asking a question here. Shout out specific news updates only.";
            		 }
            		 for(int j = 0; j < calamities_length; j++)
            		 {
            			 if((words_array[i].toLowerCase()).equals(calamities[j]))
            				 label_calamities_text = "Updating false news of natural calamity might trouble a lot of people. We hope you are sure about it";
            		 }
            		 for(int j = 0; j < hot_words_length; j++)
            		 {
            			 if((words_array[i].toLowerCase()).equals(hot_words[j]))
            				 label_hot_words_text = "Be 100 percent sure before shouting anything that could stir an outrage";
            		 }
            		 
            		 if((words_array[i].equals("?")) || (words_array[i].equals("!")))
            			 label_question_mark_text = "It seems you are asking a question here. Shout out specific news updates only.";
            	 }
            	 
            	 String message = null;
         		
         		if(label_pronoun_text != null)
         			message = label_pronoun_text;
         		else if(label_questions_text != null || label_question_mark_text != null)
         			message = label_questions_text;
         		else if(label_calamities_text != null)
         		    message = label_calamities_text;
         		else if(label_hot_words_text != null)
         		    message = label_hot_words_text;
            	 
            	if((label_pronoun_text != null) || (label_questions_text != null) || (label_calamities_text != null) 
              			 || (label_hot_words_text != null))
            	{
            		AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActionShout.this);
            		
            		
            		String guidelines = "\n\nOnly shout specific news, events, incidents, natural calamities, government negilgience " +
            				       "or any piece of news relevant to the general public.\n\n" +
            				      "1. You are not allowed to share opinion or anything that is related to you" +
            				                       "that is of no concern to people" +
            				                       "\n2. You are not allowed to ask questions" +
            				                       "\n3. No spamming is allowed, only specific news updates." +
            				                       "\n3. Be sure before posting any news related to natural calamities." +
            				                       "\n\nYou might be blocked and flagged down for not adhering to Shout Out Loud policies";
            	
                    alertDialog.setTitle("Shouting Guidelines Overriden");
             
                    // Setting Dialog Message 
                    alertDialog.setMessage(message +"\n\nShouting Guidelines"+guidelines);
             
                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.delete);
             
                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("Go Back", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {
                        	Intent intent = getIntent();
                        	intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
                        }
                    });
             
                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("Shout Out Loud", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        	new AddInDatabase().execute();
                        }
                    });
             
                    // Showing Alert Message
                    alertDialog.show();
                    
            	}
            	else
            	{
            		if(editText1.getText().length() >= 10 && words >= 2)
                    {
                    	if(latitude == 0 || longitude == 0 || addresses == "null")
                    	{
                    		Toast.makeText(getApplicationContext(), "We can't find where you are. Try with GPS on.", Toast.LENGTH_LONG).show();
                    	}
                    	else
                    	{
                    		new AddInDatabase().execute();
                    	}
                    }
                    else
                    {
                    	Toast.makeText(getApplicationContext(), "You can't post an empty or such short  News Update! " +
                    			                                "Should be 10 characters or 2 words atleast", Toast.LENGTH_LONG).show(); 
                    } 
            	}
            	 
            	//Toast.makeText(getApplicationContext(), label_pronoun_text, Toast.LENGTH_LONG).show();
           
            }
        });
		
		/* Operations on shout button click finished here */
		
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 1:
	           if (resultCode == RESULT_OK) {
	                
	                if(f1==null){
	                    if(cameraFile1!=null)
	                        f1 = new File(cameraFile1);
	                    else
	                        Log.e("check", "camera file object null line no 279");
	                }else
	                    Log.e("check", f1.getAbsolutePath());
	                
	                //largeImage1 = BitmapFactory.decodeFile(f1.getAbsolutePath());
	               
	                BitmapFactory.Options options = new BitmapFactory.Options();
	                options.inSampleSize=2;      // 1/8 of original image
	                largeImage1 = BitmapFactory.decodeFile(cameraFile1,options);
	                
	                FileOutputStream fOut;
					try {
						fOut = new FileOutputStream(cameraFile1);
					
	                    largeImage1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
	                    fOut.flush();
	                    fOut.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                
	                Bitmap new_image = ExifUtil.rotateBitmap(cameraFile1, largeImage1);
	                
	                int h = new_image.getHeight();
	                int w = new_image.getWidth();
	                Bitmap created_image = BitmapScale.createBitmap(cameraFile1, h, w);
	                
	                Bitmap scaled = BitmapScale.resize(created_image, width, height);
	                
	                //Bitmap scaled = Bitmap.createScaledBitmap(new_image, height, width, true);
	                
	                ImageButton picture1 = (ImageButton) findViewById(R.id.addPicture1);
	                picture1.setImageBitmap(scaled);
	                
	            }
	            break;
		case 2:       
			if (resultCode == RESULT_OK) {
				if(f2==null){
                    if(cameraFile2!=null)
                        f2 = new File(cameraFile2);
                    else
                        Log.e("check", "camera file object null line no 279");
                }else
                    Log.e("check", f2.getAbsolutePath());
                //largeImage2 = BitmapFactory.decodeFile(f2.getAbsolutePath());
                
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=8;      // 1/8 of original image
                largeImage2 = BitmapFactory.decodeFile(cameraFile2,options);
                
                FileOutputStream fOut;
				try {
					fOut = new FileOutputStream(cameraFile2);
				
                    largeImage2.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
				Bitmap new_image = ExifUtil.rotateBitmap(cameraFile2, largeImage2);
                
                int h = new_image.getHeight();
                int w = new_image.getWidth();
                Bitmap created_image = BitmapScale.createBitmap(cameraFile2, h, w);
                
                Bitmap scaled = BitmapScale.resize(created_image, width, height);
                
                //Bitmap scaled = Bitmap.createScaledBitmap(largeImage2, height, width, true);
                
                ImageButton picture2 = (ImageButton) findViewById(R.id.addPicture2);
                picture2.setImageBitmap(scaled);
            }
            break;
		case 3:       
			if (resultCode == RESULT_OK) {
				if(f3==null){
                    if(cameraFile3!=null)
                        f3 = new File(cameraFile3);
                    else
                        Log.e("check", "camera file object null line no 279");
                }else
                    Log.e("check", f3.getAbsolutePath());
                //largeImage3 = BitmapFactory.decodeFile(f3.getAbsolutePath());
                
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=8;      // 1/8 of original image
                largeImage3 = BitmapFactory.decodeFile(cameraFile3,options);
                
                FileOutputStream fOut;
				try {
					fOut = new FileOutputStream(cameraFile3);
				
                    largeImage3.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Bitmap new_image = ExifUtil.rotateBitmap(cameraFile3, largeImage3);
                
                int h = new_image.getHeight();
                int w = new_image.getWidth();
                Bitmap created_image = BitmapScale.createBitmap(cameraFile3, h, w);
                
                Bitmap scaled = BitmapScale.resize(created_image, width, height);
                
                //Bitmap scaled = Bitmap.createScaledBitmap(largeImage3, height, width, true);
        
                ImageButton picture3 = (ImageButton) findViewById(R.id.addPicture3);
                picture3.setImageBitmap(scaled);
            }
            break;
		case 4:       
			if (resultCode == RESULT_OK) {
				if(f4==null){
                    if(cameraFile4!=null)
                        f4 = new File(cameraFile4);
                    else
                        Log.e("check", "camera file object null line no 279");
                }else
                    Log.e("check", f4.getAbsolutePath());
                //largeImage4 = BitmapFactory.decodeFile(f4.getAbsolutePath());
                
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize=8;      // 1/8 of original image
                largeImage4 = BitmapFactory.decodeFile(cameraFile4,options);
                
                FileOutputStream fOut;
				try {
					fOut = new FileOutputStream(cameraFile4);
				
                    largeImage4.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				Bitmap new_image = ExifUtil.rotateBitmap(cameraFile4, largeImage4);
                
                int h = new_image.getHeight();
                int w = new_image.getWidth();
                Bitmap created_image = BitmapScale.createBitmap(cameraFile4, h, w);
                
                Bitmap scaled = BitmapScale.resize(created_image, width, height);
                
                //Bitmap scaled = Bitmap.createScaledBitmap(largeImage4, height, width, true);
        
                ImageButton picture4 = (ImageButton) findViewById(R.id.addPicture4);
                picture4.setImageBitmap(scaled);
            }
            break;
		}
	/*	case 5:
			if(resultCode == RESULT_OK){ 
			        Uri selectedImage = data.getData();
			        try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
						Bitmap scaled = Bitmap.createScaledBitmap(bitmap, height, width, true);
						
						ImageButton picture1 = (ImageButton) findViewById(R.id.addPicture1);
		                picture1.setImageBitmap(scaled);
		                
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			    }

			break;
		case 6:
			if(resultCode == RESULT_OK){ 
			        Uri selectedImage = data.getData();
			        try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
						Bitmap scaled = Bitmap.createScaledBitmap(bitmap, height, width, true);
						
						ImageButton picture2 = (ImageButton) findViewById(R.id.addPicture2);
		                picture2.setImageBitmap(scaled);
		                
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			    }

			break; 
		case 7:
			if(resultCode == RESULT_OK){ 
			        Uri selectedImage = data.getData();
			        try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
						Bitmap scaled = Bitmap.createScaledBitmap(bitmap, height, width, true);
						
						ImageButton picture3 = (ImageButton) findViewById(R.id.addPicture3);
		                picture3.setImageBitmap(scaled);
		                
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }

			break; 
		case 8:
			if(resultCode == RESULT_OK){ 
			        Uri selectedImage = data.getData();
			        try {
						Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
						Bitmap scaled = Bitmap.createScaledBitmap(bitmap, height, width, true);
						
						ImageButton picture4 = (ImageButton) findViewById(R.id.addPicture4);
		                picture4.setImageBitmap(scaled);
		                
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			    } 

			break; 
			}  */
		
	}
	
	/*public static boolean copyFile(String from, String to) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            if (sd.canWrite()) {
                int end = from.toString().lastIndexOf("/");
                String str1 = from.toString().substring(0, end);
                String str2 = from.toString().substring(end+1, from.length());
                File source = new File(str1, str2);
                File destination= new File(to, str2);
                if (source.exists()) {
                    FileChannel src = new FileInputStream(source).getChannel();
                    FileChannel dst = new FileOutputStream(destination).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    } 
	 
	 @SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		    String[] projection = { MediaStore.Images.Media.DATA };
		    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		    startManagingCursor(cursor);
		    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		    cursor.moveToFirst();
		    return cursor.getString(column_index);
		}
    */
	
private class AddInDatabase extends AsyncTask<String, String, String> {
    	
    	/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ActionShout.this);
			pDialog.setMessage("The News update is being shouted !");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		/**
		 * Adding into database
		 * */
		protected String doInBackground(String... args) {
			/* Adding into Database */
			
			
            /* Building Parameters */
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("NewsUpdate", news_update));
			params.add(new BasicNameValuePair("Anonymous", anony));
			params.add(new BasicNameValuePair("Latitude", latit));
			params.add(new BasicNameValuePair("Longitude", longit));
			params.add(new BasicNameValuePair("GeoLocation", finalAddress));
			params.add(new BasicNameValuePair("EmailId", email_id));
			
			Log.d("DEBUG", "Building NameValuePair");
			
			/* Calling Json Object */
		    JSONParser jsonParser = new JSONParser();
		    
            
			/* Getting Json Object */
			/* Note that insert into database URL accepts POST method */
			JSONObject json = jsonParser.makeHttpRequest(url_insert_data, "POST", params);
			
			Log.d("DEBUG", "Json Object Created");
			
			/* Check CAT Log for response */
			Log.d("Create Response", json.toString());
			
			
			// check for success tag
			 try {
				   Log.d("Debug", "above json.getint()");
			 	   int success = json.getInt(TAG_SUCCESS);
			 	   Log.d("Debug", "json.getInt(TAG_SUCCESS)");
			 	   if(success == 1) 
			 	   {
			 		  Log.d("Debug", "inside if success = 1");
			 		  flag_success = 1;
				   } 
			 	   else 
					{
			 		  Log.d("Debug", "inside else of success = 1");
			 		  flag_success = 2;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}  

              if(flag_success == 1)
              {
            	  if(largeImage1 != null)
      			{
      				try {
      					ImageUpload.upload(cameraFile1Hash);
      				} catch (IOException e) {
      					// TODO Auto-generated catch block
      					e.printStackTrace();
      				}
      			}
            	  if(largeImage2 != null)
        			{
        				try {
        					ImageUpload.upload(cameraFile2Hash);
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        			}
            	  if(largeImage3 != null)
        			{
        				try {
        					ImageUpload.upload(cameraFile3Hash);
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        			}
            	  if(largeImage4 != null )
        			{
        				try {
        					ImageUpload.upload(cameraFile4Hash);
        				} catch (IOException e) {
        					// TODO Auto-generated catch block
        					e.printStackTrace();
        				}
        			}
              }
			 
			/* Finished adding into the database */
			
			return null;
		}
		
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if(flag_success == 1)
			{
				Toast.makeText(getApplicationContext(), "The Update has been shouted at all your Stalkers", Toast.LENGTH_LONG).show();
			}
			else if(flag_success == 2)
			{
				Toast.makeText(getApplicationContext(), "Failed To Update. Shout Again!", Toast.LENGTH_LONG).show();
			}
			Intent intent = new Intent(getApplicationContext(), LandingPage.class);
            startActivity(intent);
		}
    	
    }

private class Places extends AsyncTask<Void, Void, Void>{
	@Override 
	protected Void doInBackground(Void ... params) {
		addressTextNew = GooglePlaces.ReturnPlaces(latit, longit);
		return null;
	    }

    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_shout, menu);
		return true;
	}
}
