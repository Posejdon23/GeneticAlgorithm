package ag1;

public class NewPop {

    public int liczbaGeneracji;
    public int wielkośćPopulacji;
    public int długośćCiągu;
    public double[][] stats;
    private ParamSpec ps;
    private static String fitFunction;

    public NewPop(ParamSpec ps, int liczbaGeneracji, int wielkośćPopulacji,String fitFunction) {
        this.ps = ps;
        this.liczbaGeneracji = liczbaGeneracji;
        this.wielkośćPopulacji = wielkośćPopulacji;
        this.długośćCiągu = ps.getAllelLength();
        stats = new double[liczbaGeneracji][4];
        NewPop.fitFunction = fitFunction;
    }

    public double[][] populate(double pcross,double pmutation,double scale) {
        Unit[] populacja = Funkcje.genGen(ps, wielkośćPopulacji, długośćCiągu,
                liczbaGeneracji,pmutation,pcross, scale, fitFunction); // ostatnie tylko dla wyswietlania w reprodukcji
        for (int i = 1; i <= liczbaGeneracji; i++) {    
            Object[] popIstat = Funkcje.reprodukcja(ps, populacja);
            populacja = (Unit[]) popIstat[0];       
            stats[i-1] = (double[]) popIstat[1];
        }
        return stats;
    }
}

