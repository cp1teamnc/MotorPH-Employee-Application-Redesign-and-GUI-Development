package org.example.motorphui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EmployeeDashboard {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private Button profile_button, attendance_button, viewsalary_button, leaveform_button;
    @FXML
    private Button logout_button;

    @FXML
    public void initialize() {
        loadView("employee_profile.fxml");
        setActiveButton(profile_button);
    }
    @FXML
    private void onProfileClicked() {
        loadView("employee_profile.fxml");
        setActiveButton(profile_button);
    }
    @FXML
    private void onAttendanceClicked() {
        loadView("employee_attendance.fxml");
        setActiveButton(attendance_button);
    }
    @FXML
    private void onViewSalaryClicked() {
        loadView("employee_view_salary.fxml");
        setActiveButton(viewsalary_button);
    }
    @FXML
    private void onLeaveFormClicked() {
        loadView("employee_leave_form.fxml");
        setActiveButton(leaveform_button);
    }

    private void loadView(String fxml) {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource(fxml));
            contentPane.getChildren().setAll(pane);

            // Anchor the pane to all sides of contentPane to fill it entirely
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadProfile(Employee employee) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("employee_profile.fxml"));
            Parent root = loader.load();
            EmployeeProfile profileController = loader.getController();
            profileController.setEmployeeData(employee);

            contentPane.getChildren().setAll(root);

            AnchorPane.setTopAnchor(root, 0.0);
            AnchorPane.setBottomAnchor(root, 0.0);
            AnchorPane.setLeftAnchor(root, 0.0);
            AnchorPane.setRightAnchor(root, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button active) {
        profile_button.getStyleClass().remove("menu-button-active");
        attendance_button.getStyleClass().remove("menu-button-active");
        viewsalary_button.getStyleClass().remove("menu-button-active");
        leaveform_button.getStyleClass().remove("menu-button-active");
        logout_button.getStyleClass().remove("menu-button-active");

        if (!active.getStyleClass().contains("menu-button-active")) {
            active.getStyleClass().add("menu-button-active");
        }
    }

    @FXML
    private void onLogoutClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("landing_page.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logout_button.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
