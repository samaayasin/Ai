package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.ArrayList;
import java.util.Scanner;

public class GameController {
    public TextField removeStick2;
    public Label nameLable;
    private NimGame nimGame;
    private AlphaBetaAlgorithm alphaBeta;
    private EvaluationFunction E;
    boolean remove1flag =false;
    boolean addstones= false;
    int difficultyLevel=0;
    boolean easy= false;
    boolean m= false;
    boolean hard= false;


    public GameController() {
        this.nimGame = new NimGame(new int[]{});
        this.alphaBeta = new AlphaBetaAlgorithm();
        this.E = new EvaluationFunction();

    }


    @FXML
    private Button ExitbuttoLose;

    @FXML
    private Button Exitbutton;

    @FXML
    private Button Exitbutton1;

    @FXML
    private Button Exitbutton2;

    @FXML
    private Button ExitbuttonWin;

    @FXML
    private AnchorPane LoseScreen;

    @FXML
    private TextField Name;

    @FXML
    private Button Nextbutton;

    @FXML
    private AnchorPane WinScreen;

    @FXML
    public Button addScreen2;

    @FXML
    private TextArea easyRule;

    @FXML
    public RadioButton easybutton;

    @FXML
    private TextArea hardRule;

    @FXML
    public RadioButton hardbutton;

    @FXML
    private TextArea heapState;

    @FXML
    private TextArea medRule1;

    @FXML
    public RadioButton medbutton;

    @FXML
    private Button nextScreen2;

    @FXML
    public TextField numberOfHeap;

    @FXML
    public TextField numberOfStick;

    @FXML
    private Button remove1;

    @FXML
    private Button remove2;

    @FXML
    public TextField removeHeap1;

    @FXML
    private TextField removeHeap2;

    @FXML
    public TextField removeStick1;

    @FXML
    private AnchorPane screen1;

    @FXML
    private AnchorPane screen2;

    @FXML
    private AnchorPane screen3;

    void hideAllPanels(){
        screen1.setVisible(true);
        screen2.setVisible(false);
        screen3.setVisible(false);
        WinScreen.setVisible(false);
        LoseScreen.setVisible(false);
    }

    @FXML
    public void OnExitbuttonClicked(MouseEvent event) {
        if(event.getSource()==Exitbutton) {
            Window window = ((Node) (event.getSource())).getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }

    }

    public void OnNextbuttonClicked(MouseEvent mouseEvent) {
        screen1.setVisible(false);
        screen2.setVisible(true);

    }

    public void OnExitbutton1Clicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==Exitbutton1) {
            Window window = ((Node) (mouseEvent.getSource())).getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
    }

    public void OnExitbuttonWinClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==ExitbuttonWin) {
            Window window = ((Node) (mouseEvent.getSource())).getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
    }

    public void OnExitbuttonLoseClicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==ExitbuttoLose) {
            Window window = ((Node) (mouseEvent.getSource())).getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
    }

    public void OnRemove1Clicked(MouseEvent mouseEvent) {
        remove1flag=true;
        moveBasedOnLevel(nimGame,difficultyLevel);
        if(nimGame.isGameOver()){
            screen3.setVisible(false);
            displayFinalGameResult();
        }
    }




    public void OnExitbutton2Clicked(MouseEvent mouseEvent) {
        if(mouseEvent.getSource()==Exitbutton2) {
            Window window = ((Node) (mouseEvent.getSource())).getScene().getWindow();
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        }
    }

    public void OnEasybuttonClicked(MouseEvent mouseEvent) {
        medRule1.setVisible(false);
        easyRule.setVisible(true);
        hardRule.setVisible(false);
        easy=true;
        difficultyLevel =1;
    }

    public void OnMedbuttonClicked(MouseEvent mouseEvent) {
        medRule1.setVisible(true);
        medRule1.setStyle("-fx-text-alignment: center;");
        easyRule.setVisible(false);
        hardRule.setVisible(false);
        m=true;
        difficultyLevel =2;
    }

    public void OnHardbuttonClicked(MouseEvent mouseEvent) {
        medRule1.setVisible(false);
        easyRule.setVisible(false);
        hardRule.setVisible(true);
        hard=true;
        difficultyLevel =3;

    }

    public void OnNextScreen2Clicked(MouseEvent mouseEvent) {
        screen1.setVisible(false);
        screen2.setVisible(false);
        screen3.setVisible(true);
        nameLable.setText(Name.getText());
        startGame();
    }

    public void OnAddScreen2Clicked(MouseEvent mouseEvent) {
        // screen3.setVisible(true);
        addstones= true;

    }
