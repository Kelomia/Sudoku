/*
@ Author: Zimu Jiao
  SWID:1458119
 */
package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;

public class Main extends Application {
    // Inner class of Sudoku:
    static class Sudoku{
        char[][] Board;
        char[] checklist=new char[9];

        /*
        Sudoku(){
            Board=new char[9][9];
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    Board[i][j]= (char)((i+j)%9+49);
                }
            }
        }
        non-Constructor is NOT completed.
        */

        Sudoku(String filename) throws Exception {
            File file=new File(filename);
            BufferedReader BR=new BufferedReader(new FileReader(file));
            Board = new char[9][9];
            String line=BR.readLine();
            String nums;
            boolean END=false;
            int i=0,j=0;
            do{
                // Assume all blank are filled, valid file_input should only contain [1-9],
                // space" " and end_signal"#"
                nums=line.replace(" ","");
                while (j<9 && !END){ // Warning, but i didn't find error case:
                    char temp=nums.charAt(j);
                    if(temp=='#') {
                        END=true;
                        break;
                    }
                    try{
                        if(isValid(temp)){
                            Board[i][j]=temp;
                        }else{
                            throw new Exception("At line"+(i+1)+" has invalid value:"+temp);
                            // Throw out the Invalid value position
                        }
                    }finally {
                        j++;
                    }
                }
                j=0;
                i++;
                line=BR.readLine();
                if(i==9)    // If input 9 line value, end loop:
                    break;
            }while(!END);    // When meet End Signal, OR i=9, End the loop
            System.out.println("Load Completed.");
            BR.close();
        }

        public boolean isValid(char ch){
            return ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9' || ch == ' ';
        }

        public String printGame(){
            String game="";
            for(int i=0;i<9;i++){
                if((i%3==0)){
                    game=game.concat("----------------------\n");
                }
                for(int j=0;j<9;j++){
                    if((j%3==0)){
                        game=game.concat("|");
                    }
                    if(Board[i][j]==0){
                        game=game.concat("   ");
                    }else{
                        game=game.concat(" "+ Board[i][j]);
                    }
                }
                game=game.concat(" |\n");
            }
            return game;
        }

        public void SetCheckList(){
            checklist= new char[]{'1','2','3','4','5','6','7','8','9'};
        }
        public boolean checkRow(int index){
            for(int cur=0;cur<9;cur++) {
                for (int i = cur+1; i < 9; i++) {
                    if (Board[index][i] == Board[index][cur]) {
                        System.out.println("At Row"+(1+index)+" Exist already:"+Board[index][i]);
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean checkCol(int index){
            for(int cur=0;cur<9;cur++) {
                for (int i = cur+1; i < 9; i++) {
                    if (Board[i][index] == Board[cur][index]) {
                        System.out.println("At Col"+(1+index)+" Exist already:"+Board[i][index]);
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean checkWin() {
            // check missing
            SetCheckList();
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(Board[i][j]==' '||Board[i][j]=='0'){
                        return false;
                    }
                }
            }
            // check row and col:
            for(int i=0;i<9;i++){
                if(!checkCol(i)){
                    return false;
                }
                if(!checkRow(i)){
                    return false;
                }
            }
            // 9_Cube check, which I think is not necessary
            return true;
        }
    }


    @Override
    public void start(Stage primaryStage)  {
        Text text = new Text("--Sudoku--");
        Button Load=new Button("Load File");
        Button showReasult=new Button("Submit");

        GridPane gridPane=new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(20);
        gridPane.add(text,0,0);
        gridPane.add(Load,1,0);

        // Load file and print Game:
        final Sudoku[] sudoku = new Sudoku[1];
        Load.setOnAction(event->{
            try{
                sudoku[0] = new Sudoku("Sudoku.txt");
                Text game=new Text(sudoku[0].printGame());
                gridPane.add(game,0,3);
                gridPane.add(showReasult,2,3);
            } catch (Exception e) {
                Text error=new Text(e.getMessage());
                gridPane.add(error,1,1);
            }

        });
        showReasult.setOnAction(event->{
            Text Win;
            if(sudoku[0].checkWin()){
                Win=new Text("Completed!");
            }else{
                Win=new Text("Not Yet--");
            }
            gridPane.add(Win,3,3);
        });


        Scene scene=new Scene(gridPane,500,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sudoku");
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
