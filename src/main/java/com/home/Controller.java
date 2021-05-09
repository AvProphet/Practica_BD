package com.home;

import com.home.entity.*;
import com.home.exception.NullFieldsException;
import com.home.exception.TooShortNameException;
import com.home.repository.*;
import com.jfoenix.controls.*;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

@Slf4j
@Component
public class Controller implements Initializable {

    // + - IMPORTANT, I'M USING HIGHLIGHTED COMMENTS PLUGIN (COMMENTED HIGHLIGHTER)

    private Person person = new Person();

    private Movie movie = new Movie();

    private Roles roles = new Roles();

    private Genre genre = new Genre();

    private final RoleRepository roleRepository;

    private final PersonRepository personRepository;

    private final GenreRepository genreRepository;

    private final MovieRepository movieRepository;

    private final ParticipateRepository participateRepository;

    private final MovieGenRepository movieGenRepository;

    public Controller(RoleRepository roleRepository, PersonRepository personRepository, GenreRepository genreRepository, MovieRepository movieRepository, ParticipateRepository participateRepository, MovieGenRepository movieGenRepository) {
        this.roleRepository = roleRepository;
        this.personRepository = personRepository;
        this.genreRepository = genreRepository;
        this.movieRepository = movieRepository;
        this.participateRepository = participateRepository;
        this.movieGenRepository = movieGenRepository;
    }

    @FXML // + Menu Block
    private JFXButton mngMovies, mngGenres, mngActors, mngRoles, homeButton, exit, closeMenu, openMenu, helpMenu;

    @FXML // + Menu Block - slider menu
    private AnchorPane menuList, helpMenuPane;

    @FXML // + Main Pane Block
    private AnchorPane moviePane, genrePane, actorPane, rolePane, homePane;

    @FXML // + Additional Relationship modify block
    private AnchorPane modActorRelPane, modMovieRelPane;

    @FXML // + goTo or BackFrom add. rel. block
    private JFXButton backFromActorModRel, backFromMovieModRel;

    // + - Actor Management Block, i have no idea why i didnt make it in different fxml files, too bad.
    @FXML
    private JFXTextField nationalityTxt, cityBirthTxt, countryBirthTxt, secondNameTxt, personNameTxt;

    @FXML
    private JFXDatePicker birthDateTxt;

    @FXML
    private JFXButton actorImageBtn, delPersonButton, updatePersonBtn, actorModRel;

    @FXML
    private ImageView actorImage;

    @FXML
    private JFXListView<Person> actorActorListBox;

    @FXML
    private JFXListView<Movie> actorMovieListBox;

    @FXML
    private JFXListView<Roles> actorRolesListBox;

    // + Set person method --------------------------------------------------------------------
    @FXML
    private void addActionActor(ActionEvent event) throws IOException {
        Person person = new Person();

        setPersonFields(person);

        if (personExceptionHandler(person)) return;

        personRepository.save(person);

        updateForActors();

        log.info("Person with name " + person.getName() + " was successfully added");
    }

