package com.wootasm.lifecounter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class GeneralDamageActivity extends Activity{
	
	public static final String LIFE_TOTALS = "LifeTotals";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);        
        set_values();
    }
	
	@Override
    public void onDestroy(){
		save_values();		
		super.onDestroy();
	}	
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    
    //Sets all values on Activity creation
    public void set_values(){
        Bundle extras = getIntent().getExtras();
        CharSequence active_player = extras.getCharSequence("active_player");
        CharSequence[] player_names = extras.getCharSequenceArray("player_names");
        
        SharedPreferences settings = getSharedPreferences(LIFE_TOTALS, 0);
        
        setContentView(R.layout.general_damage);
        
        TextView this_player = (TextView) findViewById(R.id.player_name_general);
        TextView tv_1 = (TextView) findViewById(R.id.general_damage_1);
        TextView tv_2 = (TextView) findViewById(R.id.general_damage_2);
        TextView tv_3 = (TextView) findViewById(R.id.general_damage_3);
        TextView tv_4 = (TextView) findViewById(R.id.general_damage_4);        
        
        this_player.setText(active_player);
        set_player_names(player_names);
        
        tv_1.setText((CharSequence) Integer.toString(settings.getInt(active_player + "p1_damage", 0)));
        tv_2.setText((CharSequence) Integer.toString(settings.getInt(active_player + "p2_damage", 0)));
        tv_3.setText((CharSequence) Integer.toString(settings.getInt(active_player + "p3_damage", 0)));
        tv_4.setText((CharSequence) Integer.toString(settings.getInt(active_player + "p4_damage", 0)));
    }
    
    //Adds values to LIFE_TOTALS preferences during onDestroy()
    public void save_values(){
		SharedPreferences settings = getSharedPreferences(LIFE_TOTALS, 0);
		SharedPreferences.Editor editor = settings.edit();
		
        TextView tv_1 = (TextView) findViewById(R.id.general_damage_1);
        TextView tv_2 = (TextView) findViewById(R.id.general_damage_2);
        TextView tv_3 = (TextView) findViewById(R.id.general_damage_3);
        TextView tv_4 = (TextView) findViewById(R.id.general_damage_4);
        
        TextView active_player = (TextView) findViewById(R.id.player_name_general);
		
		CharSequence active_player_name = active_player.getText();
        
        editor.putInt(active_player_name + "p1_damage", Integer.parseInt(tv_1.getText().toString()));
        editor.putInt(active_player_name + "p2_damage", Integer.parseInt(tv_2.getText().toString()));
        editor.putInt(active_player_name + "p3_damage", Integer.parseInt(tv_3.getText().toString()));
        editor.putInt(active_player_name + "p4_damage", Integer.parseInt(tv_4.getText().toString()));
        
        editor.commit();    	
    }
    
    //Handles plus events for Commander damage buttons.
    public void on_click_plus_general(View v){
    	int number;
    	CharSequence text_int;
    	
    	switch(v.getId()){
    		case R.id.general_damage_plus_1:
    			final TextView general_damage_1 = (TextView) findViewById(R.id.general_damage_1);
    			text_int = (CharSequence) general_damage_1.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			general_damage_1.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.general_damage_plus_2:
    			final TextView general_damage_2 = (TextView) findViewById(R.id.general_damage_2);
    			text_int = (CharSequence) general_damage_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			general_damage_2.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.general_damage_plus_3:
    			final TextView general_damage_3 = (TextView) findViewById(R.id.general_damage_3);
    			text_int = (CharSequence) general_damage_3.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			general_damage_3.setText((CharSequence)Integer.toString(number));
    			break;	
    		case R.id.general_damage_plus_4:
    			final TextView general_damage_4 = (TextView) findViewById(R.id.general_damage_4);
    			text_int = (CharSequence) general_damage_4.getText();
    			number = Integer.parseInt(text_int.toString());
    			number += 1;
    			general_damage_4.setText((CharSequence)Integer.toString(number));
    			break;
    	}
    }
    
    //Same as above, but for minus events. I swear there's a better way to do this than the switches, but it seems to be standard...
    //I'm actually pretty sure you could do it without a Switch if you applied some sort of flag to each of the Buttons, flagging them as minus or plus buttons...
    //^Figure this out later.
    public void on_click_minus_general(View v){
    	int number;
    	CharSequence text_int;
    	
    	switch(v.getId()){
    		case R.id.general_damage_minus_1:
    			final TextView life_total_main = (TextView) findViewById(R.id.general_damage_1);
    			text_int = (CharSequence) life_total_main.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_main.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.general_damage_minus_2:
    			final TextView life_total_main_2 = (TextView) findViewById(R.id.general_damage_2);
    			text_int = (CharSequence) life_total_main_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_main_2.setText((CharSequence)Integer.toString(number));
    			break;
    		case R.id.general_damage_minus_3:
    			final TextView life_total_1 = (TextView) findViewById(R.id.general_damage_3);
    			text_int = (CharSequence) life_total_1.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_1.setText((CharSequence)Integer.toString(number));
    			break;	
    		case R.id.general_damage_minus_4:
    			final TextView life_total_2 = (TextView) findViewById(R.id.general_damage_4);
    			text_int = (CharSequence) life_total_2.getText();
    			number = Integer.parseInt(text_int.toString());
    			number -= 1;
    			life_total_2.setText((CharSequence)Integer.toString(number));
    			break;
    	}
    }
    
	public void set_player_names(CharSequence[] player_names){
		TextView tv_1 = (TextView) findViewById(R.id.general_damage_player_1);
		TextView tv_2 = (TextView) findViewById(R.id.general_damage_player_2);
		TextView tv_3 = (TextView) findViewById(R.id.general_damage_player_3);
		TextView tv_4 = (TextView) findViewById(R.id.general_damage_player_4);
		
		tv_1.setText(player_names[0]);
		tv_2.setText(player_names[1]);
		tv_3.setText(player_names[2]);
		tv_4.setText(player_names[3]);
	}    
}
