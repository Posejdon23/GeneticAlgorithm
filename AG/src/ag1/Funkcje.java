/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ag1;

import java.util.Random;

public class Funkcje {

    private static double PMUTATION;
    private static double PCROSS;
    public static double maxPopFit;
    private static double mnożnikSkalowania;
    public static int licznikGen=0;
    public static int liczbaGen;
    public static String fitFunction;

    public static Unit[] genGen(ParamSpec ps, int popsize, int length,
            int lGen, double PMUTATION, double PCROSS,double scale, String fitFunction) {
    	Funkcje.fitFunction = fitFunction;
        liczbaGen = lGen;
        Funkcje.PMUTATION = PMUTATION;
        Funkcje.PCROSS = PCROSS;
        Funkcje.mnożnikSkalowania = scale;
        Unit[] newpop = new Unit[popsize];
        Random random = new Random();
        StringBuilder sb;
        for (int i = 0; i < popsize; i++) {
            sb = new StringBuilder();
            for (int j = 0; j < length; j++) {
                if (random.nextBoolean()) {
                    sb.append((char) 48);
                } else {
                    sb.append((char) 49);
                }
            }
            newpop[i] = new Unit(ps, sb.toString(),fitFunction);
        }
        return newpop;

    }
public static int wybierz(double sumaFit, Unit populacja[]) {
        int popsize = populacja.length;
        double[] fxy = new double[popsize];
        for (int j = 0; j < popsize; j++) {
            fxy[j] = populacja[j].getFx();
        }
        Random rand = new Random();
        double LL = rand.nextDouble();
        double sumap=0;
        int pointer = 0;
        while(LL>sumap){
            sumap+=fxy[pointer++]/sumaFit;
        }
        return (pointer-1);

    }

    public static Object[] reprodukcja(ParamSpec ps, Unit[] oldpop) {
        Unit[] newpop = new Unit[oldpop.length];
        int mate1 = 0, mate2 = 0, popsize = oldpop.length;
        String s1 = null,s2 = null;    
        Object[] obj = statystyki(oldpop);
        
        oldpop = (Unit[])obj[0];
        double[] stats = (double[]) obj[1];
        
        double sumfitness = 0;
        for (int j = 0; j < popsize; j = j + 2) {
            sumfitness = 0;
            for (int i = 0; i < popsize; i++) {
                sumfitness += oldpop[i].getFx();
            }
            mate1 = wybierz(sumfitness, oldpop);
            s1 = oldpop[mate1].getAllel();
            mate2 = wybierz(sumfitness, oldpop);
            s2 = oldpop[mate2].getAllel();
            Random rand = new Random();
            String[] sNew = null;
            if (rand.nextDouble() < PCROSS) {
                sNew = krzyżuj(s1, s2);
            } else {
                sNew = new String[]{s1, s2};
            }
            Unit unit1 = new Unit(ps, sNew[0],fitFunction);
            Unit unit2 = new Unit(ps, sNew[1],fitFunction);
            newpop[j] = unit1;
            newpop[j + 1] = unit2;
            
//            if((j==6) && (licznikGen == 0 || licznikGen==liczbaGen-1)){
//                System.out.println("Czterech typowych przedstawicieli generacji " + 
//                        (licznikGen+1) + ":");
//                for(int i=0;i<4;i++){
//                    //System.out.println("popsize: " + popsize);
//                    System.out.println(newpop[i].getAllel());
//                }
//            } 
        }
        licznikGen++;
        return new Object[]{newpop,stats};
    }

    public static Object[] statystyki(Unit[] populacja) {

        int popsize = populacja.length;
        double sumaFit = 0;
        double minFit = populacja[0].getFx();
        double maxFit = minFit;
        double fitness = 0;
        for (int i = 0; i < popsize; i++) {
            fitness = populacja[i].getFx();
            sumaFit += fitness;
            if (fitness > maxFit) {
                maxFit = fitness;
            }
            if (fitness < minFit) {
                minFit = fitness;
            }
        }
        maxPopFit = maxFit;
        double średniaFit = sumaFit / popsize;
        return skalujpop(populacja, minFit, maxFit, średniaFit, sumaFit);
    }

