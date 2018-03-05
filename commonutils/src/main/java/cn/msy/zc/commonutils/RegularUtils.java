package cn.msy.zc.commonutils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by hxy on 2016/7/22 0022.
 * description :  正则表达式的工具类.
 */
public class RegularUtils {
    /**
     * email的正则
     */
    public static final String EMAIL_REGUAL = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";

    /**
     * 身份证
     */
    public static final String ID_CARD_REGUAL = "\\d{17}[0-9a-zA-Z]";
    /**
     * 手机号
     */
    public static final String MOBILE_REGUAL = "^[1][34578]\\d{9}$";

    /**
     * 固定电话
     */
    public static final String PHONE_REGUAL = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";

    /**
     * 整数
     */
    public static final String DIGIT_REGUAL = "\\-?[1-9]\\d+";
    /**
     * 浮点数
     */
    public static final String DECIMALS_REGUAL = "\\-?[1-9]\\d+(\\.\\d+)?";
    /**
     * 空格
     */
    public static final String BLANK_SPACE_REGUAL = "\\s+";

    /**
     * 中文
     */
    public static final String CHINESE_REGUAL = "^[\u4E00-\u9FA5]+$";
    /**
     * 生日
     */
    public static final String BIRTHDAY_REGUAL = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";

    /**
     * url
     */
    public static final String URL_REGUAL = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*" + "(\\??(.+=.*)?(&.+=.*)?)?";
    /**
     * 一级域名
     */
    public static final String DOMAIN_REGUAL = "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)";
    /**
     * 完整域名
     */
    public static final String FULL_DOMAIN_REGUAL = "(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)";
    /**
     * 邮政编码
     */
    public static final String POSTCODE_REGUAL = "[1-9]\\d{5}";

