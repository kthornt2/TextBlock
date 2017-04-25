package edu.oakland.textblock;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String response = "4\nURL: http:xxxxx\nURL: http:xxxxx\n";
        response = "0\n";
        response = "4;" + "URL: 52.41.167.226/photos/IMG_20170414_162429.jpg;" +
                "URL: 52.41.167.226/photos/IMG_20170414_162437.jpg;" +
                "URL: 52.41.167.226/photos/IMG_20170414_162618.jpg;";
        String[] result = response.split(";");
        System.out.println(result[0].indexOf(";"));
        System.out.println("Length: " + result.length);
//        if (result.length > 1) {
        int numberOfResult = Integer.valueOf(result[0]);
        System.out.println(numberOfResult);
        for (String element : result) {
            System.out.print(element);
        }

        String string = "abcbdbebfb";
        String[] bString = string.split("b");
        for (String e : bString) {
            System.out.print(e);
        }
        System.out.print(bString.length);

//        }
    }


    @Test
    public void test() {
        String s1 = "http://52.41.167.226/photos/IMG_20170424_021813.jpg";
        String s2 = "http://52.41.167.226/photos/IMG_20170424_021818.jpg";
        String s3 = "http://52.41.167.226/photos/IMG_20170424_023425.jpg";
        System.out.println(decideTrueTwinPhoto(s1, s2, s3));
    }

    private String decideTrueTwinPhoto(String photo_url, String photo_url_neighbor1, String photo_url_neighbor2) {
        Long millisecond0 = parsePhotoUrl(photo_url);
        Long millisecond1 = parsePhotoUrl(photo_url_neighbor1);
        Long millisecond2 = parsePhotoUrl(photo_url_neighbor2);
        if (returnCloserOne(millisecond0, millisecond1, millisecond2) == 1) {
            return photo_url_neighbor1;
        } else {
            return photo_url_neighbor2;
        }
    }

    private int returnCloserOne(Long millisecond0, Long millisecond1, Long millisecond2) {
        Long difference1 = Math.abs(millisecond0 - millisecond1);
        Long difference2 = Math.abs(millisecond0 - millisecond2);
        if (difference1 < difference2) {
            return 1;
        } else {
            return 2;
        }
    }

    private long parsePhotoUrl(String photo_url) {
        Date date = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        try {
            date = simpleDateFormat.parse(photo_url.substring(32, 46));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }


    @Test
    public void testSimpleDateFormat() {
        SimpleDateFormat mySimpleDateFormat2 = new SimpleDateFormat("EEE, MMM dd, yyyy, HH:mm:ss z");
        Date date = new Date();
        String string = mySimpleDateFormat2.format(date);
        System.out.println(string);
        Date date2 = null;
        try {
            date2 = mySimpleDateFormat2.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date2.toString());

        SimpleDateFormat mySimpleDateFormat1 = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date3 = new Date();
        String dateString = mySimpleDateFormat1.format(date3);
        System.out.println(dateString);
        try {
            System.out.println(mySimpleDateFormat1.parse(dateString).toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPhotoURLAssistant() {
        String url = "http://52.41.167.226/photos/IMG_20170424_005050.jpg";
        url = "http://52.41.167.226/photos/IMG_20170424_001220.jpg";

        String name = PhotoURLAssistant.getNameFromURL(url);
        System.out.println(name);

        String dateString = name.substring(4, 18);
        System.out.println(dateString);

        Date date1_1 = PhotoURLAssistant.getDateFromString1(name.substring(4, 19));
        String dateString1 = PhotoURLAssistant.getStringFromDate1(date1_1);
        System.out.println(date1_1);

        String dateString2 = PhotoURLAssistant.getStringFromDate2(date1_1);
        System.out.println(dateString2);

        Date date2 = PhotoURLAssistant.getDateFromString2(dateString2);
        System.out.println(date2.toString());


        String readString = PhotoURLAssistant.getReadableDateLabelFromPhotoURL(url);

        System.out.println(PhotoURLAssistant.getDateFromString2(readString).toString());

        System.out.println(PhotoURLAssistant.getStringFromDate1(PhotoURLAssistant.getDateFromString2(PhotoURLAssistant.getReadableDateLabelFromPhotoURL(url))));

        System.out.println(PhotoURLAssistant.getURLFromReadableDateLabel(PhotoURLAssistant.getReadableDateLabelFromPhotoURL(url)));

    }

}