package com.home;

import com.home.entity.Movie;
import com.home.entity.Person;
import com.home.entity.Roles;
import com.home.repository.GenreRepository;
import com.home.repository.MovieRepository;
import com.home.repository.PersonRepository;
import com.home.repository.RoleRepository;
import com.jfoenix.controls.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

@Slf4j
@Component
public class Controller implements Initializable {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;


    @FXML // + Menu Block
    private JFXButton mngMovies, mngGenres, mngActors, mngRoles, homeButton, exit, closeMenu, openMenu, helpMenu;

    @FXML // + Menu Block - slider menu
    private AnchorPane menuList, helpMenuPane;

    @FXML // + Main Pane Block
    private AnchorPane moviePane, genrePane, actorPane, rolePane, homePane;

    @FXML // + Additional Relationship modify block
    private AnchorPane modRelPane;

    @FXML // + goTo or BackFrom add. rel. block
    private JFXButton backFromModRel;

    // + - Actor Management Block, i have no idea why i didnt make it in different fxml files, too bad.
    @FXML
    private JFXTextField nationalityTxt, cityBirthTxt, countryBirthTxt, secondNameTxt, personNameTxt;

    @FXML
    private JFXDatePicker birthDateTxt;

    @FXML
    private JFXButton actorImageBtn, delPersonButton, updatePersonBtn;

    @FXML
    private ImageView actorImage;

    @FXML
    private JFXListView<Person> actorActorListBox;

    @FXML
    private void addActionActor(ActionEvent event) {
        Person person = new Person();

        setPersonFields(person);

        personRepository.save(person);
        actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
        log.info("Person with name " + person.getName() + " was successfully added");
    }

    @FXML
    private void actorActorActionEvent(MouseEvent event) throws IOException {
        delPersonButton.setDisable(false);

        Person person = actorActorListBox.getSelectionModel().getSelectedItem();

        personNameTxt.setText(person.getName());
        secondNameTxt.setText(person.getSecond_name());
        nationalityTxt.setText(person.getNationality());
        birthDateTxt.setValue(person.getBirth_date());
        cityBirthTxt.setText(person.getCity_birth());
        countryBirthTxt.setText(person.getCountry_birth());


        delPersonButton.setOnMouseClicked(deleteEvent -> {
            if (personRepository.existsById(person.getId_person())) {
                personRepository.deleteById(person.getId_person());
                actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
                log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " was successfully deleted");
            }
        });

        updatePersonBtn.setOnMouseClicked(updateEvent -> {
            if (personRepository.existsById(person.getId_person())) {

                setPersonFields(person);

                personRepository.save(person);
                actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
                log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " was successfully updated");
            }
        });

    }

    private void setPersonFields(Person person) {
        person.setName(personNameTxt.getText());
        person.setSecond_name(secondNameTxt.getText());
        person.setNationality(nationalityTxt.getText());
        person.setBirth_date(birthDateTxt.getValue());
        person.setCity_birth(cityBirthTxt.getText());
        person.setCountry_birth(countryBirthTxt.getText());
        person.setPhoto_person(new File(actorImage.toString()));
    }

    @FXML
    private void actorClearAction(ActionEvent event) {
        personNameTxt.clear();
        secondNameTxt.clear();
        nationalityTxt.clear();
        birthDateTxt.setValue(LocalDate.now());
        cityBirthTxt.clear();
        countryBirthTxt.clear();

        actorImage.setImage(new Image("/images/defaultIcon.png"));
    }

//    @FXML
//    private void

    // + Movie management Bloc, and we continue with this badly made structure, but now i have no choice but making comments to separate all this code

    @FXML
    private JFXTextField movieEsTitleTxt, movieTitleTxt, movieDurationTxt;

    @FXML
    private JFXDatePicker movieReleaseTxt;

    @FXML
    private JFXTextArea movieDescTxt;

    @FXML
    private JFXButton movieImageBtn;

    @FXML
    private ImageView movieImage;

    @FXML
    private JFXListView<Movie> movieMovieListBox;

    @FXML
    private void addActionMovie(ActionEvent event) {
        Movie movie = new Movie();

        movie.setTitle(movieTitleTxt.getText());
        movie.setEs_title(movieEsTitleTxt.getText());
        movie.setRelease_date(movieReleaseTxt.getValue());
        movie.setDuration(movieDurationTxt.getText());
        movie.setSynopsis(movieDescTxt.getText());

        movieRepository.save(movie);
        movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
        log.info("Movie with title " + movie.getTitle() + " was successfully added");
    }

    @FXML
    private void movieClearAction(ActionEvent event) {
        movieTitleTxt.clear();
        movieEsTitleTxt.clear();
        movieReleaseTxt.setValue(LocalDate.now());
        movieDurationTxt.clear();
        movieDescTxt.clear();

        movieImage.setImage(new Image("/images/defaultIcon.png"));
    }

    // + Role management block

    @FXML
    private JFXListView<Roles> roleRoleListBox;

    // + there i have button events handlers, at this moment i wasnt able to implement this better, but i dont like it.

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

    @FXML
    private void imageAddAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);

        if (event.getSource() == actorImageBtn) {
            try {
                actorImage.setImage(new Image(file.toURI().toString()));
            } catch (NullPointerException e1) {
                System.err.println("The URI is null - Select image");
            }

        }
        if (event.getSource() == movieImageBtn) {
            try {
                movieImage.setImage(new Image(file.toURI().toString()));
            } catch (NullPointerException e1) {
                System.err.println("The URI is null - Select image");
            }
        }
    }

    // + - very strange realization of slider, and other functionality, like taking info from DB to listBox

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        movieReleaseTxt.setValue(LocalDate.now());
        birthDateTxt.setValue(LocalDate.now());

        actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));

        roleRoleListBox.setItems(FXCollections.observableArrayList(roleRepository.findAll()));

        movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));

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
