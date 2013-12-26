package us.zamzow.mazwoz.dirtyunicornschangelog;

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
        XmlParser xmp = new XmlParser();
        try
        {
            VERS = xmp.GetDevs();
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
                    String changes = xmp.GetDeviceID(((TextView) view).getText().toString());
                    Intent intnet = new Intent(getBaseContext(), ListVersions.class);
                    System.out.println("Starting ListVers with " + changes + " " + ((TextView)view).getText());
                    intnet.putExtra("deviceID",changes);
                    System.out.println("Sent Device ID: " + changes);
                    intnet.putExtra("deviceName", ((TextView)view).getText());
                    System.out.println("Sent Title " + ((TextView)view).getText());
                    startActivity(intnet);
                }
                catch (IOException e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
                catch (XmlPullParserException e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
            }
        });
    }

    public class XmlParser {
        private Resources res;
        @SuppressWarnings("unused")
		private int x = 0;


        public XmlParser(){};
        public String[] GetDevs() throws XmlPullParserException, IOException {
        String[] VerList = null;
        List<String> verList = new ArrayList<String>();
        res = getResources();
        XmlResourceParser xpp = res.getXml(R.xml.du_devices);
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

        public String GetDeviceID(String DeviceID) throws XmlPullParserException, IOException {
            String VerList = null;
            res = getResources();

            XmlResourceParser xpp = res.getXml(R.xml.du_devices);
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG) {

                    if(xpp.getIdAttribute() != null)
                    {
                        if(xpp.getIdAttribute().equals(DeviceID))
                        {
                            System.out.println("ID ATTRIBUTE = " + xpp.getIdAttribute() + "   Device ID = " + xpp.getAttributeValue(1));
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
