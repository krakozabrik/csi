package domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Price implements Serializable {

    public Price(long id, String productCode, int number, int depart, Date begin, Date end, long value) {
        this.id = id;
        this.productCode = productCode;
        this.number = number;
        this.depart = depart;
        this.begin = begin;
        this.end = end;
        this.value = value;
    }

    public Price() {
    }

    /**
     * Идентификатор в БД
     */
    private long id;

    /**
     * Код товара
     */
    private String productCode;

    /**
     * Номер цены
     */
    private int number;

    /**
     * Номер отдела
     */
    private int depart;

    /**
     * Начало действия
     */
    private Date begin;

    /**
     * Конец действия
     */
    private Date end;

    /**
     * Значение цены в копейках
     */
    private long value;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepart() {
        return depart;
    }

    public void setDepart(int depart) {
        this.depart = depart;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return number == price.number &&
                depart == price.depart &&
                Objects.equals(productCode, price.productCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, number, depart);
    }
}
