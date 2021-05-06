package com.home;

import com.home.entity.Person;
import com.home.entity.Roles;
import com.home.repository.PersonRepository;
import com.home.repository.RoleRepository;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

@Component
public class Controller implements Initializable {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;


    @FXML //Menu Block
    private JFXButton mngMovies, mngGenres, mngActors, mngRoles, homeButton, exit, closeMenu, openMenu, helpMenu;

    @FXML //Menu Block - slider menu
    private AnchorPane menuList, helpMenuPane;

    @FXML //Main Pane Block
    private AnchorPane moviePane, genrePane, actorPane, rolePane, homePane;

    @FXML //Additional Relationship modify block
    private AnchorPane modRelPane;

    @FXML //goTo or BackFrom add. rel. block
    private JFXButton backFromModRel;

    //Todo - Actor Management Block, i have no idea why i didnt make it in different fxml files, too bad.
    @FXML
    private JFXTextField nationalityTxt, cityBirthTxt, countryBirthTxt, secondNameTxt, personNameTxt;

    @FXML
    private JFXDatePicker birthDateTxt;

    @FXML
    private ImageView actorImage;

    @FXML
    private JFXListView<Person> actorActorListBox;

    @FXML
    ImageView imageAddAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);

        actorImage.setImage(new Image(file.toURI().toString()));

        return actorImage;
    }

    @FXML
    void addActionActor(ActionEvent event) {
        Person person = new Person();

        person.setName(personNameTxt.getText());
        person.setSecond_name(secondNameTxt.getText());
        person.setNationality(nationalityTxt.getText());

//        ZoneId defaultZoneId = ZoneId.systemDefault();
//        Date date = Date.from(birthDateTxt.getValue().atStartOfDay(defaultZoneId).toInstant());

//        System.out.println(birthDateTxt.getValue());
//        System.out.println(date);
//        Date date = new Date(String.valueOf(birthDateTxt.getValue()));

//        person.setBirth_date(new Date(String.valueOf(localDate)));
        person.setBirth_date(birthDateTxt.getValue());
        person.setCity_birth(cityBirthTxt.getText());
        person.setCountry_birth(countryBirthTxt.getText());
        person.setPhoto_person(new File(actorImage.toString()));

        personRepository.save(person);
        actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
    }


    @FXML
    private JFXListView<Roles> roleRoleListBox;

    //Todo : there i have button events handlers, at this moment i wasnt able to implement this better, but i dont like it.

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == mngMovies) {

            moviePane.setVisible(true);

            genrePane.setVisible(false);
            actorPane.setVisible(false);
            rolePane.setVisible(false);
            homePane.setVisible(false);

        } else if (event.getSource() == mngGenres) {

            genrePane.setVisible(true);

            moviePane.setVisible(false);
            actorPane.setVisible(false);
            rolePane.setVisible(false);
            homePane.setVisible(false);

        } else if (event.getSource() == mngActors) {

            actorPane.setVisible(true);

            moviePane.setVisible(false);
            genrePane.setVisible(false);
            rolePane.setVisible(false);
            homePane.setVisible(false);

        } else if (event.getSource() == mngRoles) {

            rolePane.setVisible(true);

            moviePane.setVisible(false);
            genrePane.setVisible(false);
            actorPane.setVisible(false);
            homePane.setVisible(false);

        } else if (event.getSource() == homeButton) {

            homePane.setVisible(true);

            moviePane.setVisible(false);
            genrePane.setVisible(false);
            actorPane.setVisible(false);
            rolePane.setVisible(false);

            if (helpMenuPane.visibleProperty().getValue().equals(true)) {
                helpMenuPane.setVisible(false);
            }
        }
    }

    @FXML
    private void handleButtonActionHelp(ActionEvent event) {
        if (event.getSource() == helpMenu) {
            if (helpMenuPane.visibleProperty().getValue().equals(false)) {
                helpMenuPane.setVisible(true);
            } else if (helpMenuPane.visibleProperty().getValue().equals(true)) {
                helpMenuPane.setVisible(false);
            }
        }
    }

    @FXML
    private void handleButtonActionModRel(ActionEvent event) {
        modRelPane.setVisible(true);
            if (event.getSource() == backFromModRel) {
                modRelPane.setVisible(false);
            }
    }

    //Todo - very strange realization of slider, and other functionality, like taking info from DB to listBox

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));

        roleRoleListBox.setItems(FXCollections.observableArrayList(roleRepository.findAll()));

        exit.setOnMouseClicked(event -> System.exit(0));

        menuList.setTranslateX(-189);

        openMenu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(menuList);

            slide.setToX(0);
            slide.play();

            menuList.setTranslateX(-189);

            slide.setOnFinished((e) -> {
                openMenu.setVisible(false);
                closeMenu.setVisible(true);
            });
        });

        closeMenu.setOnMouseClicked(event -> {
            TranslateTransition slide = new TranslateTransition();
            slide.setDuration(Duration.seconds(0.5));
            slide.setNode(menuList);

            slide.setToX(-189);
            slide.play();

            menuList.setTranslateX(0);

            slide.setOnFinished((e) -> {
                openMenu.setVisible(true);
                closeMenu.setVisible(false);
            });
        });
    }
}
