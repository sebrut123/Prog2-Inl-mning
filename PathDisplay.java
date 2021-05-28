import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PathDisplay extends Application {
    //private FlowPane center;
    private Pane center;
    private Stage stage;
    private ListGraph<Nodes.GraphNode> graph;
    private Symbol s1=null, s2=null;
    ArrayList<Symbol> symboler;
    ClickHandler cl;
    Button findPathButton;
    Button showConnectionButton;
    Button newPlaceButton;
    Button newConnectionButton;
    Button changeConnectionButton;


    @Override public void start(Stage stage){
        graph = new ListGraph<>();
        cl=new ClickHandler();

        this.stage=stage;
        BorderPane root = new BorderPane();
        //center = new FlowPane(); //center är vår panel
        center = new Pane();
        center.setStyle("-fx-font-size: 15");
        //center.setAlignment(Pos.CENTER);
        root.setCenter(center);

        FlowPane flowPaneTop = new FlowPane();
        flowPaneTop.setAlignment(Pos.CENTER);
        flowPaneTop.setHgap(8);
        flowPaneTop.setPadding(new Insets(5));

        findPathButton = new Button("Find Path");
        showConnectionButton = new Button("Show Connection");
        showConnectionButton.setId("btnShowConnection");
        showConnectionButton.setOnAction(new showConnectionHandler());
        newPlaceButton = new Button("New Place");
        newPlaceButton.setOnAction(new NewPlaceHandler());
        newConnectionButton = new Button("New Connection");
        newConnectionButton.setOnAction(new NewConnectionHandler());
        changeConnectionButton = new Button("Change Connection");
        changeConnectionButton.setOnAction(new showConnectionHandler());
        changeConnectionButton.setId("btnChangeConnection");

        flowPaneTop.getChildren().addAll(findPathButton,showConnectionButton,newPlaceButton,
                newConnectionButton,changeConnectionButton);

        BorderPane topBar = new BorderPane();
        topBar.setStyle("-fx-font-size: 15");
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        menuBar.getMenus().add(fileMenu);
        MenuItem newMap = new MenuItem("New Map");
        newMap.setOnAction(new NewMapHandler());
        MenuItem open = new MenuItem("Open");
        open.setOnAction(new OpenHandler());
        MenuItem save = new MenuItem("Save");
        MenuItem saveImage = new MenuItem("Save Image");
        MenuItem exit = new MenuItem("Exit");
        fileMenu.getItems().addAll(newMap,open,save,saveImage,exit);

        topBar.setBottom(flowPaneTop);
        topBar.setTop(menuBar);
        root.setTop(topBar);

        Scene scene = new Scene(root);
        stage.setTitle("Path Display");
        stage.setScene(scene);
        stage.show();
    }

    class NewMapHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            Image image = new Image("file:/Users/sebastian/IdeaProjects/Inlämningsuppgift del 2 prog 2/europa.gif");
            ImageView imageView = new ImageView(image);
            center.getChildren().add(imageView);
            stage.sizeToScene();
        }
    }

    class OpenHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try {
                Map<Nodes.LocationNode, Set<Edge>> cityGraph = new HashMap<>();
                FileReader reader = new FileReader("europa.graph");
                BufferedReader in = new BufferedReader(reader);
                String line;
                String test = in.readLine();
                int j = 0;
                int k = 1;
                int h = 2;
                while ((line = in.readLine()) != null) {
                    String[] tokens = line.split(";");
                    for (int i = 0; i < tokens.length; i = i + 3) {
                        String name = tokens[j];
                        double x = Double.parseDouble(tokens[k]);
                        double y = Double.parseDouble(tokens[h]);
                        Nodes.LocationNode ln = new Nodes.LocationNode(name, x, y);
                        graph.add(ln);
                        j += 3;
                        k += 3;
                        h += 3;
                    }
                    break;

                }
                while ((line = in.readLine()) != null) {
                    Nodes.GraphNode node1 = null;
                    Nodes.GraphNode node2 = null;
                    boolean node1Found = false;
                    boolean node2Found = false;
                    String[] tokens = line.split(";");
                    String name1 = tokens[0];
                    String name2 = tokens[1];
                    String travel = tokens[2];
                    int weight = Integer.parseInt(tokens[3]);
                    Set<Nodes.GraphNode> nodes = graph.getNodes();
                    for (Nodes.GraphNode n : nodes) {
                        Nodes.LocationNode n1 = (Nodes.LocationNode) n;
                        if (n1.getName().equals(name1) && !node1Found) {
                            node1 = n1;
                            node1Found = true;
                        }
                        if (n1.getName().equals(name2) && !node2Found) {
                            node2 = n1;
                            node2Found = true;
                        }
                    }
                    graph.connect(node1, node2, travel, weight);
                }
                Set<Nodes.GraphNode> nodes = graph.getNodes();
                for (Nodes.GraphNode n : nodes) {
                    Nodes.LocationNode n1 = (Nodes.LocationNode) n;
                    Symbol s = new Symbol(n1.getX(),n1.getY(), n1.getName());
                    s.setOnMouseClicked(cl);
                    Text name = new Text(n1.getX()+3,n1.getY()+27, n1.getName());
                    center.getChildren().add(s);
                    center.getChildren().add(name);
                }
                in.close();
                reader.close();
            } catch (FileNotFoundException e) {
                System.err.println("Filfel");
            } catch (IOException e) {
                System.err.println("IO-fel");
            }
        }
    }

    class ClickHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Symbol b = (Symbol) event.getSource();
