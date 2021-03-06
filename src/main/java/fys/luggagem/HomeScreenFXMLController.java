package fys.luggagem;

import fys.luggagem.models.Data;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.swing.Timer;

/**
 *
 * @author jordan
 */
public class HomeScreenFXMLController implements Initializable {

    private Timer t;
    private Data data = MainApp.getData();

    @FXML
    private StackPane workspace;

    @FXML
    private Label nameLabel;
    @FXML
    private Button reportsButton;
    @FXML
    private Button accountManagementButton;

    @FXML
    private void handleLogOutAction(ActionEvent event) throws IOException {
        MainApp.setScene(this.getClass().getResource("/fxml/FXMLDocument.fxml"));
    }

    @FXML
    private void handleGebruikersbeheerAction(ActionEvent event) throws IOException {
        MainApp.loadFXMLFile(this.getClass().getResource("/fxml/AccountbeheerFXML.fxml"));
    }

    @FXML
    private void handleBeschadigdeBagageAction(ActionEvent event) throws IOException {
        MainApp.loadFXMLFile(this.getClass().getResource("/fxml/BeschadigdeBagageFXML.fxml"));
    }

    @FXML
    private void handleRapportageAction(ActionEvent event) throws IOException {
        MainApp.setScene(this.getClass().getResource("/fxml/RapportageFXML.fxml"));
    }

    @FXML
    private void handleDatabaseAction(ActionEvent event) throws IOException {
        MainApp.setScene(this.getClass().getResource("/fxml/DatabaseFXML.fxml"));
    }

    @FXML
    private void handleVerlorenBagageAction(ActionEvent event) throws IOException {
        MainApp.loadFXMLFile(this.getClass().getResource("/fxml/VerlorenBagageFXML.fxml"));
    }

    @FXML
    private void handleHomeAction(ActionEvent event) {
        MainApp.loadFXMLFile(this.getClass().getResource("/fxml/HomeScreenContentFXML.fxml"));
    }

    @FXML
    private void handleGevondenBagageAction(ActionEvent event) throws IOException {
        MainApp.loadFXMLFile(this.getClass().getResource("/fxml/GevondenBagageFXML.fxml"));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        data.setWorkspace(workspace);
        MainApp.loadFXMLFile(this.getClass().getResource("/fxml/HomeScreenContentFXML.fxml"));
        nameLabel.setText(data.getName());

        if (data.getPermissions() < 1) {
            reportsButton.setDisable(true);
        }

        if (data.getPermissions() < 2) {
            accountManagementButton.setDisable(true);
        }
    }
}
