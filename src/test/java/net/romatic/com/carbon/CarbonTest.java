package net.romatic.com.carbon;

import junit.framework.TestCase;
import net.romatic.utils.JsonUtils;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * @author zhrlnt@gmail.com
 */
public class CarbonTest extends TestCase {

    @Test
    public void testCarbon() {

        Assert.assertEquals(Carbon.of("2010-01-01").toDateString(), "2010-01-01");
        Assert.assertEquals(Carbon.of("2010-1-02").toDateString(), "2010-01-02");
        Assert.assertEquals(Carbon.of("2010-1-3").toDateString(), "2010-01-03");

        Assert.assertEquals(Carbon.of("2010-1-3 11:23:38").toDateString(), "2010-01-03");
        Assert.assertEquals(Carbon.of("2010-1-3 11:23:38").toString(), "2010-01-03 11:23:38");
        Assert.assertEquals(Carbon.of("2010-1-3 11:23").toString(), "2010-01-03 11:23");
        Assert.assertEquals(Carbon.of("2010-1-3 11:23:00").toString(), "2010-01-03 11:23");

        Assert.assertEquals(Carbon.of("2010-1-3 11:23:39").toDateTime(), LocalDateTime.of(2010, 1, 3, 11, 23, 39));
    }

    @Test
    public void testSerialize() {
        Assert.assertEquals(Carbon.of("2010-1-3 11:23").toJson(), "\"2010-01-03 11:23\"");

        Assert.assertEquals(JsonUtils.parseToObject("\"2010-01-03 11:23\"", Carbon.class), Carbon.of(2010, 1, 3, 11, 23));
        Assert.assertEquals(new Carbon().parseJson("\"2010-01-03 11:23\""), Carbon.of(2010, 1, 3, 11, 23));
    }
}
