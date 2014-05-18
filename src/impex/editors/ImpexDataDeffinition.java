package impex.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.internal.preferences.Base64;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import synchroniser.Activator;
import synchroniser.preferences.PreferenceConstants;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class ImpexDataDeffinition {

	private Map<String, JsonArray> impexDataDef = null;
	private Preferences preferences = InstanceScope.INSTANCE
			.getNode(Activator.PLUGIN_ID);
	private ImpexHttpClient impexHttpClient = new ImpexHttpClient();

	public Map<String, JsonArray> loadImpexDataDef() {

		impexDataDef = new HashMap<String, JsonArray>();
		try {
			for (JsonValue type : impexHttpClient.getAllTypes()) {
				impexDataDef.put(type.asString(), impexHttpClient
						.getTypeandAttribute(type.asString()).get("attributes")
						.asArray());
			}
			preferences.put(PreferenceConstants.P_IMPEX_DATA_DEF,
					serializeDateDef());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  try {
			    // prefs are automatically flushed during a plugin's "super.stop()".
			   preferences.flush();
			  } catch(BackingStoreException e) {
			    //TODO write a real exception handler.
			    e.printStackTrace();
			  }
		return impexDataDef;
	}

	public Map<String, JsonArray> getImpexDataDef() {
		String storedImpexDef = preferences.get(
				PreferenceConstants.P_IMPEX_DATA_DEF, "");
		if (impexDataDef != null)
			return impexDataDef;
		if (storedImpexDef.isEmpty()) {
			return loadImpexDataDef();
		} else {
			return this.impexDataDef = deserialiseImpexDataDef(storedImpexDef);
		}
	}

	@SuppressWarnings("restriction")
	private String serializeDateDef() {
		// serialize the object
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);

			so.writeObject(impexDataDef);
			so.flush();
			return new String(Base64.encode(bo.toByteArray()));

		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "restriction" })
	public Map<String, JsonArray> deserialiseImpexDataDef(
			String serializedObject) {
		Map<String, JsonArray> impexDatadef = null;
		// deserialize the object
		try {
			byte b[] = Base64.decode(serializedObject.getBytes());
			ByteArrayInputStream bi = new ByteArrayInputStream(b);
			ObjectInputStream si = new ObjectInputStream(bi);
			impexDatadef = (Map<String, JsonArray>) si.readObject();
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}
		return impexDatadef;
	}
}
