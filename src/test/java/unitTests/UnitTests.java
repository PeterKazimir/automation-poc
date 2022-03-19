package unitTests;

import Utils.DateUtils;
import org.junit.Assert;
import org.junit.Test;

public class UnitTests {

    @Test
    public void testExpectedMonthHeaderConversion() {
        String departureDate = "30.09.2022";
        Assert.assertEquals(DateUtils.getExpectedMonthHeaderDE(departureDate), "September 2022");

        departureDate = "01.12.2999";
        Assert.assertEquals(DateUtils.getExpectedMonthHeaderDE(departureDate), "Dezember 2999");
    }

    @Test
    public void testUnexpectedMonthHeaderConversion() {
        String date = "99.99.9999";
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> DateUtils.getExpectedMonthHeaderDE(date));
    }

    @Test
    public void testGetMonthNumber() {
        Assert.assertEquals(12, DateUtils.getMonthNumber("Dezember"));
        Assert.assertEquals(1, DateUtils.getMonthNumber("Januar"));
        Assert.assertEquals(5, DateUtils.getMonthNumber("Mai"));
    }

    @Test
    public void testGetInvalidMonthNumber() {
        Assert.assertEquals(0, DateUtils.getMonthNumber("Bingo Bongo"));
    }
}
