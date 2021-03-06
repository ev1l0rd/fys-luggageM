package fys.luggagem;

import fys.luggagem.models.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author jordan
 */
public class FXMLDocumentController implements Initializable {

    private Data data = MainApp.getData();
    Scene window;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label logInLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button logInButton;

    @FXML
    private Button dutchButton;

    @FXML
    private Button englishButton;

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label statusMessage;

    @FXML
    private void handleLogInAction(ActionEvent event) throws IOException {
        String name = username.getText();
        String pass = password.getText();
        userCheck(name, pass);
    }

    @FXML
    private void handleExitAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void handleEnglishAction(ActionEvent event) {
        if (!data.getLanguage().equals("en") && !data.getCountry().equals("US")) {
            data.setLanguage("en");
            data.setCountry("US");
            data.setLocale();
            dutchButton.setStyle("-fx-background-color: white");
            englishButton.setStyle("-fx-background-color: grey");
            refreshText();
        }
    }

    @FXML
    private void handleDutchAction(ActionEvent event) {
        if (!data.getLanguage().equals("nl") && !data.getCountry().equals("NL")) {
            data.setLanguage("nl");
            data.setCountry("NL");
            data.setLocale();
            dutchButton.setStyle("-fx-background-color: grey");
            englishButton.setStyle("-fx-background-color: white");
            refreshText();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setText("500500"); // ONLY FOR DEVELOPMENT BUILD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        password.setText("test");   // ONLY FOR DEVELOPMENT BUILD!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (data.getLanguage().equals("en") && data.getCountry().equals("US")) {
            dutchButton.setStyle("-fx-background-color: white");
            englishButton.setStyle("-fx-background-color: grey");
        } else {
            dutchButton.setStyle("-fx-background-color: grey");
            englishButton.setStyle("-fx-background-color: white");
        }

        // Set application icon
        // Get stage
        // Add a custom icon
        data.getStage().getIcons().add(new Image(this.getClass().getResource("/images/corendon_star_logo.png").toString()));
    }

    private void userCheck(String username, String password) throws IOException {
        try {
            ResultSet resultSet = MainApp.myJDBC.executeResultSetQuery("SELECT * FROM Account");
            while (resultSet.next()) {
                if (username.equals(resultSet.getString("Employee_code")) && Encryptor.encrypt(password,
                        resultSet.getString("salt")).equals(resultSet.getString("password"))) {
                    ResultSet userDataSet = MainApp.myJDBC.executeResultSetQuery("SELECT * FROM Employee WHERE code=" + resultSet.getInt("Employee_code"));
                    if (userDataSet.next()) {
                        data.setName(userDataSet.getString("first_name") + (userDataSet.getString("preposition") != null ? " "
                                + userDataSet.getString("preposition") : "") + " " + userDataSet.getString("last_name"));
                        data.setEmployeeNr(resultSet.getInt("Employee_code"));
                        ResultSet accountInfoSet = MainApp.myJDBC.executeResultSetQuery("SELECT user_level FROM Account WHERE Employee_code=" + resultSet.getInt("Employee_code"));
                        if (accountInfoSet.next()) {
                            data.setPermissions(accountInfoSet.getInt("user_level"));
                        }
                        ResultSet airportInfoSet = MainApp.myJDBC.executeResultSetQuery("SELECT timezone FROM Airport A"
                                + " INNER JOIN employee E ON A.IATA = E.Luchthaven_IATA"
                                + " WHERE E.code = " + resultSet.getInt("Employee_code"));
                        if(airportInfoSet.next()) {
                            data.setTimezone(airportInfoSet.getString("timezone"));
                        } 
                        MainApp.setScene(this.getClass().getResource("/fxml/HomeScreenFXML.fxml"));
                    } else {
                        statusMessage.setText("Impossible error has occured. Please try again.");
                    }
                }
            }
            statusMessage.setText(data.getResourceBundle().getString("wrongInformation"));
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    private void refreshText() {
        logInLabel.setText(data.getResourceBundle().getString("logIn"));
        logInButton.setText(data.getResourceBundle().getString("logIn"));
        exitButton.setText(data.getResourceBundle().getString("exit"));
        username.setPromptText(data.getResourceBundle().getString("username"));
        password.setPromptText(data.getResourceBundle().getString("password"));
    }
}
