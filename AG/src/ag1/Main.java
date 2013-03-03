package ag1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {

	private double[][] stats;
	private TableView<Parameter> paramTab = new TableView<Parameter>();
	private final ObservableList<Parameter> data = FXCollections
			.observableArrayList(new Parameter("a", "3", "-10", "10"),
					new Parameter("b", "3", "-10", "10"));
	private TextField fitFunction, popSize, genSize, pcross, pmutation,scale;
	private LineChart<Number, Number> wykres;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage stage) {

		paramTab.setEditable(true);
		TableColumn paramNameCol = new TableColumn("Nazwa parametru");
		paramNameCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"name"));

		TableColumn paramSizeCol = new TableColumn("Liczba pozycji kodujących");
		paramSizeCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"length"));

		TableColumn zakresCols = new TableColumn("Zakres Wartości");

		TableColumn paramMinCol = new TableColumn("Min");
		paramMinCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"minparm"));
		TableColumn paramMaxCol = new TableColumn("Max");
		paramMaxCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"maxparm"));

		zakresCols.getColumns().addAll(paramMinCol, paramMaxCol);

		paramTab.getColumns().addAll(paramNameCol, paramSizeCol, zakresCols);
		paramTab.setMaxHeight(200);
		paramTab.setItems(data);

		final TextField addName = new TextField();
		addName.setPromptText("Nazwa parametru");
		addName.setMaxWidth(paramNameCol.getPrefWidth());
		final TextField addLength = new TextField();
		addLength.setMaxWidth(paramSizeCol.getPrefWidth());
		addLength.setPromptText("Długość");
		final TextField addMinVal = new TextField();
		addMinVal.setMaxWidth(paramMinCol.getPrefWidth());
		addMinVal.setPromptText("Min");
		final TextField addMaxVal = new TextField();
		addMaxVal.setMaxWidth(paramMaxCol.getPrefWidth());
		addMaxVal.setPromptText("Max");

		final Button addButton = new Button("Dodaj");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				data.add(new Parameter(addName.getText(), addLength.getText(),
						addMinVal.getText(), addMaxVal.getText()));
				addName.clear();
				addLength.clear();
				addMinVal.clear();
				addMaxVal.clear();
			}
		});
		HBox addingBox = new HBox();
		addingBox.getChildren().addAll(addName, addLength, addMinVal,
				addMaxVal, addButton);

		final GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(5, 25, 25, 25));

		Label title = new Label("Parametry populacji:");
		//title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(title, 0, 0);

		grid.add(paramTab, 0, 1);
		grid.add(addingBox, 0, 2);

		final Label fitFunctionLabel = new Label("Funkcja przystosowania");
		//fitFunctionLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(fitFunctionLabel, 0, 3);
		fitFunction = new TextField();
		fitFunction.setText("a+b");
		grid.add(fitFunction, 0, 4);

		final Label popSizeLabel = new Label("Wielkość populacji");
		//popSizeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(popSizeLabel, 0, 5);
		popSize = new TextField();
		popSize.setText("100");
		grid.add(popSize, 0, 6);

		final Label genSizeLabel = new Label("Liczba generacji");
		//genSizeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(genSizeLabel, 0, 7);
		genSize = new TextField();
		genSize.setText("20");
		grid.add(genSize, 0, 8);

		final Label pLabel = new Label("           Prawdopodobieństwo");
		//pLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(pLabel, 0, 9);

		final Label pcrossLabel = new Label("        Krzyżowania");
		//pcrossLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		pcross = new TextField();
		pcross.setText("0.6");

		final Label pmutationLabel = new Label("                            Mutacji");
		//pmutationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		pmutation = new TextField();
		pmutation.setText("0.01");

		HBox pboxL = new HBox();
		pboxL.getChildren().addAll(pcrossLabel, pmutationLabel);

		HBox pbox = new HBox();
		pbox.getChildren().addAll(pcross, pmutation);
		grid.add(pboxL, 0, 10);
		grid.add(pbox, 0, 11);
		
		final Label scaleLabel = new Label("Współczynnik skalowania");
		//scaleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(scaleLabel, 0, 12);
		scale = new TextField();
		scale.setText("2");
		grid.add(scale, 0, 13);

		Button rysujBtn = new Button("Rysuj wykres");
		HBox hbBtn = new HBox();
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(rysujBtn);
		grid.add(hbBtn, 0, 14);
		rysujBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				oblicz(Integer.parseInt(popSize.getText()),
						Integer.parseInt(genSize.getText()),
						Double.parseDouble(pcross.getText()),
						Double.parseDouble(pmutation.getText()),
						Double.parseDouble(scale.getText()),
						fitFunction.getText());
				if (grid.getChildren().contains(wykres)) {
					grid.getChildren().remove(wykres);
				}
				grid.add(addChart(), 1, 0, 1, 14);
			}
		});
		
		final ContextMenu contextMenu = new ContextMenu();

		MenuItem item1 = new MenuItem("Usuń parametr");
		item1.setOnAction(new EventHandler<ActionEvent>() {
		    public void handle(ActionEvent e) {
		       
		       data.remove(paramTab.getSelectionModel().getSelectedItem());
		    }
		});
		contextMenu.getItems().addAll(item1);
		paramTab.setContextMenu(contextMenu);

		Scene scene = new Scene(grid, 800, 600);
		stage.setTitle("Algorytm genetyczny");
		stage.setScene(scene);
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void oblicz(int popSize, int genSize, double pcross,
			double pmutation, double scale, String fitFunction) {
		ParamSpec ps = new ParamSpec();
		Parameter[] params = new Parameter[data.size()];
		data.toArray(params);
		for (Parameter p : params) {
			ps.addParam(p.getName(), p.getLength(), p.getMinparm(),
					p.getMaxparm());
		}
		// liczba generacji,wielkość populacji(PARZYSTA! >?)
		NewPop np = new NewPop(ps, genSize, popSize, fitFunction);
		stats = np.populate(pcross, pmutation, scale); // min,max, sum,avg

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private LineChart addChart() {
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Kolejne generacje");
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Przystosowanie");
		wykres = new LineChart<Number, Number>(xAxis, yAxis);

		wykres.setTitle("Wykres przystosowania");

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
//		for(int i=0;i<stats.length;i++){
//			for(int j = 0;j<stats[0].length;j++){
//				System.out.print(stats[i][j] + "\t");
//			}
//			System.out.println();
//		}
		wykres.getData().addAll(series1, series2, series4);
		return wykres;
	}
}
