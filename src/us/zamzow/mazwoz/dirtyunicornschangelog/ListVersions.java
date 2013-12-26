package us.zamzow.mazwoz.dirtyunicornschangelog;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
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

public class ListVersions extends ListActivity{
	private String[] VERS;
	private String DeviceId = "";
	private String DeviceName = "";
	@Override
    protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
    	System.out.println("Started ListVers without proper variables");

        savedInstanceState = getIntent().getExtras();
        if(savedInstanceState != null)
        {
            DeviceId = savedInstanceState.getString("deviceID");
            DeviceName = savedInstanceState.getString("deviceName");

            System.out.println("Started ListVers with proper variables");
        }
        setTitle("Changelog for " + DeviceName);
        final int resId = getResources().getIdentifier(DeviceId, "xml", getPackageName());
    	System.out.println("Pulling versions");
        XmlParser xmp = new XmlParser();
        try
        {
            VERS = xmp.GetVers(resId);
            //VERS = xmp.GetVers(R.xml.l900);
        }
        catch (IOException e) {Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}
        catch (XmlPullParserException e){Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();}
        System.out.println("No errors 1");
        setListAdapter(new ArrayAdapter<String>(this, R.layout.list_vers, VERS));
        System.out.println("set adapter");
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        System.out.println("displaying data");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                XmlParser xmp = new XmlParser();
                try
                {
                	System.out.println(((TextView) view).getText().toString() + "," + DeviceId);
                    String changes = xmp.GetChanges(((TextView) view).getText().toString(), resId);
                    System.out.println(((TextView) view).getText().toString() + "," + DeviceId);
                    Intent intnet = new Intent(getBaseContext(), ChangeList.class);
                    intnet.putExtra("changes",changes);
                    intnet.putExtra("title", DeviceName + " V" + ((TextView)view).getText());
                    startActivity(intnet);
                }
                catch (IOException e) {Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
                catch (XmlPullParserException e){Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();}
            }
        });
    }

    public class XmlParser {
        private Resources res;
        public XmlParser(){};
        public String[] GetVers(int ResId) throws XmlPullParserException, IOException {
        String[] VerList = null;
        List<String> verList = new ArrayList<String>();
        res = getResources();
        XmlResourceParser xpp = res.getXml(ResId);
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                        if(xpp.getIdAttribute() != null)
                        {
                            verList.add(xpp.getIdAttribute());
                        }
                }
                eventType = xpp.next();

            }
        VerList = new String[verList.size()];
        VerList = verList.toArray(VerList);
        return VerList;
        }

        public int getResId(String deviceName, Class<?> c)
        {
        	try
        	{
        		Field idField = c.getDeclaredField(deviceName);
        		return idField.getInt(idField);
        	}
        	catch (Exception e) {
        		e.printStackTrace();
        		return -1;
        	}
        }

        public String GetChanges(String ChangeId, int ResId) throws XmlPullParserException, IOException {
            String VerList = null;
            res = getResources();

            XmlResourceParser xpp = res.getXml(ResId);
            xpp.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,true);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                if(eventType == XmlPullParser.START_TAG) {

                    if(xpp.getIdAttribute() != null)
                    {
                        if(xpp.getIdAttribute().equals(ChangeId))
                        {
                            System.out.println("ID ATTRIBUTE = " + xpp.getIdAttribute() + "   Change ID = " + ChangeId);
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
