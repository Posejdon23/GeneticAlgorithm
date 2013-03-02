package ag1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        ParamSpec ps = new ParamSpec();
        ps.addParam("1", 10, -512, 512);
        ps.addParam("2", 10, -512, 512);
        ps.addParam("3", 10, -512, 512);

        //liczba generacji,wielkość populacji(PARZYSTA! >?), długość ciągu
        NewPop np = new NewPop(ps, 45, 20, 30);
        double[][] stats = np.populate(); //min,max, sum,avg

        /*
         Parameter[] pars = Funkcje.dekoduj(ps, "010101010101010101010101010101", 2);
        


         for (int i = 0; i < pars.length; i++) {
         System.out.println(
         pars[i].getName() + "\t\t"
         + Math.round(pars[i].getValue() * 100.0) / 100.0 + 
         ",\tz przedziału:\t(" + pars[i].getMinparm()
         + " - " + pars[i].getMaxparm() + ")");
         }
         */

        stage.setTitle("Wykres dla statystyk kolejnych generacji");
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Generacja");
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("Ewolucja populacji");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Min");
        for (int i = 0; i < stats.length; i++) {
            series1.getData().add(new XYChart.Data(i, stats[i][0]));
        }
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Max");
        for (int i = 0; i < stats.length; i++) {
            series2.getData().add(new XYChart.Data(i, stats[i][1]));
        }

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Suma");
        for (int i = 0; i < stats.length; i++) {
            series3.getData().add(new XYChart.Data(i, stats[i][2]));
        }

        XYChart.Series series4 = new XYChart.Series();
        series4.setName("Średnia");
        for (int i = 0; i < stats.length; i++) {
            series4.getData().add(new XYChart.Data(i, stats[i][3]));
        }

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().addAll(series1, series2, series4);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

