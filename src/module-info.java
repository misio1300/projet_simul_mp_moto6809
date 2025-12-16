module MicroMOTO {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;
	
	opens simul.moto to javafx.graphics, javafx.fxml;
}
