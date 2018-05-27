/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sithara
 * w165448
 * 2016372
 */
import java.util.Comparator;
import java.util.PriorityQueue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import static java.lang.Math.abs;


public class Main {
    private Label title = new Label("Path Finder");//title
    
    private TextField txtStartX = new TextField (); //text fields to enter values
    private TextField txtStartY = new TextField ();
    private TextField txtEndX = new TextField ();
    private TextField txtEndY = new TextField ();
    
    private Button buttonFind = new Button("Find Path");//control buttons
    private Button btnClose = new Button("Exit");
    
    
    private Label labeStart = new Label("Starting Point :");//labels of the interface
    private Label labeStartX = new Label("X : ");
    private Label labeStartY = new Label("Y :");
    private Label labeEnd = new Label("Ending Point :");
    private Label labeEndX = new Label("X :");
    private Label labeEndY = new Label("Y :");
    
    private Label showCost = new Label();//showing the cost
    private Label showCostLabel = new Label("Cost :");
    
    private Label showTimeElapsed = new Label();//showing the elapsed time
    private Label showTimeLabel = new Label("Time Elapsed :");
    
    private long timeStart = 0;//find the algorithm time
    private long timeEnd = 0;
    private double totalTime = 0;
    
    private BorderPane pane = new BorderPane();
    private VBox vbox = new VBox(); //vbox to add all the controls 

    private GridPane gridPane = new GridPane();
    private ToggleGroup group = new ToggleGroup();
    
    private RadioButton metricIsSelected1 = new RadioButton("Euclidean");//radio buttons to select the matrice type
    private RadioButton metricIsSelected2 = new RadioButton("Manhattan");
    private RadioButton metricIsSelected3 = new RadioButton("Chebyshev");
    
    public String heuristics;
   
    public int Xlength_map;// length of the map which pass through the constructor from StageMenu
    public int Ylength_map;
    
    public int[][] finalMap;//current active map
    
    private Node[][] grid = new Node[Xlength_map][Xlength_map];//creatig the map
    
    private PriorityQueue<Node> openSet;//getting all values of the sorrouding node of current node
                        //aand then sorting them
    
    private CheckBox checkHeuristic = new CheckBox("Heuristic");//check box to add hueristic value to cost
    
    private boolean[][] closedSet;
    private int position_start_Y, position_start_X , position_End_Y, position_End_X;//chpsen postions
    
    public double final_f_cost = 0;//final cost

