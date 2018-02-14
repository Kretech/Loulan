package net.romatic.utils;

/**
 * @author zhrlnt@gmail.com
 */
public class WordUtils {

    public static String upperFirst(String word) {
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static String lowerFirst(String word) {
        return word.substring(0, 1).toLowerCase() + word.substring(1);
    }


    public static String snake(String word) {
        return snake(word, "_");
    }

    public static String snake(String word, String sep) {
        return lowerFirst(word);
    }

}
