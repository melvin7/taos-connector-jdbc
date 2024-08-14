package com.taosdata.jdbc.utils;

import com.taosdata.jdbc.TSDBDriver;

import java.util.Properties;
import java.util.StringTokenizer;

public class StringUtils {

    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * check string every char is numeric or false
     * so string is negative number or include decimal point，will return false
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }

        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static Properties parseUrl(String url, Properties defaults) {
        Properties urlProps = (defaults != null) ? defaults : new Properties();
        if (StringUtils.isEmpty(url)) {
            return urlProps;
        }

        // parse properties in url
        int beginningOfSlashes = url.indexOf("//");
        int index = url.indexOf("?");
        if (index != -1) {
            String paramString = url.substring(index + 1);
            url = url.substring(0, index);
            StringTokenizer queryParams = new StringTokenizer(paramString, "&");
            while (queryParams.hasMoreElements()) {
                String parameterValuePair = queryParams.nextToken();
                int indexOfEqual = parameterValuePair.indexOf("=");
                String parameter = null;
                String value = null;
                if (indexOfEqual != -1) {
                    parameter = parameterValuePair.substring(0, indexOfEqual);
                    if (indexOfEqual + 1 < parameterValuePair.length()) {
                        value = parameterValuePair.substring(indexOfEqual + 1);
                    }
                }
                if (value != null && value.length() > 0 && parameter.length() > 0) {
                    urlProps.setProperty(parameter, value);
                }
            }
        }

        // parse Product Name
        String dbProductName = url.substring(0, beginningOfSlashes);
        dbProductName = dbProductName.substring(dbProductName.indexOf(":") + 1);
        dbProductName = dbProductName.substring(0, dbProductName.indexOf(":"));
        urlProps.setProperty(TSDBDriver.PROPERTY_KEY_PRODUCT_NAME, dbProductName);
        // parse dbname
        url = url.substring(beginningOfSlashes + 2);
        int indexOfSlash = url.indexOf("/");
        if (indexOfSlash != -1) {
            if (indexOfSlash + 1 < url.length()) {
                urlProps.setProperty(TSDBDriver.PROPERTY_KEY_DBNAME, url.substring(indexOfSlash + 1).toLowerCase());
            }
            url = url.substring(0, indexOfSlash);
        }
        // parse port
        int indexOfColon = url.indexOf(":");
        if (indexOfColon != -1) {
            if (indexOfColon + 1 < url.length()) {
                urlProps.setProperty(TSDBDriver.PROPERTY_KEY_PORT, url.substring(indexOfColon + 1));
            }
            url = url.substring(0, indexOfColon);
        }
        // parse host
        if (url.length() > 0 && url.trim().length() > 0) {
            urlProps.setProperty(TSDBDriver.PROPERTY_KEY_HOST, url);
        }
        return urlProps;
    }


    public static byte[] hexToBytes(String hex)
    {
        int byteLen = hex.length() / 2;
        byte[] bytes = new byte[byteLen];

        for (int i = 0; i < hex.length() / 2; i++) {
            int i2 = 2 * i;
            if (i2 + 1 > hex.length())
                throw new IllegalArgumentException("Hex string has odd length");

            int nib1 = hexToInt(hex.charAt(i2));
            int nib0 = hexToInt(hex.charAt(i2 + 1));
            byte b = (byte) ((nib1 << 4) + (byte) nib0);
            bytes[i] = b;
        }
        return bytes;
    }
    private static int hexToInt(char hex)
    {
        int nib = Character.digit(hex, 16);
        if (nib < 0)
            throw new IllegalArgumentException("Invalid hex digit: '" + hex + "'");
        return nib;
    }

    public static String bytesToHex(byte[] bytes)
    {
        return toHex(bytes);
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes a byte array
     * @return a string of hexadecimal digits
     */
    public static String toHex(byte[] bytes)
    {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            buf.append(toHexDigit((b >> 4) & 0x0F));
            buf.append(toHexDigit(b & 0x0F));
        }
        return buf.toString();
    }

    private static char toHexDigit(int n)
    {
        if (n < 0 || n > 15)
            throw new IllegalArgumentException("Nibble value out of range: " + n);
        if (n <= 9)
            return (char) ('0' + n);
        return (char) ('A' + (n - 10));
    }

}
