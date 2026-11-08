package com.attendance.studentattendancesystem;

import com.attendance.studentattendancesystem.dao.AttendanceDAO;
import com.attendance.studentattendancesystem.dao.ClassSessionDAO;
import com.attendance.studentattendancesystem.dao.CourseDAO;
import com.attendance.studentattendancesystem.dao.StudentDAO;
import com.attendance.studentattendancesystem.model.Attendance;
import com.attendance.studentattendancesystem.i18n.I18n;
import com.attendance.studentattendancesystem.i18n.Language;
import com.attendance.studentattendancesystem.model.PeriodCell;
import com.attendance.studentattendancesystem.model.StudentAttendanceRow;
import com.attendance.studentattendancesystem.util.AttendanceCalculator;
import com.attendance.studentattendancesystem.model.ClassSession;
import com.attendance.studentattendancesystem.model.Course;
import com.attendance.studentattendancesystem.model.Student;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

public class Main extends Application {
    private static final String NAVY = "#17375E";
    private static final String NAVY_DARK = "#102B4A";
    private static final String BLUE = "#1F6FE5";
    private static final String BLUE_LIGHT = "#EAF2FF";
    private static final String PURPLE = "#6A4CE0";
    private static final String GREEN = "#287D3C";
    private static final String RED = "#B42318";
    private static final String TEXT = "#1F3B64";
    private static final String MUTED = "#6B7A90";
    private static final String BORDER = "#DDE6F0";
    private static final String BACKGROUND = "#F4F7FB";

