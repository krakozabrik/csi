import domain.Price;
import org.junit.jupiter.api.Test;
import service.PriceService;
import service.PriceServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriceServiceTest {

    private PriceService priceService = new PriceServiceImpl();

    @Test
    public void testWithInternalPrice() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 50);
        Price price2 = new Price(2, "134", 1, 1, new Date(2000), new Date(3000), 60);

        List<Price> result = priceService.mergePrices(singletonList(price1), singletonList(price2));
        assertEquals(result.size(), 3);
        assertAll(
                () -> assertEquals(result.get(0).getBegin(), new Date(1000)),
                () -> assertEquals(result.get(0).getEnd(), new Date(2000)),
                () -> assertEquals(result.get(0).getValue(), 50)
        );
        assertAll(
                () -> assertEquals(result.get(1).getBegin(), new Date(2000)),
                () -> assertEquals(result.get(1).getEnd(), new Date(3000)),
                () -> assertEquals(result.get(1).getValue(), 60)
        );
        assertAll(
                () -> assertEquals(result.get(2).getBegin(), new Date(3000)),
                () -> assertEquals(result.get(2).getEnd(), new Date(4000)),
                () -> assertEquals(result.get(2).getValue(), 50)
        );
    }

    @Test
    public void testWithOneIntersectPrice() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 100);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(6000), 120);
        Price price3 = new Price(3, "134", 1, 1, new Date(3000), new Date(5000), 110);

        List<Price> result = priceService.mergePrices(asList(price1, price2), singletonList(price3));
        assertEquals(result.size(), 3);
        assertAll(
                () -> assertEquals(result.get(0).getBegin(), new Date(1000)),
                () -> assertEquals(result.get(0).getEnd(), new Date(3000)),
                () -> assertEquals(result.get(0).getValue(), 100)
        );
        assertAll(
                () -> assertEquals(result.get(1).getBegin(), new Date(3000)),
                () -> assertEquals(result.get(1).getEnd(), new Date(5000)),
                () -> assertEquals(result.get(1).getValue(), 110)
        );
        assertAll(
                () -> assertEquals(result.get(2).getBegin(), new Date(5000)),
                () -> assertEquals(result.get(2).getEnd(), new Date(6000)),
                () -> assertEquals(result.get(2).getValue(), 120)
        );
    }

    @Test
    public void testWithTwoIntersectPrice() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(3000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(3000), new Date(5000), 87);
        Price price3 = new Price(3, "134", 1, 1, new Date(5000), new Date(7000), 90);
        Price price4 = new Price(3, "134", 1, 1, new Date(2000), new Date(4000), 80);
        Price price5 = new Price(3, "134", 1, 1, new Date(4000), new Date(6000), 85);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), asList(price4, price5));
        assertEquals(result.size(), 3);
        assertAll(
                () -> assertEquals(result.get(0).getBegin(), new Date(1000)),
                () -> assertEquals(result.get(0).getEnd(), new Date(4000)),
                () -> assertEquals(result.get(0).getValue(), 80)
        );
        assertAll(
                () -> assertEquals(result.get(1).getBegin(), new Date(4000)),
                () -> assertEquals(result.get(1).getEnd(), new Date(6000)),
                () -> assertEquals(result.get(1).getValue(), 85)
        );
        assertAll(
                () -> assertEquals(result.get(2).getBegin(), new Date(6000)),
                () -> assertEquals(result.get(2).getEnd(), new Date(7000)),
                () -> assertEquals(result.get(2).getValue(), 90)
        );
    }

    @Test
    public void testWithTwoIntersectPriceAndDepart() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(31000), 11000);
        Price price2 = new Price(2, "134", 2, 1, new Date(10000), new Date(20000), 99000);
        Price price3 = new Price(3, "66", 1, 2, new Date(1000), new Date(31000), 5000);
        Price price4 = new Price(4, "134", 1, 1, new Date(20000), new Date(51000), 11000);
        Price price5 = new Price(5, "134", 2, 1, new Date(15000), new Date(25000), 92000);
        Price price6 = new Price(6, "66", 1, 2, new Date(12000), new Date(13000), 4000);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), asList(price4, price5, price6));
        assertEquals(result.size(), 6);
        assertAll(
                () -> assertEquals(result.get(0).getBegin(), new Date(1000)),
                () -> assertEquals(result.get(0).getEnd(), new Date(12000)),
                () -> assertEquals(result.get(0).getValue(), 5000)
        );
        assertAll(
                () -> assertEquals(result.get(1).getBegin(), new Date(12000)),
                () -> assertEquals(result.get(1).getEnd(), new Date(13000)),
                () -> assertEquals(result.get(1).getValue(), 4000)
        );
        assertAll(
                () -> assertEquals(result.get(2).getBegin(), new Date(13000)),
                () -> assertEquals(result.get(2).getEnd(), new Date(31000)),
                () -> assertEquals(result.get(2).getValue(), 5000)
        );
        assertAll(
                () -> assertEquals(result.get(3).getBegin(), new Date(10000)),
                () -> assertEquals(result.get(3).getEnd(), new Date(15000)),
                () -> assertEquals(result.get(3).getValue(), 99000)
        );
        assertAll(
                () -> assertEquals(result.get(4).getBegin(), new Date(15000)),
                () -> assertEquals(result.get(4).getEnd(), new Date(25000)),
                () -> assertEquals(result.get(4).getValue(), 92000)
        );
        assertAll(
                () -> assertEquals(result.get(5).getBegin(), new Date(1000)),
                () -> assertEquals(result.get(5).getEnd(), new Date(51000)),
                () -> assertEquals(result.get(5).getValue(), 11000)
        );
    }

    @Test
    public void testWithDoubleOverlap() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(3000), 100);
        Price price2 = new Price(2, "134", 1, 1, new Date(3000), new Date(5000), 110);
        Price price3 = new Price(3, "134", 1, 1, new Date(1000), new Date(7000), 50);

        List<Price> result = priceService.mergePrices(asList(price1, price2), Arrays.asList(price3));
        assertEquals(result.size(), 1);
        assertAll(
                () -> assertEquals(result.get(0).getBegin(), new Date(1000)),
                () -> assertEquals(result.get(0).getEnd(), new Date(7000)),
                () -> assertEquals(result.get(0).getValue(), 50)
        );
    }
}
