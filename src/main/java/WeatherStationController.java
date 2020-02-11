/**
 * Sample Skeleton for 'WeatherStation.fxml' Controller Class
 */

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller class to send query and get results from Json
 */

public class WeatherStationController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="parameterChoiceBox"
    private ChoiceBox<String> parameterChoiceBox; // Value injected by FXMLLoader

    @FXML // fx:id="barChart"
    private BarChart<String, Number> barChart; // Value injected by FXMLLoader

    @FXML
    public CategoryAxis xAxis;

    @FXML
    public NumberAxis yAxis;


    @FXML // fx:id="miastoTextView"
    private TextField miastoTextView; // Value injected by FXMLLoader

    @FXML // fx:id="wczytajBtn"
    private Button wczytajBtn; // Value injected by FXMLLoader

    @FXML // fx:id="wyswietlBtn"
    private Button wyswietlBtn; // Value injected by FXMLLoader

    @FXML // fx:id="zapiszBtn"
    private Button zapiszBtn; // Value injected by FXMLLoader

    @FXML // fx:id="obrazJakosciPowietrza"
    private ImageView obrazJakosciPowietrza; // Value injected by FXMLLoader

    @FXML // fx:id="dataDatePicker"
    private DatePicker dataDatePicker; // Value injected by FXMLLoader

    @FXML // fx:id="hBox"
    private HBox hBox; // Value injected by FXMLLoader

    @FXML // fx:id="infoVBox"
    private VBox infoVBox; // Value injected by FXMLLoader

    @FXML // fx:id="resultsVBox"
    private VBox resultsVBox; // Value injected by FXMLLoader

    /**
     * method that create bar chart and update info about result
     */

    void rysujWykres() {
        barChart.getData().clear(); // czysc wykres
        double[] daneWykres = yChartValues(); // metoda liczaca avg,std,min i max z listy resluts

        double avg = daneWykres[0];
        double std = daneWykres[1];
        double min = daneWykres[2];
        double max = daneWykres[3];

        if (parameterChoiceBox.getValue().equals("CO")) { // parametr CO nalezy przeskalowac bo mamy ug/m3 a tabla ma mg/m3
            avg = avg / 1000;
            std = std / 1000;
            max = max / 1000;
            min = min / 1000;
        }

//make new series and populate it with data
        XYChart.Series<String, Number> series = new XYChart.Series<>();


        series.getData().add(new XYChart.Data<>("mean", avg));
        series.getData().add(new XYChart.Data<>("std", std));
        series.getData().add(new XYChart.Data<>("min", min));
        series.getData().add(new XYChart.Data<>("max", max));


        barChart.getData().add(series); // dodaj serie

        infoVBox.getChildren().clear(); // czysc boxy
        resultsVBox.getChildren().clear();


        infoVBox.getChildren().add(new Label("Data najwczesniejszego pomiaru: ")); // dodaj labele z inforamacjami
        infoVBox.getChildren().add(new Label("Data ostatniego pomiaru: "));
        infoVBox.getChildren().add(new Label("Srednia wartosc:  "));
        infoVBox.getChildren().add(new Label("Odchylenie standardowe :  "));
        infoVBox.getChildren().add(new Label("Max: "));
        infoVBox.getChildren().add(new Label("Min: "));


        resultsVBox.getChildren().add(new Label(measurements.get(0).getDate().getLocal()));
        resultsVBox.getChildren().add(new Label(measurements.get(measurements.size() - 1).getDate().getLocal()));
        resultsVBox.getChildren().add(new Label(String.valueOf(avg)));
        resultsVBox.getChildren().add(new Label(String.valueOf(std)));
        resultsVBox.getChildren().add(new Label(String.valueOf(max)));
        resultsVBox.getChildren().add(new Label(String.valueOf(min)));

    }


    /**
     * method that load measurements from file
     * @param event
     */
    @FXML
    void wczytajClicked(ActionEvent event) {

        Stage window = (Stage) zapiszBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {

            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {

                Gson gson = new Gson();
                measurements = gson.fromJson(reader, new TypeToken<List<Measurement>>() {
                }.getType());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (measurements!=null)
        rysujWykres();
        else {
            infoVBox.getChildren().clear();
            resultsVBox.getChildren().clear();
            infoVBox.getChildren().add(new Label("Dane są puste"));
        }
    }

    /**
     * method that take values from text field and others and send url and map results
     * @param event
     */
    @FXML
    void wyswietlClicked(ActionEvent event) {
        String city = miastoTextView.getText();
        LocalDate date = dataDatePicker.getValue();
        String parameter = parameterChoiceBox.getValue().toLowerCase();
        if (parameter.equals("c6h6"))
            parameter = "bc";

        // System.out.println(city + date.toString() + parameter);


        if (city.isEmpty() || date == null || parameter.isEmpty()) {
            infoVBox.getChildren().clear();
            resultsVBox.getChildren().clear();
            infoVBox.getChildren().add(new Label("Prosze wybrac poprawnie \n dane do przekazania"));
            return;
        }

        //System.out.println("I isc dalej " + date.toString());

        StringBuilder stringBuilder = new StringBuilder();
        String zapytanie = stringBuilder.append("https://api.openaq.org/v1/measurements?city=").append(city).append("&parameter=").append(parameter).append("&date_from=").append(date.toString()).toString();

        measurements = JsonConnect.getResponse(zapytanie); // otrzymuje liste Measurmentsów

        rysujWykres();
        zapiszBtn.setDisable(false);
    }

    /**
     * calculate avg,std,min and max form results
     * @return
     */
    double[] yChartValues() {

        double min = measurements.stream()
                .min(Comparator.comparing(Measurement::getValue))
                .get()
                .getValue();

        double max = measurements.stream()
                .max(Comparator.comparing(Measurement::getValue))
                .get()
                .getValue();
        double avg = measurements.stream()
                .mapToDouble(Measurement::getValue)
                .average()
                .getAsDouble();

        double std = Math.sqrt( // root from variance
                measurements.stream()
                        .map(i -> i.getValue() - avg) // (x-avg)
                        .map(i -> i * i) // (x-avg)^2
                        .mapToDouble(i -> i) // to double
                        .average() // avg
                        .getAsDouble());

        return new double[]{avg, std, min, max};

    }

    /**
     * save measurements to Json file
     * @param event
     */
    @FXML
    void zapiszClicked(ActionEvent event) {
        Stage window = (Stage) zapiszBtn.getScene().getWindow();
        File file = fileChooser.showSaveDialog(window);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

            Gson gson = new Gson();
            writer.write(gson.toJson(measurements));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<Measurement> measurements;
    private FileChooser fileChooser;


    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert parameterChoiceBox != null : "fx:id=\"parameterChoiceBox\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert barChart != null : "fx:id=\"barChart\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert miastoTextView != null : "fx:id=\"miastoTextView\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert wczytajBtn != null : "fx:id=\"wczytajBtn\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert wyswietlBtn != null : "fx:id=\"wyswietlBtn\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert zapiszBtn != null : "fx:id=\"zapiszBtn\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert obrazJakosciPowietrza != null : "fx:id=\"obrazJakosciPowietrza\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert dataDatePicker != null : "fx:id=\"dataDatePicker\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert hBox != null : "fx:id=\"hBox\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert infoVBox != null : "fx:id=\"infoVBox\" was not injected: check your FXML file 'WeatherStation.fxml'.";
        assert resultsVBox != null : "fx:id=\"resultsVBox\" was not injected: check your FXML file 'WeatherStation.fxml'.";

        zapiszBtn.setDisable(true); // zapisz nie aktywny dopoki nie wykona sie wyswietl
        ArrayList<String> parameters = new ArrayList<>();
        parameters.add("PM10");
        parameters.add("PM2.5");
        parameters.add("O3");
        parameters.add("NO2");
        parameters.add("SO2");
        parameters.add("C6H6");
        parameters.add("CO");
        parameterChoiceBox.getItems().addAll(parameters);
        parameterChoiceBox.setValue(parameters.get(0));

        wyswietlBtn.setDisable(true);
        dataDatePicker.setOnMouseClicked(MouseEvent -> wyswietlBtn.setDisable(false)); // dopoki nie wybierzemy daty to wyswietl bedzie nie aktywne

        barChart.setLegendVisible(false);
        xAxis.setCategories(FXCollections.observableArrayList("mean", "std", "min", "max"));


        fileChooser = new FileChooser(); // file chooser do zapisu i odczytu plikow
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Json files (*json)", "*.json");
        fileChooser.getExtensionFilters().add(extensionFilter);


    }


}
