package com.example.countdown;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class HelloController {
    @FXML
    private TextField input;

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        String inputText = input.getText();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        try {
            LocalDateTime enteredDate = LocalDateTime.parse(inputText, formatter);
            LocalDateTime now = LocalDateTime.now();

            if (enteredDate.isAfter(now)) {
                startCountdown(enteredDate);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("A megadott dátum már elmult.");
                alert.showAndWait();
            }
        } catch (DateTimeParseException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Hibás dátum formátum");
            alert.showAndWait();
        }
    }

    private void startCountdown(LocalDateTime end) {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1), e -> {
            java.time.Duration remainingTime = java.time.Duration.between(LocalDateTime.now(), end);
            Period remainingPeriod = Period.between(LocalDateTime.now().toLocalDate(), end.toLocalDate());

            if (remainingTime.isNegative() || !remainingPeriod.isZero()) {
                welcomeText.setText(formatCountdown(remainingPeriod, remainingTime));
            } else {
                welcomeText.setText(formatCountdown(Period.ZERO, java.time.Duration.ZERO));
                timeline.stop();
                finish();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private String formatCountdown(Period period, java.time.Duration duration) {
        long hours = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long seconds = duration.getSeconds() % 60;

        return String.format("%d év, %d hónap, %d nap, %02d:%02d:%02d",
                period.getYears(), period.getMonths(), period.getDays(),
                hours, minutes, seconds);
    }

    private void finish() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Jejj");
            alert.setHeaderText(null);
            alert.setContentText("Lejárt az időzítő");
            alert.showAndWait();
        });
    }
}