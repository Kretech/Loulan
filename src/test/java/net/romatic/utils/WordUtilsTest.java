package net.romatic.utils;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author zhrlnt@gmail.com
 */
public class WordUtilsTest extends TestCase {

    @Test
    public void testUpperFirst() {
        Assert.assertEquals(WordUtils.upperFirst("asd"), "Asd");
        Assert.assertEquals(WordUtils.upperFirst("Asd"), "Asd");
        Assert.assertEquals(WordUtils.upperFirst("1sd"), "1sd");

        Assert.assertEquals(WordUtils.camel("author_id"), "authorId");
    }
}
