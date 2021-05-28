import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;




public class Symbol extends Canvas {
    String stringName;
    Text name;

    public Symbol(double x, double y,String name){
        super(15,15);
        relocate(x,y);
        paintBlue();
        this.stringName=name;
        //addText(name);
    }

    public void paintBlue(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0,0,getWidth(),getHeight());
        gc.setFill(Color.BLUE);
        gc.fillOval(0,0,getWidth(),getHeight());
    }
    public void paintRed(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0,0,getWidth(),getHeight());
        gc.setFill(Color.RED);
        gc.fillOval(0,0,getWidth(),getHeight());
    }
    private void addText(String stringName){
        //Text cityName = new TextObject(getWidth(),getHeight(),name);
        //cityName.setFill(Color.BLACK);
        GraphicsContext gc = getGraphicsContext2D();
        name = new Text(getWidth()+3,getHeight()+27,stringName);
        name.setFont(Font.font("Times New Roman",12));
    }
    public void setName(String name){
        stringName=name;
    }
    public String getName(){
        return stringName;
    }

    /*private void removeText(String stringName){

    }*/
}
