package JBrailler.SampleApp;

import JBrailler.PerkinsBraillerTextArea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Controller {

    @FXML
    public GridPane grid;

    @FXML
    public PerkinsBraillerTextArea brailleTextInput;


    public void save(ActionEvent actionEvent) {

        FileChooser fileChooser = getFileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(grid.getScene().getWindow());

        if (file != null) {
            try ( PrintWriter out = new PrintWriter(file.getAbsolutePath()) ){
                out.println( brailleTextInput.getText() );
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public void open(ActionEvent actionEvent) {
        FileChooser fileChooser = getFileChooser();
        fileChooser.setTitle("Open File");
        File file = fileChooser.showOpenDialog(grid.getScene().getWindow());

        if (file != null) {
            try{
                byte[] encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
                String content = new String(encoded, StandardCharsets.UTF_8);
                brailleTextInput.setText(content);
                brailleTextInput.positionCaret(brailleTextInput.getLength());
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    public FileChooser getFileChooser(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("*.txt");

        return fileChooser;
    }
}
