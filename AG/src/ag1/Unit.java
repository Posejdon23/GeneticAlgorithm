/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ag1;

/**
 *
 * @author kamil
 */
public class Unit {
		
	public Unit(ParamSpec ps, String allel){
            
		this.allel=allel;
        this.params = Funkcje.dekoduj(ps, allel, 2);
		this.fx = fPrzystosowania();
	}
	private String allel;
	private double fx;
    private Parameter[] params;
	
	public String getAllel() {
		return allel;
	}
	public double getFx() {
		return fx;
	}
	public void setFx(double fx) {
		this.fx = fx;
	}
        private double fPrzystosowania(){
            double wynik = (Math.pow(params[0].getValue(),2.0) 
                    +Math.pow(params[1].getValue(),2.0) + 
                    Math.pow(params[2].getValue(),2.0)
                    ); 
            return wynik;
        }
}

