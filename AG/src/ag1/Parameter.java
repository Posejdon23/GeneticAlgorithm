/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ag1;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author kamil
 */
public class Parameter {
    private SimpleIntegerProperty length;
    private SimpleStringProperty name;
    private SimpleDoubleProperty value,minparm,maxparm;

    public Parameter(String name, int length, double value, double minparm, double maxparm) {
    	this.name = new SimpleStringProperty(name);
        this.length = new SimpleIntegerProperty(length);
        this.value = new SimpleDoubleProperty(value);
        this.minparm = new SimpleDoubleProperty(minparm);
        this.maxparm = new SimpleDoubleProperty(maxparm);
    }
    public Parameter(String s1,String s2, String s3,String s4){
    	this.name = new SimpleStringProperty(s1);
        this.length = new SimpleIntegerProperty(Integer.parseInt(s2));
        this.value = new SimpleDoubleProperty(0.0d);
        this.minparm = new SimpleDoubleProperty(Double.parseDouble(s3));
        this.maxparm = new SimpleDoubleProperty(Double.parseDouble(s4));
    }

    public int getLength() {
        return length.get();
    }

    public String getName() {
        return name.get();
    }

    public void setLength(int length) {
        this.length.set(length);
    }

    public double getValue() {
        return value.get();
    }

    public void setValue(double value) {
        this.value.set(value);
    }

    public double getMinparm() {
        return minparm.get();
    }

    public void setMinparm(double minparm) {
        this.minparm.set(minparm);
    }

    public double getMaxparm() {
        return maxparm.get();
    }

    public void setMaxparm(double maxparm) {
        this.maxparm.set(maxparm);
    }
    
}
