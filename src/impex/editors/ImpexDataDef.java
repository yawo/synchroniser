package impex.editors;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import org.apache.tools.ant.util.Base64Converter;
import org.eclipse.core.internal.preferences.Base64;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonValue;

public class ImpexDataDef implements Serializable {

	
	@SuppressWarnings({ "unchecked", "restriction" })
	public static void main(String[] args) throws Exception {
		String serializedObject = "";
		ImpexDataDeffinition impexDataDeffinition=new ImpexDataDeffinition();
		impexDataDeffinition.loadImpexDataDef();
		Map<String,JsonArray> myObject=impexDataDeffinition.getImpexDataDef();
		 // serialize the object
		 try {
		     ByteArrayOutputStream bo = new ByteArrayOutputStream();
		     ObjectOutputStream so = new ObjectOutputStream(bo);
		
			so.writeObject(myObject);
		     so.flush();
		     serializedObject = new String(Base64.encode(bo.toByteArray()));
		     
		 } catch (Exception e) {
		     System.out.println(e);
		     System.exit(1);
		 }
		 // deserialize the object
		 try {
		     @SuppressWarnings("restriction")
			byte b[] = Base64.decode(serializedObject.getBytes()); 
		     ByteArrayInputStream bi = new ByteArrayInputStream(b);
		     ObjectInputStream si = new ObjectInputStream(bi);
		     Map<String,JsonArray> obj = (Map<String,JsonArray>) si.readObject();
		     for (String type : obj.keySet()) {
					System.out.println("type :"+type);
					 for (JsonValue string : obj.get(type)) {
						System.out.println("---- " +string.asString());
					}
				}
		 } catch (Exception e) {
		     System.out.println(e);
		     System.exit(1);
		 }
		
	}
}
