package com.company.interview.javafx.controllers;

import com.company.interview.javafx.entities.FxEmployee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class FxEmployeeController {

    @FXML
    private TableView<FxEmployee> tableView;

    @FXML
    private VBox dataContainer;

    @Value("${employeesServiceUrl}")
    private String employeesServiceUrl;

    private final ObservableList<FxEmployee> employees = FXCollections.observableArrayList();

    @FXML
    private void initialize() throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(employeesServiceUrl, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(Objects.requireNonNull(response.getBody()));
        ArrayNode content = (ArrayNode) root.path("content");

        for (JsonNode node : content) {
            FxEmployee employee = new FxEmployee(Integer.parseInt(String.valueOf(node.get("id"))), String.valueOf(node.get("name")));
            employees.add(employee);
        }
        tableView = new TableView<>(FXCollections.observableList(employees));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<FxEmployee, SimpleIntegerProperty> id = new TableColumn<>("ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<FxEmployee, SimpleStringProperty> name = new TableColumn<>("NAME");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        ObservableList<TableColumn<FxEmployee, ?>> tableColumns = tableView.getColumns();
        tableColumns.add(id);
        tableColumns.add(name);

        dataContainer.getChildren().add(tableView);
    }

}