    // + - updating all the data
    private void updateForActors() {
        actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));
        actorRolesListBox.getItems().clear();
        actorMovieListBox.getItems().clear();

        delPersonButton.setDisable(true);
        updatePersonBtn.setDisable(true);
        actorModRel.setDisable(true);
    }

    // + - Checking for null method
    private boolean personExceptionHandler(Person person) {
        try {
            if ((person.getName().trim().length() | person.getSecond_name().trim().length()) < 2) throw new TooShortNameException();
            else if ((person.getNationality().trim().length() | person.getCity_birth().trim().length() | person.getCountry_birth().trim().length()) == 0)
                throw new NullFieldsException();
        } catch (TooShortNameException e) {
            updateForActors();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The  Name or Second name may contain at least 2 characters");

            alert.showAndWait();
            return true;
        } catch (NullFieldsException e) {
            updateForActors();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You can't have fields with no data");

            alert.showAndWait();
            return true;
        }
        return false;
    }

    // + Update and delete actions on person --------------------------------------------------
    @FXML
    private void actorActorActionEvent(MouseEvent event) {

        delPersonButton.setDisable(false);
        updatePersonBtn.setDisable(false);
        actorModRel.setDisable(false);

        person = actorActorListBox.getSelectionModel().getSelectedItem();

        actorMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
        actorRolesListBox.getItems().clear();

        log.info("yo have selected person with ID - " + person.getId_person());

        personNameTxt.setText(person.getName());
        secondNameTxt.setText(person.getSecond_name());
        nationalityTxt.setText(person.getNationality());
        birthDateTxt.setValue(person.getBirth_date());
        cityBirthTxt.setText(person.getCity_birth());
        countryBirthTxt.setText(person.getCountry_birth());

        actorImage.setImage(new Image("/images/" + person.getPhoto_person()));

        // + - delete button for actor
        delPersonButton.setOnMouseClicked(deleteEvent -> {

            if (personRepository.existsById(person.getId_person())) {

                personRepository.deleteById(person.getId_person());

                updateForActors();

                log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " was successfully deleted");
            }
        });

        // + - update button for actor
        updatePersonBtn.setOnMouseClicked(updateEvent -> {
            if (personRepository.existsById(person.getId_person())) {

                try {
                    setPersonFields(person);

                    if (personExceptionHandler(person)) return;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                personRepository.save(person);

                updateForActors();

                log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " was successfully updated");
            }
        });
    }

    // + Finding persons with the roles in the selected movie
    @FXML
    private void actorMovieActionEvent(MouseEvent event) {

        movie = actorMovieListBox.getSelectionModel().getSelectedItem();
        actorRolesListBox.setItems(FXCollections.observableArrayList(roleRepository.findRolesByMovie(movie.getId_movie(), person.getId_person())));
        log.info("You have selected movie with ID " + movie.getId_movie());

    }

    // + Clearing all the fields in actorPane
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

    // + Movie management Bloc, and we continue with this badly made structure, now i have no choice but making comments to separate all this code

    @FXML
    private JFXTextField movieEsTitleTxt, movieTitleTxt, movieDurationTxt;

    @FXML
    private JFXDatePicker movieReleaseTxt;

    @FXML
    private JFXTextArea movieDescTxt;

    @FXML
    private JFXButton movieImageBtn, delMovieButton, updateMovieBtn, movieModRel;

    @FXML
    private ImageView movieImage;

    @FXML
    private JFXListView<Movie> movieMovieListBox;

    @FXML
    private JFXListView<Genre> movieGenreListBox;

    @FXML
    private JFXListView<Person> moviePersonListBox;

    @FXML
    private JFXListView<Roles> movieRolesListBox;

    // + adding new movie -----------------------------------------------------------------------
    @FXML
    private void addActionMovie(ActionEvent event) throws IOException {
        Movie movie = new Movie();

        setMovieFields(movie);

        if (movieExceptionHandler(movie)) return;

        if (uniqueMovieTitleCheck(movie)) return;

        updateForMovie();

        log.info("Movie with title " + movie.getTitle() + " was successfully added");
    }

    private void updateForMovie() {
        movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
        movieRolesListBox.getItems().clear();
        moviePersonListBox.getItems().clear();
        movieGenreListBox.getItems().clear();

        delMovieButton.setDisable(true);
        updateMovieBtn.setDisable(true);
        movieModRel.setDisable(true);
    }

    // + method for some actions you can make with movie, deleting, updating, selection of object, searching for the related things

    @FXML
    private void movieMovieActionEvent(MouseEvent event) {
        delMovieButton.setDisable(false);
        updateMovieBtn.setDisable(false);
        movieModRel.setDisable(false);

        movie = movieMovieListBox.getSelectionModel().getSelectedItem();

        movieGenreListBox.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));
        moviePersonListBox.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(movie.getId_movie())));
        movieRolesListBox.getItems().clear();

        movieTitleTxt.setText(movie.getTitle());
        movieEsTitleTxt.setText(movie.getEs_title());
        movieReleaseTxt.setValue(movie.getRelease_date());
        movieDurationTxt.setText(movie.getDuration());
        movieDescTxt.setText(movie.getSynopsis());

        movieImage.setImage(new Image("/images/" + movie.getMovie_poster()));

        // + deleting selected movie
        delMovieButton.setOnMouseClicked(deleteEvent -> {
            if (movieRepository.existsById(movie.getId_movie())) {

                movieRepository.deleteById(movie.getId_movie());

                updateForMovie();

                log.info("Movie with ID " + movie.getId_movie() + " and title " + movie.getTitle() + " was successfully deleted");

            }
        });

        // + updating selected movie
        updateMovieBtn.setOnMouseClicked(updateEvent -> {
            if (movieRepository.existsById(movie.getId_movie())) {

                try {
                    setMovieFields(movie);

                    if (movieExceptionHandler(movie)) return;

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (uniqueMovieTitleCheck(movie)) return;

                updateForMovie();

                log.info("Movie with ID " + movie.getId_movie() + " and title " + movie.getTitle() + " was successfully updated");

            }
        });
    }

    // + - checking title for unique name
    private boolean uniqueMovieTitleCheck(Movie movie) {
        try {
            movieRepository.save(movie);
        } catch (Exception e) {
            updateForMovie();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("The title is a unique valor");

            alert.showAndWait();
            return true;
        }
        return false;
    }

    // + - checking fields for null or too short names
    private boolean movieExceptionHandler(Movie movie) {
        try {
            if ((movie.getTitle().trim().length() | movie.getEs_title().trim().length()) < 2) throw new TooShortNameException();
            else if ((movie.getDuration().trim().length() | movie.getSynopsis().trim().length()) == 0)
                throw new NullFieldsException();
        } catch (TooShortNameException e) {
            updateForMovie();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Titles may contain at least 2 characters");

            alert.showAndWait();
            return true;
        } catch (NullFieldsException e) {
            updateForMovie();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You can't have fields with no data");

            alert.showAndWait();
            return true;
        }
        return false;
    }

    // + - setting for the roles of person in the selected movie
    @FXML
    private void movieActorActionEvent(MouseEvent event) {

        person = moviePersonListBox.getSelectionModel().getSelectedItem();
        movieRolesListBox.setItems(FXCollections.observableArrayList(roleRepository.findRolesByMovie(movie.getId_movie(), person.getId_person())));
        log.info("You have selected person with ID " + person.getId_person());

    }

    // + - clearing movie fields
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

    // + Role management block ----------------------------------------------------------------------------------

    @FXML
    private JFXListView<Roles> roleRoleListBox;

    @FXML
    private JFXListView<Movie> roleMovieListBox;

    @FXML
    private JFXListView<Person> roleActorListBox;

    @FXML
    private JFXButton updateRoleButton, deleteRoleButton, roleModButton;

    @FXML
    private JFXTextArea roleDescTxt;

    // + adding new role to DB
    @FXML
    private void roleAddAction(ActionEvent event) {
        Roles role = new Roles();

        if (uniqueRolesDescCheck(role)) return;

        updateForRole();

        log.info("Genre with description " + role.getDesc_role() + " was successfully added");
    }

    // + same method as for person and movie --------------------------------------------------------------------
    @FXML
    private void roleRoleActionEvent(MouseEvent event) {
        deleteRoleButton.setDisable(false);
        updateRoleButton.setDisable(false);
        roleModButton.setDisable(false);

        Roles roles = roleRoleListBox.getSelectionModel().getSelectedItem();

        log.info("You have selected role with ID " + roles.getId_role() + " and description " + roles.getDesc_role());

        roleDescTxt.setText(roles.getDesc_role());

        roleActorListBox.setItems(FXCollections.observableArrayList(personRepository.findPersonByRole(roles.getId_role())));

        deleteRoleButton.setOnMouseClicked(deleteButton -> {
            if (roleRepository.existsById(roles.getId_role())) {
                roleRepository.deleteById(roles.getId_role());

                updateForRole();

                log.info("Role with ID " + roles.getId_role() + " and description " + roles.getDesc_role() + " was successfully deleted");
            }
        });

        // + - update button for roles
        updateRoleButton.setOnMouseClicked(updateButton -> {
            if (roleRepository.existsById(roles.getId_role())) {

                if (uniqueRolesDescCheck(roles)) return;

                updateForRole();

                log.info("Genre with description " + roles.getDesc_role() + " was successfully updated");
            }
        });
    }

    private void updateForRole() {
        roleRoleListBox.setItems(FXCollections.observableArrayList(roleRepository.findAll()));

        deleteRoleButton.setDisable(true);
        updateRoleButton.setDisable(true);
        roleModButton.setDisable(true);
    }


    // + - checking for unique and null fields description
    private boolean uniqueRolesDescCheck(Roles roles) {
        roles.setDesc_role(roleDescTxt.getText());

        try {
            if (roles.getDesc_role().trim().length() < 3) throw new NullFieldsException();
            roleRepository.save(roles);
        } catch (NullFieldsException e) {
            updateForRole();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You must have at least 3 characters in description");

            alert.showAndWait();
            return true;

        } catch (Exception e) {
            updateForRole();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Description is a unique valor");

            alert.showAndWait();
            return true;
        }
        return false;
    }

    // + - setting up movies by roles
    @FXML
    private void rolePersonAction(MouseEvent event) {

        person = roleActorListBox.getSelectionModel().getSelectedItem();
        roleMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
        log.info("You have selected person with ID " + person.getId_person());
    }

    // + - as we have same functions in another method, i'd made to user go to Person page to made modification instead of making same thing for this window
    @FXML
    private void goToPerson(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modification info");
        alert.setHeaderText("For modification, you'd better to go to Person page");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            rolePane.setVisible(false);
            actorPane.setVisible(true);
            for (int i = 0; i < actorActorListBox.getItems().size(); i++) {
                if (actorActorListBox.getSelectionModel().isSelected(i)) {
                    actorRoleModRelList.getItems().clear();
                }
            }
        } else {
            alert.close();
        }
    }

    // + - clearing role fields
    @FXML
    private void clearRoleAction(ActionEvent event) {
        roleDescTxt.clear();
    }

    // + Genre management block -----------------------------------------------------------------------------------

    @FXML
    private JFXButton updateGenreButton, delGenreButton, genreModRel;

    @FXML
    private JFXListView<Genre> genreGenreListBox;

    @FXML
    private JFXListView<Movie> genreMovieListBox;

    @FXML
    private JFXTextArea genreDescTxt;

    // + - adding new genre to DB ---------------------------------------------------------------------------------
    @FXML
    private void genreAddAction(ActionEvent event) {
        Genre genre = new Genre();

        if (uniqueGenreDescCheck(genre)) return;

        updateForGenre();

        log.info("Genre with description " + genre.getDesc_genre() + " was successfully added");
    }

    private void updateForGenre() {
        genreGenreListBox.setItems(FXCollections.observableArrayList(genreRepository.findAll()));
        genreMovieListBox.getItems().clear();

        delGenreButton.setDisable(true);
        updateGenreButton.setDisable(true);
        genreModRel.setDisable(true);
    }

    // + - same method as for person, movie and roles -------------------------------------------------------------
    @FXML
    private void genreGenreActionEvent(MouseEvent event) {
        delGenreButton.setDisable(false);
        updateGenreButton.setDisable(false);
        genreModRel.setDisable(false);

        Genre genre = genreGenreListBox.getSelectionModel().getSelectedItem();

        genreMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByGenre(genre.getId_genre())));

        genreDescTxt.setText(genre.getDesc_genre());

        // + - delete button for genres
        delGenreButton.setOnMouseClicked(deleteButton -> {
            if (genreRepository.existsById(genre.getId_genre())) {

                genreRepository.deleteById(genre.getId_genre());

                updateForGenre();

                log.info("Genre with ID " + genre.getId_genre() + " and description " + genre.getDesc_genre() + " was successfully deleted");
            }
        });


        // + - update button for genres
        updateGenreButton.setOnMouseClicked(updateButton -> {
            if (genreRepository.existsById(genre.getId_genre())) {

                if (uniqueGenreDescCheck(genre)) return;

                updateForGenre();

                log.info("Genre with description " + genre.getDesc_genre() + " was successfully updated");
            }
        });
    }

    // + - checking for null or unique field of description
    private boolean uniqueGenreDescCheck(Genre genre) {
        genre.setDesc_genre(genreDescTxt.getText());

        try {
            if (genre.getDesc_genre().trim().length() < 3) throw new NullFieldsException();
            genreRepository.save(genre);
        } catch (NullFieldsException e) {
            updateForGenre();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("You must have at least 3 characters in description");

            alert.showAndWait();
            return true;
        } catch (Exception e) {
            updateForGenre();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Description is a unique valor");

            alert.showAndWait();
            return true;
        }
        return false;
    }

    // + - Clearing fields of genres
    @FXML
    private void genreClearAction(ActionEvent event) {
        genreDescTxt.clear();
    }

    // + - same as for roles, but for genres, instead of person window, we are going to movie window
    @FXML
    private void goToMovie(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Modification info");
        alert.setHeaderText("For modification, you'd better to go to Movie page");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            genrePane.setVisible(false);
            moviePane.setVisible(true);
            for (int i = 0; i < movieMovieListBox.getItems().size(); i++) {
                if (movieMovieListBox.getSelectionModel().isSelected(i)) {
                    movieGenreModRelList.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));
                }
            }
        } else {
            alert.close();
        }
    }

    // + Mod actors rel Block -----------------------------------------------------------------------------------

    @FXML
    private JFXListView<Movie> actorMovieModRelList;

    @FXML
    private JFXListView<Roles> actorRoleModRelList;

    @FXML
    private JFXListView<Movie> actorMovieParticipationList;

    @FXML
    private JFXListView<Roles> actorRolesParticipationList;

    @FXML
    private JFXButton addRelButtonActor, delRelButtonActor;

    // + checking and setting movie from movieListView to further adding relationships
    @FXML
    private void actorMovieModRelListAction(MouseEvent event) {

        movie = actorMovieModRelList.getSelectionModel().getSelectedItem();

        checkActorRelSelection();

        log.info("yo have selected movie with ID - " + movie.getId_movie());
    }

    // + checking and setting role from rolesListView to further adding relationships
    @FXML
    private void actorRoleModRelListAction(MouseEvent event) {

        roles = actorRoleModRelList.getSelectionModel().getSelectedItem();

        checkActorRelSelection();

        log.info("yo have selected role with ID - " + roles.getId_role());
    }

    // + opening a window where i can modify all the relationships of selected previously person, add movie and role

    @FXML
    private void startActorModRel(ActionEvent event) {
        modActorRelPane.setVisible(true);

        // + updating data in listView
        actorMovieModRelList.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
        actorRoleModRelList.setItems(FXCollections.observableArrayList(roleRepository.findAll()));

        // + updating data in listView
        actorMovieParticipationList.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
        actorRolesParticipationList.getItems().clear();
        actorMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));

        // + setting movie from selected object of lisView and updating existing roles in this relationship
        actorMovieParticipationList.setOnMouseClicked(event1 -> {
            movie = actorMovieParticipationList.getSelectionModel().getSelectedItem();

            checkActorParticipationRelSelection();

            log.info("You have selected movie with ID - " + movie.getId_movie());
            actorRolesParticipationList.setItems(FXCollections.observableArrayList(roleRepository.findRolesByMovie(movie.getId_movie(), person.getId_person())));
            delRelButtonActor.setDisable(true);
        });
        // + setting role from selected object of lisView
        actorRolesParticipationList.setOnMouseClicked(event1 -> {
            roles = actorRolesParticipationList.getSelectionModel().getSelectedItem();

            checkActorParticipationRelSelection();

            log.info("You have selected role with ID - " + roles.getId_role());
        });
        // + add new rel. button
        addRelButtonActor.setOnMouseClicked(addRelButtonActorEvent -> {
            participateRepository.save(new Participate(new ParticipateKey(person.getId_person(), movie.getId_movie(), roles.getId_role()), movie, person,
                    roles));

            actorMovieParticipationList.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
            actorRolesParticipationList.getItems().clear();

            log.info("Person with ID " + person.getId_person() + " and name " + person.getName() + " now participate in movie " +
                    movie.getTitle() + " with role " + roles.getDesc_role());
        });
        // + del rel. button
        delRelButtonActor.setOnMouseClicked(delRelButtonActorEvent -> {
            participateRepository.deleteParticipate(person.getId_person(), movie.getId_movie(), roles.getId_role());

            actorMovieParticipationList.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
            actorRolesParticipationList.setItems(FXCollections.observableArrayList(roleRepository.findRolesByMovie(movie.getId_movie(), person.getId_person())));

            log.info("Participation of person " + person.getName() + " was deleted");
        });
    }

    // + i don't know how to make deselection from listView, so i've made this two methods to check selection of objects in lists, so when i select another
    // + list, i will update all the data of other lists in window, so they can be deselected, also i will deactivate delete button till client has selected
    // + role and movie he wants to delete from bottom lists

    private void checkActorParticipationRelSelection() {
        for (int i = 0; i < actorMovieModRelList.getItems().size(); i++) {
            if (actorMovieParticipationList.getSelectionModel().isSelected(i)) {

                actorMovieModRelList.setItems(FXCollections.observableArrayList(movieRepository.findAll()));
                actorRoleModRelList.setItems(FXCollections.observableArrayList(roleRepository.findAll()));
            }
            if (actorRolesParticipationList.getSelectionModel().isSelected(i)) {
                delRelButtonActor.setDisable(false);
                addRelButtonActor.setDisable(true);
            }
        }
    }

    private void checkActorRelSelection() {
        for (int i = 0; i < actorMovieModRelList.getItems().size(); i++) {
            if (actorMovieModRelList.getSelectionModel().isSelected(i) || actorRoleModRelList.getSelectionModel().isSelected(i)) {

                actorMovieParticipationList.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
                actorRolesParticipationList.getItems().clear();

                delRelButtonActor.setDisable(true);
                addRelButtonActor.setDisable(false);
            }
        }
    }

    // + Mod actors rel Block -----------------------------------------------------------------------------------

    @FXML
    private JFXListView<Person> movieActorModRelList;

    @FXML
    private JFXListView<Roles> movieRoleModRelList;

    @FXML
    private JFXListView<Genre> movieGenreModRelList;

    @FXML
    private JFXListView<Person> movieActorParticipationList;

    @FXML
    private JFXListView<Roles> movieRoleParticipationList;

    @FXML
    private JFXListView<Genre> movieGenreParticipationList;

    @FXML
    private JFXButton addRelButtonMovie, delRelButtonMovie, delRelGenreButtonMovie, addRelGenreButtonMovie;

    // + checking and setting person from rolesListView to further adding relationships

    @FXML
    private void movieActorModRelListAction(MouseEvent event) {

        person = movieActorModRelList.getSelectionModel().getSelectedItem();

        checkMovieRelSelection();

        log.info("yo have selected movie with ID - " + person.getId_person());
    }

    // + checking and setting role from rolesListView to further adding relationships

    @FXML
    private void movieRoleModRelListAction(MouseEvent event) {

        roles = movieRoleModRelList.getSelectionModel().getSelectedItem();

        checkMovieRelSelection();

        log.info("yo have selected role with ID - " + roles.getId_role());
    }

    // + checking and setting genre from rolesListView to further adding relationships

    @FXML
    private void movieGenreModRelListAction(MouseEvent event) {

        genre = movieGenreModRelList.getSelectionModel().getSelectedItem();

        checkMovieRelSelectionForGenre();

        log.info("yo have selected genre with ID - " + genre.getId_genre());
    }

    @FXML
    private void startMovieModRel(ActionEvent event) {
        modMovieRelPane.setVisible(true);

        // + updating data in listView
        movieActorModRelList.setItems(FXCollections.observableArrayList(personRepository.findAll()));
        movieRoleModRelList.setItems(FXCollections.observableArrayList(roleRepository.findAll()));
        movieGenreModRelList.setItems(FXCollections.observableArrayList(genreRepository.findAll()));

        // + updating data in listView
        movieActorParticipationList.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(movie.getId_movie())));
        movieRoleParticipationList.getItems().clear();
        moviePersonListBox.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(movie.getId_movie())));
        movieGenreParticipationList.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));

        // + setting person from selected object of lisView and updating existing roles in this relationship
        movieActorParticipationList.setOnMouseClicked(event1 -> {
            person = movieActorParticipationList.getSelectionModel().getSelectedItem();

            log.info("You have selected person with ID - " + person.getId_person() + " and name " + person.getName());
            movieRoleParticipationList.setItems(FXCollections.observableArrayList(roleRepository.findRolesByMovie(movie.getId_movie(), person.getId_person())));

            checkMovieParticipationRelSelection();

            delRelButtonMovie.setDisable(true);
        });
        // + setting role from selected object of lisView
        movieRoleParticipationList.setOnMouseClicked(event1 -> {
            roles = movieRoleParticipationList.getSelectionModel().getSelectedItem();

            checkMovieParticipationRelSelection();

            log.info("You have selected role with ID - " + roles.getId_role());
        });
        // + setting genre from selected object of lisView
        movieGenreParticipationList.setOnMouseClicked(event1 -> {
            genre = movieGenreModRelList.getSelectionModel().getSelectedItem();

            checkMovieParticipationRelSelectionForGenre();

            log.info("You have selected genre with ID - " + genre.getId_genre());
        });

        // + add Person rel. button
        addRelButtonMovie.setOnMouseClicked(addRelButtonActorEvent -> {
            participateRepository.save(new Participate(new ParticipateKey(person.getId_person(), movie.getId_movie(), roles.getId_role()), movie, person,
                    roles));

            movieActorParticipationList.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(movie.getId_movie())));
            movieRoleParticipationList.getItems().clear();

            log.info("Movie with ID " + movie.getId_movie() + " and title " + movie.getId_movie() + " now participate in movie " +
                    movie.getTitle() + " with role " + roles.getDesc_role());
        });

        // + add Genre rel. button
        addRelGenreButtonMovie.setOnMouseClicked(addRelGenreButtonMovieEvent -> {
            movieGenRepository.save(new MovieGen(new MovieGenKey(genre.getId_genre(), movie.getId_movie()), movie, genre));

            movieGenreParticipationList.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));
            log.info("Movie with ID " + movie.getId_movie() + " and title " + movie.getId_movie() + " now have genre " +
                    genre.getDesc_genre() + " with ID " + genre.getId_genre());
        });
        // + del person rel. button
        delRelButtonMovie.setOnMouseClicked(delRelButtonActorEvent -> {
            participateRepository.deleteParticipate(person.getId_person(), movie.getId_movie(), roles.getId_role());

            movieActorParticipationList.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(person.getId_person())));
            movieRoleParticipationList.setItems(FXCollections.observableArrayList(roleRepository.findRolesByMovie(movie.getId_movie(), person.getId_person())));

            log.info("Participation of person " + person.getName() + " was deleted");
        });
        // + del genre rel. button
        delRelGenreButtonMovie.setOnMouseClicked(delRelGenreButtonMovieEvent -> {
            movieGenRepository.deleteMovieGen(movie.getId_movie(), genre.getId_genre());

            movieGenreParticipationList.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));

            log.info("Genre with desc" + genre.getDesc_genre() + " was deleted from movie " + movie.getTitle() + " successfully");
        });
    }

    // + Same methods as for a person mod. rel. block, deselection and blocking buttons by updating listView data

    private void checkMovieParticipationRelSelection() {
        for (int i = 0; i < movieActorModRelList.getItems().size() + movieRoleModRelList.getItems().size(); i++) {
            if (movieActorParticipationList.getSelectionModel().isSelected(i)) {

                movieActorModRelList.setItems(FXCollections.observableArrayList(personRepository.findAll()));
                movieRoleModRelList.setItems(FXCollections.observableArrayList(roleRepository.findAll()));

                addRelButtonMovie.setDisable(true);
            }
            if (movieRoleParticipationList.getSelectionModel().isSelected(i)) {

                delRelButtonMovie.setDisable(false);
            }
        }
    }

    private void checkMovieRelSelection() {
        for (int i = 0; i < movieActorModRelList.getItems().size() + movieRoleModRelList.getItems().size(); i++) {
            if (movieActorModRelList.getSelectionModel().isSelected(i) || movieRoleModRelList.getSelectionModel().isSelected(i)) {

                movieActorParticipationList.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(movie.getId_movie())));
                movieRoleParticipationList.getItems().clear();

                delRelButtonMovie.setDisable(true);
            }
            for (int j = 0; j < movieActorModRelList.getItems().size() + movieRoleModRelList.getItems().size(); j++) {
                if (movieActorModRelList.getSelectionModel().isSelected(i) && movieRoleModRelList.getSelectionModel().isSelected(j)) {
                    addRelButtonMovie.setDisable(false);
                }
            }
        }
    }

    private void checkMovieRelSelectionForGenre() {
        for (int i = 0; i < movieActorModRelList.getItems().size() + movieRoleModRelList.getItems().size(); i++) {
            if (movieGenreModRelList.getSelectionModel().isSelected(i)) {

                movieGenreParticipationList.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));

                delRelGenreButtonMovie.setDisable(true);
                addRelGenreButtonMovie.setDisable(false);
            }
        }
    }

    private void checkMovieParticipationRelSelectionForGenre() {
        for (int i = 0; i < movieActorModRelList.getItems().size() + movieRoleModRelList.getItems().size(); i++) {
            if (movieGenreParticipationList.getSelectionModel().isSelected(i)) {

                movieGenreModRelList.setItems(FXCollections.observableArrayList(genreRepository.findAll()));

                addRelGenreButtonMovie.setDisable(true);
                delRelGenreButtonMovie.setDisable(false);
            }
        }
    }

    // + there i have button events handlers, at this moment i wasn't able to implement this better, but i dont like it.

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == mngMovies) {

            moviePane.setVisible(true);

            genrePane.setVisible(false);
            actorPane.setVisible(false);
            rolePane.setVisible(false);
            homePane.setVisible(false);

            movieGenreListBox.getItems().clear();
            moviePersonListBox.getItems().clear();
            movieRolesListBox.getItems().clear();
            movieMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findAll()));

        } else if (event.getSource() == mngGenres) {

            genrePane.setVisible(true);

            moviePane.setVisible(false);
            actorPane.setVisible(false);
            rolePane.setVisible(false);
            homePane.setVisible(false);

            genreMovieListBox.getItems().clear();
            genreGenreListBox.setItems(FXCollections.observableArrayList(genreRepository.findAll()));

        } else if (event.getSource() == mngActors) {

            actorPane.setVisible(true);

            moviePane.setVisible(false);
            genrePane.setVisible(false);
            rolePane.setVisible(false);
            homePane.setVisible(false);

            actorMovieListBox.getItems().clear();
            actorRolesListBox.getItems().clear();
            actorActorListBox.setItems(FXCollections.observableArrayList(personRepository.findAll()));

        } else if (event.getSource() == mngRoles) {

            //noinspection DuplicatedCode
            rolePane.setVisible(true);

            moviePane.setVisible(false);
            genrePane.setVisible(false);
            actorPane.setVisible(false);
            homePane.setVisible(false);

            roleMovieListBox.getItems().clear();
            roleActorListBox.getItems().clear();
            roleRoleListBox.setItems(FXCollections.observableArrayList(roleRepository.findAll()));


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

    // + help button, zero functions

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

    // + - Calling the modRelPanes

    @FXML
    private void handleButtonActionModRel(ActionEvent event) {
        if (event.getSource() == backFromActorModRel) {
            modActorRelPane.setVisible(false);
            actorMovieListBox.setItems(FXCollections.observableArrayList(movieRepository.findMoviesByPerson(person.getId_person())));
            actorRolesListBox.getItems().clear();
        }
        if (event.getSource() == backFromMovieModRel) {
            modMovieRelPane.setVisible(false);
            moviePersonListBox.setItems(FXCollections.observableArrayList(personRepository.findPersonByMovie(person.getId_person())));
            movieGenreListBox.setItems(FXCollections.observableArrayList(genreRepository.findGenreByMovie(movie.getId_movie())));
            movieRolesListBox.getItems().clear();
        }
    }

    // + - adding image to ImageView in actorPane or moviePane

    @FXML
    private void imageAddAction(ActionEvent event) {
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

    // + - Extraction of URI to further use within image setting in person or movie

    private String fileUriExtraction(ImageView movieImage) throws IOException {
        String fileName = movieImage.getImage().getUrl();
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);

        String fileURL = movieImage.getImage().getUrl();
        fileURL = fileURL.substring(fileURL.lastIndexOf(":") + 1);

        Files.copy(Paths.get(fileURL), Paths.get("src/main/resources/images/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(fileURL), Paths.get("target/classes/images/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }

    // + - i won't provide delete function to user :)
    @FXML
    private void deleteDbAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete DB Alert");
        alert.setHeaderText("Are you really want delete all the data?");
        alert.setContentText("Choose wisely");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Alert alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setTitle("Error Dialog");
            alert2.setHeaderText("You really wanted to do this?");
            alert2.setContentText("Well, you won't do this.");

            alert2.showAndWait();
        } else {
            alert.close();
        }
    }

    // + - very strange realization of slider, and other functionality, like taking info from DB to listBox

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        movieReleaseTxt.setValue(LocalDate.now());
        birthDateTxt.setValue(LocalDate.now());

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
