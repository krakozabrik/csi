package service;

import domain.Price;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toList;

public class PriceServiceImpl implements PriceService {
    @Override
    public List<Price> mergePrices(List<Price> availablePrices, List<Price> newPrices) {
        Objects.requireNonNull(availablePrices);
        Objects.requireNonNull(newPrices);

        List<Price2Old> oldPreparedPrices = toPreparedList(availablePrices, true);
        List<Price2Old> newPreparedPrices = toPreparedList(newPrices, false);

        Map<Price, List<Price2Old>> groupingPrices = Stream.concat(oldPreparedPrices.stream(), newPreparedPrices.stream())
                .collect(groupingBy(Price2Old::getPrice));

        List<Price> result = new ArrayList<>();

        // для всех списков цен выполним сортировку и выровним
        groupingPrices.values().forEach(prices -> {
            LinkedList<Price2Old> sortedPrices = prices.stream()
                    .sorted(this::comparePrice)
                    .collect(toCollection(LinkedList::new));
            result.addAll(aligmentListPrices(sortedPrices));
        });

        return result;
    }

    /**
     * Выравнивание списка цен
     * Суть работы такова, что по очереди добавляем все элементы в стек, выравнивая пересекающиеся цены
     * Имеющиеся цены имеем право обрезать по границам, новые цены проверяем только граничащие ситуации
     * Остальные ситуации разрешаются за счет сортированности списка
     *
     * @param prices сортированный список цен {@link #comparePrice}
     * @return список объединенных цен
     */
    private List<Price> aligmentListPrices(List<Price2Old> prices) {
        Deque<Price2Old> result = new ArrayDeque<>();
        result.push(prices.get(0));
        for (int i = 1; i < prices.size(); i++) {
            Price2Old curPrepPrice = prices.get(i);
            Price curPrice = curPrepPrice.getPrice();
            Price lastPrice = result.peek().getPrice();

            Date beginCur = curPrice.getBegin();
            Date endCur = curPrice.getEnd();
            Date endLast = lastPrice.getEnd();
            //если следующая цена не пересекается с добавленной
            if (!beginCur.before(endLast)) {
                result.push(curPrepPrice);
                continue;
            }
            //если цены равны, то проставим самый большой конец, т.к. начало по умолчанию бужет меньше
            if (lastPrice.getValue() == curPrice.getValue()) {
                lastPrice.setEnd(endCur.after(endLast) ? endCur : endLast);
                continue;
            }

            if (curPrepPrice.isOld()) {
                // существующие цены просто обрезаем
                if (endCur.after(endLast)) {
                    curPrice.setBegin(lastPrice.getEnd());
                    result.push(curPrepPrice);
                }
            } else {
                // новая цена всегда будет последней, по этому проверим только граничащие ситуации
                lastPrice.setEnd(beginCur);
                Price2Old newPreparedPrice = generateNewPreparedPrice(lastPrice, endCur, endLast);
                if (lastPrice.getBegin().equals(beginCur)) {
                    result.pop();
                }
                result.push(curPrepPrice);
                if (endLast.after(endCur)) {
                    result.push(newPreparedPrice);
                }
            }

        }
        return result.stream()
                .map(Price2Old::getPrice)
                .collect(Collectors.toList());
    }

    private Price2Old generateNewPreparedPrice(Price price, Date begin, Date end) {
        Price newPrice = new Price();
        newPrice.setProductCode(price.getProductCode());
        newPrice.setNumber(price.getNumber());
        newPrice.setDepart(price.getDepart());
        newPrice.setValue(price.getValue());
        newPrice.setBegin(begin);
        newPrice.setEnd(end);
        return new Price2Old(newPrice, true);
    }

    /**
     * Компаратор для сортировки списка цен
     * Цены сортируются по возрастанию начала действия {@link Price#begin}
     * Если начала действия равны, то цена из новых цен встает выше цены из существующих
     */
    private int comparePrice(Price2Old o1, Price2Old o2) {
        Date begin1 = o1.getPrice().getBegin();
        Date begin2 = o2.getPrice().getBegin();

        int res = begin1.compareTo(begin2);
        return res != 0 ? res : (o1.isOld() ? -1 : 1);
    }

    private List<Price2Old> toPreparedList(List<Price> prices, boolean isOld) {
        return prices.stream()
                .map(price -> new Price2Old(price, isOld))
                .collect(toList());
    }
}
