package us.zamzow.mazwoz.dirtyunicornschangelog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.widget.AdapterView.OnItemClickListener;
import android.preference.Preference.OnPreferenceClickListener;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity {

    private String[] VERS;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage(R.string.du_message)
               .setTitle(R.string.du_message_title)
               .setPositiveButton(R.string.du_download, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                           String url = "https://plus.google.com/u/0/communities/109738128866939227235";
                           Intent i = new Intent(Intent.ACTION_VIEW);
                           i.setData(Uri.parse(url));
                           startActivity(i);
                   }
               })
               .setNegativeButton(R.string.du_ok, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                           dialog.dismiss();
                   }
               });

        AlertDialog du_dialog = builder.create();
        du_dialog.show();

        XmlParser xmp = new XmlParser();
        try
        {
            VERS = xmp.GetVers();
        }
        catch (IOException e) {Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}
        catch (XmlPullParserException e){Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}

        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_vers, VERS));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                XmlParser xmp = new XmlParser();
                try
                {
                    String changes = xmp.GetChanges(((TextView) view).getText().toString());
                    Intent intnet = new Intent(getBaseContext(), ChangeList.class);
                    intnet.putExtra("changes",changes);
                    intnet.putExtra("title", ((TextView)view).getText());
                    startActivity(intnet);
                }
                catch (IOException e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
                catch (XmlPullParserException e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
            }
        });
    }

    public class XmlParser {
        private Resources res;
        private int x = 0;


        public XmlParser(){};
        public String[] GetVers() throws XmlPullParserException, IOException {
        String[] VerList = null;
        List<String> verList = new ArrayList<String>();
        res = getResources();
        XmlResourceParser xpp = res.getXml(R.xml.du_changelog);
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                        if(xpp.getIdAttribute() != null)
                        {
                            verList.add(xpp.getIdAttribute());
                            x++;
                        }
                }
                eventType = xpp.next();

            }
        VerList = new String[verList.size()];
        VerList = verList.toArray(VerList);
        return VerList;
        }

        public String GetChanges(String ChangeID) throws XmlPullParserException, IOException {
            String VerList = null;
            res = getResources();
            XmlResourceParser xpp = res.getXml(R.xml.du_changelog);
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG) {


                    if(xpp.getIdAttribute() != null)
                    {
                        if(xpp.getIdAttribute().equals(ChangeID))
                        {
                            System.out.println("ID ATTRIBUTE = " + xpp.getIdAttribute() + "   CHANGE ID = " + ChangeID);
                            VerList = xpp.getAttributeValue(1);
                        }
                    }
                }
                eventType = xpp.next();

            }

            return VerList;
        }
    }
}
