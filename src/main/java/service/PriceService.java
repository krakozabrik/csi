package service;

import domain.Price;

import java.util.List;

public interface PriceService {
    /**
     * Объединяет существующие и новые цены
     * @param oldPrices существующие цены
     * @param newPrices новые цены
     * @return список объединенных цен
     */
    List<Price> mergePrices(List<Price> oldPrices, List<Price> newPrices);
}
