package JBrailler;


import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.Arrays;

public class PerkinsBraillerTextArea extends TextArea {

    private boolean[] currentCell;
    private ArrayList<KeyEvent> keysPressed;

    public PerkinsBraillerTextArea(){

        currentCell = new boolean[6];
        keysPressed = new ArrayList<>();

        this.setOnKeyTyped(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {

                keyTyped(event);

            }
        });


        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                keyPressed(event);

            }
        });


        this.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                keyReleased(event);

            }
        });

    }

    private void updateText(){

        int caretPosition = this.getCaretPosition();
        String newCell = getCurrentCellAsString();

        String allText = new StringBuilder(this.getText()).insert(caretPosition, newCell).toString();

        this.setText(allText);

        Arrays.fill(currentCell,false);

        this.positionCaret(caretPosition + 1);
    }

    private String getCurrentCellAsString(){

        int cellValue = 0;

        for (int i = 0; i < currentCell.length; i++) {
            if (currentCell[i]){
                cellValue += Math.pow(2,i);
            }
        }

        int totalValue = Integer.parseInt("2800",16) + cellValue;

        return Character.toString((char)(totalValue) );
    }

    private void keyPressed(KeyEvent event){

        if (isRelevantKey(event)) {

            if (isUniqueKey(event)) {

                keysPressed.add(event);

                updateCurrentCell(event);

            }
        }
    }

    private boolean isUniqueKey(KeyEvent event){

        boolean isUniqueKey = true;

        for (KeyEvent keyEvent : keysPressed) {
            if (keyEvent.getText().equals(event.getText())) {
                isUniqueKey = false;
                break;
            }
        }

        return isUniqueKey;
    }

    private void updateCurrentCell(KeyEvent event){
        switch (event.getText()) {
            case "s":
                currentCell[2] = true;
                break;
            case "d":
                currentCell[1] = true;
                break;
            case "f":
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

    }

    private void keyReleased(KeyEvent event){

        if (isRelevantKey(event)) {

            removeFromKeysPressed(event);

            if (noKeysPressed()) {
                updateText();
            }

        }

    }

    private boolean isRelevantKey(KeyEvent event){

        String[] relevantKeys = {"s","d","f"," ","j","k","l"};

        return Arrays.asList(relevantKeys).contains(event.getText());

    }

    private void removeFromKeysPressed(KeyEvent event){
        for (KeyEvent keyEvent : keysPressed) {
            if (keyEvent.getText().equals(event.getText())) {
                keysPressed.remove(keyEvent);
                break;
            }
        }
    }

    private boolean noKeysPressed(){
        return keysPressed.size() == 0;
    }

    private void keyTyped(KeyEvent event){
        // consume key typed events so the letter isn't typed into the text input box
        event.consume();
    }


}
