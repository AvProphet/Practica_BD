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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    @FXML // * Set person method
    private void addActionActor(ActionEvent event) throws IOException {
        Person person = new Person();

        setPersonFields(person);

        personRepository.save(person);
        actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
        log.info("Person with name " + person.getName() + " was successfully added");
    }

    @FXML // * Update and delete actions on person
    private void actorActorActionEvent(MouseEvent event) {
        delPersonButton.setDisable(false);
        updatePersonBtn.setDisable(false);

        Person person = actorActorListBox.getSelectionModel().getSelectedItem();

        personNameTxt.setText(person.getName());
        secondNameTxt.setText(person.getSecond_name());
        nationalityTxt.setText(person.getNationality());
        birthDateTxt.setValue(person.getBirth_date());
        cityBirthTxt.setText(person.getCity_birth());
        countryBirthTxt.setText(person.getCountry_birth());
        actorImage.setImage(new Image("/images/" + person.getPhoto_person()));


        delPersonButton.setOnMouseClicked(deleteEvent -> {
            if (personRepository.existsById(person.getId_person())) {
                personRepository.deleteById(person.getId_person());
                actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
                log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " was successfully deleted");
            }
        });

        updatePersonBtn.setOnMouseClicked(updateEvent -> {
            if (personRepository.existsById(person.getId_person())) {

                try {
                    setPersonFields(person);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                personRepository.save(person);
                actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
                log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " was successfully updated");
            }
        });

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

    private void setPersonFields(Person person) throws IOException {

        person.setName(personNameTxt.getText());
        person.setSecond_name(secondNameTxt.getText());
        person.setNationality(nationalityTxt.getText());
        person.setBirth_date(birthDateTxt.getValue());
        person.setCity_birth(cityBirthTxt.getText());
        person.setCountry_birth(countryBirthTxt.getText());

        String fileName = fileUriExtraction(actorImage);

        person.setPhoto_person(fileName);
    }
//    @FXML
//    private void

    // + Movie management Bloc, and we continue with this badly made structure, now i have no choice but making comments to separate all this code

    @FXML
    private JFXTextField movieEsTitleTxt, movieTitleTxt, movieDurationTxt;

    @FXML
    private JFXDatePicker movieReleaseTxt;

    @FXML
    private JFXTextArea movieDescTxt;

    @FXML
    private JFXButton movieImageBtn, delMovieButton, updateMovieBtn;

    @FXML
    private ImageView movieImage;

    @FXML
    private JFXListView<Movie> movieMovieListBox;

    @FXML
    private void addActionMovie(ActionEvent event) throws IOException {
        Movie movie = new Movie();

        setMovieFields(movie);

        movieRepository.save(movie);
        movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
        log.info("Movie with title " + movie.getTitle() + " was successfully added");
    }

    @FXML
    private void movieMovieActionEvent(MouseEvent event) {
        delMovieButton.setDisable(false);
        updateMovieBtn.setDisable(false);

        Movie movie = movieMovieListBox.getSelectionModel().getSelectedItem();

        movieTitleTxt.setText(movie.getTitle());
        movieEsTitleTxt.setText(movie.getEs_title());
        movieReleaseTxt.setValue(movie.getRelease_date());
        movieDurationTxt.setText(movie.getDuration());
        movieDescTxt.setText(movie.getSynopsis());

        movieImage.setImage(new Image("/images/" + movie.getMovie_poster()));

        delMovieButton.setOnMouseClicked(deleteEvent -> {
            if (movieRepository.existsById(movie.getId_movie())) {
                movieRepository.deleteById(movie.getId_movie());
                movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
                log.info("Movie with ID " + movie.getId_movie() + " and title " + movie.getTitle() + " was successfully deleted");
            }
        });

        updateMovieBtn.setOnMouseClicked(updateEvent -> {
            if (movieRepository.existsById(movie.getId_movie())) {

                try {
                    setMovieFields(movie);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                movieRepository.save(movie);
                movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
                log.info("Movie with ID " + movie.getId_movie() + " and title " + movie.getTitle() + " was successfully updated");
            }
        });
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

    private void setMovieFields(Movie movie) throws IOException {
        movie.setTitle(movieTitleTxt.getText());
        movie.setEs_title(movieEsTitleTxt.getText());
        movie.setRelease_date(movieReleaseTxt.getValue());
        movie.setDuration(movieDurationTxt.getText());
        movie.setSynopsis(movieDescTxt.getText());

        String fileName = fileUriExtraction(movieImage);

        movie.setMovie_poster(fileName);
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
    private void imageAddAction(ActionEvent event) throws URISyntaxException {
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg");
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(imageFilter);
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

    private String fileUriExtraction(ImageView movieImage) throws IOException {
        String fileName = movieImage.getImage().getUrl();
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        String fileURL = movieImage.getImage().getUrl();
        fileURL = fileURL.substring(fileURL.lastIndexOf(":") + 1);

        Files.copy(Paths.get(fileURL), Paths.get("src/main/resources/images/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(fileURL), Paths.get("target/classes/images/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
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
