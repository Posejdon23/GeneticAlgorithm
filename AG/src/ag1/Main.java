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

        //liczba generacji,wielkość populacji(PARZYSTA! >?)
        NewPop np = new NewPop(ps, 150, 50);
        double[][] stats = np.populate(0.6,0.0333); //min,max, sum,avg

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

