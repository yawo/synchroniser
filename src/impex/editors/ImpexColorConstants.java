package impex.editors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.RGB;

public abstract class ImpexColorConstants {
	
	public static String TYPE_IMAGE_ID="hybris_type_image";
	public static String KEYWORD_IMAGE_ID="hybris_keyword_image";
	static RGB XML_COMMENT = new RGB(128, 0, 0);
	static RGB PROC_INSTR = new RGB(128, 128, 128);
	static RGB STRING = new RGB(0, 128, 0);
	static RGB DEFAULT = new RGB(0, 0, 0);
	static RGB TAG = new RGB(0, 0, 128);
	static RGB IMPEX_COMMAND = new RGB(34, 34, 255);
	static RGB IMPEX_VARIABLE = new RGB(0, 191, 255);
	static RGB IMPEX_HEADER_TYPE = new RGB(1, 223, 1);
	static RGB IMPEX_MODIFIER = new RGB(255, 0, 255);
	static RGB IMPEX_SEPARATOR = new RGB(254, 154, 46);
	static RGB IMPEX_ATOMIC= new RGB(116,1,223);


	 public static final Map<String, String> IMPEX_KEYWORDS;
	static {
		Map<String, String> aMap = new HashMap<String, String>();
		aMap.put("INSERT", "impex_cmd");
		aMap.put("INSERT_UPDATE", "impex_cmd");
		aMap.put("REMOVE", "impex_cmd");
		aMap.put("UPDATE", "impex_cmd");
		aMap.put("alias", "impex_modifier");
		aMap.put("allownull", "impex_modifier");
		aMap.put("batchmode", "impex_modifier");
		aMap.put("cacheUnique", "impex_modifier");
		aMap.put("cellDecorator", "impex_modifier");
		aMap.put("collection-delimiter", "impex_modifier");
		aMap.put("dateformat", "impex_modifier");
		aMap.put("default", "impex_modifier");
		aMap.put("false", "impex_atomic");
		aMap.put("forceWrite", "impex_modifier");
		aMap.put("ignoreKeyCase", "impex_modifier");
		aMap.put("ignorenull", "impex_modifier");
		aMap.put("key2value-delimiter", "impex_modifier");
		aMap.put("lang", "impex_modifier");
		aMap.put("map-delimiter", "impex_modifier");
		aMap.put("mode", "impex_modifier");
		aMap.put("numberformat", "impex_modifier");
		aMap.put("parallel", "impex_modifier");
		aMap.put("path-delimiter", "impex_modifier");
		aMap.put("pos", "impex_modifier");
		aMap.put("processor", "impex_modifier");
		aMap.put("translator", "impex_modifier");
		aMap.put("true", "impex_atomic");
		aMap.put("unique", "impex_modifier");
		aMap.put("virtual", "impex_modifier");
		IMPEX_KEYWORDS = Collections.unmodifiableMap(aMap);
	}
}
