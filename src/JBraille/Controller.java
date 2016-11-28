package JBraille;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

    boolean[] currentCell = new boolean[6];
    ArrayList<KeyEvent> keysPressed = new ArrayList<>();

    @FXML
    public TextArea textInput;

    @FXML
    public GridPane grid;


    public void updateText(){

        int caretPosition = textInput.getCaretPosition();
        String newCell = getCurrentCellAsString();

        String allText = new StringBuilder(textInput.getText()).insert(caretPosition, newCell).toString();

        textInput.setText(allText);

        Arrays.fill(currentCell,false);

        textInput.positionCaret(caretPosition + 1);
    }
    
    public String getCurrentCellAsString(){
        
        int cellValue = 0;

        for (int i = 0; i < currentCell.length; i++) {
            if (currentCell[i]){
                cellValue += Math.pow(2,i);
            }
        }

        int totalValue = Integer.parseInt("2800",16) + cellValue;
        
        return Character.toString((char)(totalValue) );
    }

    public void keyPressed(KeyEvent event){

        if (isRelevantKey(event)) {

            if (isUniqueKey(event)) {

                keysPressed.add(event);

                updateCurrentCell(event);

            }
        }
    }

    public boolean isUniqueKey(KeyEvent event){

        boolean isUniqueKey = true;

        for (KeyEvent keyEvent : keysPressed) {
            if (keyEvent.getText().equals(event.getText())) {
                isUniqueKey = false;
                break;
            }
        }

        return isUniqueKey;
    }

    public void updateCurrentCell(KeyEvent event){
        switch (event.getText()) {
            case "a":
                currentCell[2] = true;
                break;
            case "s":
                currentCell[1] = true;
                break;
            case "d":
                currentCell[0] = true;
                break;
            case "j":
                currentCell[3] = true;
                break;
            case "k":
                currentCell[4] = true;
                break;
            case "l":
                currentCell[5] = true;
                break;
            case " ":
                break;
        }

        return;
    }

    public void keyReleased(KeyEvent event){

        if (isRelevantKey(event)) {

            removeFromKeysPressed(event);

            if (noKeysPressed()) {
                updateText();
            }

        }

    }

    public boolean isRelevantKey(KeyEvent event){

        String[] keysICareAbout = {"a","s","d"," ","j","k","l"};

        return Arrays.asList(keysICareAbout).contains(event.getText());

    }

    public void removeFromKeysPressed(KeyEvent event){
        for (KeyEvent keyEvent : keysPressed) {
            if (keyEvent.getText().equals(event.getText())) {
                keysPressed.remove(keyEvent);
                break;
            }
        }
        return;
    }

    public boolean noKeysPressed(){
        return keysPressed.size() == 0;
    }

    public void keyTyped(KeyEvent event){
        // consume key typed events so the letter isn't typed into the text input box
        event.consume();
    }


    public void save(ActionEvent actionEvent) {

        FileChooser fileChooser = getFileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(grid.getScene().getWindow());

        if (file != null) {
            try ( PrintWriter out = new PrintWriter(file.getAbsolutePath()) ){
                out.println( textInput.getText() );
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
                textInput.setText(content);
                textInput.positionCaret(textInput.getLength());
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
