module com.myprojects325 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;


    opens com.myprojects325 to javafx.fxml;
    exports com.myprojects325;
    exports matrix.largematrix;
    exports matrix.mediummatrix;
    exports matrix.smallmatrix;
    exports matrix.xlmatrix;
}
