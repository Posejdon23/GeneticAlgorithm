package ag1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;
import de.congrace.exp4j.UnknownFunctionException;
import de.congrace.exp4j.UnparsableExpressionException;

public class Main extends Application {

	private double[][] stats;
	private TableView<Parameter> paramTab = new TableView<Parameter>();
	private final ObservableList<Parameter> data = FXCollections
			.observableArrayList(new Parameter("a", "3", "0", "7"),
					new Parameter("b", "3", "0", "7"),
					new Parameter("c", "3", "0", "7"),
					new Parameter("d", "3", "0", "7"));
	private TextField fitFunction, popSize, genSize, pcross, pmutation, scale;
	private LineChart<Number, Number> wykres;
	private Stage stage;
	private GridPane gridMenu;
	private boolean isWykres = false;
	private boolean dodawanie = false;
	private double minH = 30;
	private Parameter[] params;
	private Tab tab3;
	private TabPane tabPane;
	private TextArea taPrzegląd;
	private Label fitFunctionLabel, popSizeLabel, genSizeLabel, pcrossLabel,
			pmutationLabel, scaleLabel;
	private ParamSpec ps;
	private SplitPane sp;
	private StackPane sp1, sp2;
	private HBox addingBox;
	private Button removeBtn;

	@Override
	public void start(final Stage stage) {

		this.stage = stage;
		gridMenu = new GridPane();
		gridMenu.setVgap(5);
		tabelaSettings(gridMenu);

		fitFunctionLabel = new Label("Funkcja przystosowania");
		fitFunctionLabel.setFont(Font.font("Tahoma", FontWeight.NORMAL, 15));
		fitFunctionLabel.setStyle("-fx-textfill: white");
		fitFunctionLabel.setMinWidth(200);
		gridMenu.add(fitFunctionLabel, 0, 3, 1, 1);
		fitFunction = new TextField();
		fitFunction.setText("a-b+c-d");
		fitFunction.setMinHeight(minH);
		gridMenu.add(fitFunction, 1, 3, 1, 1);

		popSizeLabel = new Label("Wielkość populacji");
		gridMenu.add(popSizeLabel, 0, 5);
		popSize = new TextField();
		popSize.setText("100");
		popSize.setMinHeight(minH);
		gridMenu.add(popSize, 1, 5);

		genSizeLabel = new Label("Liczba generacji");
		gridMenu.add(genSizeLabel, 0, 7);
		genSize = new TextField();
		genSize.setText("50");
		genSize.setMinHeight(minH);
		gridMenu.add(genSize, 1, 7);

		pcrossLabel = new Label("Ppb. krzyżowania");
		pcross = new TextField();
		pcross.setText("0.6");
		pcross.setMinHeight(minH);

		pmutationLabel = new Label("Ppb mutacji");
		pmutation = new TextField();
		pmutation.setText("0.01");
		pmutation.setMinHeight(minH);

		gridMenu.add(pcrossLabel, 0, 9);
		gridMenu.add(pcross, 1, 9);
		gridMenu.add(pmutationLabel, 0, 10);
		gridMenu.add(pmutation, 1, 10);

		scaleLabel = new Label("Współczynnik skalowania");
		scaleLabel.setMinWidth(200);
		gridMenu.add(scaleLabel, 0, 11);
		scale = new TextField();
		scale.setText("1.2");
		scale.setMinHeight(minH);

		gridMenu.add(scale, 1, 11);

		final Button wyczyśćBtn = new Button("Schowaj wykres");
		wyczyśćBtn.setVisible(false);
		wyczyśćBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				wyczyśćBtn.setVisible(true);
				if (isWykres) {
					sp.getItems().remove(1);
					stage.setWidth(0.5 * gridMenu.widthProperty().doubleValue());
					wyczyśćBtn.setText("Pokaż wykres");
					isWykres = false;
				} else {
					sp.getItems().add(sp2);
					isWykres = true;
					stage.setWidth(2.0 * gridMenu.widthProperty().doubleValue());
					wyczyśćBtn.setText("Schowaj wykres");
				}

			}
		});

		Button rysujBtn = new Button("Start");
		rysujBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				if (checkNoErrors()) {
					oblicz(Integer.parseInt(popSize.getText()),
							Integer.parseInt(genSize.getText()),
							Double.parseDouble(pcross.getText()),
							Double.parseDouble(pmutation.getText()),
							Double.parseDouble(scale.getText()),
							fitFunction.getText());

					if (!isWykres) {
						stage.setWidth(2.0 * gridMenu.widthProperty()
								.doubleValue());
					}
					tab3.setDisable(false);
					wyczyśćBtn.setText("Schowaj wykres");
					taPrzegląd.setText(Funkcje.sbOpis.toString());
					Funkcje.sbOpis.delete(0, Funkcje.sbOpis.length());
					Funkcje.licznikGen = 0;
					wyczyśćBtn.setVisible(true);

					if (!sp2.getChildren().isEmpty())
						sp2.getChildren().remove(wykres);

					sp2.getChildren().add(addChart());

					if (sp.getItems().size() == 1)
						sp.getItems().add(sp2);
					tabPane.getSelectionModel().select(2);
					isWykres = true;
				} else {
					if (isWykres) {
						stage.setWidth(0.5 * stage.widthProperty()
								.doubleValue());
						sp.getItems().remove(1);
						wyczyśćBtn.setText("Pokaż wykres");
						isWykres = false;
					}
				}
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
		gridMenu.add(hbBtn, 0, 13, 2, 1);

		tabPane = new TabPane();
		Tab tab1 = new Tab();
		tab1.setContent(gridMenu);
		tab1.setText("Główna");

		Tab tab2 = new Tab();
		tab2.setText("Objaśnienia");
		tab2.setContent(objaśnienia());
		tab3 = new Tab();
		tab3.setText("Wyniki");
		setPrzegląd();
		HBox h = new HBox();
		h.getChildren().addAll(taPrzegląd);
		tab3.setContent(h);

		tab1.setClosable(false);
		tab2.setClosable(false);
		tab3.setClosable(false);
		tab3.setDisable(true);

		tabPane.getTabs().addAll(tab1, tab2, tab3);

		sp = new SplitPane();
		sp1 = new StackPane();
		sp2 = new StackPane();
		sp2.setMinWidth(15);

		sp1.getChildren().add(tabPane);

		sp.getItems().addAll(sp1);

		Scene scene = new Scene(sp, 500, 600);
		stage.centerOnScreen();
		stage.setMinHeight(500);
		stage.setMinWidth(500);

		stage.setTitle("Prosty algorytm genetyczny");
		scene.getStylesheets().add(
				Main.class.getResource("Styl.css").toExternalForm());
		stage.setScene(scene);
		this.stage.getIcons().add(new Image("file:resources/images/icon.png"));
		stage.show();
	}

	private TextArea objaśnienia() {
		TextArea ta = new TextArea();
		ta.setWrapText(true);
		ta.setEditable(false);
		ta.setText("\n\t\t\t\t\tTABELA\n\nTabela zawiera informacje o parametrach " +
				"wpisanych w allel każdego osobnika. Po wprowadzeniu do tabeli, nazwy " +
				"parametrów można stosować w polu \"Funkcja przystosowania\". Liczba " +
				"bitów kodujących określa ile bitów zostanie przeznaczonych na " +
				"zakodowanie danego parametru. Od tej wartości zależy dokładność z " +
				"jaką mierzony będzie parametr."
				
				+ "\n\n\t\t\tFUNKCJA PRZYSTOSOWANIA\n\nDo dyspozycji mamy parser o " +
				"następujacych możliwościach:\n\n"
				+ "Dodawanie: 2 + 2"
				+ "\nOdejmowanie: 2 - 2"
				+ "\nMnożenie: 2 * 2"
				+ "\nDzielenie: 2 / 2"
				+ "\nPierwiastek: sqrt()"
				+ "\nFunkcja wykładnicza: 2 ^ 2"
				+ "\nJednoargumentowe znaki +/-: +2 - (-2)"
				+ "\nReszta z dzielenia: 2 % 2"
				+ "\nWartość bezwzględna: abs()"
				+ "\nFunkcja eksponencjalna: exp()"
				+ "\nCosinus: cos()"
				+ "\nSinus: sin()"
				+ "\nTangens: tan()"
				+ "\nArcus cosinus: acos()"
				+ "\nArcus sinus: asin()"
				+ "\nArcus tangens: atan()"
				+ "\nCosinus hiperboliczny cosh()"
				+ "\nSinus hiperboliczny: sinh()"
				+ "\nTangens hiperboliczny: tanh()"
				+ "\nPierwiastek sześcienny: cbrt()"
				+ "\nFunkcja sufit: ceil()"
				+ "\nFunkcja podłoga: floor()"
				+ "\nLogarytm naturalny o podstawie e: log()"
				+ "\n\nUżyty parser to exp4j, pobrany z:"
				+ "\nhttp://www.objecthunter.net/exp4j/\n"
				
				+ "\n\t\t\t\t\tWIELKOŚĆ POPULACJI\n\nOkreślia liczbę osobników w populacji. "
				+ "Wartość ta musi być nieujemna i parzysta"
				+ "(osobniki dobierane są w pary podczas krzyżowania)."
				
				+ "\n\n\t\t\t\t\tLICZBA GENERACJI\n\n Wartość nieujemna." +
				
				"\n\n\t\tPRAWDOPODOBIEŃSTWO KRZYŻOWANIA\n\nWspółczynnik określający" +
				" z jakim prawdopodobieństwem osobniki w populacji będą się ze sobą " +
				"krzyżować. Wartość z przedziału [0, 1]." +
				"\n\n\t\t\tPRAWDOPODOBIEŃSTWO MUTACJI\n\nOkreśla prawdopodobieństwo zajścia"
				+ "pojedyńczej mutacji(czyli zamiany 0 na 1 lub odwrotnie) podczas " +
				"krzyżowania." +
				"\n\n\t\tWSPÓŁCZYNNIK SKALOWANIA(LINIOWEGO)\n\nStosowany  w celu " +
				"zapewnienia odpowiedniego poziomu konkurencji podczas wykonywania " +
				"algorytmu. Początkowo pomniejsza wartości funkcji przystosowania " +
				"\"superosobników\" aby nie opanowali populacji. W poźniejszej fazie " +
				"wartości te są zwiększane w celu wyróżnienia " +
				"lepiej przystosowanych, bądź zmniejszane przeciętnym " +
				"osobnikom.");
		return ta;
	}

	private boolean checkNoErrors() {

		int pozycja = 15;
		int sizeMenu = gridMenu.getChildren().size();
		if (sizeMenu > 15) {
			for (int i = pozycja; i < sizeMenu; i++) {
				gridMenu.getChildren()
						.remove(gridMenu.getChildren().size() - 1);
			}
		}
		boolean bezBłędu = true;
		boolean formatError = false;

		try {
			ExpressionBuilder eb = new ExpressionBuilder(fitFunction.getText());
			ps = new ParamSpec();
			params = new Parameter[data.size()];
			data.toArray(params);
			for (Parameter p : params) {
				ps.addParam(p.getName(), p.getLength(), p.getMinparm(),
						p.getMaxparm());
			}
			for (Parameter p : params)
				eb.withVariable(p.getName(), p.getValue());

			Calculable calc = null;
			calc = eb.build();
			fitFunctionLabel.setId(null);
		} catch (IllegalArgumentException | UnknownFunctionException
				| UnparsableExpressionException e) {
			bezBłędu = false;
			Label l = new Label(
					"Niepoprawne wyrażenie w funkcji przystosowania.");
			l.setId("error-label");
			gridMenu.add(l, 0, pozycja++, 2, 1);
			fitFunctionLabel.setId("error-label");
		}

		try {
			if (Integer.parseInt(popSize.getText()) < 1) {
				Label l = new Label(
						"Populacja powinna liczyć conajmniej 2 osobników.");
				l.setId("error-label");
				gridMenu.add(l, 0, pozycja++, 2, 1);
				popSizeLabel.setId("error-label");
				bezBłędu = false;
				throw new NumberFormatException();

			} else if (Integer.parseInt(popSize.getText()) % 2 != 0) {
				Label l = new Label("Populacja powinna składać się z"
						+ " parzystej liczby osobników.");
				l.setId("error-label");
				gridMenu.add(l, 0, pozycja++, 2, 1);
				popSizeLabel.setId("error-label");
				bezBłędu = false;
				throw new NumberFormatException();
			}
			popSizeLabel.setId(null);
		} catch (NumberFormatException e) {
			formatError = true;
			popSizeLabel.setId("error-label");
			bezBłędu = false;
		}
		try {
			Integer.parseInt(genSize.getText());
			if (Integer.parseInt(genSize.getText()) <= 1) {

				Label l = new Label(
						"Liczba generacji powinna być większa niż 1.");
				gridMenu.add(l, 0, pozycja++, 2, 1);
				l.setId("error-label");
				genSizeLabel.setId("error-label");
				bezBłędu = false;
				throw new NumberFormatException();
			}
			genSizeLabel.setId(null);
		} catch (NumberFormatException e) {
			formatError = true;
			genSizeLabel.setId("error-label");
			bezBłędu = false;
		}
		try {
			if (Double.parseDouble(pcross.getText()) < 0
					|| Double.parseDouble(pcross.getText()) > 1) {
				Label l = new Label("Ppb. krzyżowania powinno "
						+ "to liczba z przedziału [0, 1].");
				l.setId("error-label");
				gridMenu.add(l, 0, pozycja++, 2, 1);
				pcrossLabel.setId("error-label");
				bezBłędu = false;
				throw new NumberFormatException();
			}
			pcrossLabel.setId(null);
		} catch (NumberFormatException e) {
			formatError = true;
			pcrossLabel.setId("error-label");
			bezBłędu = false;
		}
		try {
			if (Double.parseDouble(pmutation.getText()) < 0
					|| Double.parseDouble(pmutation.getText()) > 1) {
				Label l = new Label(
						"Ppb. mutacji to liczba z przedziału [0, 1].");
				l.setId("error-label");
				gridMenu.add(l, 0, pozycja++, 2, 1);
				pmutationLabel.setId("error-label");
				bezBłędu = false;
				throw new NumberFormatException();
			}
			pmutationLabel.setId(null);
		} catch (NumberFormatException e) {
			formatError = true;
			pmutationLabel.setId("error-label");
			bezBłędu = false;
		}
		try {
			if (Double.parseDouble(scale.getText()) < 0) {
				Label l = new Label(
						"Wsp. skalowania powinien być większy od 0.");
				l.setId("error-label");
				gridMenu.add(l, 0, pozycja++, 2, 1);
				scaleLabel.setId("error-label");
				bezBłędu = false;
				throw new NumberFormatException();
			}
			scaleLabel.setId(null);
		} catch (NumberFormatException e) {
			formatError = true;
			scaleLabel.setId("error-label");
			bezBłędu = false;
		}
		if (formatError) {
			Label l = new Label("Błąd formatu liczby.");
			l.setId("error-label");
			gridMenu.add(l, 0, pozycja++, 2, 1);
		}
		if (bezBłędu) {
			fitFunctionLabel.setId(null);
			popSizeLabel.setId(null);
			genSizeLabel.setId(null);
			pcrossLabel.setId(null);
			pmutationLabel.setId(null);
			scaleLabel.setId(null);
		}
		return bezBłędu;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void tabelaSettings(final GridPane grid) {

		TableColumn titleCol = new TableColumn("Parametry osobników");
		paramTab.setEditable(true);
		
		TableColumn paramNameCol = new TableColumn("Nazwa");
		paramNameCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"name"));
		paramNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		paramNameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<Parameter, String>>() {
                @Override
                public void handle(CellEditEvent<Parameter, String> t) {
                    ((Parameter) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
		
		TableColumn paramSizeCol = new TableColumn(
				"Liczba bitów\nkodujących\nparametr");
		paramSizeCol
				.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"length"));
		
		TableColumn zakresCols = new TableColumn("Zakres Wartości");
		zakresCols.setPrefWidth(180);
		TableColumn paramMinCol = new TableColumn("Min");
		paramMinCol.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"minparm"));
		TableColumn paramMaxCol = new TableColumn("Max");
		paramMaxCol.setCellValueFactory(new PropertyValueFactory<Parameter, String>(
						"maxparm"));
		paramNameCol.prefWidthProperty().bind(
				paramTab.widthProperty().divide(4));
		paramSizeCol.prefWidthProperty().bind(
				paramTab.widthProperty().divide(4.1));
		paramMinCol.prefWidthProperty()
				.bind(paramTab.widthProperty().divide(4));
		paramMaxCol.prefWidthProperty()
				.bind(paramTab.widthProperty().divide(4));
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
		addLength.setPromptText("Liczba bitów");
		addLength.setMinHeight(minH);
		final TextField addMinVal = new TextField();
		addMinVal.setPromptText("Min");
		addMinVal.setMinHeight(minH);
		final TextField addMaxVal = new TextField();
		addMaxVal.setPromptText("Max");
		addMaxVal.setMinHeight(minH);
		addName.setVisible(true);
		addLength.setVisible(true);
		addMinVal.setVisible(true);
		addMaxVal.setVisible(true);

		addName.prefWidthProperty().bind(stage.widthProperty().divide(4));
		addLength.prefWidthProperty().bind(stage.widthProperty().divide(4));
		addMinVal.prefWidthProperty().bind(stage.widthProperty().divide(4));
		addMaxVal.prefWidthProperty().bind(stage.widthProperty().divide(4));

		final Button addButton = new Button("Dodaj parametr");
		addButton.setMinWidth(100);
		addButton.setMaxWidth(200);
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				if (dodawanie) {
					try {
						if (Integer.parseInt(addLength.getText()) < 1) {
							throw new NumberFormatException();
						}
						data.add(new Parameter(addName.getText(), addLength
								.getText(), addMinVal.getText(), addMaxVal
								.getText()));
						addName.clear();
						addLength.clear();
						addMinVal.clear();
						addMaxVal.clear();
						addingBox.getChildren().removeAll(addName, addLength,
								addMinVal, addMaxVal);
						addingBox.getChildren().add(removeBtn);
						addButton.prefWidthProperty().bind(
								stage.widthProperty().divide(2));
						addButton.setText("Dodaj parametr");
						dodawanie = false;
					} catch (NumberFormatException ex) {

					}

				} else {
					dodawanie = true;
					addButton.prefWidthProperty().bind(
							stage.widthProperty().divide(4));
					addButton.setText("Dodaj");
					addingBox.getChildren().removeAll(removeBtn, addButton);
					addingBox.getChildren().addAll(addName, addLength,
							addMinVal, addMaxVal, addButton);
				}
			}
		});

		removeBtn = new Button("Usuń parametr");
		removeBtn.setMinWidth(100);
		removeBtn.setMaxWidth(200);
		removeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				data.remove(paramTab.getSelectionModel().getSelectedItem());
			}
		});
		addButton.prefWidthProperty().bind(stage.widthProperty().divide(2));
		removeBtn.prefWidthProperty().bind(stage.widthProperty().divide(2));

		addingBox = new HBox();
		addingBox.getChildren().addAll(addButton, removeBtn);
		addingBox.setSpacing(7);
		addingBox.alignmentProperty().set(Pos.CENTER_RIGHT);
		addingBox.prefWidthProperty().bind(paramTab.widthProperty());
		grid.add(addingBox, 0, 2, 2, 1);
	}

	public static void main(String[] args) {
		launch(args);
	}

	private void oblicz(int popSize, int genSize, double pcross,
			double pmutation, double scale, String fitFunction) {
		ps = new ParamSpec();
		params = new Parameter[data.size()];
		data.toArray(params);
		for (Parameter p : params) {
			ps.addParam(p.getName(), p.getLength(), p.getMinparm(),
					p.getMaxparm());
		}
		NewPop np = new NewPop(ps, genSize, popSize, fitFunction);
		stats = np.populate(pcross, pmutation, scale); // min,max, sum,avg

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private LineChart addChart() {
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Generacja");
		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Wartość funkcji przystosowania");
		wykres = new LineChart<Number, Number>(xAxis, yAxis);

		wykres.setTitle("Wykres przystosowania generacji");
		wykres.prefWidthProperty().bind(gridMenu.widthProperty().divide(2));
		wykres.prefHeightProperty().bind(gridMenu.heightProperty());
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

	private void setPrzegląd() {
		taPrzegląd = new TextArea();
		taPrzegląd.setId("taPrzeglad");
		taPrzegląd.setEditable(false);
		taPrzegląd.setPrefRowCount(10);
		taPrzegląd.setPrefColumnCount(100);
		taPrzegląd.setWrapText(true);
	}
}
