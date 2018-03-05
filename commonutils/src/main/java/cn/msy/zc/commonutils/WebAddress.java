package cn.msy.zc.commonutils;

/**
 * Created by hxy on 2016/7/19 0019.
 *
 * description : 网上抄来的一段用来解析url的类
 */

import com.orhanobut.logger.Logger;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.util.Patterns.GOOD_IRI_CHAR;


public class WebAddress {


    static final int MATCH_GROUP_SCHEME = 1;
    static final int MATCH_GROUP_AUTHORITY = 2;
    static final int MATCH_GROUP_HOST = 3;
    static final int MATCH_GROUP_PORT = 4;
    static final int MATCH_GROUP_PATH = 5;
    private static final String TAG = WebAddress.class.getSimpleName();
    //根据正则表达式进行判断
    static Pattern sAddressPattern = Pattern.compile(
            /* scheme    */ "(?:(http|https|file)\\:\\/\\/)?" +
            /* authority */ "(?:([-A-Za-z0-9$_.+!*'(),;?&=]+(?:\\:[-A-Za-z0-9$_.+!*'(),;?&=]+)?)@)?" +
            /* host      */ "([" + GOOD_IRI_CHAR + "%_-][" + GOOD_IRI_CHAR + "%_\\.-]*|\\[[0-9a-fA-F:\\.]+\\])?" +
            /* port      */ "(?:\\:([0-9]*))?" +
            /* path      */ "(\\/?[^#]*)?" +
            /* anchor    */ ".*", Pattern.CASE_INSENSITIVE);
    private String mScheme;
    private String mHost;
    private int mPort;
    private String mPath;
    private String mAuthInfo;

    /**
     * parses given uriString.
     */
    //用法:将需要判断的url传入,new WebAddress(address) 如果抛出异常,则会出现问题
    public WebAddress(String address) throws ParseException {
        if (address == null) {
            Logger.e(TAG, "address 为空");
            throw new NullPointerException();
        }

        mScheme = "";
        mHost = "";
        mPort = -1;
        mPath = "/";
        mAuthInfo = "";


        Matcher m = sAddressPattern.matcher(address);
        String t;
        if (m.matches()) {
            t = m.group(MATCH_GROUP_SCHEME);
            if (t != null)
                mScheme = t.toLowerCase(Locale.getDefault());
            t = m.group(MATCH_GROUP_AUTHORITY);
            if (t != null)
                mAuthInfo = t;
            t = m.group(MATCH_GROUP_HOST);
            if (t != null)
                mHost = t;
            t = m.group(MATCH_GROUP_PORT);
            if (t != null && t.length() > 0) {
                // The ':' character is not returned by the regex.
                try {
                    mPort = Integer.parseInt(t);
                } catch (NumberFormatException ex) {
                    throw new ParseException("Bad port");
                }
            }
            t = m.group(MATCH_GROUP_PATH);
            if (t != null && t.length() > 0) {
                /* handle busted myspace frontpage redirect with
                   missing initial "/" */
                if (t.charAt(0) == '/') {
                    mPath = t;
                } else {
                    mPath = "/" + t;
                }
            }


        } else {
            Logger.e(TAG, "无效的地址");
            throw new ParseException("Bad address");
        }


        /* Get port from scheme or scheme from port, if necessary and
           possible */
        if (mPort == 443 && mScheme.equals("")) {
            mScheme = "https";
        } else if (mPort == -1) {
            if (mScheme.equals("https"))
                mPort = 443;
            else
                mPort = 80; // default
        }
        if (mScheme.equals(""))
            mScheme = "http";
    }

    @Override
    public String toString() {
        String port = "";
        if ((mPort != 443 && mScheme.equals("https")) || (mPort != 80 && mScheme.equals("http"))) {
            port = ":" + Integer.toString(mPort);
        }
        String authInfo = "";
        if (mAuthInfo.length() > 0) {
            authInfo = mAuthInfo + "@";
        }


        return mScheme + "://" + authInfo + mHost + port + mPath;
    }

    public String getScheme() {
        return mScheme;
    }

    public void setScheme(String scheme) {
        mScheme = scheme;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        mHost = host;
    }

    public int getPort() {
        return mPort;
    }

    public void setPort(int port) {
        mPort = port;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getAuthInfo() {
        return mAuthInfo;
    }

    public void setAuthInfo(String authInfo) {
        mAuthInfo = authInfo;
    }

    public static class ParseException extends RuntimeException {
        public String response;

        ParseException(String response) {
            this.response = response;
        }
    }
}