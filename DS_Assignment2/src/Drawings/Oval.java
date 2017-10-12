import java.awt.*;

public class Oval extends Drawings{
    void Drawing(Graphics2D g2d){
        g2d.setPaint(new Color(R,G,B));
        g2d.setStroke(new BasicStroke(width));
        g2d.drawOval(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x1-x2),Math.abs(y1-y2));
    }




}
