package com.home.controllers;

import com.home.entity.Role;
import com.home.entityList.RoleList;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class Controller implements Initializable {


    @FXML
    private JFXButton mngMovies, mngGenres, mngActors, mngRoles, homeButton, exit, closeMenu, openMenu, helpMenu;

    @FXML
    private AnchorPane menuList, helpMenuPane;

    @FXML
    private AnchorPane moviePane, genrePane, actorPane, rolePane, homePane;

    @FXML
    private AnchorPane modRelPane;

    @FXML
    private JFXButton backFromModRel, goToModRel, goToModRel1, goToModRel2, goToModRel3;

    @FXML
    private JFXListView<Role> roleRolesList;


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
        if (event.getSource() == goToModRel) {
            modRelPane.setVisible(true);
        } else if (event.getSource() == goToModRel1) {
            modRelPane.setVisible(true);
        } else if (event.getSource() == goToModRel2) {
            modRelPane.setVisible(true);
        } else if (event.getSource() == goToModRel3) {
            modRelPane.setVisible(true);
        } else {
            if (event.getSource() == backFromModRel) {
                modRelPane.setVisible(false);
            }
        }
    }



    private static List<Role> getRoleList() {

        RestTemplate restTemplate = new RestTemplate();

        RoleList response = restTemplate.getForObject(
                "http://localhost:4001/role/",
                RoleList.class);
        List<Role> roles = response.getRoles();

        return roles;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roleRolesList.setItems(FXCollections.observableArrayList(getRoleList()));

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
