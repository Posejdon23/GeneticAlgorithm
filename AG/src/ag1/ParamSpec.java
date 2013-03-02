/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ag1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kamil
 */
public class ParamSpec {
    int liczbaParametrów;
    int długośćCiągu = 0;
    
    List<Parameter> paramList;
    
    public ParamSpec(){   
        liczbaParametrów = 0;
        paramList = new ArrayList<>();
    }
    public void addParam(String pname, int długość, double min,double max){
        liczbaParametrów++;
        paramList.add(new Parameter(pname, długość, 0, min, max));
        długośćCiągu +=długość;
    }
    public int getNumberOf(){
        return liczbaParametrów;
    }
    public Parameter[] getParams(){
        Parameter[] p = new Parameter[liczbaParametrów];
        paramList.toArray(p);
        return p;
    }
    public int getAllelLength(){
    	return długośćCiągu;
    }
//    public Parameter getParamByName(String pname){
//    }
}
