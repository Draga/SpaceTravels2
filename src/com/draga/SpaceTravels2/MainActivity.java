/**
 *
 */

package com.draga.SpaceTravels2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * @author Draga
 */

public class MainActivity extends Activity implements OnClickListener {
	private Spinner mSpinner;
    /*
     * Called when the activity is first created.
     */
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.main);

		mSpinner = (Spinner) findViewById(R.id.level_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.levels_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
//		mSpinner.setOnItemSelectedListener(this);

        Button playButton = (Button) this.findViewById(R.id.playButton);
        playButton.setOnClickListener(this);
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> arg0) {
//        // TODO Auto-generated method stub
//
//    }

    @Override
    public void onClick(final View v) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
		//TODO: proper level mapping
		intent.putExtra(GameActivity.EXTRA_TAG_LEVEL,mSpinner.getSelectedItemPosition()+1 );
        startActivity(intent);
    }
}
