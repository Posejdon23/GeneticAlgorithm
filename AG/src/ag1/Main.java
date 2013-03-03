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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

	private double[][] stats;
	private TableView<Parameter> paramTab = new TableView<Parameter>();
	private final ObservableList<Parameter> data = FXCollections
			.observableArrayList();
	private TextField popSize, genSize, pcross, pmutation;
	private LineChart<Number, Number> wykres;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void start(Stage stage) {

		paramTab.setEditable(true);
		paramTab.setTableMenuButtonVisible(true);
		TableColumn paramNameCol = new TableColumn("Nazwa parametru");
		paramNameCol.setCellValueFactory(new PropertyValueFactory<Parameter, String>("name"));
		
		TableColumn paramSizeCol = new TableColumn("Liczba pozycji kodujących");
		paramSizeCol.setCellValueFactory(new PropertyValueFactory<Parameter, String>("length"));
		
		TableColumn zakresCols = new TableColumn("Zakres Wartości");
		
		TableColumn paramMinCol = new TableColumn("Min");
		paramMinCol.setCellValueFactory(new PropertyValueFactory<Parameter, String>("minparm"));
		TableColumn paramMaxCol = new TableColumn("Max");
		paramMaxCol.setCellValueFactory(new PropertyValueFactory<Parameter, String>("maxparm"));
		
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

		Label title = new Label("Parametry algorytmu:");
		title.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(title, 0, 0);

		grid.add(paramTab, 0, 1);
		grid.add(addingBox, 0, 2);

		final Label popSizeLabel = new Label("Wielkość populacji");
		popSizeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(popSizeLabel, 0, 3);
		popSize = new TextField();
		grid.add(popSize, 0, 4);

		final Label genSizeLabel = new Label("Liczba generacji");
		genSizeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(genSizeLabel, 0, 5);
		genSize = new TextField();
		grid.add(genSize, 0, 6);

		final Label pcrossLabel = new Label("Prawdopodobieństwo krzyżowania");
		pcrossLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(pcrossLabel, 0, 7);
		pcross = new TextField();
		grid.add(pcross, 0, 8);

		final Label pmutationLabel = new Label("Prawdopodobieństwo mutacji");
		pmutationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(pmutationLabel, 0, 9);
		pmutation = new TextField();
		grid.add(pmutation, 0, 10);

		Button rysujBtn = new Button("Rysuj wykres");
		HBox hbBtn = new HBox(11);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(rysujBtn);
		grid.add(hbBtn, 0, 12);
		rysujBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				oblicz(Integer.parseInt(popSize.getText()),
						Integer.parseInt(genSize.getText()),
								Double.parseDouble(pcross.getText()),
								Double.parseDouble(pmutation.getText()));
				if (grid.getChildren().contains(wykres)) {
					grid.getChildren().remove(wykres);
				}
				grid.add(addChart(), 1, 0, 1, 12);
			}
		});

		Scene scene = new Scene(grid, 800, 600);
		stage.setTitle("Algorytm genetyczny");
		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

	private void oblicz(int popSize, int genSize, double pcross,
			double pmutation) {
		ParamSpec ps = new ParamSpec();
		Parameter[] params = new Parameter[data.size()]; 
				data.toArray(params);
		for(Parameter p : params){
			System.out.println(p.getName());
			ps.addParam(p.getName(), p.getLength(),p.getMinparm(),p.getMaxparm());	
		}
		// liczba generacji,wielkość populacji(PARZYSTA! >?)
		NewPop np = new NewPop(ps, popSize, genSize);
		stats = np.populate(pcross, pmutation); // min,max, sum,avg

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private LineChart addChart() {
		final NumberAxis xAxis = new NumberAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Generacja");
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

		wykres.getData().addAll(series1, series2, series4);
		return wykres;
	}
}
