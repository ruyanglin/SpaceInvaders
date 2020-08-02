export FILENAME=SpaceInvaders
export JAVA_FX_HOME='/usr/local/lib/javafx/javafx-sdk-11.0.2'

default:
	javac --module-path $(JAVA_FX_HOME)/lib --add-modules javafx.controls,javafx.graphics,javafx.base,javafx.fxml,javafx.media *.java

run: default
	java --module-path $(JAVA_FX_HOME)/lib --add-modules javafx.controls,javafx.graphics,javafx.base,javafx.fxml,javafx.media $(FILENAME)

clean:
	rm *.class