//getSource returnerar en Object, därför castar vi den till Bricka
// b pekar ut den bricka vi klickat på
            if(s1 == null){
                s1 = b;
                s1.paintRed();
            }
            else if (s2 == null && b != s1 ){
                s2 = b;
                s2.paintRed();
            }
        }
    }

    class NewPlaceHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            center.setOnMouseClicked(new NewPlaceClickHandler());
            newPlaceButton.setDisable(true); //när man klickat på knappen så blir den disabled (slutar funka)
            center.setCursor(Cursor.CROSSHAIR); //att musen ska vara en Cursor när man klickat på knappen
        }
    }
    class NewPlaceClickHandler implements EventHandler<MouseEvent>{
        @Override public void handle(MouseEvent event){
            String cityName="";
            double x = event.getX(); //kordinat där muspekaren har klickats
            double y = event.getY();
            Symbol symbolToCreate = new Symbol(x, y,cityName);
            symbolToCreate.setOnMouseClicked(new ClickHandler());
            center.setOnMouseClicked(null);
            newPlaceButton.setDisable(false); // när man satt ut en postitlapp blir knappen enabled igen
            center.setCursor(Cursor.DEFAULT); //att musen ska sluta va en cursor när operationen är klar
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Name of place:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                cityName=result.get();
            }
            center.getChildren().add(symbolToCreate);
            Text name = new Text(x+3,y+27, cityName);
            center.getChildren().add(name);
            Nodes.LocationNode newNode = new Nodes.LocationNode(cityName,x,y);
            graph.add(newNode);
            symbolToCreate.setName(cityName);
        }
    }

    public class NewConnectionAlert extends Alert{
        private Label nameLabel = new Label("Name: ");
        private Label timeLabel = new Label("Time: ");
        private TextField nameField = new TextField();
        private TextField timeField = new TextField();
        public NewConnectionAlert(){
            super(AlertType.CONFIRMATION);
            GridPane grid = new GridPane();
            grid.addRow(0,nameLabel,nameField);
            grid.addRow(1,timeLabel,timeField);
           // getDialogPane().setContentText("Name of place: ");
            getDialogPane().setContent(grid);
        }
        public void setName(String name){
            nameField.setText(name);
            nameField.setDisable(true);
        }
        public void setTimeDisabled(){
            timeField.setDisable(true);
        }
        public void setTime(int time){
            String stringTime = ""+time;
            timeField.setText(stringTime);
        }
        public String getName(){
            return nameField.getText();
        }
        public int getTime(){
            return Integer.parseInt(timeField.getText());
        }
    }
    class NewConnectionHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event) {
            if (s1 == null || s2 == null) {
                showError("Two places must be selected");
                resetSelectedNodes();
            } else {
                boolean node1Found = false;
                boolean node2Found = false;
                Nodes.LocationNode node1 = null;
                Nodes.LocationNode node2 = null;
                String connectionName = "";
                int connectionTime;
                newConnectionButton.setDisable(true); //när man klickat på knappen så blir den disabled (slutar funka)
                NewConnectionAlert nca = new NewConnectionAlert();
                nca.setHeaderText("Connection from " + s1.getName() + " to " + s2.getName());
                nca.setTitle("Connection");
                nca.showAndWait();
                connectionName = nca.getName();
                connectionTime = nca.getTime();

                Set<Nodes.GraphNode> nodes = graph.getNodes();
                for (Nodes.GraphNode n : nodes) {
                    Nodes.LocationNode n1 = (Nodes.LocationNode) n;
                    if (n1.getName().equals(s1.getName()) && !node1Found) {
                        node1 = n1;
                        node1Found = true;
                    }
                    if (n1.getName().equals(s2.getName()) && !node2Found) {
                        node2 = n1;
                        node2Found = true;
                    }
                }
                graph.connect(node1, node2, connectionName, connectionTime);
                resetSelectedNodes();
                newConnectionButton.setDisable(false);

            }
        }
        }
        public void showError(String errorMessage){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(errorMessage);
            errorAlert.showAndWait();
        }
        public void resetSelectedNodes(){
        if(s1!=null){
            s1.paintBlue();
            s1=null;
        }
        if(s2!=null){
            s2.paintBlue();
            s2=null;
        }

        }

        class showConnectionHandler implements EventHandler<ActionEvent>{
        @Override
            public void handle(ActionEvent e){
            System.out.println(e.getSource());
            boolean node1Found = false;
            boolean node2Found = false;
            Nodes.LocationNode node1 = null;
            Nodes.LocationNode node2 = null;

            if (s1 == null || s2 == null) {
                showError("Two places must be selected!");

                resetSelectedNodes();
            }else {

                Set<Nodes.GraphNode> nodes = graph.getNodes();
                for (Nodes.GraphNode n : nodes) {
                    Nodes.LocationNode n1 = (Nodes.LocationNode) n;
                    if (n1.getName().equals(s1.getName()) && !node1Found) {
                        node1 = n1;
                        node1Found = true;
                    }
                    if (n1.getName().equals(s2.getName()) && !node2Found) {
                        node2 = n1;
                        node2Found = true;
                    }
                }
                if (graph.getEdgeBetween(node1, node2) == null) {
                    showError("No connection between the places");
                    resetSelectedNodes();
                } else{
                    String id = ((Node) e.getSource()).getId();
                    System.out.println(id);
                    if(id=="btnShowConnection") {
                        NewConnectionAlert nca = new NewConnectionAlert();
                        nca.setName(graph.getEdgeBetween(node1, node2).getName());
                        nca.setTime(graph.getEdgeBetween(node1, node2).getWeight());
                        nca.setTimeDisabled();
                        nca.setHeaderText("Connection from " + s1.getName() + " to " + s2.getName());
                        nca.setTitle("Connection");
                        nca.showAndWait();
                        resetSelectedNodes();

                    }else{

                        NewConnectionAlert nca = new NewConnectionAlert();
                        nca.setName(graph.getEdgeBetween(node1, node2).getName());
                        nca.setTime(graph.getEdgeBetween(node1, node2).getWeight());
                        nca.setHeaderText("Connection from " + s1.getName() + " to " + s2.getName());
                        nca.setTitle("Connection");
                        nca.showAndWait();
                        graph.setConnectionWeight(node1,node2, nca.getTime());
                        resetSelectedNodes();

                    }
                }
            }

        }

        }
    }



