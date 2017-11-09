package cc.sjyuan.springboothtmlpdf.model;

import java.io.Serializable;

public class InvoiceRow implements Serializable {

    private String description;

    private Double quantity = 1.0;

    private String unit = "h";

    private Double price = 1.0;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Get the value of description
     *
     * @return the value of description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRowtotal() {
        if (price == null || quantity == null) {
            return 0.0;
        } else {
            return price * quantity;
        }
    }

}