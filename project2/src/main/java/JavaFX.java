import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
public class JavaFX extends Application {
	TextField temperature,weather;
	Stage primaryStage;
	Scene homeScene, forecastScene;
	ArrayList<Period> forecast;

	private static final String SUNNY_ICON_PATH = "/images/sun.png";
	private static final String CLOUDY_ICON_PATH = "/images/clouds.png";
	private static final String RAINY_ICON_PATH = "/images/rainy-day.png";
	private static final String SNOWY_ICON_PATH = "/images/snowy.png";

	public static void main(String[] args) {
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("I'm a professional Weather App!");
		//int temp = WeatherAPI.getTodaysTemperature(77,70);
		//ArrayList<Period>
		forecast = WeatherAPI.getForecast("LOT",77,70);
		if (forecast == null){
			throw new RuntimeException("Forecast did not load");
		}
		temperature = new TextField();
		weather = new TextField();
		temperature.setText("Today's weather is: "+String.valueOf(forecast.get(0).temperature));
		weather.setText(forecast.get(0).detailedForecast);

		Button sceneSwitch = new Button("Forecast");
		sceneSwitch.setOnAction(e -> {
			primaryStage.setScene(forecastScene);
		});

		VBox homeVbox = new VBox(10, temperature, weather, sceneSwitch);
		Scene homeScene = new Scene(homeVbox, 700, 700);

		Button backButton = new Button("Back");
		backButton.setOnAction(e -> {
			primaryStage.setScene(homeScene);
		});
		LocalTime now = LocalTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault());
		String civilian = now.format(formatter);

		HBox forecastHBox = new HBox(10);
		VBox forecastVBox = new VBox(new TextField("Forecast: "), new TextField(String.valueOf(forecast.get(1).temperature)), backButton);
		forecastVBox.getChildren().add(new Label("3-Day Forecast:    " + civilian));
		forecastVBox.getChildren().add(forecastHBox);


		LocalDate today = LocalDate.now();

		VBox day1Forecast = createForecast(today, 0);
		forecastHBox.getChildren().add(day1Forecast);

		VBox day2Forecast = createForecast(today.plusDays(1), 2);
		forecastHBox.getChildren().add(day2Forecast);

		VBox day3Forecast = createForecast(today.plusDays(2), 4);
		forecastHBox.getChildren().add(day3Forecast);


		forecastScene = new Scene(forecastVBox, 700, 700);

		primaryStage.setScene(homeScene);
		primaryStage.show();


	}

	private VBox createForecast(LocalDate date, int day) {
		Period dayOfForecast = forecast.get(day);
		Period nightForecast = forecast.get(day+1);

		DayOfWeek dayOfWeek = date.getDayOfWeek();
		String weekDay = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault());
		int dayRain = dayOfForecast.probabilityOfPrecipitation.value;
		int nightRain = nightForecast.probabilityOfPrecipitation.value;
		VBox dayBox = new VBox(10);

		Label weekText = new Label(weekDay + ":");
//		weekText.setEditable(false);
//		weekText.setPrefWidth(50);
//		weekText.setPrefHeight(50);
		TextArea weekDayText = new TextArea("Day: " + dayOfForecast.temperature + "°F  ");
		weekDayText.setEditable(false);
		weekDayText.setPrefWidth(310);
		weekDayText.setPrefHeight(40);
//		weekDayText.setPrefRowCount(3);
		TextArea dayCast = new TextArea("Weather: \n"
				+ dayOfForecast.shortForecast
				+ " \n"
				+ dayRain + " % change of Precipitation"
				+ "\nWind Speed: " + dayOfForecast.windSpeed
				+ "\nWind Direction: " + dayOfForecast.windDirection);
		dayCast.setEditable(false);
		dayCast.setPrefWidth(310);
		dayCast.setPrefHeight(150);
		dayCast.setPrefRowCount(4);

		TextArea weekNightText = new TextArea("Night: " + nightForecast.temperature + "°F  ");
		weekNightText.setEditable(false);
		weekNightText.setPrefWidth(310);
		weekNightText.setPrefHeight(40);
//		weekNightText.setPrefRowCount(3);
		TextArea nightCast = new TextArea("Weather: \n"
				+ nightForecast.shortForecast
				+ "  \n"
				+ nightRain
				+ " % chance of Precipitation"
				+ " \nWind Speed: " + nightForecast.windSpeed
				+ " \nWind Direction: " + nightForecast.windDirection);
		nightCast.setEditable(false);
		nightCast.setPrefWidth(310);
		nightCast.setPrefHeight(150);
		nightCast.setPrefRowCount(4);


		ImageView dayIcon = getWeatherIcon(dayOfForecast.shortForecast);
		ImageView nightIcon = getWeatherIcon(nightForecast.shortForecast);

		HBox dayHbox = new HBox(10, weekDayText, dayIcon);
		HBox nightHbox = new HBox(10, weekNightText, nightIcon);
		dayBox.getChildren().addAll(weekText, dayHbox, dayCast, nightHbox, nightCast);
		return dayBox;

	}

	private ImageView getWeatherIcon(String Wicon) {
		Image image;

		String forecastIcon = Wicon.toLowerCase();
		if(forecastIcon.contains("sun")){
			image = new Image(getClass().getResource(SUNNY_ICON_PATH).toExternalForm());
		} else if(forecastIcon.contains("rain")){
			image = new Image(getClass().getResource(RAINY_ICON_PATH).toExternalForm());
		} else if(forecastIcon.contains("cloud")){
			image = new Image(getClass().getResource(CLOUDY_ICON_PATH).toExternalForm());
		} else if(forecastIcon.contains("snow")){
			image = new Image(getClass().getResource(SNOWY_ICON_PATH).toExternalForm());
		} else {
			image = new Image(getClass().getResource(SUNNY_ICON_PATH).toExternalForm());
		}

		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(50);
		imageView.setFitWidth(50);
		imageView.setPreserveRatio(true);

		return imageView;

	}


}
