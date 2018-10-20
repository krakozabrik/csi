import domain.Price;
import org.junit.jupiter.api.Test;
import service.PriceService;
import service.PriceServiceImpl;

import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Тесты объединения цен
 * todo именовать тесты в виде "testWithInternalPrice" не стал, т.к. в этом случае удобне по номерам
 */
public class PriceServiceTest {

    private PriceService priceService = new PriceServiceImpl();

    @Test
    public void test1() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 50);
        Price price2 = new Price(2, "134", 1, 1, new Date(2000), new Date(3000), 60);

        List<Price> result = priceService.mergePrices(singletonList(price1), singletonList(price2));
        assertEquals(result.size(), 3);
        assertEquals("[1000|50|2000] [2000|60|3000] [3000|50|4000] ", pricesToString(result));

    }

    @Test
    public void test2() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 100);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(6000), 120);
        Price price3 = new Price(3, "134", 1, 1, new Date(3000), new Date(5000), 110);

        List<Price> result = priceService.mergePrices(asList(price1, price2), singletonList(price3));
        assertEquals(result.size(), 3);
        assertEquals("[1000|100|3000] [3000|110|5000] [5000|120|6000] ", pricesToString(result));
    }

    @Test
    public void test3() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(3000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(3000), new Date(5000), 87);
        Price price3 = new Price(3, "134", 1, 1, new Date(5000), new Date(7000), 90);
        Price price4 = new Price(3, "134", 1, 1, new Date(2000), new Date(4000), 80);
        Price price5 = new Price(3, "134", 1, 1, new Date(4000), new Date(6000), 85);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), asList(price4, price5));
        assertEquals(result.size(), 3);
        assertEquals("[1000|80|4000] [4000|85|6000] [6000|90|7000] ", pricesToString(result));
    }

    @Test
    public void test4() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(31000), 11000);
        Price price2 = new Price(2, "134", 2, 1, new Date(10000), new Date(20000), 99000);
        Price price3 = new Price(3, "66", 1, 2, new Date(1000), new Date(31000), 5000);
        Price price4 = new Price(4, "134", 1, 1, new Date(20000), new Date(51000), 11000);
        Price price5 = new Price(5, "134", 2, 1, new Date(15000), new Date(25000), 92000);
        Price price6 = new Price(6, "66", 1, 2, new Date(12000), new Date(13000), 4000);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), asList(price4, price5, price6));
        assertEquals(result.size(), 6);


    }

    @Test
    public void test5() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(3000), 100);
        Price price2 = new Price(2, "134", 1, 1, new Date(3000), new Date(5000), 110);
        Price price3 = new Price(3, "134", 1, 1, new Date(1000), new Date(7000), 50);

        List<Price> result = priceService.mergePrices(asList(price1, price2), singletonList(price3));
        assertEquals(result.size(), 1);
        assertEquals("[1000|50|7000] ", pricesToString(result));
    }

    @Test
    public void test6() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(8000), 87);
        Price price3 = new Price(3, "134", 1, 1, new Date(2000), new Date(5000), 80);
        Price price4 = new Price(4, "134", 1, 1, new Date(5000), new Date(7000), 85);

        List<Price> result = priceService.mergePrices(asList(price1, price2), asList(price3, price4));
        assertEquals(result.size(), 3);
    }

    @Test
    public void test7() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(8000), 87);

        List<Price> result = priceService.mergePrices(emptyList(), asList(price1, price2));
        assertEquals(result.size(), 2);
        assertEquals("[1000|80|4000] [4000|87|8000] ", pricesToString(result));
    }

    @Test
    public void test8() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 100);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(5000), 80);
        Price price3 = new Price(3, "134", 1, 1, new Date(5000), new Date(6000), 110);
        Price price4 = new Price(4, "134", 1, 1, new Date(1000), new Date(7000), 80);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), singletonList(price4));
        assertEquals(result.size(), 1);
        assertEquals("[1000|80|7000] ", pricesToString(result));
    }

    @Test
    public void test9() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(4000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(6000), 87);
        Price price3 = new Price(3, "134", 1, 1, new Date(2000), new Date(3000), 80);
        Price price4 = new Price(4, "134", 1, 1, new Date(3000), new Date(5000), 85);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), singletonList(price4));
        assertEquals(result.size(), 3);
        assertEquals("[1000|80|3000] [3000|85|5000] [5000|87|6000] ", pricesToString(result));
    }

    @Test
    public void test10() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(2000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(2000), new Date(5000), 87);
        Price price3 = new Price(3, "134", 1, 1, new Date(5000), new Date(7000), 90);
        Price price4 = new Price(4, "134", 1, 1, new Date(3000), new Date(4000), 60);
        Price price5 = new Price(5, "134", 1, 1, new Date(4000), new Date(6000), 70);

        List<Price> result = priceService.mergePrices(asList(price1, price2, price3), asList(price4, price5));
        assertEquals(result.size(), 5);
        assertEquals(
                "[1000|80|2000] [2000|87|3000] [3000|60|4000] [4000|70|6000] [6000|90|7000] ",
                pricesToString(result)
        );
    }

    @Test
    public void test11() {
        Price price1 = new Price(1, "134", 1, 1, new Date(1000), new Date(3000), 80);
        Price price2 = new Price(2, "134", 1, 1, new Date(4000), new Date(6000), 87);
        Price price3 = new Price(3, "134", 1, 1, new Date(2000), new Date(5000), 90);
        Price price4 = new Price(4, "134", 1, 1, new Date(7000), new Date(8000), 100);

        List<Price> result = priceService.mergePrices(asList(price1, price2), asList(price3, price4));
        assertEquals(result.size(), 4);
        assertEquals("[1000|80|2000] [2000|90|5000] [5000|87|6000] [7000|100|8000] ", pricesToString(result));
    }

    private String pricesToString(List<Price> prices) {
        StringBuilder builder = new StringBuilder();
        for (int i = prices.size() - 1; i >= 0; i--) {
            Price price = prices.get(i);
            builder.append("[")
                    .append(price.getBegin().getTime())
                    .append("|")
                    .append(price.getValue())
                    .append("|")
                    .append(price.getEnd().getTime())
                    .append("] ");
        }
        return builder.toString();
    }
}
