/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ag1;

/**
 *
 * @author kamil
 */
class Parameter {
    private int length;
    private String name;
    private double value,minparm,maxparm;

    public Parameter(String name, int length, double value, double minparm, double maxparm) {
        this.name = name;
        this.length = length;
        this.value = value;
        this.minparm = minparm;
        this.maxparm = maxparm;
    }

    public int getLength() {
        return length;
    }

    public String getName() {
        return name;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMinparm() {
        return minparm;
    }

    public void setMinparm(double minparm) {
        this.minparm = minparm;
    }

    public double getMaxparm() {
        return maxparm;
    }

    public void setMaxparm(double maxparm) {
        this.maxparm = maxparm;
    }
    
}
