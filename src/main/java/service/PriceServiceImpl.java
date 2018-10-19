package service;

import domain.Price;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class PriceServiceImpl implements PriceService {
    @Override
    public List<Price> mergePrices(List<Price> availablePrices, List<Price> newPrices) {
        Objects.requireNonNull(availablePrices);
        Objects.requireNonNull(newPrices);

        Map<Price, List<Price>> multiAblePrices = toMultiMap(availablePrices);
        Map<Price, List<Price>> multiNewPrices = toMultiMap(newPrices);

        //todo можно через toMap, но так читабельнее (?)
        multiNewPrices.forEach((price, prices) -> {
            multiAblePrices.merge(price, prices, this::remappingPrices);
        });

        return multiAblePrices.values().stream()
                .flatMap(Collection::stream)
                .collect(toList());
    }

    private Map<Price, List<Price>> toMultiMap(List<Price> pricesList) {
        return pricesList.stream()
                .collect(groupingBy(Function.identity()));
    }

    private List<Price> remappingPrices(List<Price> ablePrices, List<Price> newPrices) {
        ablePrices.sort(comparing(Price::getBegin));
        newPrices.sort(comparing(Price::getBegin));

        List<Price> result = new ArrayList<>(ablePrices);
        List<Price> resultTemp = new ArrayList<>();
        for (Price newPrice : newPrices) {
            for (Price curPrice : result) {
                resultTemp.addAll(syncPrices(curPrice, newPrice));
            }
            result = new ArrayList<>(resultTemp);
            resultTemp.clear();
        }

        return result;
    }

    private List<Price> syncPrices(Price curPrice, Price newPrice) {
        Date newBeginDate = newPrice.getBegin();
        Date newEndDate = newPrice.getEnd();
        Date curBeginDate = curPrice.getBegin();
        Date curEndDate = curPrice.getEnd();

        if (isIntersect(curBeginDate, curEndDate, newBeginDate, newEndDate)) {
            if (curPrice.getValue() == newPrice.getValue()) {
                curPrice.setBegin(curBeginDate.before(newBeginDate) ? curBeginDate : newBeginDate);
                curPrice.setEnd(curEndDate.after(newEndDate) ? curEndDate : newEndDate);
                return singletonList(curPrice);
            }

            if (curBeginDate.equals(newBeginDate) && curEndDate.equals(newEndDate)) {
                return singletonList(newPrice);
            }

            if (curBeginDate.before(newBeginDate) || curBeginDate.equals(newBeginDate)) {
                if (newEndDate.before(curEndDate)) {
                    Price lastPrice = createPrice(curPrice, newEndDate, curEndDate);

                    curPrice.setEnd(newBeginDate);
                    return asList(curPrice, newPrice, lastPrice);
                } else {
                    curPrice.setEnd(newBeginDate);
                    if (curBeginDate.before(curPrice.getEnd())) {
                        return asList(curPrice, newPrice);
                    } else return singletonList(newPrice);
                }
            } else {
                curPrice.setBegin(newEndDate);
                if (curPrice.getBegin().before(curEndDate)) {
                    return singletonList(curPrice);
                } else return emptyList();
            }
        }
        return singletonList(curPrice);
    }

    private Price createPrice(Price curPrice, Date newEndDate, Date curEndDate) {
        Price lastPrice = new Price();
        lastPrice.setProductCode(curPrice.getProductCode());
        lastPrice.setNumber(curPrice.getNumber());
        lastPrice.setDepart(curPrice.getDepart());
        lastPrice.setValue(curPrice.getValue());
        lastPrice.setBegin(newEndDate);
        lastPrice.setEnd(curEndDate);
        return lastPrice;
    }

    private boolean isIntersect(Date curBeginDate, Date curEndDate, Date newBeginDate, Date newEndDate) {
        return curBeginDate.before(newEndDate) && curEndDate.after(newBeginDate);
    }
}