    /**
     * ip地址
     */
    public static final String IP_ADDRESS_REGUAL = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?)" +
            "" + ")\\.(0|([1-9](\\d{1,2})?))";

    /**
     * 替换手机的第4-7位为星*号
     */
    public static final String CHANGE_PHONENUM = "(\\d{3})\\d{4}(\\d{4})";

    /**
     * emoji字符的正则
     */
    public static final String EMOJI_REGUAL="/\\uD83C[\\uDF00-\\uDFFF]|\\uD83D[\\uDC00-\\uDE4F]/g";

    public static boolean isEmojiExist(String string){
        if (TextUtils.isEmpty(string)) {
            return false;
        }
        int len = string.length();
        for (int i = 0; i < len; i++) {
            char codePoint = string.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }

        return false;
    }

    // 校验Tag Alias 只能是数字,英文字母和中文
    public static boolean isValidTagAndAlias(String s) {
        Pattern p = Pattern.compile("^[\u4E00-\u9FA50-9a-zA-Z_-]{0,}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }
    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    public static final String changePhoneNum(String phoneNum) {
        if (TextUtils.isEmpty(phoneNum)) {
            return "";
        }
        phoneNum = phoneNum.replaceAll("\\s+", "");
        phoneNum = phoneNum.replaceAll(CHANGE_PHONENUM, "$1****$2");
        return phoneNum;
    }
    public static final String changeEmailNum(String emailNum) {
        if (TextUtils.isEmpty(emailNum)) {
            return "";
        }
        emailNum = emailNum.replaceAll("\\s+", "");
        String surplusStr = emailNum.substring(0, emailNum.indexOf("@"));
        if(surplusStr.length()<3){
            emailNum = emailNum.substring(0,1)+"*****"+emailNum.substring(emailNum.indexOf("@"), emailNum.length());
        }else{
            emailNum = emailNum.substring(0,3)+"*****"+emailNum.substring(emailNum.indexOf("@"), emailNum.length());
        }
        return emailNum;
    }
    /**
     * 银行卡替换
     */
    public static String CHANGE_BANKCARD = "(?<=\\d{0})\\d(?=\\d{4})";

    /**
     * 银行卡替换，保留后四位
     * <p/>
     * 如果银行卡号为空 或者 null ,返回null ；否则，返回替换后的字符串；
     *
     * @param bankCard 银行卡号
     * @return
     */
    public static String bankCardReplaceWithStar(String bankCard) {
        if (bankCard.isEmpty() || bankCard == null) {
            return null;
        } else {
            bankCard = bankCard.replaceAll("\\s+", "");
            StringBuilder sb = new StringBuilder("**** **** **** ");
            int length = bankCard.length();
            String endText = bankCard.substring(length-4,length);
            String result = sb.append(endText).toString();
            return result;
        }
    }

    /**
     * 每4位加入空格
     * @param bankCardNum
     * @return
     */
    public static String replaceSplace(String bankCardNum) {
        StringBuilder sb = new StringBuilder(bankCardNum);
        int length = bankCardNum.length() / 4 + bankCardNum.length();
        for (int i = 0; i < length; i++) {
            if (i % 5 == 0) {
                sb.insert(i, " ");
            }
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }

    /**
     * 校验输入的  只能是中文或者英文
     * @param text
     * @return
     */
    public static boolean commitText(String text) {
        // 只能输入汉字或者英文
        if (!text.matches("^[\\u4e00-\\u9fa5_a-zA-Z]+$")) {
            return false;
        }
        return true;
    }
    /**
     * 校验银行卡卡号  http://bbs.csdn.net/topics/300203203
     *
     * @param cardId
     * @return  true 代表通过校验  false没通过校验
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    /**
     * 验证Email
     *
     * @param email email地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkEmail(String email) {
        return Pattern.matches(EMAIL_REGUAL, email);
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String idCard) {
        return Pattern.matches(ID_CARD_REGUAL, idCard);
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param mobile 移动、联通、电信运营商的号码段
     *               <p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *               、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *               <p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *               <p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     * 044
     */
    public static boolean checkMobile(String mobile) {
        return Pattern.matches(MOBILE_REGUAL, mobile);
    }

    /**
     * 验证固定电话号码
     *
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     *              <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     *              数字之后是空格分隔的国家（地区）代码。</p>
     *              <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     *              对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     *              <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPhone(String phone) {
        return Pattern.matches(PHONE_REGUAL, phone);
    }

    /**
     * 验证整数（正整数和负整数）
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDigit(String digit) {
        return Pattern.matches(DIGIT_REGUAL, digit);
    }

    /**
     * 验证整数和浮点数（正负整数和正负浮点数）
     *
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkDecimals(String decimals) {
        return Pattern.matches(DECIMALS_REGUAL, decimals);
    }

    /**
     * 验证空白字符
     *
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBlankSpace(String blankSpace) {
        return Pattern.matches(BLANK_SPACE_REGUAL, blankSpace);
    }

    /**
     * 验证中文
     *
     * @param chinese 中文字符
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkChinese(String chinese) {
        return Pattern.matches(CHINESE_REGUAL, chinese);
    }

    /**
     * 验证日期（年月日）
     *
     * @param birthday 日期，格式：1992-09-03，或1992.09.03
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkBirthday(String birthday) {
        return Pattern.matches(BIRTHDAY_REGUAL, birthday);
    }


    /**
     * 验证URL地址
     *
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkURL(String url) {
        return Pattern.matches(URL_REGUAL, url);
    }

    /**
     * <pre>
     * 获取网址 URL 的一级域名
     * http://detail.tmall.com/item.htm?spm=a230r.1.10.44.1xpDSH&id=15453106243&_u=f4ve1uq1092 ->> tmall.com
     * </pre>
     *
     * @param url
     * @return
     */
    public static String getDomain(String url) {
        Pattern p = Pattern.compile(DOMAIN_REGUAL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 获取完整的域名
     *
     * @param url
     * @return
     */
    public static String getFullDomain(String url) {
        Pattern p = Pattern.compile(FULL_DOMAIN_REGUAL, Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(url);
        matcher.find();
        return matcher.group();
    }

    /**
     * 匹配中国邮政编码
     *
     * @param postcode 邮政编码
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkPostcode(String postcode) {
        return Pattern.matches(POSTCODE_REGUAL, postcode);
    }


    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     *
     * @param ipAddress IPv4标准地址
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIpAddress(String ipAddress) {
        return Pattern.matches(IP_ADDRESS_REGUAL, ipAddress);
    }
}
