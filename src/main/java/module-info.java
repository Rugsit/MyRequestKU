module ku.cs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires bcrypt;
    requires java.management;
    requires jdk.compiler;
    requires java.naming;

    opens ku.cs.cs211671project to javafx.fxml;
    exports ku.cs.cs211671project;

    exports ku.cs.controllers;
    opens ku.cs.controllers to javafx.fxml;

    exports ku.cs.controllers.department;
    opens ku.cs.controllers.department to javafx.fxml;

    exports ku.cs.views.components;
    opens ku.cs.views.components to javafx.fxml;

    exports ku.cs.models.user;
    opens ku.cs.models.user to javafx.base;
    exports ku.cs.services;
    opens ku.cs.services to javafx.fxml;
    exports ku.cs.controllers.admin;
    opens ku.cs.controllers.admin to javafx.fxml;
    exports ku.cs.controllers.requests;
    opens ku.cs.controllers.requests to javafx.fxml;
    exports ku.cs.controllers.student;
    opens ku.cs.controllers.student to javafx.fxml;
    exports ku.cs.controllers.advisor;
    exports ku.cs.controllers.faculty;
    opens ku.cs.controllers.faculty to javafx.fxml;
    exports ku.cs.models.request;
    opens ku.cs.models.request to javafx.base;
    exports ku.cs.models.department;
    opens ku.cs.models.department to javafx.fxml;
    exports ku.cs.models.faculty;
    opens ku.cs.models.faculty to javafx.fxml;
    exports ku.cs.models.request.approver;
    opens ku.cs.models.request.approver to java.base, javafx.fxml;
    opens ku.cs.controllers.advisor to javafx.base, javafx.fxml;
    exports ku.cs.controllers.requests.information;
    opens ku.cs.controllers.requests.information to javafx.base, javafx.fxml;
    exports ku.cs.controllers.requests.approver;
    opens ku.cs.controllers.requests.approver to javafx.fxml;
}