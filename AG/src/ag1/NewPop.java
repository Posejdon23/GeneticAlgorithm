package ag1;

public class NewPop {

    public int liczbaGeneracji;
    public int wielkośćPopulacji;
    public int długośćCiągu;
    public double[][] stats;
    private ParamSpec ps;

    public NewPop(ParamSpec ps, int liczbaGeneracji, int wielkośćPopulacji) {
        this.ps = ps;
        this.liczbaGeneracji = liczbaGeneracji;
        this.wielkośćPopulacji = wielkośćPopulacji;
        this.długośćCiągu = ps.getAllelLength();
        stats = new double[liczbaGeneracji][4];
        
        System.out.println("1:" + długośćCiągu);
    }

    public double[][] populate(double pcross,double pmutation) {
        Unit[] populacja = Funkcje.genGen(ps, wielkośćPopulacji, długośćCiągu,
                liczbaGeneracji,pmutation,pcross); // ostatnie tylko dla wyswietlania w reprodukcji
        Unit[] newpop;
        //System.out.println("Generacja 0:");
        //Funkcje.statystyki(populacja);
        for (int i = 1; i <= liczbaGeneracji; i++) {
            
            Object[] popIstat = Funkcje.reprodukcja(ps, populacja);
            populacja = (Unit[]) popIstat[0];
           // System.out.println("Generacja " + i + ":");          
            stats[i-1] = (double[]) popIstat[1];

            
            // INNA WERSJA 3 OSTATNICH LINIJEK
//            Object[] obj = Funkcje.statystyki(populacja); // dodaje również skalowanie przystosowania
//            populacja = (Unit[]) obj[0];
//            stats[i-1] = (double[]) obj[1];
        }
        return stats;
    }
}

