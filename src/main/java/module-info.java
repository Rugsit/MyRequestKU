module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires bcrypt;
    requires java.management;
    requires jdk.compiler;


    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;

    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;

    exports ku.cs.controllers.department;
    opens ku.cs.controllers.department to javafx.fxml;

    exports ku.cs.views.components;
    opens ku.cs.views.components to javafx.fxml;

    exports ku.cs.models;
    opens ku.cs.models to javafx.base;
    exports ku.cs.models.user;
    opens ku.cs.models.user to javafx.base;
}