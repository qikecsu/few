package util;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import net.sf.json.JSONObject;


/**
 * Title: Ext JS 杈呭姪绫�
 * Description: 璇ョ被鐢ㄤ簬杞崲java瀵硅薄涓篨ML鏂囦欢鏍煎紡鎴朖SON鏂囦欢鏍煎紡
 * @author weijun
 * @time: 2008.07.09
 */
public class ExtHelper {
	/**
	 * 閫氳繃List鐢熸垚XML鏁版嵁
	 * @param recordTotal 璁板綍鎬绘暟锛屼笉涓�畾涓巄eanList涓殑璁板綍鏁扮浉绛�
	 * @param beanList 鍖呭惈bean瀵硅薄鐨勯泦鍚�
	 * @return 鐢熸垚鐨刋ML鏁版嵁
	 */
	public static String getXmlFromList(long recordTotal , List beanList) {
		Total total = new Total();
		total.setResults(recordTotal);
		List results = new ArrayList();
		results.add(total);
		results.addAll(beanList);
		XStream sm = new XStream(new DomDriver());
		for (int i = 0; i < results.size(); i++) {
			Class c = results.get(i).getClass();
			String b = c.getName();
			String[] temp = b.split("\\.");
			sm.alias(temp[temp.length - 1], c);
		}
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"	+ sm.toXML(results);
		return xml;
	}
	/**
	 * 閫氳繃List鐢熸垚XML鏁版嵁
	 * @param beanList 鍖呭惈bean瀵硅薄鐨勯泦鍚�
	 * @return 鐢熸垚鐨刋ML鏁版嵁
	 */
	public static String getXmlFromList(List beanList){
		return getXmlFromList(beanList.size(),beanList);
	}
	/**
	 * 閫氳繃List鐢熸垚JSON鏁版嵁
	 * @param recordTotal 璁板綍鎬绘暟锛屼笉涓�畾涓巄eanList涓殑璁板綍鏁扮浉绛�
	 * @param beanList 鍖呭惈bean瀵硅薄鐨勯泦鍚�
	 * @return 鐢熸垚鐨凧SON鏁版嵁
	 */
	public static String getJsonFromList(long recordTotal , List beanList){
		TotalJson total = new TotalJson();
		total.setResults(recordTotal);
		total.setItems(beanList);
		JSONObject JsonObject = JSONObject.fromObject(total);
		return JsonObject.toString();
	}
	/**
	 * 閫氳繃List鐢熸垚JSON鏁版嵁
	 * @param beanList 鍖呭惈bean瀵硅薄鐨勯泦鍚�
	 * @return 鐢熸垚鐨凧SON鏁版嵁
	 */
	public static String getJsonFromList(List beanList){
		return getJsonFromList(beanList.size(),beanList);
	}
	/**
	 * 閫氳繃bean鐢熸垚JSON鏁版嵁
	 * @param bean bean瀵硅薄
	 * @return 鐢熸垚鐨凧SON鏁版嵁
	 */
	public static String getJsonFromBean(Object bean){
		JSONObject JsonObject = JSONObject.fromObject(bean);
		return JsonObject.toString();
	}
}