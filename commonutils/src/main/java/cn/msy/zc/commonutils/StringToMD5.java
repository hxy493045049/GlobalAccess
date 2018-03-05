package cn.msy.zc.commonutils;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * 输入一个String(需要加密的文本)，得到一个加密输出String（加密后的文本）
 */



public class StringToMD5 {
    /**
     * 密钥
     */
    public static final String PASS = "81aa6e5dd073339cda44ece15ab4a435";

    public static String MD5(String sourceStr) {
        try {
            // 获得MD5摘要算法的 MessageDigest对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(sourceStr.getBytes());
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < md.length; i++) {
                int tmp = md[i];
                if (tmp < 0)
                    tmp += 256;
                if (tmp < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(tmp));
            }
            // 16位加密
//			 return buf.toString().substring(8, 24);
            // 32位加密
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 签名生成算法
     *
     * @param params 请求参数集，所有参数必须已转换为字符串类型
     * @return 签名
     * @throws IOException
     */
    public static String getSignature(HashMap<String, String> params) throws IOException {
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap(params);
        Set<Map.Entry<String, String>> entrys = sortedParams.entrySet();
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        StringBuilder basestring = new StringBuilder();
        int j = 0;
        String add = "&";
        for (Map.Entry<String, String> param : entrys) {
            j++;
            if (entrys.size() == j) {
                add = "";
            }
            basestring.append(param.getKey()).append("=").append(param.getValue()).append(add);
        }
        String sign = MD5(MD5(basestring.toString()) + PASS);
        return sign.toString().toUpperCase();
    }
}
