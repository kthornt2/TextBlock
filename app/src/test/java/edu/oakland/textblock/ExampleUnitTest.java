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

}