package edu.oakland.textblock;

import org.junit.Test;

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
}