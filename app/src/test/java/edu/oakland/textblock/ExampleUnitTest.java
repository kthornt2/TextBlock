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
        String[] result = response.split("'\n'");
        System.out.println(result[0].indexOf('\n'));

//        if (result.length > 1) {
        int numberOfResult = Integer.valueOf(result[0].substring(0, result[0].indexOf('\n')));
        System.out.println(numberOfResult);
        for (String element : result) {
            System.out.println(element);
        }
//        }
    }
}