    private int[][] weightageMap = new int[][]{ //how weightage is shared among all squres / 20x20 2d array
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,2, 1, 2, 1, 1, 1, 1, 1, 1, 1},
            { 4, 4, 1, 4, 1, 1, 1, 2, 2, 2 ,2, 2, 2, 1, 1, 1, 1, 1, 1, 1},
            { 4, 4, 4, 4, 4, 4, 1, 1, 2, 3 ,3, 3, 2, 1, 1, 1, 1, 1, 1, 1},
            { 4, 4, 4, 4, 4, 4, 1, 1, 2, 3 ,3, 3, 3, 2, 1, 1, 1, 1, 1, 1},
            { 1, 1, 4, 1, 1, 1, 1, 1, 2, 2 ,3, 3, 3, 2, 1, 1, 2, 2, 1, 1},
            { 1, 4, 4, 1, 2, 2, 1, 1, 1, 2 ,2, 2, 2, 2, 1, 2, 2, 2, 1, 1},
            { 4, 2, 1, 1, 2, 2, 1, 1, 1, 1 ,1, 1, 1, 1, 1, 2, 2, 1, 1, 1},
            { 1, 2, 1, 2, 1, 1, 1, 1, 1, 1 ,1, 1, 1, 1, 1, 1, 1, 1, 4, 4},
            { 1, 1, 2, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4},
            { 1, 2, 3, 3, 3, 3, 2, 2, 1, 1 ,1, 1, 4, 4, 4, 4, 4, 4, 4, 1},
            { 1, 2, 3, 2, 2, 2, 3, 2, 4, 1 ,1, 1, 4, 4, 4, 4, 1, 1, 1, 1},
            { 1, 2, 2, 1, 1, 1, 4, 4, 4, 4 ,1, 1, 4, 4, 4, 1, 1, 1, 1, 1},
            { 1, 1, 4, 4, 4, 4, 4, 4, 4, 4 ,1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            { 4, 4, 4, 4, 4, 4, 4, 4, 4, 1 ,1, 5, 1, 1, 1, 1, 1, 1, 1, 5},
            { 1, 1, 4, 4, 4, 4, 1, 1, 1, 2 ,1, 5, 5, 1, 1, 1, 1, 1, 1, 5},
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,2, 1, 5, 5, 5, 1, 1, 5, 5, 5},
            { 1, 2, 2, 2, 2, 2, 1, 1, 1, 2 ,1, 1, 1, 5, 5, 5, 5, 5, 5, 5},
            { 2, 2, 1, 1, 1, 1, 1, 1, 1, 1 ,1, 1, 1, 5, 5, 5, 5, 5, 5, 5},
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1, 5, 5, 5, 5, 5, 5, 5, 5, 5},
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1, 5, 5, 5, 5, 5, 5, 5, 5, 5}
    };
       
    private int [][] blocked = new int[][]{//blocked nodes
        {19, 11}, {19, 12}, {19, 13}, {19, 14}, {19, 15}, {19, 16}, {19, 17}, {19, 18}, {19, 19},
        {18, 11}, {18, 12}, {18, 13}, {18, 14}, {18, 15}, {18, 16}, {18, 17}, {18, 18}, {18, 19},
        {17, 13}, {17, 14}, {17, 15}, {17, 16}, {17, 17}, {17, 18}, {17, 19},
        {16, 13}, {16, 14}, {16, 15}, {16, 16}, {16, 17}, {16, 18}, {16, 19},
        {15, 12}, {15, 13}, {15, 14}, {15, 17}, {15, 18}, {15, 19},
        {14, 11}, {14, 12}, {14, 19},
        {13, 11}, {13, 19}
    };
    
    private int[][] doublingArray = new int[][]{ //how weightage is shared among all squres / 40x40 2d array
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 2, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1},
        {1, 1, 1, 1, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1},
        {1, 1, 4, 4, 4, 4, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1},
        {1, 1, 4, 4, 4, 4, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1},
        {4, 4, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1},
        {4, 4, 2, 2, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1},
        {1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4},
        {1, 1, 2, 2, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4},
        {1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4},
        {1, 1, 1, 1, 2, 2, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4},
        {1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1},
        {1, 1, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1},
        {1, 1, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 4, 4, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 1, 1, 1, 1, 1, 1},
        {1, 1, 2, 2, 3, 3, 2, 2, 2, 2, 2, 2, 3, 3, 2, 2, 4, 4, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 2, 2, 1, 1, 1, 1, 1, 1},
        {1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5},
        {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5},
        {1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5},
        {1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 5, 5, 5, 5, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5},
        {1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
        {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5},
            };
            
     public Main(int i) {//i is the curent selected size of the two dimenstional array
    	this.Xlength_map = i;
    	this.Ylength_map = i;
    	if(i == 20 ) { //if i equals 20 whole class will be using 20 dimentional array
    		finalMap = weightageMap;
    	} else if (i == 40) {
    		finalMap = doublingArray;
    	}else if(i == 80){
          //  finalMap = 
        }
		
	}
    
    
    public void start(Stage primaryStage) throws Exception {//creating the view and placing the components

        FlowPane TopLayer = new FlowPane(Orientation.HORIZONTAL, 60, 3);
        TopLayer.setPadding(new Insets(20));
        TopLayer.setAlignment(Pos.CENTER);
        TopLayer.getChildren().addAll(title);
      
        FlowPane starting = new FlowPane(Orientation.HORIZONTAL, 60, 3);
        starting.setPadding(new Insets(20));
        starting.setAlignment(Pos.CENTER);
        starting.getChildren().addAll(labeStartX ,txtStartX,labeStartY,txtStartY);
      
        FlowPane ending = new FlowPane(Orientation.HORIZONTAL, 60, 3);
        ending.setPadding(new Insets(20));
        ending.setAlignment(Pos.CENTER);
        ending.getChildren().addAll(labeEndX,txtEndX,labeEndY,txtEndY);
        
        FlowPane controls = new FlowPane(Orientation.HORIZONTAL, 60, 3);
        controls.setPadding(new Insets(20));
        controls.setAlignment(Pos.CENTER);
        controls.getChildren().addAll(btnClose,buttonFind);
        
        
        metricIsSelected1.setToggleGroup(group);
        metricIsSelected2.setToggleGroup(group);
        metricIsSelected3.setToggleGroup(group);
        
        FlowPane selectingMatrices = new FlowPane(Orientation.HORIZONTAL, 60, 3);
        selectingMatrices.setPadding(new Insets(20));
        selectingMatrices.setAlignment(Pos.CENTER);
        selectingMatrices.getChildren().addAll(metricIsSelected1,metricIsSelected2,metricIsSelected3);
        
        FlowPane showingCost = new FlowPane(Orientation.HORIZONTAL, 60, 3);
        showingCost.setPadding(new Insets(20));
        showingCost.setAlignment(Pos.CENTER);
        showingCost.getChildren().addAll( showCostLabel , showCost , showTimeLabel, showTimeElapsed);
        
        vbox.getChildren().addAll(selectingMatrices,checkHeuristic, labeStart,starting,labeEnd,ending,showingCost ,controls);
        vbox.setAlignment(Pos.CENTER);


        for (int i = 0; i < Xlength_map; i++) {
            ColumnConstraints constraints = new ColumnConstraints();//creating the map
            constraints.setHgrow(Priority.SOMETIMES);
            constraints.setMinWidth(10.00);
            constraints.setPrefWidth(10.00);
            gridPane.getColumnConstraints().add(constraints);
        }
        
        for (int i = 0; i < Xlength_map; i++) {
            RowConstraints constraints = new RowConstraints();
            constraints.setVgrow(Priority.SOMETIMES);
            constraints.setMinHeight(10.00);
            constraints.setPrefHeight(10.00);
            gridPane.getRowConstraints().add(constraints);
        }

        
       SettingGrid();
       pane.setBottom(vbox);
       pane.setTop(TopLayer);
       pane.setCenter(gridPane);

        //close button action
        btnClose.setOnAction((ActionEvent e) -> {
            primaryStage.close();
        });
 
        //radio button ation
        buttonFind.setOnAction((ActionEvent e) -> {
            if(metricIsSelected1.isSelected()){
             heuristics = "Euclidean";
            } else if(metricIsSelected2.isSelected()){
                   heuristics = "Manhattan";
            } else if(metricIsSelected3.isSelected()){
                    heuristics = "Chebyshev";
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error!!!!");
                alert.setContentText("Please Select a Metric Unit");
                alert.showAndWait();
                return;
            }
            
        buttonFind.setDisable(true);//once pressed find button disble it and getting all the values
        position_start_Y = Integer.parseInt(txtStartY.getText());
        position_start_X  = Integer.parseInt(txtStartX.getText());
        position_End_Y  = Integer.parseInt(txtEndY.getText());
        position_End_X = Integer.parseInt(txtEndX.getText());
        
        if(position_start_X > Xlength_map || position_start_Y > Ylength_map || position_End_Y > Ylength_map || position_End_X > Xlength_map){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Error!!!!");
                alert.setContentText("Enter a Value Between 1 and 20!!!!");
                alert.showAndWait();

                position_start_X = 0;
                position_start_Y = 0;
                position_End_Y = 0;
                position_End_X = 0;

                buttonFind.setDisable(false);
                primaryStage.close();
                primaryStage.show();

            }else{
                timeStart = System.nanoTime();
                FindingTheShotestPath();
                timeEnd = System.nanoTime();
      
                showCost.setText(Double.toString(final_f_cost));
                final_f_cost = 0;
                totalTime = (double)(timeEnd - timeStart)/100000;
                showTimeElapsed.setText(Double.toString(totalTime) + " ms");
                System.out.println("Total Time : " + totalTime + " ms");            
            }        
        });

       Scene scene = new Scene(pane,750, 800);//setting the stage
       primaryStage.setScene(scene);
       primaryStage.setMinHeight(950);
       primaryStage.setMinWidth(750);
       primaryStage.setMaxHeight(850);
       primaryStage.setMaxWidth(950);
       primaryStage.show();
    }

    //implementing the A* Algoritham
      public void Algo_A_Star(){ 
        Node currentNode;
        while(true){ 
            currentNode = openSet.poll();
            if(currentNode==null)break;
            closedSet[currentNode.y][currentNode.x]=true;
            if(currentNode.equals(grid[position_End_Y][position_End_X])){
                
                System.out.print(currentNode.g_cost);
                final_f_cost = currentNode.g_cost;  
               
                return;
            } 

            Node node;
            if(currentNode.y-1>=0){
                node = grid[currentNode.y-1][currentNode.x];
                UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
                if(currentNode.x-1>=0){                      
                    node = grid[currentNode.y-1][currentNode.x-1];
                    UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
                }
                if(currentNode.x+1<grid[0].length){
                    node = grid[currentNode.y-1][currentNode.x+1];
                    UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
                }
            }
            if(currentNode.x-1>=0){
                node = grid[currentNode.y][currentNode.x-1];
                UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
            }
            if(currentNode.x+1<grid[0].length){
                node = grid[currentNode.y][currentNode.x+1];
                UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
            }
            if(currentNode.y+1<grid.length){
                node = grid[currentNode.y+1][currentNode.x];
                UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));

                if(currentNode.x-1>=0){
                    node = grid[currentNode.y+1][currentNode.x-1];
                    UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
                }
                
                if(currentNode.x+1<grid[0].length){
                   node = grid[currentNode.y+1][currentNode.x+1];
                    UpdateCost(currentNode, node, currentNode.g_cost+getNodeWeight(node));
                }  
            }
        } 
    }
      
      //intializing the grid and setting the colors
    public void SettingGrid(){
        for(int i = 0 ; i < Xlength_map ; i ++){
            for(int j = 0 ; j < Xlength_map ; j ++){
                Pane t = new Pane();
                if (finalMap[i][j] == 1){
                    t.setStyle(" -fx-background-color: rgba(255,255,255,0);");
                    gridPane.add(t,j,i);
                }
                if (finalMap[i][j] == 2){
                    t.setStyle(" -fx-background-color: #cdcdcd;");
                    gridPane.add(t,j,i);
                }
                if (finalMap[i][j] == 3){
                    t.setStyle(" -fx-background-color: #8f8f8f;");
                    gridPane.add(t,j,i);
                }
                if (finalMap[i][j] == 4){
                    t.setStyle(" -fx-background-color: #636363;");
                    gridPane.add(t,j,i);
                }
                if (finalMap[i][j] == 5){
                    t.setStyle(" -fx-background-color: #000000;");
                    gridPane.add(t,j,i);
                }
            }
        }
        gridPane.gridLinesVisibleProperty().setValue(true);
    }

   

