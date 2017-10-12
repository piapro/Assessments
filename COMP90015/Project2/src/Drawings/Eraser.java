import java.awt.*;

public class Eraser extends Drawings{

    void Drawings(Graphics2D g2d){
        g2d.setPaint(new Color(255,255,255));
        g2d.setStroke(new BasicStroke(width,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
        g2d.drawLine(x1,y1,x2,y2);
    }


}
