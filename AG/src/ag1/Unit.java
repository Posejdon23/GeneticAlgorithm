package ag1;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 *
 * @author kamil
 */
public class Unit {
		
	public Unit(ParamSpec ps, String allel,String funkcja){
            
		this.allel=allel;
        this.params = Funkcje.dekoduj(ps, allel, 2);
		this.fx = fPrzystosowania(funkcja);
	}
	private String allel;
	private double fx;
    private Parameter[] params;
	
    private static final JexlEngine jexl = new JexlEngine();
    static {
       jexl.setCache(512);
       jexl.setLenient(false);
       jexl.setSilent(false);
    }
	public String getAllel() {
		return allel;
	}
	public double getFx() {
		return fx;
	}
	public void setFx(double fx) {
		this.fx = fx;
	}
        private double fPrzystosowania(String funkcja){
        	
		    Expression e = (Expression) jexl.createExpression(funkcja);
		    JexlContext context = new MapContext();
		    
		    for(Parameter p : params)
		    context.set(p.getName(), p.getValue());
		    
		    double wynik = 0;
		    Object num = e.evaluate(context);
		    if(num.getClass().equals(Long.class)){
		    	wynik = ((Long)num).doubleValue();
		    }
			if(num.getClass().equals(Double.class)){
				wynik= ((Double)num).doubleValue();	 
			}
			if(num.getClass().equals(Float.class)){
				wynik= ((Float)num).doubleValue();	
			}
			if(wynik<0) wynik=0.0d;
		    return Math.round(wynik*1000)/1000;

        }
}