//finding the huersitic cost
   public double hCost(int i , int j){
       double cost = 0;
       if(checkHeuristic.isSelected()){
           
       
            if (heuristics.equals("Euclidean")) {
                double finalX = Math.pow(j-position_End_X,2);
                double finalY = Math.pow(i-position_End_Y,2);
                cost = Math.sqrt(finalX+finalY);
            } else if (heuristics.equals("Manhattan")) {
                 cost =  abs(i-position_End_Y)+abs(j-position_End_X);
            } else if (heuristics.equals("Chebyshev")) {
                double finalX = Math.abs(j-position_End_X);
                double finalY = Math.abs(i-position_End_Y);
                cost = Math.max(finalX,finalY);
            }
        return cost;
   
}else {
    return 0;
}
   }

   //getting the weightage of the node
   public double getNodeWeight(Node node){
       double weightage = 0;

       if(node != null){
           weightage = finalMap[node.y][node.x];
       }
       return weightage;
   }

   //updating the current cost
 public void UpdateCost(Node currentNode, Node node, double cost){
        if(node == null || closedSet[node.y][node.x]){
            return;
        }
        double f_Cost = node.heuristicCost + cost;
        boolean inOpen = openSet.contains(node);
        if(!inOpen || f_Cost<node.f_Cost){
            node.f_Cost = node.heuristicCost+cost;
            node.g_cost = cost;
            node.parentNode = currentNode;
     
            if(!inOpen){
                openSet.add(node);
            }
        }
    }
  

 //finding the shortest path in the grid using a* algorithm
    public void FindingTheShotestPath(){
           grid = new Node[Xlength_map][Ylength_map];
           closedSet = new boolean[Xlength_map][Ylength_map];
           openSet = new PriorityQueue<>(new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                Node c1 = (Node)o1;
                Node c2 = (Node)o2;

                if (c1.f_Cost < c2.f_Cost) {
                    return -1;
                } else if (c1.f_Cost > c2.f_Cost) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });



        for(int i=0; i < Xlength_map;++i){
              for(int j=0;j<Ylength_map;++j){
                  grid[i][j] = new Node(i, j);
                  grid[i][j].heuristicCost =  hCost(i,j);
              }
           }
           
        //cost of blocked nodes
          grid[position_start_Y][position_start_X].f_Cost = 0;
           for(int i=0;i<blocked.length;++i){
                grid[blocked[i][0]][blocked[i][1]] = null;
           }
           
            openSet.add(grid[position_start_Y][position_start_X]);
 
           
           Algo_A_Star(); 
           
           if(closedSet[position_End_Y][position_End_X]){
               System.out.println("Metric used : " + heuristics);
               System.out.println();
               System.out.println("Path: ");

                Node current = grid[position_End_Y][position_End_X];

                while(current.parentNode!=null){
                    Pane markerPane = new Pane();
                    markerPane.setStyle(" -fx-background-color: #bf0500;");
                    gridPane.add(markerPane, current.x, current.y);
                    current = current.parentNode;
                }


               Pane marker = new Pane();
               Pane marker2 = new Pane();
               marker.setStyle(" -fx-background-color: #09bf00;");
               marker2.setStyle(" -fx-background-color: #09bf00;");
               gridPane.add(marker2, position_start_X, position_start_Y);
               gridPane.add(marker, position_End_X, position_End_Y);


           }else { //showing the alert if path isn't available
               Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Sorry!!!!");
                alert.setContentText("No possible Path Available");
                alert.showAndWait();System.out.println("No possible Path Available");
           }
    }

       
    
}
