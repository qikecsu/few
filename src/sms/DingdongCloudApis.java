package sms;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;

/**
 * ����http client 4.2.3��������ؽӿ�
 * 
 * @author liangyang
 * @date 2015��5��5�� ����11:47:45
 * @version 1.0
 */

public class DingdongCloudApis {
	// ���˻���Ϣ��http��ַ
    private static String URL_GET_USER_INFO = "https://api.dingdongcloud.com/v1/sms/userinfo";

    // ��ѯ�˻�����http��ַ
    private static String URL_GET_BALANCE = "https://api.dingdongcloud.com/v1/sms/querybalance";

    // ��֤����ŷ��ͽӿڵ�http��ַ
    private static String URL_SEND_YZM = "https://api.dingdongcloud.com/v1/sms/sendyzm";

    private static String URL_SEND_YYYZM = "https://api.dingdongcloud.com/v1/sms/sendyyyzm";

    // ֪ͨ���ŷ��ͽӿڵ�http��ַ
    private static String URL_SEND_TZ = "https://api.dingdongcloud.com/v1/sms/sendtz";

    // Ӫ�����ŷ��ͽӿڵ�http��ַ
    private static String URL_SEND_YX = "https://api.dingdongcloud.com/v1/sms/sendyx";

    // �����ʽ�����ͱ����ʽͳһ��UTF-8
    private static String ENCODING = "UTF-8";

    public static void main(String[] args) throws IOException, URISyntaxException {

        // �޸�Ϊ����apikey. apikey���ڹ�����https://www.dingdongcloud.com)��¼���ȡ
        //String apikey = "4c2e5256617cead0734fc78819d797fb";
        String apikey = "4cc12a6dc4747224bc875722e9e6b8dd";

        // �޸�Ϊ��Ҫ���͵��ֻ���
        //String mobile = "13805178732,13182813303,13605175556";
        String mobile = "13805178732";
      //  String mobile = "13182813303";

        /**************** ���˻���Ϣ����ʾ�� *****************/
        // System.out.println(DingdongCloudApis.getUserInfo(apikey));

        /**************** ���˻�������ʾ�� *****************/
        // System.out.println(DingdongCloudApis.getUserInfo(apikey));

        /**************** ������֤����� *****************/
        // ������Ҫ���͵�����(���ݱ����ĳ��ģ��ƥ�䡣��������ƥ�����ϵͳ�ṩ��1��ģ�壩
        String yzmContent = "�������ơ�������֤���ǣ�1234";

        // ����֤����ŵ���ʾ��
 //       System.out.println(DingdongCloudApis.sendYzm(apikey, mobile, yzmContent));

        /**************** ����������֤����� *****************/
        // ���봿����4-6λ
        String yyContent = "1234";

        // �����ŵ���ʾ��
//        System.out.println(DingdongCloudApis.sendYyYzm(apikey, mobile, yyContent));

        /**************** ����֪ͨ���� *****************/
        // ������Ҫ���͵�����
        String tzContent = "��ɽ��Ԥ���豸��֪ͨ����5906��182��,��2016��9��12��9��57�ֱ������봦��";

        // �����ŵ���ʾ��
        System.out.println(DingdongCloudApis.sendTz(apikey, mobile, tzContent));

        /**************** ����Ӫ������ *****************/
        // ������Ҫ���͵����ݣ�����ĩβ������С��˶���T��
        String yxContent = "�������ơ����ѳɹ�ע��̽ɽ�𱨾�������ϵ֧����Ա���ŶԽӲ��ԡ��˶���t";

        // �����ŵ���ʾ��
//        System.out.println(DingdongCloudApis.sendYx(apikey, mobile, yxContent));
    }

    /**
     * ��ѯ�˻���Ϣ�ӿ�
     * 
     * @param apikey
     * @return
     */
    public static String getUserInfo(String apikey) {

        NameValuePair[] data = { new NameValuePair("apikey", apikey) };
        return doPost(URL_GET_USER_INFO, data);
    }

    /**
     * ��ѯ�˻����ӿ�
     * 
     * @param apikey
     * @return
     */
    public static String getBalance(String apikey) {

        NameValuePair[] data = { new NameValuePair("apikey", apikey) };
        return doPost(URL_GET_BALANCE, data);
    }

    /**
     * ������֤�����
     * 
     * @param apikey
     *            apikey
     * @param mobile
     *            �ֻ�����(Ψһ��������)
     * @param content
     *            ���ŷ������ݣ����뾭��utf-8��ʽ����)
     * @return json��ʽ�ַ���
     */
    public static String sendYzm(String apikey, String mobile, String content) {

        if (StringUtils.isNotBlank(content)) {
            try {
                content = URLEncoder.encode(content, ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        NameValuePair[] data = { new NameValuePair("apikey", apikey),

        new NameValuePair("mobile", mobile),

        new NameValuePair("content", content) };

        return doPost(URL_SEND_YZM, data);
    }

    /**
     * ����������֤��
     * 
     * @param apikey
     * @param mobile
     *            �ֻ�����(Ψһ��������)
     * @param content
     *            ���ŷ�������(���봿����4-6λ)
     * @return
     */
    public static String sendYyYzm(String apikey, String mobile, String content) {

        NameValuePair[] data = { new NameValuePair("apikey", apikey),

        new NameValuePair("mobile", mobile),

        new NameValuePair("content", content) };

        return doPost(URL_SEND_YYYZM, data);
    }

    /**
     * ����֪ͨ����
     * 
     * @param apikey
     *            apikey
     * @param mobile
     *            �ֻ����루���������Ӣ�İ�Ƕ��ŷֿ��������ύ1000����
     * @param content
     *            ���ŷ������ݣ����뾭��utf-8��ʽ����)
     * @return json��ʽ�ַ���
     */
    public static String sendTz(String apikey, String mobile, String content) {

        if (StringUtils.isNotBlank(content)) {
            try {
                content = URLEncoder.encode(content, ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        NameValuePair[] data = { new NameValuePair("apikey", apikey),

        new NameValuePair("mobile", mobile),

        new NameValuePair("content", content) };

        return doPost(URL_SEND_TZ, data);
    }

    /**
     * ����Ӫ������
     * 
     * @param apikey
     *            apikey
     * @param mobile
     *            �ֻ����루���������Ӣ�İ�Ƕ��ŷֿ��������ύ1000����
     * @param content
     *            ���ŷ������ݣ����뾭��utf-8��ʽ���룬����ĩβ������С��˶���T����
     * @return json��ʽ�ַ���
     */
    public static String sendYx(String apikey, String mobile, String content) {

        if (StringUtils.isNotBlank(content)) {
            try {
                content = URLEncoder.encode(content, ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        NameValuePair[] data = { new NameValuePair("apikey", apikey),

        new NameValuePair("mobile", mobile),

        new NameValuePair("content", content) };

        return doPost(URL_SEND_YX, data);
    }

    /**
     * ����HttpClient��post����
     * 
     * @param url
     *            �ύ��URL
     * 
     * @param data
     *            �ύNameValuePair����
     * @return �ύ��Ӧ
     */
    private static String doPost(String url, NameValuePair[] data) {

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);
        // method.setRequestHeader("ContentType",
        // "application/x-www-form-urlencoded;charset=UTF-8");
        method.setRequestBody(data);
        // client.getParams().setContentCharset("UTF-8");
        client.getParams().setConnectionManagerTimeout(10000);
        try {
            client.executeMethod(method);
            return method.getResponseBodyAsString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
