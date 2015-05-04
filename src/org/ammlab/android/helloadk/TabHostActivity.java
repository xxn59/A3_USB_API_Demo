package org.ammlab.android.helloadk;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;
import android.widget.TabHost;

public class TabHostActivity extends TabActivity {

	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);
		//requestWindowFeature(Window.FEATURE_ACTION_BAR);
		
		try
		{
		    Resources res = getResources(); 
		    @SuppressWarnings("deprecation")
			TabHost tabHost = getTabHost();  
		    TabHost.TabSpec spec; 
		    Intent intent;  
		    tabHost.clearAllTabs();
		    
		    // create an intent for the tab which points at the class file for that tab
		    intent = new Intent().setClass(this, HelloADKActivity.class);

		    //give the tab a name and set the icon for the tab
		    spec = tabHost.newTabSpec("tab1").setIndicator("", res.getDrawable(R.drawable.icon_config)).setContent(intent);
		    tabHost.addTab(spec);
	
		    intent = new Intent().setClass(this, Tab2.class);
		    spec = tabHost.newTabSpec("tab2").setIndicator("", res.getDrawable(R.drawable.icon_config2)).setContent(intent);
		    tabHost.addTab(spec);
	
		    intent = new Intent().setClass(this, Tab3.class);
		    spec = tabHost.newTabSpec("tab3").setIndicator("", res.getDrawable(R.drawable.icon_config3)).setContent(intent);
		    tabHost.addTab(spec);
	
		    tabHost.setCurrentTab(0);
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
