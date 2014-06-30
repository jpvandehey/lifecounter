package com.wootasm.lifecounter;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final String PREFS_NAME = "UserPrefs";
	public static final String LIFE_TOTALS = "LifeTotals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        
        //Apply user preferences and default content view.
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);        
        int default_life = settings.getInt("default_life", 20);
        
        //Wipe out Commander damage.
        SharedPreferences life_totals = getSharedPreferences(LIFE_TOTALS, 0);
        SharedPreferences.Editor editor = life_totals.edit();
        editor.clear();
        editor.commit();
        
        setContentView(R.layout.activity_main);
        
        TextView tv_1 = (TextView) findViewById(R.id.life_total_main);
        TextView tv_2 = (TextView) findViewById(R.id.life_total_main_2);
        
        tv_1 = set_default_life(tv_1, default_life);
        tv_2 = set_default_life(tv_2, default_life);
    }
    
    //Power saving through screen brightness manipulation.
    @Override
    public void onUserInteraction(){    	
    	
    	final Handler main_handler = new Handler(Looper.getMainLooper());
    	
    	new Thread(new Runnable(){
    		public void run(){
    			final WindowManager.LayoutParams lp = getWindow().getAttributes();
    			
	        	lp.screenBrightness=0.6f;
	        	
	        	main_handler.post(new Runnable(){
	        		
	        		public void run(){
	        			getWindow().setAttributes(lp);
	        		}
	        	});
	        	
	        	try{
	        		Thread.sleep(7500);
	        	} catch(InterruptedException ex){
	        		Thread.currentThread().interrupt();
	        	}
	        	
	        	lp.screenBrightness = 0.01f;
	        	
	        	main_handler.post(new Runnable(){
	        		
	        		public void run(){
	        			getWindow().setAttributes(lp);
	        		}
	        	});    	
    		}
    	}).start();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch (item.getItemId()){
    		case R.id.player_count:
    			player_count();
    			return true;
    		case R.id.default_life_settings:
    			default_life();
    			return true;
    		case R.id.new_game:
    			reset_game();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    //Takes TextView, returns TextView instance with default life set from user preferences.
    public TextView set_default_life(TextView tv, int default_life){       
        tv.setText(Integer.toString(default_life));
        return tv;
    }
    
    //Bring up default life settings dialog.
    public void default_life(){
    	final EditText input = new EditText(MainActivity.this);
    	int max_length = 3;
    	input.setInputType(InputType.TYPE_CLASS_NUMBER);
    	input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(max_length)});
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.MATCH_PARENT);
    	input.setLayoutParams(lp);
    	builder.setTitle("Enter Default Life")
    		   .setView(input)    		   
    		   .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int id){
                       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                       SharedPreferences.Editor editor = settings.edit();
                       editor.putInt("default_life", Integer.parseInt(input.getText().toString()));
                       
                       editor.commit();
                       
                       //Restart Activity to force changes to take effect. This may be dirty...
                       finish();
                       startActivity(getIntent());
                   }
               })
               .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                	   //Nothing happens and Android automatically closes the dialog.
                   }
               });
    	builder.show();
    }
    
    //Handles selection of reset game option. Restarts activity.
    public void reset_game(){
    	finish();
        startActivity(getIntent());
    }
    
    //Bring up player count settings dialog.
    public void player_count(){
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	CharSequence options[] = new CharSequence[] {"2", "3", "4"};
    	
    	builder.setTitle("Player Count")
    		   .setItems(options, new DialogInterface.OnClickListener(){
                   @SuppressLint("CutPasteId") 
                   public void onClick(DialogInterface dialog, int which){
                	   
                       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);                       
                       int default_life = settings.getInt("default_life", 20);
                       
                	   //Switches are horrible, and I'm not entirely convinced that this is better than using an Intent for each case.
                	   switch (which){
                	   	   case 0:
                	   		   setContentView(R.layout.activity_main);
                	   		   
                               TextView tv_1_1 = (TextView) findViewById(R.id.life_total_main);
                               TextView tv_1_2 = (TextView) findViewById(R.id.life_total_main_2);
                	   		   
                	   		   tv_1_1 = set_default_life(tv_1_1, default_life);
                	   		   tv_1_2 = set_default_life(tv_1_2, default_life);         	   		   
                	   		   break;
                	   	   case 1:              	   		   
                	   		   setContentView(R.layout.three_players);
                	   		   
                	   		   //TODO: Write a method that return an Int[] with all of the resources needed, based on player count to improve clarity, and get rid
                	   		   //of the ugly yellow lines.
                               TextView tv_2_1 = (TextView) findViewById(R.id.life_total_main);
                               TextView tv_2_2 = (TextView) findViewById(R.id.life_total_main_2);
                               TextView tv_2_3 = (TextView) findViewById(R.id.life_total_1);
                	   		   
                	           tv_2_1 = set_default_life(tv_2_1, default_life);
                	           tv_2_2 = set_default_life(tv_2_2, default_life);
                	           tv_2_3 = set_default_life(tv_2_3, default_life);                	   		   
                	   		   break;
                	   	   case 2:
                	   		   setContentView(R.layout.four_players);
                	   		   
                               TextView tv_3_1 = (TextView) findViewById(R.id.life_total_main);
                               TextView tv_3_2 = (TextView) findViewById(R.id.life_total_main_2);
                               TextView tv_3_3 = (TextView) findViewById(R.id.life_total_1);
                               TextView tv_3_4 = (TextView) findViewById(R.id.life_total_2);
                	   		   
                	           tv_3_1 = set_default_life(tv_3_1, default_life);
                	           tv_3_2 = set_default_life(tv_3_2, default_life);
                	           tv_3_3 = set_default_life(tv_3_3, default_life);
                	           tv_3_4 = set_default_life(tv_3_4, default_life);
                	   		   break;
                	   }
                   }
               });
    	builder.show();
    }
    
    public void on_click_minus(View v){
    	int number;
    	CharSequence text_int;
    	
    	//Have I mentioned that I hate switches?
    	switch(v.getId()){
    		case R.id.minus_button_main:
    			final TextView life_total_main = (TextView) findViewById(R.id.life_total_main);
    			text_int = (CharSequence) life_total_main.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_main.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.minus_button_main_2:
    			final TextView life_total_main_2 = (TextView) findViewById(R.id.life_total_main_2);
    			text_int = (CharSequence) life_total_main_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_main_2.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.minus_button_1:
    			final TextView life_total_1 = (TextView) findViewById(R.id.life_total_1);
    			text_int = (CharSequence) life_total_1.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_1.setText((CharSequence)Integer.toString(number));
    			break;	
    		case R.id.minus_button_2:
    			final TextView life_total_2 = (TextView) findViewById(R.id.life_total_2);
    			text_int = (CharSequence) life_total_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_2.setText((CharSequence)Integer.toString(number));
    			break;    			
    	}
    }
    
    public void on_click_plus(View v){
    	int number;
    	CharSequence text_int;
    	
    	switch(v.getId()){
    		case R.id.plus_button_main:
    			final TextView life_total_main = (TextView) findViewById(R.id.life_total_main);
    			text_int = (CharSequence) life_total_main.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			life_total_main.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.plus_button_main_2:
    			final TextView life_total_main_2 = (TextView) findViewById(R.id.life_total_main_2);
    			text_int = (CharSequence) life_total_main_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			life_total_main_2.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.plus_button_1:
    			final TextView life_total_1 = (TextView) findViewById(R.id.life_total_1);
    			text_int = (CharSequence) life_total_1.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			life_total_1.setText((CharSequence)Integer.toString(number));
    			break;	
    		case R.id.plus_button_2:
    			final TextView life_total_2 = (TextView) findViewById(R.id.life_total_2);
    			text_int = (CharSequence) life_total_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			life_total_2.setText((CharSequence)Integer.toString(number));
    			break;
    	}
    }
    
    //Dialog after clicking on TextView for editing player names.
    public void on_click_name(View v){    	
    	final EditText input = new EditText(MainActivity.this);
    	final TextView tv = (TextView) findViewById(v.getId());
    	int max_length = 12;
    	input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(max_length)});    	
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                  LinearLayout.LayoutParams.MATCH_PARENT,
                  LinearLayout.LayoutParams.MATCH_PARENT);
    	input.setLayoutParams(lp);    	    	
    	builder.setTitle("Enter Player Name")
    		   .setView(input)
    		   .setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener(){
    			   @Override
    			   public void onClick(DialogInterface dialog, int id){
    				   tv.setText(input.getText());
    			   }
    		   })
    		   .setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener(){
    			   @Override
    			   public void onClick(DialogInterface dialog, int id) {
    				   //Nothing happens and Android automatically closes the dialog.
    			   }
    		   });
    	builder.show();
    }
    
    //Click event for life totals, brings up Commander damage screen and passes relevant data over.
    public void on_click_life(View v){    	
    	Intent intent = new Intent(this, GeneralDamageActivity.class);
    	
    	TextView player_1 = (TextView) findViewById(R.id.player_name_1);
    	TextView player_2 = (TextView) findViewById(R.id.player_name_2);
    	TextView player_3 = (TextView) findViewById(R.id.player_name_3);
    	TextView player_4 = (TextView) findViewById(R.id.player_name_4);
    	
    	ArrayList<String> player_names = new ArrayList<String>();
    	
    	if(player_1 != null){
    		player_names.add(player_1.getText().toString());
    	}
    	else{
    		player_names.add("Player 1");
    	}
    	
    	if(player_2 != null){
    		player_names.add(player_2.getText().toString());
    	}
    	else{
    		player_names.add("Player 2");
    	}
    	
    	if(player_3 != null){
    		player_names.add(player_3.getText().toString());
    	}
    	else{
    		player_names.add("Player 3");
    	}
    	
    	if(player_4 != null){
    		player_names.add(player_4.getText().toString());
    	}
    	else{
    		player_names.add("Player 4");
    	}
    	
    	String[] player_names_arr = new String[player_names.size()];
    	player_names_arr = player_names.toArray(player_names_arr);
    	CharSequence active_player = find_active_player(v);
    	
    	intent.putExtra("player_names", player_names_arr);
    	intent.putExtra("active_player", active_player);
    	
    	startActivity(intent);
    }
        
    public CharSequence find_active_player(View v){
    	
    	TextView tv;
    	
    	if(v.getId() == R.id.life_total_main){
    		tv = (TextView) findViewById(R.id.player_name_1);
    		return tv.getText();
    	}
    	else if(v.getId() == R.id.life_total_main_2){
    		tv = (TextView) findViewById(R.id.player_name_2);
    		return tv.getText();
    	}
    	else if(v.getId() == R.id.life_total_1){
    		tv = (TextView) findViewById(R.id.player_name_3);
    		return tv.getText();
    	}
    	if(v.getId() == R.id.life_total_2){
    		tv = (TextView) findViewById(R.id.player_name_4);
    		return tv.getText();
    	}
    	return "";
    }
}
