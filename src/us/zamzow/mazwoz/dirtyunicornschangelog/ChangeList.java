package us.zamzow.mazwoz.dirtyunicornschangelog;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

/**
 * Created by mazwoz on 6/8/13.
 */
public class ChangeList extends FragmentActivity {
    public String VerChanges = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_change_list);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            VerChanges = extras.getString("changes");
            setTitle("Changelog for " + extras.getString("title"));
            TextView tv = (TextView) findViewById(R.id.changes);
            tv.setMovementMethod(new ScrollingMovementMethod());
            tv.setText(VerChanges);
        }
    }
}