    public static Object[] skalujpop(Unit[] populacja, double minFit, double maxFit,
            double średniaFit, double sumaFit) {
        
        double[] wsp = preskala(minFit, maxFit, średniaFit);
        double a = wsp[0];
        double b = wsp[1];
       // System.out.println("a:" + a + " b: " + b);
        int popsize = populacja.length;
        double minFit2 = populacja[0].getFx();
        double maxFit2 = minFit2;
        double sumaFit2 = 0;
        double fitness2 = 0;

        for (int i = 0; i < popsize; i++) {
            fitness2 = a * populacja[i].getFx() + b;
            
            if (fitness2 > maxFit2) {
                maxFit2 = fitness2;
            }
            if (fitness2 < minFit2) {
                minFit2 = fitness2;
            }
            populacja[i].setFx(fitness2);
            sumaFit2 += fitness2;     
        }
        maxPopFit = maxFit2;
        double średniaFit2 = sumaFit2 / popsize;
    
        double[] stats = new double[]{minFit2, maxFit2, sumaFit2, średniaFit2};
        Object[] objects = new Object[]{populacja, stats};
        // System.out.println("Suma przystowowania populacji: " + sumaFit2);
        // System.out.println("Przystowowanie min: " + minFit2);
        // System.out.println("Przystowowanie max: " + maxFit2);
        // System.out.println("Przystowowanie średnie: " + średniaFit2);
        return objects;
    }

    private static double[] preskala(double umin, double umax, double uavg) { 
        double delta, a, b;

        if (umin > ((mnożnikSkalowania * uavg - umax) / (mnożnikSkalowania - 1.0))) {
            delta = umax - uavg;
            a = ((mnożnikSkalowania - 1.0) * uavg) / delta;
            b = uavg * (umax - mnożnikSkalowania * uavg) / delta;
        } else {
            delta = uavg - umin;
            a = uavg / delta;
            b = -umin * uavg / delta;

        }
        return new double[]{a, b};
    }
    private static String mutacja(String s1) {
        Random rand = new Random();
        double LL = rand.nextDouble();
        if (LL <= PMUTATION) {
            int gdzieMutować = rand.nextInt(s1.length() - 1) + 1;
            int c = s1.codePointAt(gdzieMutować);// 0-48, 1-49
            if (c == 48) { // ZAMIANA 0:=1 lub 1:=0
                c = 49;
            } else if (c == 49) {
                c = 48;
            }
            
            s1 = s1.replace(s1.charAt(gdzieMutować), (char)c);
            
            String temp1 = s1.substring(0, gdzieMutować - 1);
            String temp2 = s1.substring(gdzieMutować, s1.length());
            String zmutowany = temp1 + (char) c + temp2;
            s1 = zmutowany.substring(0, s1.length());
        }
        return s1;
    }
    private static String[] krzyżuj(String s1, String s2) {
        String s1New = null;
        String s2New = null;
        if (s1.length() != s2.length()) {
            System.err.println("Błąd. Ciągi muszą być równej długości");
            return null;
        }
        Random rand = new Random();
        int podział = rand.nextInt(s1.length() - 1) + 1;
        String s1left = s1.substring(0, podział);
        String s1right = s1.substring(podział, s1.length());
        // System.out.println(podział + ", " + s1left + " | " + s1right);
        String s2left = s2.substring(0, podział);
        String s2right = s2.substring(podział, s1.length());
        s1New = s1left + s2right;
        s2New = s2left + s1right;
        return new String[]{mutacja(s1New), mutacja(s2New)};
    }
    public static Parameter[] dekoduj(ParamSpec ps, String allel,int poz){
    	
        int liczbaParametrów = ps.getNumberOf();
        Parameter[] params = ps.getParams();
//        int allDł = allel.length();
//        int suma = 0;
//        for(Parameter p : params){
//            suma += p.getLength();
//        }
//        if(allDł != suma){
//            System.err.print("Błąd. Suma długości parametrów musi być "
//                    + "równa długości allela.");
//            return null;
//        }
        int wskaźnik = 0;
        Parameter[] parametry = new Parameter[liczbaParametrów];
        String subs = null;
        for(int i = 0; i< liczbaParametrów; i++){
            int parDł = params[i].getLength();
            double minRange = params[i].getMinparm();
            double maxRange = params[i].getMaxparm();
            String pname = params[i].getName();
           subs = allel.substring(wskaźnik, wskaźnik+parDł);
           double wartośćLiczbowa = Integer.parseInt(subs, poz);
           double paramval = minRange + (maxRange-minRange)/
                   (Math.pow(poz,parDł)-1.0)* wartośćLiczbowa;
           parametry[i] = new Parameter(pname, parDł,paramval,minRange,
                   maxRange);
           wskaźnik+= parDł;
        }
        return parametry;
    }     
}
