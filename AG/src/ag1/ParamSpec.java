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
    
    List<Parameter> paramList;
    
    public ParamSpec(){   
        liczbaParametrów = 0;
        paramList = new ArrayList<>();
    }
    public void addParam(String pname, int długość, double min,double max){
        liczbaParametrów++;
        paramList.add(new Parameter(pname, długość, 0, min, max));
    }
    public int getNumberOf(){
        return liczbaParametrów;
    }
    public Parameter[] getParams(){
        Parameter[] p = new Parameter[liczbaParametrów];
        paramList.toArray(p);
        return p;
    }
//    public Parameter getParamByName(String pname){
//    }
}