    public void start(Stage stage) {
        Image appIcon = this.loadAppIcon();
        if (appIcon != null) {
            stage.getIcons().add(appIcon);
        }
        Stage splashStage = this.createSplashStage();
        splashStage.show();
        ComboBox classesCourseComboBox = new ComboBox();
        ComboBox attendanceCourseComboBox = new ComboBox();
        ImageView logoImage = this.createLogoImage(34.0);
        StackPane logoLabel = new StackPane(logoImage);
        logoLabel.setMinSize(38.0, 38.0);
        logoLabel.setPrefSize(38.0, 38.0);
        logoLabel.setMaxSize(38.0, 38.0);
        logoLabel.setStyle("-fx-background-color: white;-fx-background-radius: 9;");
        Label title = new Label("Student Attendance System");
        title.setStyle("-fx-font-size: 18px;-fx-font-weight: bold;-fx-text-fill: #17375E;");
        Label subtitle = new Label("Manage Students  \u2022  Track Attendance");
        subtitle.setStyle("-fx-font-size: 11px;-fx-text-fill: #60789A;");
        VBox titleBox = new VBox(1.0, title, subtitle);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        HBox brandBox = new HBox(8.0, logoLabel, titleBox);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        Label dateIcon = new Label("\u25a3");
        dateIcon.setStyle("-fx-font-size: 17px;-fx-text-fill: #1F6FE5;");
        Label dateLabel = new Label();
        dateLabel.setStyle("-fx-font-size: 11px;-fx-text-fill: #1F3B64;-fx-font-weight: bold;");
        VBox dateBox = new VBox(1.0, dateLabel);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        HBox dateArea = new HBox(6.0, dateIcon, dateBox);
        dateArea.setAlignment(Pos.CENTER_LEFT);
        Label userIcon = new Label("\u25cf");
        userIcon.setStyle("-fx-font-size: 16px;-fx-text-fill: #557AA5;");
        Label welcomeLabel = new Label("Welcome\nAdministrator");
        welcomeLabel.setStyle("-fx-font-size: 11px;-fx-text-fill: #1F3B64;-fx-font-weight: bold;");
        HBox userArea = new HBox(7.0, userIcon, welcomeLabel);
        userArea.setAlignment(Pos.CENTER_LEFT);
        userArea.setPadding(new Insets(6.0, 10.0, 6.0, 9.0));
        userArea.setStyle("-fx-background-color: #EAF1FC;-fx-background-radius: 8;");
        Label languageLabel = new Label(I18n.t("lang.label"));
        languageLabel.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #526174;");
        ComboBox<Language> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll(Language.values());
        languageCombo.setValue(Language.ENGLISH);
        languageCombo.setPrefWidth(110.0);
        languageCombo.setStyle("-fx-background-color: #F7FAFF;-fx-border-color: #D6E1EE;-fx-border-radius: 14;-fx-background-radius: 14;-fx-font-size: 11px;-fx-padding: 2 6 2 6;");
        HBox languageBox = new HBox(6.0, languageLabel, languageCombo);
        languageBox.setAlignment(Pos.CENTER_LEFT);
        languageBox.setPadding(new Insets(4.0, 8.0, 4.0, 8.0));
        languageBox.setStyle("-fx-background-color: #EEF5FF;-fx-background-radius: 16;-fx-border-color: #D4E4F8;-fx-border-radius: 16;");
        Region headerSpacer = new Region();
        HBox.setHgrow((Node)headerSpacer, (Priority)Priority.ALWAYS);
        HBox topHeader = new HBox(12.0, brandBox, headerSpacer, languageBox, dateArea, userArea);
        topHeader.setAlignment(Pos.CENTER_LEFT);
        topHeader.setPadding(new Insets(8.0, 14.0, 8.0, 14.0));
        topHeader.setStyle("-fx-background-color: white;");
        Button dashboardButton = this.createNavButton("\u2302", I18n.t("nav.dashboard"));
        Button studentsButton = this.createNavButton("\u265f", I18n.t("nav.students"));
        Button attendanceButton = this.createNavButton("\u25a3", I18n.t("nav.attendance"));
        Button classesButton = this.createNavButton("\u25a4", I18n.t("nav.classes"));
        Button refreshButton = this.createNavButton("\u21bb", I18n.t("nav.refresh"));
        HBox topNavigation = new HBox(0.0);
        topNavigation.setAlignment(Pos.CENTER_LEFT);
        topNavigation.setPadding(new Insets(0.0, 6.0, 0.0, 6.0));
        topNavigation.setStyle("-fx-background-color: white;-fx-border-color: #DDE6F0;-fx-border-width: 0 0 1 0;");
        topNavigation.getChildren().addAll(dashboardButton, studentsButton, attendanceButton, classesButton, refreshButton);
        VBox contentArea = new VBox(12.0);
        contentArea.setPadding(new Insets(14.0, 16.0, 20.0, 16.0));
        contentArea.setStyle("-fx-background-color: #F4F7FB;");
        ScrollPane contentScroll = new ScrollPane((Node)contentArea);
        contentScroll.setFitToWidth(true);
        contentScroll.setPannable(true);
        contentScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contentScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        contentScroll.setStyle("-fx-background-color: #F4F7FB;-fx-background-insets: 0;");
        ImageView sideLogoImage = this.createLogoImage(38.0);
        StackPane sideBrandIcon = new StackPane(sideLogoImage);
        sideBrandIcon.setMinSize(42.0, 42.0);
        sideBrandIcon.setPrefSize(42.0, 42.0);
        sideBrandIcon.setMaxSize(42.0, 42.0);
        sideBrandIcon.setStyle("-fx-background-color: white;-fx-background-radius: 10;");
        Label sideBrand = new Label("Attendance\nSystem");
        sideBrand.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: white;");
        HBox sideLogo = new HBox(7.0, sideBrandIcon, sideBrand);
        sideLogo.setAlignment(Pos.CENTER_LEFT);
        sideLogo.setPadding(new Insets(17.0, 12.0, 18.0, 14.0));
        VBox sideMenu = new VBox(5.0);
        sideMenu.setPadding(new Insets(3.0, 8.0, 0.0, 8.0));
        Button sideDashboard = this.createSidebarButton("\u2302", I18n.t("nav.dashboard"));
        Button sideStudents = this.createSidebarButton("\u265f", I18n.t("nav.students"));
        Button sideAttendance = this.createSidebarButton("\u25a3", I18n.t("nav.attendance"));
        Button sideClasses = this.createSidebarButton("\u25a4", I18n.t("nav.classes"));
        Button sideRefresh = this.createSidebarButton("\u21bb", I18n.t("nav.refresh"));
        sideMenu.getChildren().addAll(sideDashboard, sideStudents, sideAttendance, sideClasses, sideRefresh);
        Region sideSpacer = new Region();
        VBox.setVgrow((Node)sideSpacer, (Priority)Priority.ALWAYS);
        Label sideQuote = new Label("Better Attendance\nBrighter Future");
        sideQuote.setStyle("-fx-font-size: 11px;-fx-font-style: italic;-fx-text-fill: #D7E3F1;-fx-alignment: center;");
        sideQuote.setTextAlignment(TextAlignment.CENTER);
        VBox sideBottom = new VBox(6.0, sideQuote);
        sideBottom.setAlignment(Pos.CENTER);
        sideBottom.setPadding(new Insets(10.0, 6.0, 20.0, 6.0));
        VBox sidebar = new VBox(sideLogo, sideMenu, sideSpacer, sideBottom);
        sidebar.setPrefWidth(175.0);
        sidebar.setMinWidth(175.0);
        sidebar.setMaxWidth(175.0);
        sidebar.setStyle("-fx-background-color: linear-gradient(to bottom, #17375E, #214B79);");
        Label dashboardPageTitle = this.pageTitle("Dashboard");
        Label dashboardPageSubtitle = this.pageSubtitle("Overview of students, courses, classes, and attendance");
        VBox dashboardHeader = this.pageHeader(dashboardPageTitle, dashboardPageSubtitle);
        Label studentsPageTitle = this.pageTitle("Student Management");
        Label studentsPageSubtitle = this.pageSubtitle("Manage student information, search records, and update student details");
        VBox studentsHeader = this.pageHeader(studentsPageTitle, studentsPageSubtitle);
        Label attendancePageTitle = this.pageTitle("Attendance Management");
        Label attendancePageSubtitle = this.pageSubtitle("Click week/period cells to mark attendance for the selected course");
        VBox attendanceHeader = this.pageHeader(attendancePageTitle, attendancePageSubtitle);
        Label classesPageTitle = this.pageTitle("Classes");
        Label classesPageSubtitle = this.pageSubtitle("Manage courses, schedule classes, and review class sessions");
        VBox classesHeader = this.pageHeader(classesPageTitle, classesPageSubtitle);
        TextField courseNameField = this.styledField("Enter course name", 150.0);
        TextField courseCodeField = this.styledField("Enter course code", 115.0);
        TextField teacherField = this.styledField("Enter teacher name", 145.0);
        TextField totalClassesField = this.styledField("e.g. 30", 75.0);
        Label courseNameLabel = this.fieldLabel("Course Name:");
        Label courseCodeLabel = this.fieldLabel("Course Code:");
        Label teacherLabel = this.fieldLabel("Teacher:");
        Label totalClassesLabel = this.fieldLabel("Total Classes:");
        Button addCourseButton = this.primaryButton("\u2295  Add Course", 110.0);
        Button editCourseButton = this.secondaryButton("\u270e  Edit Selected", 135.0);
        Button updateCourseButton = this.successButton("\u27f3  Update", 105.0);
        Button deleteCourseButton = this.dangerButton("\u25a3  Delete Selected", 145.0);
        TextField studentIdField = this.styledField("Enter student ID", 140.0);
        TextField studentNameField = this.styledField("Enter student name", 180.0);
        Label studentIdLabel = this.fieldLabel("Student ID:");
        Label studentNameLabel = this.fieldLabel("Student Name:");
        Button addStudentButton = this.primaryButton("\u2295  Add Student", 115.0);
        Button editStudentButton = this.secondaryButton("\u270e  Edit Selected", 140.0);
        Button updateStudentButton = this.successButton("\u27f3  Update", 105.0);
        Button deleteStudentButton = this.dangerButton("\u25a3  Delete Selected", 150.0);
        TextField weekField = this.styledField("e.g. 1", 65.0);
        TextField classNumberField = this.styledField("e.g. 1", 65.0);
        TextField dateField = this.styledField("YYYY-MM-DD", 115.0);
        Label weekLabel = this.fieldLabel("Week:");
        Label classNumberLabel = this.fieldLabel("Period (1-4):");
        Label classDateLabel = this.fieldLabel("Date:");
        Button addClassButton = this.purpleButton("\u2295  Add Class", 110.0);
        Button deleteClassButton = this.dangerButton("\u25a3  Delete Selected", 150.0);
        TableView studentTable = new TableView();
        TableColumn studentIdManagementColumn = new TableColumn("Student ID");
        studentIdManagementColumn.setCellValueFactory((Callback)new PropertyValueFactory("studentId"));
        TableColumn studentNameManagementColumn = new TableColumn("Student Name");
        studentNameManagementColumn.setCellValueFactory((Callback)new PropertyValueFactory("name"));
        studentTable.getColumns().addAll(studentIdManagementColumn, studentNameManagementColumn);
        studentTable.setPrefHeight(250.0);
        studentTable.setFixedCellSize(32.0);
        studentTable.setPlaceholder((Node)new Label("No student records found."));
        this.styleTable(studentTable);
        studentIdManagementColumn.setPrefWidth(160.0);
        studentNameManagementColumn.setPrefWidth(280.0);
        this.centerColumn(studentIdManagementColumn);
        this.centerColumn(studentNameManagementColumn);
        TableView classTable = new TableView();
        TableColumn classWeekColumn = new TableColumn("Week");
        classWeekColumn.setCellValueFactory((Callback)new PropertyValueFactory("weekNumber"));
        TableColumn classNumberColumn = new TableColumn("Period");
        classNumberColumn.setCellValueFactory((Callback)new PropertyValueFactory("classNumber"));
        TableColumn classDateColumn = new TableColumn("Date");
        classDateColumn.setCellValueFactory((Callback)new PropertyValueFactory("classDate"));
        classTable.getColumns().addAll(classWeekColumn, classNumberColumn, classDateColumn);
        classTable.setPrefHeight(270.0);
        classTable.setFixedCellSize(32.0);
        classTable.setPlaceholder((Node)new Label("No classes found.\n\nAdd a course and class to see the data here."));
        this.styleTable(classTable);
        classWeekColumn.setPrefWidth(150.0);
        classNumberColumn.setPrefWidth(150.0);
        classDateColumn.setPrefWidth(180.0);
        this.centerColumn(classWeekColumn);
        this.centerColumn(classNumberColumn);
        this.centerColumn(classDateColumn);
        TableView<StudentAttendanceRow> attendanceTable = new TableView<>();
        AtomicReference<List<Integer>> attendanceWeeks = new AtomicReference<>(List.of(1));
        attendanceTable.setPrefHeight(420.0);
        attendanceTable.setFixedCellSize(40.0);
        attendanceTable.setPlaceholder(new Label("Select a course to open the attendance grid.\nClick cells under W1/W2... P1-P4 to mark Present/Absent."));
        this.styleTable(attendanceTable);
        FlowPane studentForm = this.createFormFlowPane();
        studentForm.getChildren().addAll(this.formPair(studentIdLabel, (Control)studentIdField), this.formPair(studentNameLabel, (Control)studentNameField), addStudentButton);
        Label studentInformationTitle = this.cardTitle("Student Information");
        VBox studentFormCard = this.card(studentInformationTitle, studentForm);
        TextField studentSearchField = this.styledField("Search by ID or name", 220.0);
        Label studentSearchLabel = this.fieldLabel("Search:");
        Button loadStudentsButton = this.secondaryButton("\u21bb  Load Students", 125.0);
        FlowPane studentSearchRow = this.createFormFlowPane();
        studentSearchRow.getChildren().addAll(this.formPair(studentSearchLabel, (Control)studentSearchField), loadStudentsButton);
        FlowPane studentActionRow = this.createFormFlowPane();
        studentActionRow.getChildren().addAll(editStudentButton, updateStudentButton, deleteStudentButton);
        Label studentRecordsTitle = this.cardTitle("Student Records");
        Label studentRecordsSubtitle = this.cardSubtitle("Search and manage registered students.");
        VBox studentRecordsCard = this.card(studentRecordsTitle, studentRecordsSubtitle, studentSearchRow, studentActionRow, studentTable);
        this.configureCourseCombo((ComboBox<Course>)classesCourseComboBox);
        classesCourseComboBox.setPrefWidth(210.0);
        Label courseSelectorLabel = this.fieldLabel("Course:");
        FlowPane courseSelectorRow = this.createFormFlowPane();
        courseSelectorRow.getChildren().add(this.formPair(courseSelectorLabel, (Control)classesCourseComboBox));
        FlowPane courseForm = this.createFormFlowPane();
        courseForm.getChildren().addAll(this.formPair(courseNameLabel, (Control)courseNameField), this.formPair(courseCodeLabel, (Control)courseCodeField), this.formPair(teacherLabel, (Control)teacherField), this.formPair(totalClassesLabel, (Control)totalClassesField), addCourseButton, editCourseButton, updateCourseButton, deleteCourseButton);
        Label courseManagementTitle = this.cardTitle("Course Management");
        Label courseCardSubtitle = this.cardSubtitle("Create and maintain course information used throughout the attendance system.");
        VBox courseManagementCard = this.card(courseManagementTitle, courseCardSubtitle, courseSelectorRow, courseForm);
        FlowPane classForm = this.createFormFlowPane();
        classForm.getChildren().addAll(this.formPair(weekLabel, (Control)weekField), this.formPair(classNumberLabel, (Control)classNumberField), this.formPair(classDateLabel, (Control)dateField), addClassButton);
        FlowPane classManagementButtons = this.createFormFlowPane();
        classManagementButtons.getChildren().add(deleteClassButton);
        Label classManagementTitle = this.cardTitle("Class Management");
        Label classCardSubtitle = this.cardSubtitle("Add weekly class sessions and manage the schedule for the selected course.");
        VBox classManagementCard = this.card(classManagementTitle, classCardSubtitle, classForm, classManagementButtons);
        Label classesTableTitle = this.cardTitle("Class Records");
        Label classesTableSubtitle = this.cardSubtitle("View the scheduled sessions for the currently selected course.");
        VBox classesTableCard = this.card(classesTableTitle, classesTableSubtitle, classTable);
        this.configureCourseCombo((ComboBox<Course>)attendanceCourseComboBox);
        attendanceCourseComboBox.setPrefWidth(260.0);
        Label attendanceCourseLabel = this.fieldLabel("Course:");
        Button addWeekButton = this.secondaryButton("+ Add Week", 110.0);
        Label gridHint = new Label("Click a cell to mark attendance: empty \u2192 \u2713 Present \u2192 \u2717 Absent \u2192 clear");
        gridHint.setStyle("-fx-font-size: 11px;-fx-text-fill: #6B7A90;");
        FlowPane attendanceToolbar = this.createFormFlowPane();
        attendanceToolbar.getChildren().addAll(
                this.formPair(attendanceCourseLabel, (Control)attendanceCourseComboBox),
                addWeekButton);
        Label attendanceRecordsTitle = this.cardTitle("Attendance Grid");
        Label attendanceRecordsSubtitle = this.cardSubtitle(
                "Select a course, then click P1-P4 under each week (W1, W2, ...) to mark attendance. Percentage is at the end of each student row.");
        VBox attendanceRecordsCard = this.card(
                attendanceRecordsTitle, attendanceRecordsSubtitle, attendanceToolbar, gridHint, attendanceTable);
        Label studentsCount = this.dashboardNumber("0", NAVY);
        Label coursesCount = this.dashboardNumber("0", "#2F5D8A");
        Label classesCount = this.dashboardNumber("0", "#3E6B56");
        Label attendanceRate = this.dashboardNumber("0.0%", "#7A5A22");
        VBox studentsCard = this.statCard("TOTAL STUDENTS", studentsCount, "Registered students");
        VBox coursesCard = this.statCard("TOTAL COURSES", coursesCount, "Active courses");
        VBox classesCard = this.statCard("TOTAL CLASSES", classesCount, "Scheduled classes");
        VBox attendanceCard = this.statCard("ATTENDANCE RATE", attendanceRate, "Overall attendance");
        HBox statistics = new HBox(10.0, studentsCard, coursesCard, classesCard, attendanceCard);
        statistics.setAlignment(Pos.CENTER_LEFT);
        for (Region cardRegion : new Region[]{studentsCard, coursesCard, classesCard, attendanceCard}) {
            HBox.setHgrow(cardRegion, Priority.ALWAYS);
        }
        Label dashboardInfoTitle = this.cardTitle("Attendance Overview");
        Label dashboardInfoText = new Label("Use the navigation to manage students, courses, classes, and attendance records. The statistics on this dashboard are calculated directly from your system data.");
        dashboardInfoText.setWrapText(true);
        dashboardInfoText.setStyle("-fx-font-size: 12px;-fx-text-fill: #6B7A90;");
        VBox dashboardInfoCard = this.card(dashboardInfoTitle, dashboardInfoText);
        Label dashboardTitle = new Label("Overview");
        dashboardTitle.setStyle("-fx-font-size: 16px;-fx-font-weight: bold;-fx-text-fill: #1F3B64;");
        VBox dashboardSection = new VBox(13.0, dashboardHeader, dashboardTitle, statistics, dashboardInfoCard);
        VBox studentsSection = new VBox(13.0, studentsHeader, studentFormCard, studentRecordsCard);
        VBox attendanceSection = new VBox(13.0, attendanceHeader, attendanceRecordsCard);
        VBox classesSection = new VBox(13.0, classesHeader, courseManagementCard, classManagementCard, classesTableCard);
        Runnable loadCourses = () -> {
            try {
                List<Course> courses = new CourseDAO().getAllCourses();
                Course selectedClassesCourse = (Course)classesCourseComboBox.getValue();
                int selectedClassesCourseId = selectedClassesCourse == null ? -1 : selectedClassesCourse.getId();
                Course selectedAttendanceCourse = (Course)attendanceCourseComboBox.getValue();
                int selectedAttendanceCourseId = selectedAttendanceCourse == null ? -1 : selectedAttendanceCourse.getId();
                classesCourseComboBox.getItems().setAll(courses);
                attendanceCourseComboBox.getItems().setAll(courses);
                for (Course course : courses) {
                    if (course.getId() == selectedClassesCourseId) {
                        classesCourseComboBox.setValue(course);
                    }
                    if (course.getId() != selectedAttendanceCourseId) continue;
                    attendanceCourseComboBox.setValue(course);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable loadStudents = () -> {
            try {
                List<Student> students = new StudentDAO().getAllStudents();
                studentTable.getItems().setAll(students);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable loadClassesForClassesPage = () -> {
            Course selectedCourse = (Course)classesCourseComboBox.getValue();
            if (selectedCourse == null) {
                classTable.getItems().clear();
                return;
            }
            try {
                List<ClassSession> sessions = new ClassSessionDAO().getSessionsByCourse(selectedCourse.getId());
                classTable.getItems().setAll(sessions);
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load classes.");
                e.printStackTrace();
            }
        };
        Runnable[] refreshDashboardCountsRef = new Runnable[1];
        Runnable[] refreshAttendanceRecordsRef = new Runnable[1];
        refreshAttendanceRecordsRef[0] = () -> {
            try {
                Course selectedCourse = (Course) attendanceCourseComboBox.getValue();
                if (selectedCourse == null) {
                    attendanceTable.getColumns().clear();
                    attendanceTable.getItems().clear();
                    attendanceTable.setPlaceholder(new Label("Select a course to open the attendance grid."));
                    return;
                }
                ClassSessionDAO classDAO = new ClassSessionDAO();
                List<Integer> weeks = classDAO.getWeekNumbers(selectedCourse.getId());
                if (weeks.isEmpty()) {
                    String today = LocalDate.now().toString();
                    for (int period = 1; period <= AttendanceCalculator.PERIODS_PER_CLASS; period++) {
                        classDAO.getOrCreateSession(selectedCourse.getId(), 1, period, today);
                    }
                    weeks = classDAO.getWeekNumbers(selectedCourse.getId());
                }
                attendanceWeeks.set(weeks);
                List<Student> students = new StudentDAO().getAllStudents();
                List<StudentAttendanceRow> rows = new AttendanceDAO().getCourseAttendanceGrid(
                        selectedCourse.getId(), students, weeks);
                this.buildAttendanceColumns(attendanceTable, weeks, selectedCourse, () -> {
                    refreshAttendanceRecordsRef[0].run();
                    if (refreshDashboardCountsRef[0] != null) {
                        refreshDashboardCountsRef[0].run();
                    }
                });
                attendanceTable.getItems().setAll(rows);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Runnable refreshAttendanceRecords = () -> refreshAttendanceRecordsRef[0].run();
        Runnable refreshDashboardCounts = () -> {
            try {
                StudentDAO studentDAO = new StudentDAO();
                CourseDAO courseDAO = new CourseDAO();
                ClassSessionDAO classDAO = new ClassSessionDAO();
                AttendanceDAO attendanceDAO = new AttendanceDAO();
                int studentCount = studentDAO.getStudentCount();
                int courseCount = courseDAO.getCourseCount();
                studentsCount.setText(String.valueOf(studentCount));
                coursesCount.setText(String.valueOf(courseCount));
                int classCount = classDAO.getClassCount();
                double attendancePercentage = 0.0;
                if (studentCount > 0) {
                    attendancePercentage = attendanceDAO.getAttendancePercentage();
                }
                classesCount.setText(String.valueOf(classCount));
                attendanceRate.setText(String.format("%.1f%%", attendancePercentage));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        };
        refreshDashboardCountsRef[0] = refreshDashboardCounts;
        Runnable refreshAllData = () -> {
            loadCourses.run();
            loadStudents.run();
            loadClassesForClassesPage.run();
            refreshAttendanceRecords.run();
            refreshDashboardCounts.run();
        };
        this.configureCourseCombo((ComboBox<Course>)classesCourseComboBox);
        this.configureCourseCombo((ComboBox<Course>)attendanceCourseComboBox);
        classesCourseComboBox.setOnAction(event -> loadClassesForClassesPage.run());
        attendanceCourseComboBox.setOnAction(event -> refreshAttendanceRecords.run());
        addCourseButton.setOnAction(event -> {
            try {
                int totalClasses;
                String courseName = courseNameField.getText().trim();
                String courseCode = courseCodeField.getText().trim();
                String teacherName = teacherField.getText().trim();
                String totalClassesText = totalClassesField.getText().trim();
                if (courseName.isEmpty() || courseCode.isEmpty() || teacherName.isEmpty() || totalClassesText.isEmpty()) {
                    this.showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all course fields.");
                    return;
                }
                try {
                    totalClasses = Integer.parseInt(totalClassesText);
                }
                catch (NumberFormatException e) {
                    this.showAlert(Alert.AlertType.WARNING, "Invalid Number", "Total classes must be a valid number.");
                    return;
                }
                if (totalClasses <= 0) {
                    this.showAlert(Alert.AlertType.WARNING, "Invalid Number", "Total classes must be greater than 0.");
                    return;
                }
                CourseDAO dao = new CourseDAO();
                for (Course existing : dao.getAllCourses()) {
                    if (!existing.getCourseCode().trim().equalsIgnoreCase(courseCode)) continue;
                    this.showAlert(Alert.AlertType.WARNING, "Duplicate Course Code", "Course code \"" + courseCode + "\" already exists.");
                    return;
                }
                dao.addCourse(new Course(courseName, courseCode, teacherName, totalClasses));
                courseNameField.clear();
                courseCodeField.clear();
                teacherField.clear();
                totalClassesField.clear();
                loadCourses.run();
                refreshDashboardCounts.run();
                this.showAlert(Alert.AlertType.INFORMATION, "Success", "Course added successfully.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Course Error", e.getMessage() != null ? e.getMessage() : "Could not save the course.");
                e.printStackTrace();
            }
        });
        editCourseButton.setOnAction(event -> {
            Course selected = (Course)classesCourseComboBox.getValue();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Course Selected", "Please select a course first.");
                return;
            }
            courseNameField.setText(selected.getCourseName());
            courseCodeField.setText(selected.getCourseCode());
            teacherField.setText(selected.getTeacherName());
            totalClassesField.setText(String.valueOf(selected.getTotalClasses()));
        });
        updateCourseButton.setOnAction(event -> {
            Course selected = (Course)classesCourseComboBox.getValue();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Course Selected", "Please select a course first.");
                return;
            }
            String courseName = courseNameField.getText().trim();
            String courseCode = courseCodeField.getText().trim();
            String teacherName = teacherField.getText().trim();
            String totalText = totalClassesField.getText().trim();
            if (courseName.isEmpty() || courseCode.isEmpty() || teacherName.isEmpty() || totalText.isEmpty()) {
                this.showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in all course fields.");
                return;
            }
            try {
                int totalClasses = Integer.parseInt(totalText);
                if (totalClasses <= 0) {
                    this.showAlert(Alert.AlertType.WARNING, "Invalid Number", "Total classes must be greater than 0.");
                    return;
                }
                CourseDAO dao = new CourseDAO();
                for (Course existing : dao.getAllCourses()) {
                    if (existing.getId() == selected.getId() || !existing.getCourseCode().trim().equalsIgnoreCase(courseCode)) continue;
                    this.showAlert(Alert.AlertType.WARNING, "Duplicate Course Code", "Course code \"" + courseCode + "\" already exists.");
                    return;
                }
                dao.updateCourse(selected.getId(), courseName, courseCode, teacherName, totalClasses);
                courseNameField.clear();
                courseCodeField.clear();
                teacherField.clear();
                totalClassesField.clear();
                loadCourses.run();
                refreshDashboardCounts.run();
                this.showAlert(Alert.AlertType.INFORMATION, "Course Updated", "Course information updated successfully.");
            }
            catch (NumberFormatException e) {
                this.showAlert(Alert.AlertType.WARNING, "Invalid Number", "Total classes must be a valid number.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Course Update Error", e.getMessage() != null ? e.getMessage() : "Could not update the course.");
                e.printStackTrace();
            }
        });
        deleteCourseButton.setOnAction(event -> {
            Course selected = (Course)classesCourseComboBox.getValue();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Course Selected", "Please select a course first.");
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Course");
            confirmation.setHeaderText("Delete Course: " + selected.getCourseName());
            confirmation.setContentText("This will also delete all classes and attendance records belonging to this course.\n\nDo you want to continue?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        new CourseDAO().deleteCourse(selected.getId());
                        courseNameField.clear();
                        courseCodeField.clear();
                        teacherField.clear();
                        totalClassesField.clear();
                        loadCourses.run();
                        loadClassesForClassesPage.run();
                        refreshAttendanceRecords.run();
                        refreshDashboardCounts.run();
                        this.showAlert(Alert.AlertType.INFORMATION, "Course Deleted", "Course deleted successfully.");
                    }
                    catch (Exception e) {
                        this.showAlert(Alert.AlertType.ERROR, "Course Delete Error", e.getMessage() != null ? e.getMessage() : "Could not delete the course.");
                        e.printStackTrace();
                    }
                }
            });
        });
        addStudentButton.setOnAction(event -> {
            try {
                String studentId = studentIdField.getText().trim();
                String studentName = studentNameField.getText().trim();
                if (studentId.isEmpty() || studentName.isEmpty()) {
                    this.showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter both Student ID and Student Name.");
                    return;
                }
                StudentDAO dao = new StudentDAO();
                for (Student existing : dao.getAllStudents()) {
                    if (!existing.getStudentId().trim().equalsIgnoreCase(studentId)) continue;
                    this.showAlert(Alert.AlertType.WARNING, "Duplicate Student ID", "Student ID \"" + studentId + "\" already exists.");
                    return;
                }
                dao.addStudent(new Student(studentId, studentName));
                studentIdField.clear();
                studentNameField.clear();
                loadStudents.run();
                refreshDashboardCounts.run();
                this.showAlert(Alert.AlertType.INFORMATION, "Success", "Student added successfully.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Student Error", e.getMessage() != null ? e.getMessage() : "Could not save the student.");
                e.printStackTrace();
            }
        });
        loadStudentsButton.setOnAction(event -> {
            try {
                StudentDAO dao = new StudentDAO();
                String keyword = studentSearchField.getText().trim();
                if (keyword.isEmpty()) {
                    studentTable.getItems().setAll(dao.getAllStudents());
                } else {
                    studentTable.getItems().setAll(dao.searchStudents(keyword));
                }
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Database Error", "Could not load students.");
                e.printStackTrace();
            }
        });
        studentSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                StudentDAO dao = new StudentDAO();
                String keyword = newValue.trim();
                if (keyword.isEmpty()) {
                    studentTable.getItems().setAll(dao.getAllStudents());
                } else {
                    studentTable.getItems().setAll(dao.searchStudents(keyword));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        editStudentButton.setOnAction(event -> {
            Student selected = (Student)studentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Student Selected", "Please select a student first.");
                return;
            }
            studentIdField.setText(selected.getStudentId());
            studentNameField.setText(selected.getName());
        });
        updateStudentButton.setOnAction(event -> {
            Student selected = (Student)studentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Student Selected", "Please select a student first.");
                return;
            }
            String newId = studentIdField.getText().trim();
            String newName = studentNameField.getText().trim();
            if (newId.isEmpty() || newName.isEmpty()) {
                this.showAlert(Alert.AlertType.WARNING, "Missing Information", "Please enter both Student ID and Student Name.");
                return;
            }
            try {
                StudentDAO dao = new StudentDAO();
                for (Student existing : dao.getAllStudents()) {
                    if (existing.getId() == selected.getId() || !existing.getStudentId().trim().equalsIgnoreCase(newId)) continue;
                    this.showAlert(Alert.AlertType.WARNING, "Duplicate Student ID", "Student ID \"" + newId + "\" already exists.");
                    return;
                }
                dao.updateStudent(selected.getId(), newId, newName);
                studentIdField.clear();
                studentNameField.clear();
                loadStudents.run();
                refreshDashboardCounts.run();
                this.showAlert(Alert.AlertType.INFORMATION, "Student Updated", "Student information updated successfully.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage() != null ? e.getMessage() : "Could not update the student.");
                e.printStackTrace();
            }
        });
        deleteStudentButton.setOnAction(event -> {
            Student selected = (Student)studentTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Student Selected", "Please select a student first.");
                return;
            }
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Delete Student");
            confirmation.setHeaderText("Delete Student: " + selected.getName());
            confirmation.setContentText("Are you sure you want to delete this student?");
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        new StudentDAO().deleteStudent(selected.getId());
                        loadStudents.run();
                        refreshAttendanceRecords.run();
                        refreshDashboardCounts.run();
                        studentIdField.clear();
                        studentNameField.clear();
                        this.showAlert(Alert.AlertType.INFORMATION, "Student Deleted", "Student deleted successfully.");
                    }
                    catch (Exception e) {
                        this.showAlert(Alert.AlertType.ERROR, "Database Error", e.getMessage() != null ? e.getMessage() : "Could not delete the student.");
                        e.printStackTrace();
                    }
                }
            });
        });
        addClassButton.setOnAction(event -> {
            try {
                Course selectedCourse = (Course)classesCourseComboBox.getValue();
                if (selectedCourse == null) {
                    this.showAlert(Alert.AlertType.WARNING, "No Course Selected", "Please select a course first.");
                    return;
                }
                String weekText = weekField.getText().trim();
                String classText = classNumberField.getText().trim();
                String dateText = dateField.getText().trim();
                if (weekText.isEmpty() || classText.isEmpty() || dateText.isEmpty()) {
                    this.showAlert(Alert.AlertType.WARNING, "Missing Information", "Please fill in Week, Class, and Date.");
                    return;
                }
                int weekNumber = Integer.parseInt(weekText);
                int classNumber = Integer.parseInt(classText);
                if (weekNumber <= 0) {
                    this.showAlert(Alert.AlertType.WARNING, "Invalid Number", "Week must be greater than 0.");
                    return;
                }
                if (!AttendanceCalculator.isValidPeriod(classNumber)) {
                    this.showAlert(Alert.AlertType.WARNING, "Invalid Period", "Period must be between 1 and 4.");
                    return;
                }
                ClassSessionDAO dao = new ClassSessionDAO();
                for (ClassSession existing : dao.getSessionsByCourse(selectedCourse.getId())) {
                    if (existing.getWeekNumber() != weekNumber || existing.getClassNumber() != classNumber) continue;
                    this.showAlert(Alert.AlertType.WARNING, "Duplicate Class", "Week " + weekNumber + " / Period " + classNumber + " already exists for this course.");
                    return;
                }
                dao.addClassSession(new ClassSession(selectedCourse.getId(), weekNumber, classNumber, dateText));
                weekField.clear();
                classNumberField.clear();
                dateField.clear();
                loadClassesForClassesPage.run();
                refreshDashboardCounts.run();
                this.showAlert(Alert.AlertType.INFORMATION, "Success", "Class added successfully.");
            }
            catch (NumberFormatException e) {
                this.showAlert(Alert.AlertType.WARNING, "Invalid Number", "Week and Class must be valid numbers.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Class Error", e.getMessage() != null ? e.getMessage() : "Could not save the class.");
                e.printStackTrace();
            }
        });
        deleteClassButton.setOnAction(event -> {
            ClassSession selected = (ClassSession)classTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                this.showAlert(Alert.AlertType.WARNING, "No Class Selected", "Please select a class from the table first.");
                return;
            }
            try {
                new ClassSessionDAO().deleteClassSession(selected.getId());
                loadClassesForClassesPage.run();
                refreshAttendanceRecords.run();
                refreshDashboardCounts.run();
                this.showAlert(Alert.AlertType.INFORMATION, "Class Deleted", "Class deleted successfully.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Class Error", e.getMessage() != null ? e.getMessage() : "Could not delete the class.");
                e.printStackTrace();
            }
        });
        addWeekButton.setOnAction(event -> {
            try {
                Course selectedCourse = (Course) attendanceCourseComboBox.getValue();
                if (selectedCourse == null) {
                    this.showAlert(Alert.AlertType.WARNING, "No Course Selected", "Please select a course first.");
                    return;
                }
                ClassSessionDAO classDAO = new ClassSessionDAO();
                List<Integer> weeks = classDAO.getWeekNumbers(selectedCourse.getId());
                int nextWeek = weeks.isEmpty() ? 1 : weeks.get(weeks.size() - 1) + 1;
                String today = LocalDate.now().toString();
                for (int period = 1; period <= AttendanceCalculator.PERIODS_PER_CLASS; period++) {
                    classDAO.getOrCreateSession(selectedCourse.getId(), nextWeek, period, today);
                }
                refreshAttendanceRecords.run();
                refreshDashboardCounts.run();
            } catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Week Error",
                        e.getMessage() != null ? e.getMessage() : "Could not add week.");
                e.printStackTrace();
            }
        });
        Runnable[] currentPage = new Runnable[1];
        Runnable showDashboard = () -> {
            contentArea.getChildren().setAll(dashboardSection);
            currentPage[0] = () -> contentArea.getChildren().setAll(dashboardSection);
            this.setActiveNavigation(dashboardButton, studentsButton, attendanceButton, classesButton, sideDashboard, sideStudents, sideAttendance, sideClasses);
        };
        Runnable showStudents = () -> {
            contentArea.getChildren().setAll(studentsSection);
            currentPage[0] = () -> contentArea.getChildren().setAll(studentsSection);
            this.setActiveNavigation(studentsButton, dashboardButton, attendanceButton, classesButton, sideStudents, sideDashboard, sideAttendance, sideClasses);
        };
        Runnable showAttendance = () -> {
            contentArea.getChildren().setAll(attendanceSection);
            refreshAttendanceRecords.run();
            currentPage[0] = () -> {
                contentArea.getChildren().setAll(attendanceSection);
                refreshAttendanceRecords.run();
            };
            this.setActiveNavigation(attendanceButton, dashboardButton, studentsButton, classesButton, sideAttendance, sideDashboard, sideStudents, sideClasses);
        };
        Runnable showClasses = () -> {
            contentArea.getChildren().setAll(classesSection);
            loadClassesForClassesPage.run();
            currentPage[0] = () -> {
                contentArea.getChildren().setAll(classesSection);
                loadClassesForClassesPage.run();
            };
            this.setActiveNavigation(classesButton, dashboardButton, studentsButton, attendanceButton, sideClasses, sideDashboard, sideStudents, sideAttendance);
        };
        dashboardButton.setOnAction(event -> showDashboard.run());
        studentsButton.setOnAction(event -> showStudents.run());
        attendanceButton.setOnAction(event -> showAttendance.run());
        classesButton.setOnAction(event -> showClasses.run());
        sideDashboard.setOnAction(event -> showDashboard.run());
        sideStudents.setOnAction(event -> showStudents.run());
        sideAttendance.setOnAction(event -> showAttendance.run());
        sideClasses.setOnAction(event -> showClasses.run());
        refreshButton.setOnAction(event -> {
            try {
                refreshAllData.run();
                if (currentPage[0] != null) {
                    currentPage[0].run();
                }
                this.showAlert(Alert.AlertType.INFORMATION, "System Refreshed", "All data has been refreshed successfully.");
            }
            catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Refresh Error", e.getMessage() != null ? e.getMessage() : "Could not refresh the system.");
                e.printStackTrace();
            }
        });
        sideRefresh.setOnAction(event -> refreshButton.fire());
        Runnable applyLanguage = () -> {
            I18n.setLanguage(languageCombo.getValue());
            languageLabel.setText(I18n.t("lang.label"));
            title.setText(I18n.t("app.title"));
            subtitle.setText(I18n.t("app.subtitle"));
            welcomeLabel.setText(I18n.t("welcome"));
            sideBrand.setText(I18n.t("side.brand"));
            sideQuote.setText(I18n.t("side.quote"));
            this.updateNavButtonText(dashboardButton, I18n.t("nav.dashboard"));
            this.updateNavButtonText(studentsButton, I18n.t("nav.students"));
            this.updateNavButtonText(attendanceButton, I18n.t("nav.attendance"));
            this.updateNavButtonText(classesButton, I18n.t("nav.classes"));
            this.updateNavButtonText(refreshButton, I18n.t("nav.refresh"));
            this.updateNavButtonText(sideDashboard, I18n.t("nav.dashboard"));
            this.updateNavButtonText(sideStudents, I18n.t("nav.students"));
            this.updateNavButtonText(sideAttendance, I18n.t("nav.attendance"));
            this.updateNavButtonText(sideClasses, I18n.t("nav.classes"));
            this.updateNavButtonText(sideRefresh, I18n.t("nav.refresh"));
            dashboardPageTitle.setText(I18n.t("dashboard.title"));
            dashboardPageSubtitle.setText(I18n.t("dashboard.subtitle"));
            dashboardTitle.setText(I18n.t("dashboard.overview"));
            dashboardInfoTitle.setText(I18n.t("dashboard.info.title"));
            dashboardInfoText.setText(I18n.t("dashboard.info"));
            stage.setTitle(I18n.t("app.title"));
            refreshAttendanceRecords.run();
        };
        languageCombo.setOnAction(event -> applyLanguage.run());
        refreshAllData.run();
        showDashboard.run();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE, MMM d, yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        Runnable updateClock = () -> {
            LocalDateTime now = LocalDateTime.now();
            dateLabel.setText(now.format(dateFormatter) + "\n" + now.format(timeFormatter));
        };
        updateClock.run();
        Timeline clockTimeline = new Timeline(new KeyFrame(Duration.seconds((double)1.0), event -> updateClock.run(), new KeyValue[0]));
        clockTimeline.setCycleCount(-1);
        clockTimeline.play();
        BorderPane root = new BorderPane();
        VBox centerArea = new VBox(0.0, topHeader, topNavigation, contentScroll);
        VBox.setVgrow((Node)contentScroll, (Priority)Priority.ALWAYS);
        root.setLeft((Node)sidebar);
        root.setCenter((Node)centerArea);
        root.setStyle("-fx-background-color: #F4F7FB;");
        Scene scene = new Scene((Parent)root, 1150.0, 700.0);
        scene.setFill((Paint)Color.web((String)BACKGROUND));
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(I18n.t("app.title"));
        if (appIcon != null) {
            stage.getIcons().add(appIcon);
        }
        stage.setMinWidth(900.0);
        stage.setMinHeight(600.0);
        stage.setResizable(true);
        stage.setScene(scene);
        Timeline splashTimeline = new Timeline(new KeyFrame(Duration.seconds((double)2.5), event -> {
            stage.show();
            stage.toFront();
            splashStage.close();
        }, new KeyValue[0]));
        splashTimeline.setCycleCount(1);
        splashTimeline.play();
    }

    private Stage createSplashStage() {
        Stage splashStage = new Stage();
        splashStage.initStyle(StageStyle.UNDECORATED);
        splashStage.setAlwaysOnTop(true);
        splashStage.setResizable(false);
        Image appIcon = this.loadAppIcon();
        if (appIcon != null) {
            splashStage.getIcons().add(appIcon);
        }
        VBox splashContent = new VBox(12.0);
        splashContent.setAlignment(Pos.CENTER);
        splashContent.setPadding(new Insets(30.0, 42.0, 28.0, 42.0));
        splashContent.setStyle("-fx-background-color: linear-gradient(to bottom right, #102B4A, #1F6FE5);-fx-background-radius: 18;-fx-border-color: rgba(255,255,255,0.18);-fx-border-radius: 18;-fx-border-width: 1;");
        ImageView splashLogoImage = this.createLogoImage(105.0);
        StackPane splashLogoBox = new StackPane(splashLogoImage);
        splashLogoBox.setMinSize(112.0, 112.0);
        splashLogoBox.setPrefSize(112.0, 112.0);
        splashLogoBox.setMaxSize(112.0, 112.0);
        splashLogoBox.setStyle("-fx-background-color: white;-fx-background-radius: 28;");
        Label splashTitle = new Label("Student Attendance System");
        splashTitle.setStyle("-fx-text-fill: white;-fx-font-size: 24px;-fx-font-weight: bold;");
        Label splashSubtitle = new Label("Attendance management made simple");
        splashSubtitle.setStyle("-fx-text-fill: #DDEBFF;-fx-font-size: 12px;");
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(-1.0);
        progressBar.setPrefWidth(240.0);
        progressBar.setMaxWidth(240.0);
        progressBar.setStyle("-fx-accent: white;-fx-control-inner-background: rgba(255,255,255,0.25);");
        Label loadingLabel = new Label("Starting application...");
        loadingLabel.setStyle("-fx-text-fill: #E8F1FF;-fx-font-size: 11px;");
        splashContent.getChildren().addAll(splashLogoBox, splashTitle, splashSubtitle, progressBar, loadingLabel);
        StackPane splashRoot = new StackPane(splashContent);
        splashRoot.setPadding(new Insets(6.0));
        Scene splashScene = new Scene((Parent)splashRoot, 520.0, 330.0);
        splashScene.setFill((Paint)Color.TRANSPARENT);
        splashStage.setScene(splashScene);
        splashStage.centerOnScreen();
        return splashStage;
    }

    private Image loadAppIcon() {
        try {
            InputStream inputStream = Main.class.getResourceAsStream("/student-attendance-logo.png");
            if (inputStream == null) {
                System.err.println("Application logo not found: student-attendance-logo.png");
                return null;
            }
            return new Image(inputStream);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private ImageView createLogoImage(double size) {
        Image image = this.loadAppIcon();
        ImageView imageView = image == null ? new ImageView() : new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        return imageView;
    }

    private FlowPane createFormFlowPane() {
        FlowPane pane = new FlowPane();
        pane.setHgap(10.0);
        pane.setVgap(9.0);
        pane.setAlignment(Pos.CENTER_LEFT);
        pane.setPrefWrapLength(850.0);
        pane.setMaxWidth(Double.MAX_VALUE);
        return pane;
    }

    private HBox formPair(Label label, Control control) {
        HBox pair = new HBox(6.0, label, control);
        pair.setAlignment(Pos.CENTER_LEFT);
        return pair;
    }

    private Button createNavButton(String icon, String text) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 15px;-fx-text-fill: #4C6787;");
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #456181;");
        HBox box = new HBox(6.0, iconLabel, textLabel);
        box.setAlignment(Pos.CENTER);
        Button button = new Button();
        button.setGraphic((Node)box);
        button.setPrefHeight(40.0);
        button.setMinHeight(40.0);
        button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-padding: 0 10 0 10;");
        return button;
    }

    private Button createSidebarButton(String icon, String text) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 17px;-fx-text-fill: #DDEAF8;");
        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #E7F0FA;");
        HBox box = new HBox(10.0, iconLabel, textLabel);
        box.setAlignment(Pos.CENTER_LEFT);
        Button button = new Button();
        button.setGraphic((Node)box);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setPrefHeight(42.0);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setPadding(new Insets(0.0, 8.0, 0.0, 12.0));
        button.setStyle("-fx-background-color: transparent;-fx-background-radius: 7;-fx-border-color: transparent;");
        return button;
    }

    private void setActiveNavigation(Button activeTop, Button top2, Button top3, Button top4, Button activeSide, Button side2, Button side3, Button side4) {
        this.setTopActive(activeTop);
        this.setTopInactive(top2);
        this.setTopInactive(top3);
        this.setTopInactive(top4);
        this.setSidebarActive(activeSide);
        this.setSidebarInactive(side2);
        this.setSidebarInactive(side3);
        this.setSidebarInactive(side4);
    }

    private void setTopActive(Button button) {
        button.setStyle("-fx-background-color: #F2F6FC;-fx-border-color: #1F6FE5;-fx-border-width: 0 0 2 0;-fx-padding: 0 10 0 10;");
        Node node = button.getGraphic();
        if (node instanceof HBox) {
            HBox box = (HBox)node;
            for (Node node2 : box.getChildren()) {
                if (!(node2 instanceof Label)) continue;
                Label label = (Label)node2;
                label.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #1F6FE5;");
            }
        }
    }

    private void setTopInactive(Button button) {
        button.setStyle("-fx-background-color: transparent;-fx-border-color: transparent;-fx-padding: 0 10 0 10;");
        Node node = button.getGraphic();
        if (node instanceof HBox) {
            HBox box = (HBox)node;
            for (Node node2 : box.getChildren()) {
                if (!(node2 instanceof Label)) continue;
                Label label = (Label)node2;
                label.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #456181;");
            }
        }
    }

    private void setSidebarActive(Button button) {
        button.setStyle("-fx-background-color: linear-gradient(to right, #2B70C9, #356FC0);-fx-background-radius: 7;-fx-border-color: #63B9FF;-fx-border-width: 0 0 0 3;-fx-border-radius: 7;");
        Node node = button.getGraphic();
        if (node instanceof HBox) {
            HBox box = (HBox)node;
            for (Node node2 : box.getChildren()) {
                if (!(node2 instanceof Label)) continue;
                Label label = (Label)node2;
                label.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: white;");
            }
        }
    }

    private void setSidebarInactive(Button button) {
        button.setStyle("-fx-background-color: transparent;-fx-background-radius: 7;-fx-border-color: transparent;");
        Node node = button.getGraphic();
        if (node instanceof HBox) {
            HBox box = (HBox)node;
            for (Node node2 : box.getChildren()) {
                if (!(node2 instanceof Label)) continue;
                Label label = (Label)node2;
                label.setStyle("-fx-font-size: 12px;-fx-font-weight: bold;-fx-text-fill: #E7F0FA;");
            }
        }
    }

    private Label pageTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 19px;-fx-font-weight: bold;-fx-text-fill: #1F3B64;");
        return label;
    }

    private Label pageSubtitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px;-fx-text-fill: #6B7A90;");
        return label;
    }

    private VBox pageHeader(Label title, Label subtitle) {
        VBox box = new VBox(3.0, title, subtitle);
        box.setPadding(new Insets(2.0, 2.0, 5.0, 2.0));
        return box;
    }

    private Label cardTitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px;-fx-font-weight: bold;-fx-text-fill: #1F3B64;");
        return label;
    }

    private Label cardSubtitle(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px;-fx-text-fill: #8A97A8;");
        label.setWrapText(true);
        return label;
    }

    private VBox card(Node ... nodes) {
        VBox box = new VBox(8.0);
        box.getChildren().addAll(nodes);
        box.setPadding(new Insets(12.0));
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle("-fx-background-color: white;-fx-background-radius: 9;-fx-border-radius: 9;-fx-border-color: #DDE6F0;-fx-effect: dropshadow(gaussian, rgba(31,59,100,0.06), 8, 0, 0, 2);");
        return box;
    }

    private TextField styledField(String prompt, double width) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(width);
        field.setMinWidth(60.0);
        field.setStyle("-fx-background-color: white;-fx-border-color: #D6E1EE;-fx-border-radius: 6;-fx-background-radius: 6;-fx-padding: 7 9 7 9;-fx-font-size: 11px;");
        return field;
    }

    private Label fieldLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 11px;-fx-font-weight: bold;-fx-text-fill: #526174;");
        return label;
    }

    private Button primaryButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(32.0);
        button.setStyle("-fx-background-color: #1F6FE5;-fx-text-fill: white;-fx-font-size: 11px;-fx-font-weight: bold;-fx-padding: 6 10 6 10;-fx-background-radius: 6;");
        return button;
    }

    private Button secondaryButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(32.0);
        button.setStyle("-fx-background-color: #EEF3F9;-fx-text-fill: #1F3B64;-fx-font-size: 11px;-fx-font-weight: bold;-fx-padding: 6 10 6 10;-fx-background-radius: 6;-fx-border-color: #D7E2EF;-fx-border-radius: 6;");
        return button;
    }

    private Button successButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(32.0);
        button.setStyle("-fx-background-color: #E8F3EC;-fx-text-fill: #287D3C;-fx-font-size: 11px;-fx-font-weight: bold;-fx-padding: 6 10 6 10;-fx-background-radius: 6;-fx-border-color: #BFE0C8;-fx-border-radius: 6;");
        return button;
    }

    private Button dangerButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(32.0);
        button.setStyle("-fx-background-color: #FDECEC;-fx-text-fill: #B42318;-fx-font-size: 11px;-fx-font-weight: bold;-fx-padding: 6 10 6 10;-fx-background-radius: 6;-fx-border-color: #F2C4C0;-fx-border-radius: 6;");
        return button;
    }

    private Button purpleButton(String text, double width) {
        Button button = new Button(text);
        button.setPrefWidth(width);
        button.setPrefHeight(32.0);
        button.setStyle("-fx-background-color: #6A4CE0;-fx-text-fill: white;-fx-font-size: 11px;-fx-font-weight: bold;-fx-padding: 6 10 6 10;-fx-background-radius: 6;");
        return button;
    }

    private Label dashboardNumber(String value, String color) {
        Label label = new Label(value);
        label.setStyle("-fx-font-size: 23px;-fx-font-weight: bold;-fx-text-fill: " + color + ";");
        return label;
    }

    private VBox statCard(String title, Label number, String subtitle) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-size: 10px;-fx-font-weight: bold;-fx-text-fill: #6B7A90;");
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setStyle("-fx-font-size: 10px;-fx-text-fill: #8A97A8;");
        VBox box = new VBox(5.0, titleLabel, number, subtitleLabel);
        box.setPadding(new Insets(13.0));
        box.setMinHeight(95.0);
        box.setMaxWidth(Double.MAX_VALUE);
        box.setStyle("-fx-background-color: white;-fx-background-radius: 9;-fx-border-radius: 9;-fx-border-color: #DDE6F0;-fx-effect: dropshadow(gaussian, rgba(31,59,100,0.06), 8, 0, 0, 2);");
        return box;
    }

    private <T> void styleTable(TableView<T> table) {
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setStyle("-fx-background-color: white;-fx-border-color: #DDE6F0;-fx-border-radius: 7;-fx-background-radius: 7;");
        table.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && getIndex() % 2 == 1) {
                    setStyle("-fx-background-color: #F8FAFD;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private <S, T> void centerColumn(TableColumn<S, T> column) {
        column.setStyle("-fx-alignment: CENTER;");
    }

    private void configureCourseCombo(ComboBox<Course> comboBox) {
        comboBox.setPromptText("Select Course");
        comboBox.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
                }
            }
        });
        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getCourseName() + " (" + course.getCourseCode() + ")");
                }
            }
        });
        comboBox.setStyle("-fx-background-color: white;-fx-border-color: #D6E1EE;-fx-border-radius: 6;-fx-background-radius: 6;-fx-font-size: 11px;");
    }

    private void buildAttendanceColumns(TableView<StudentAttendanceRow> table,
                                        List<Integer> weeks,
                                        Course course,
                                        Runnable afterToggle) {
        table.getColumns().clear();

        TableColumn<StudentAttendanceRow, String> studentColumn = new TableColumn<>(I18n.t("student.name"));
        studentColumn.setPrefWidth(210);
        studentColumn.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getStudentId() + " (" + data.getValue().getOverallPercentageText() + ")\n"
                        + data.getValue().getStudentName()));
        studentColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                } else {
                    setText(value);
                }
                setStyle("-fx-alignment: CENTER_LEFT;-fx-font-size: 11px;");
            }
        });
        table.getColumns().add(studentColumn);

        for (Integer week : weeks) {
            final int weekNumber = week;
            TableColumn<StudentAttendanceRow, String> weekColumn = new TableColumn<>("W" + weekNumber);
            for (int period = 1; period <= AttendanceCalculator.PERIODS_PER_CLASS; period++) {
                final int periodNumber = period;
                TableColumn<StudentAttendanceRow, String> periodColumn = new TableColumn<>("P" + periodNumber);
                periodColumn.setPrefWidth(52);
                periodColumn.setCellValueFactory(data ->
                        new SimpleStringProperty(data.getValue().getPeriodStatus(weekNumber, periodNumber)));
                periodColumn.setCellFactory(column -> new TableCell<>() {
                    @Override
                    protected void updateItem(String status, boolean empty) {
                        super.updateItem(status, empty);
                        if (empty) {
                            setText(null);
                            setStyle("");
                            setOnMouseClicked(null);
                            return;
                        }
                        setText(AttendanceCalculator.statusSymbol(status));
                        applyStatusStyle(this, status);
                        setOnMouseClicked(event -> {
                            if (event.getClickCount() < 1 || course == null) {
                                return;
                            }
                            try {
                                StudentAttendanceRow row = getTableView().getItems().get(getIndex());
                                PeriodCell cell = row.getPeriodCell(weekNumber, periodNumber);
                                String next = AttendanceCalculator.nextToggleStatus(cell.getStatus());
                                ClassSessionDAO classDAO = new ClassSessionDAO();
                                ClassSession session = classDAO.getOrCreateSession(
                                        course.getId(),
                                        weekNumber,
                                        periodNumber,
                                        LocalDate.now().toString());
                                AttendanceDAO attendanceDAO = new AttendanceDAO();
                                if (next == null) {
                                    attendanceDAO.clearAttendance(row.getDbId(), session.getId());
                                } else {
                                    attendanceDAO.saveAttendance(new Attendance(row.getDbId(), session.getId(), next));
                                }
                                afterToggle.run();
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                showAlert(Alert.AlertType.ERROR, "Attendance Error",
                                        ex.getMessage() != null ? ex.getMessage() : "Could not update attendance.");
                            }
                        });
                    }
                });
                centerColumn(periodColumn);
                weekColumn.getColumns().add(periodColumn);
            }
            table.getColumns().add(weekColumn);
        }

        TableColumn<StudentAttendanceRow, String> percentageColumn = new TableColumn<>(I18n.t("attendance.percent"));
        percentageColumn.setPrefWidth(100);
        percentageColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getOverallPercentageText()));
        centerColumn(percentageColumn);
        table.getColumns().add(percentageColumn);
    }

    private void applyStatusStyle(TableCell<?, String> cell, String status) {
        if (status == null || status.isBlank()) {
            cell.setStyle("-fx-alignment: CENTER;-fx-background-color: #F8FAFD;-fx-text-fill: #8A97A8;-fx-cursor: hand;");
            return;
        }
        switch (status.toLowerCase()) {
            case "present", "late" -> cell.setStyle(
                    "-fx-background-color: #DFF2E1;-fx-text-fill: #2E7D32;-fx-font-weight: bold;-fx-alignment: CENTER;-fx-cursor: hand;-fx-font-size: 14px;");
            case "absent" -> cell.setStyle(
                    "-fx-background-color: #F8D7DA;-fx-text-fill: #B71C1C;-fx-font-weight: bold;-fx-alignment: CENTER;-fx-cursor: hand;-fx-font-size: 14px;");
            default -> cell.setStyle("-fx-alignment: CENTER;-fx-cursor: hand;");
        }
    }

    private void updateNavButtonText(Button button, String text) {
        Node graphic = button.getGraphic();
        if (graphic instanceof HBox box && box.getChildren().size() >= 2
                && box.getChildren().get(1) instanceof Label textLabel) {
            textLabel.setText(text);
        } else {
            button.setText(text);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
        launch();
    }
}
