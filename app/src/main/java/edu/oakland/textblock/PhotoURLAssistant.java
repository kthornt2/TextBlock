package edu.oakland.textblock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Wind on 04/25/2017.
 */

public class PhotoURLAssistant {
    private static SimpleDateFormat mySimpleDateFormat1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
    // formate of date, output string like Mon, Apr 25, 2017, 02:18:01 EDT
    private static SimpleDateFormat mySimpleDateFormat2 = new SimpleDateFormat("EEE, MMM dd, yyyy, HH:mm:ss z");
    private static String PHOTO_URL_PREFIX = "http://52.41.167.226/photos/";
    private static String PHOTO_NAME_PREFIX = "IMG_";
    private static String PHOTO_EXTENTION_SUFFIX = ".jpg";

    /**
     * this is designed to construct a complete and valid url with the photo's name
     *
     * @param photoName
     * @return
     */
    //it is applied in ShowSelfies.java
    public static String getURLFromDateString1(String photoName) {
        return PHOTO_URL_PREFIX + PHOTO_NAME_PREFIX + photoName;
    }

    /**
     * this is designed to get Date from URl
     *
     * @param url
     * @return
     */
    // it is applied in the process of deciding which neighbor image is the twin image
    public static Date getDateFromURL(String url) {
        return getDateFromString1(getNameFromURL(url).substring(4, 19));

    }

    /**
     * this is designed to extract the name of photo with the photo's url
     *
     * @param url
     * @return
     */
    //it is applied in parseToReadableDate
    public static String getNameFromURL(String url) {
        return url.substring(28);
    }

    /**
     * get Date from source string in our format1
     *
     * @param myDateString
     * @return
     */
    public static Date getDateFromString1(String myDateString) {
        Date date = null;
        try {
            date = mySimpleDateFormat1.parse(myDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * get Date from the string readable to users in our format2
     *
     * @param readableLabel
     * @return
     */
    // it is applied in getREadableDateLabelFromPhotoURL()
    public static Date getDateFromString2(String readableLabel) {
        Date date = null;
        try {
            date = mySimpleDateFormat2.parse(readableLabel);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * get string from date in our format1
     *
     * @param date
     * @return
     */
    public static String getStringFromDate1(Date date) {
        return mySimpleDateFormat1.format(date);
    }

    public static String getStringFromDate2(Date date) {
        return mySimpleDateFormat2.format(date);
    }


    /**
     * this is deigned to offer a human readable time stamp with the photo's url
     *
     * @param url
     * @return
     */
    //it is applied to display the list of photos' urls in Notifications.java
    public static String getReadableDateLabelFromPhotoURL(String url) {
        String name = getNameFromURL(url);
        String dateString = name.substring(4, 19);
        return getStringFromDate2(getDateFromString1(dateString));
    }

    public static String getURLFromReadableDateLabel(String readableDateLabel) {
        return getURLFromDateString1(getStringFromDate1(getDateFromString2(readableDateLabel))) + PHOTO_EXTENTION_SUFFIX;
    }

}
