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
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	private double[][] stats;
	private TableView<Parameter> paramTab = new TableView<Parameter>();
	private final ObservableList<Parameter> data = FXCollections
			.observableArrayList(new Parameter("a", "5", "-10", "10"),
					new Parameter("b", "5", "-10", "10"));
	private TextField fitFunction, popSize, genSize, pcross, pmutation, scale;
	private LineChart<Number, Number> wykres;
	private Stage stage;
	private GridPane grid;
	private boolean isWykres = false;
	private boolean dodawanie = false;
	private double minH = 30;

	@Override
	public void start(final Stage stage) {

		this.stage = stage;
		grid = new GridPane();
		grid.setAlignment(Pos.TOP_LEFT);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 25, 25));
		grid.setStyle("-fx-background-color:#1d1d1d");
		wykresSettings(grid);

		final Label fitFunctionLabel = new Label("Funkcja przystosowania");
		// fitFunctionLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		fitFunctionLabel.setMinWidth(200);
		grid.add(fitFunctionLabel, 0, 3, 1, 1);
		fitFunction = new TextField();
		fitFunction.setText("a+b");
		fitFunction.setMinHeight(minH);
		grid.add(fitFunction, 1, 3, 1, 1);

		final Label popSizeLabel = new Label("Wielkość populacji");
		// popSizeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(popSizeLabel, 0, 5);
		popSize = new TextField();
		popSize.setText("100");
		popSize.setMinHeight(minH);
		grid.add(popSize, 1, 5);

		final Label genSizeLabel = new Label("Liczba generacji");
		// genSizeLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		grid.add(genSizeLabel, 0, 7);
		genSize = new TextField();
		genSize.setText("20");
		genSize.setMinHeight(minH);
		grid.add(genSize, 1, 7);

		final Label pcrossLabel = new Label("Ppb. krzyżowania");
		// pcrossLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		pcross = new TextField();
		pcross.setText("0.6");
		pcross.setMinHeight(minH);

		final Label pmutationLabel = new Label("Ppb mutacji");
		// pmutationLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		pmutation = new TextField();
		pmutation.setText("0.01");
		pmutation.setMinHeight(minH);

		grid.add(pcrossLabel, 0, 9);
		grid.add(pcross, 1, 9);
		grid.add(pmutationLabel, 0, 10);
		grid.add(pmutation, 1, 10);

		final Label scaleLabel = new Label("Współczynnik skalowania");
		// scaleLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		scaleLabel.setMinWidth(200);
		grid.add(scaleLabel, 0, 11);
		scale = new TextField();
		scale.setText("1.4");
		scale.setMinHeight(minH);

		grid.add(scale, 1, 11);

		final Button wyczyśćBtn = new Button("Wyczyść wykres");
		wyczyśćBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (isWykres)
					grid.getChildren().remove(wykres);

				stage.setWidth(paramTab.widthProperty().doubleValue());
				isWykres = false;
			}
		});

		Button rysujBtn = new Button("Rysuj wykres");
		rysujBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				oblicz(Integer.parseInt(popSize.getText()),
						Integer.parseInt(genSize.getText()),
						Double.parseDouble(pcross.getText()),
						Double.parseDouble(pmutation.getText()),
						Double.parseDouble(scale.getText()),
						fitFunction.getText());

				if (!isWykres) {
					paramTab.prefWidthProperty().bind(
							grid.widthProperty().divide(2));
					stage.setWidth(2.0 * grid.widthProperty().doubleValue());
				} else {
					grid.getChildren().remove(wykres);
				}
				grid.add(addChart(), 2, 0, 1, 13);
				isWykres = true;
			}
		});

		final DropShadow shadow = new DropShadow();
		wyczyśćBtn.addEventHandler(MouseEvent.MOUSE_ENTERED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						wyczyśćBtn.setEffect(shadow);
					}
				});
		// Removing the shadow when the mouse cursor is off
		wyczyśćBtn.addEventHandler(MouseEvent.MOUSE_EXITED,
				new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent e) {
						wyczyśćBtn.setEffect(null);
					}
				});
		HBox hbBtn = new HBox();
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().addAll(wyczyśćBtn, rysujBtn);
		hbBtn.setSpacing(20);
		grid.add(hbBtn, 0, 13, 2, 1);

		grid.prefWidthProperty().bind(stage.widthProperty());
		grid.prefHeightProperty().bind(stage.heightProperty());

		Scene scene = new Scene(grid, 500, 600);
		stage.centerOnScreen();
		stage.setMinHeight(500);
		stage.setMinWidth(500);

		stage.setTitle("Prosty algorytm genetyczny");

		// scene.getStylesheets().add(Main.class.getResource("Styl.css").toExternalForm());
		scene.getStylesheets().add(
				Main.class.getResource("Styl.css").toExternalForm());
		stage.setScene(scene);
		this.stage.getIcons().add(new Image("file:resources/images/icon.png"));
		stage.show();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void wykresSettings(final GridPane grid) {

		TableColumn titleCol = new TableColumn("Parametry osobników");
		TableColumn paramNameCol = new TableColumn("Nazwa");
		paramNameCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"name"));
		TableColumn paramSizeCol = new TableColumn(
				"Liczba bitów\nkodujących\nparametr");
		paramSizeCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"length"));
		TableColumn zakresCols = new TableColumn("Zakres Wartości");
		zakresCols.setPrefWidth(180);
		TableColumn paramMinCol = new TableColumn("Min");
		paramMinCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"minparm"));
		TableColumn paramMaxCol = new TableColumn("Max");
		paramMaxCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"maxparm"));
		paramNameCol.prefWidthProperty().bind(
				paramTab.widthProperty().divide(4));
		paramSizeCol.prefWidthProperty().bind(
				paramTab.widthProperty().divide(4.1));
		paramMinCol.prefWidthProperty()
				.bind(paramTab.widthProperty().divide(4));
		paramMaxCol.prefWidthProperty()
				.bind(paramTab.widthProperty().divide(4));
		// paramNameCol.setPrefWidth(120);
		// paramSizeCol.setPrefWidth(134);
		// paramMinCol.setPrefWidth(90);
		// paramMaxCol.setPrefWidth(90);
		zakresCols.getColumns().addAll(paramMinCol, paramMaxCol);

		titleCol.getColumns().addAll(paramNameCol, paramSizeCol, zakresCols);
		paramTab.getColumns().addAll(titleCol);
		paramTab.prefHeightProperty().bind(stage.heightProperty().divide(1.4));
		paramTab.prefWidthProperty().bind(stage.widthProperty());
		paramTab.setItems(data);
		grid.add(paramTab, 0, 1, 2, 1);

		final TextField addName = new TextField();
		addName.setPromptText("Nazwa parametru");
		addName.setMinHeight(minH);
		final TextField addLength = new TextField();
		addLength.setPromptText("Długość");
		addLength.setMinHeight(minH);
		final TextField addMinVal = new TextField();
		addMinVal.setPromptText("Min");
		addMinVal.setMinHeight(minH);
		final TextField addMaxVal = new TextField();
		addMaxVal.setPromptText("Max");
		addMaxVal.setMinHeight(minH);
		addName.setVisible(false);
		addLength.setVisible(false);
		addMinVal.setVisible(false);
		addMaxVal.setVisible(false);

		addName.prefWidthProperty().bind(stage.widthProperty().divide(4));
		addLength.prefWidthProperty().bind(stage.widthProperty().divide(4));
		addMinVal.prefWidthProperty().bind(stage.widthProperty().divide(4));
		addMaxVal.prefWidthProperty().bind(stage.widthProperty().divide(4));
		
		
		final Button addButton = new Button("Dodaj parametr");
		addButton.setMinWidth(100);
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				if (dodawanie) {
					data.add(new Parameter(addName.getText(), addLength
							.getText(), addMinVal.getText(), addMaxVal
							.getText()));
					addName.clear();
					addLength.clear();
					addMinVal.clear();
					addMaxVal.clear();
					addName.setVisible(false);
					addLength.setVisible(false);
					addMinVal.setVisible(false);
					addMaxVal.setVisible(false);
					addButton.prefWidthProperty().bind(stage.widthProperty().divide(2));
					addButton.setText("Dodaj parametr");
					dodawanie = false;
				} else{
					dodawanie = true;
					addButton.prefWidthProperty().bind(stage.widthProperty().divide(4));
					addButton.setText("Dodaj");
					addName.setVisible(true);
					addLength.setVisible(true);
					addMinVal.setVisible(true);
					addMaxVal.setVisible(true);
				}
			}
		});
		addButton.prefWidthProperty().bind(stage.widthProperty().divide(2));
		HBox addingBox = new HBox();
		addingBox.getChildren().addAll(addName, addLength, addMinVal,
				addMaxVal, addButton);
		addingBox.setSpacing(7);
		addingBox.prefWidthProperty().bind(paramTab.widthProperty());
		grid.add(addingBox, 0, 2, 2, 1);
		final ContextMenu contextMenu = new ContextMenu();

		MenuItem item1 = new MenuItem("Usuń parametr");
		item1.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

				data.remove(paramTab.getSelectionModel().getSelectedItem());
			}
		});
		contextMenu.getItems().addAll(item1);
		paramTab.setContextMenu(contextMenu);
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
		xAxis.setLabel("Numer generacji");
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Wartości funkcji przystosowania");
		wykres = new LineChart<Number, Number>(xAxis, yAxis);

		wykres.setTitle("Wykres przystosowania dla kolejnych generacji");
		wykres.prefWidthProperty().bind(grid.widthProperty().divide(2));
		wykres.prefHeightProperty().bind(grid.heightProperty());
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
