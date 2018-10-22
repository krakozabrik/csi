package service;

import domain.Price;

/**
 * Цена с признаком имеющаяся/новая
 *
 */
class Price2Old {

    /**
     * Цена
     */
    private final Price price;

    /**
     * true - старая
     * false - новая
     */
    private final boolean old;

    Price2Old(Price price, boolean old) {
        this.price = price;
        this.old = old;
    }

    Price getPrice() {
        return price;
    }

    boolean isOld() {
        return old;
    }
}