/////////////////////////////////////////////////////////////////////////////////////////////////

    public void startGame() {


        if(numberOfHeap !=null) {
            int numberOfHeaps = Integer.parseInt(numberOfHeap.getText());
            ArrayList<Integer> stickList = new ArrayList<>();
            if(numberOfStick !=null) {
                String numberOfStickText = numberOfStick.getText();
                String[] stickValues = numberOfStickText.split(" ");
                for (String value : stickValues) {
                    assert numberOfStick != null;
                    stickList.add(Integer.parseInt(value));
                }
                System.out.println(stickList);
            }
            nimGame.setHeapsizes(stickList);
        }
        System.out.println(difficultyLevel);

        while (!nimGame.isGameOver()) {
            displayGameState(nimGame);
            moveBasedOnLevel(nimGame,difficultyLevel);
        }
        displayFinalGameResult();
    }

    public int moveBasedOnLevel(NimGame g, int difficultyLevel) {
        switch (difficultyLevel) {
            case 1:
                return easyMove(g);
            case 2:
                return mediumMove(g);
            case 3:
                return hardMove(g);
            default:
                throw new IllegalArgumentException("Invalid difficulty level");
        }
    }

    public int easyMove(NimGame game) {
        boolean valid=true;
        while (!game.isGameOver()) {
            while (nimGame.getCurrentPlayer()==1) {
                if (removeHeap1 != null) {
                    int heapIndex = Integer.parseInt(removeHeap1.getText());
                    int maxStonesToRemove = 5;
                    if (removeStick1 != null) {
                        int stonesToRemove = Integer.parseInt(removeStick1.getText());
                        if ((stonesToRemove < 1) || (stonesToRemove > maxStonesToRemove) ||(stonesToRemove > nimGame.getHeapsizes().get(heapIndex))) {
                            valid=false;
                            heapState.setText("Invalid input.\n Please enter a number between 1 and " + maxStonesToRemove );
                            removeStick1.clear();
                        }
                        if (remove1flag == true && valid==true) {
                            game.move(heapIndex, stonesToRemove);
                            removeStick1.clear();
                            removeHeap1.clear();
                            heapState.setText("Player 1 moves " + stonesToRemove + " stone(s) from heap " + heapIndex);
                            displayGameState(game);
                        }
                    }
                }
                if (game.isGameOver()) {
                    break;
                }
            }
            /////ai turn
            int aiStonesToRemove = alphaBeta.alphabeta(game, 3, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            removeStick2.setText(""+aiStonesToRemove);
            removeHeap2.setText(""+alphaBeta.heapIndexforAi);
            game.move(alphaBeta.heapIndexforAi, aiStonesToRemove);
            heapState.appendText("\nAI moves " + aiStonesToRemove + " stone(s) from heap "+alphaBeta.heapIndexforAi);
            displayGameState(game);

        }

        return 0;
    }



    public int mediumMove(NimGame game) {
        boolean valid=true;
        while (!game.isGameOver()) {
            while (nimGame.getCurrentPlayer()==1) {
                if (removeHeap1 != null) {
                    int heapIndex = Integer.parseInt(removeHeap1.getText());
                    int maxStonesToRemove = 3;
                    if (removeStick1 != null) {
                        int stonesToRemove = Integer.parseInt(removeStick1.getText());
                        if ((stonesToRemove < 1) || (stonesToRemove > maxStonesToRemove) ||(stonesToRemove > nimGame.getHeapsizes().get(heapIndex))) {
                            valid=false;
                            heapState.setText("Invalid input.\n Please enter a number between 1 and " + maxStonesToRemove);
                            removeStick1.clear();
                        }
                        if (remove1flag == true && valid==true) {
                            game.move(heapIndex, stonesToRemove);
                            removeStick1.clear();
                            removeHeap1.clear();
                            heapState.setText("Player 1 moves " + stonesToRemove + " stone(s) from heap " + heapIndex);
                            displayGameState(game);
                        }
                    }
                }
                if (game.isGameOver()) {
                    break;
                }
            }
            /////ai turn
            int aiStonesToRemove = alphaBeta.alphabeta(game, 5, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            removeStick2.setText(""+aiStonesToRemove);
            removeHeap2.setText(""+alphaBeta.heapIndexforAi);
            game.move(alphaBeta.heapIndexforAi, aiStonesToRemove);
            heapState.appendText("\nAI moves " + aiStonesToRemove + " stone(s) from heap "+alphaBeta.heapIndexforAi);
            displayGameState(game);

        }

        return 0;
    }

    public int hardMove(NimGame game) {
        boolean valid=true;
        while (!game.isGameOver()) {
            while (nimGame.getCurrentPlayer()==1) {
                if (removeHeap1 != null) {
                    int heapIndex = Integer.parseInt(removeHeap1.getText());
                    int maxStonesToRemove = 2;
                    if (removeStick1 != null) {
                        int stonesToRemove = Integer.parseInt(removeStick1.getText());
                        if ((stonesToRemove < 1) || (stonesToRemove > maxStonesToRemove) ||(stonesToRemove > nimGame.getHeapsizes().get(heapIndex))) {
                            valid=false;
                            heapState.setText("Invalid input.\n Please enter a number between 1 and " + maxStonesToRemove );
                            removeStick1.clear();
                        }
                        if (remove1flag == true && valid==true) {
                            game.move(heapIndex, stonesToRemove);
                            removeStick1.clear();
                            removeHeap1.clear();
                            heapState.setText("Player 1 moves " + stonesToRemove + " stone(s) from heap " + heapIndex);
                            displayGameState(game);
                        }
                    }
                }
                if (game.isGameOver()) {
                    break;
                }
            }
            /////ai turn
            int aiStonesToRemove = alphaBeta.alphabeta(game, 7, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            removeStick2.setText(""+aiStonesToRemove);
            removeHeap2.setText(""+alphaBeta.heapIndexforAi);
            game.move(alphaBeta.heapIndexforAi, aiStonesToRemove);
            heapState.appendText("\nAI moves " + aiStonesToRemove + " stone(s) from heap "+alphaBeta.heapIndexforAi);
            displayGameState(game);

        }

        return 0;
    }

    public void displayGameState(NimGame game) {
        heapState.appendText("\nCurrent heap sizes: " + game.getHeapsizes());
    }
    private void displayFinalGameResult() {
        int gameResult = E.evaluate(nimGame);

        if (gameResult < 0) {
            WinScreen.setVisible(true);
        }
        else if (gameResult > 0) {
            LoseScreen.setVisible(true);
        }
    }


    public void OnNextbuttonEntered(MouseEvent mouseEvent) {
        Nextbutton.setStyle("-fx-opacity:0.2");

    }

    public void OnNextbuttonExit(MouseEvent mouseEvent) {

        Nextbutton.setStyle("-fx-background-color:  transparent;");
    }

    public void OnAddScreen2Enter(MouseEvent mouseEvent) {
        addScreen2.setStyle("-fx-opacity:0.2");
    }

    public void OnAddScreen2Exit(MouseEvent mouseEvent) {
        addScreen2.setStyle("-fx-background-color:  transparent;");
    }

    public void OnNextScreen2Enter(MouseEvent mouseEvent) {
        nextScreen2.setStyle("-fx-opacity:0.2");
    }

    public void OnNextScreen2Exit(MouseEvent mouseEvent) {
        nextScreen2.setStyle("-fx-background-color:  transparent;");

    }

    public void OnRemove1Enter(MouseEvent mouseEvent) {
        remove1.setStyle("-fx-opacity:0.2");

    }

    public void OnRemove1Exit(MouseEvent mouseEvent) {
        remove1.setStyle("-fx-background-color:  transparent;");

    }

    public void OnRemove2Enter(MouseEvent mouseEvent) {
        remove2.setStyle("-fx-opacity:0.2");

    }

    public void OnRemove2Exit(MouseEvent mouseEvent) {
        remove2.setStyle("-fx-background-color:  transparent;");

    }

    public void OnExitbuttonEnter(MouseEvent mouseEvent) {
        Exitbutton.setStyle("-fx-opacity:0.2");
    }

    public void OnExitbuttonExit(MouseEvent mouseEvent) {
        Exitbutton.setStyle("-fx-background-color:  transparent;");

    }

    public void OnExitbutton1Enter(MouseEvent mouseEvent) {
        Exitbutton1.setStyle("-fx-opacity:0.2");

    }

    public void OnExitbutton1Exit(MouseEvent mouseEvent) {
        Exitbutton1.setStyle("-fx-background-color:  transparent;");

    }

    public void OnExitbutton2Enter(MouseEvent mouseEvent) {
        Exitbutton2.setStyle("-fx-opacity:0.2");

    }

    public void OnExitbutton2Exit(MouseEvent mouseEvent) {
        Exitbutton2.setStyle("-fx-background-color:  transparent;");

    }

    public void OnExitbuttonWinEnter(MouseEvent mouseEvent) {
        ExitbuttonWin.setStyle("-fx-opacity:0.2");

    }

    public void OnExitbuttonWinExit(MouseEvent mouseEvent) {
        ExitbuttonWin.setStyle("-fx-background-color:  transparent;");

    }

    public void OnExitbuttonLoseEnter(MouseEvent mouseEvent) {
        ExitbuttoLose.setStyle("-fx-opacity:0.2");
    }

    public void OnExitbuttonLoseExit(MouseEvent mouseEvent) {
        ExitbuttoLose.setStyle("-fx-background-color:  transparent;");

    }

    public void OnRemove2Clicked(MouseEvent mouseEvent) {
    }
}
