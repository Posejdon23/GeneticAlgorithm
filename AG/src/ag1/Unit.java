package ag1;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

/**
 * 
 * @author kamil
 */
public class Unit {

	public Unit(ParamSpec ps, String allel, String funkcja) {

		this.allel = allel;
		this.params = Funkcje.dekoduj(ps, allel, 2);
		this.fx = fPrzystosowania(funkcja);
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

	private double fPrzystosowania(String funkcja) {

		ExpressionBuilder eb = new ExpressionBuilder(funkcja);
		for (Parameter p : params)
			eb.withVariable(p.getName(), p.getValue());

		Calculable calc = null;
		try {
			calc = eb.build();
		} catch (UnknownFunctionException | UnparsableExpressionException e) {
			System.out.println("Błąd się wydostał");
		}
		double wynik = calc.calculate();

		if (wynik < 0)
			wynik = 0.0d;
		return wynik;

	}
}
