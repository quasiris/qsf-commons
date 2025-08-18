package com.quasiris.qsf.commons.unit;

public class Unit {
    private String number;
    private String unit;

    /**
     * Getter for property 'number'.
     *
     * @return Value for property 'number'.
     */
    public String getNumber() {
        return number;
    }

    /**
     * Setter for property 'number'.
     *
     * @param number Value to set for property 'number'.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * Getter for property 'unit'.
     *
     * @return Value for property 'unit'.
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Setter for property 'unit'.
     *
     * @param unit Value to set for property 'unit'.
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "number='" + number + '\'' +
                ", unit='" + unit + '\'' +
                '}';
    }
}
