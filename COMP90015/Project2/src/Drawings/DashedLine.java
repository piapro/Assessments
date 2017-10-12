import java.awt.*;


public class DashedLine extends Drawings{

    @Override
    void Drawings(Graphics2D g2d) {
        super.Drawings(g2d);
        g2d.setPaint(new Color(R,G,B));
        g2d.setStroke(new BasicStroke(width,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash,0.0f));
        g2d.drawLine(x1,y1,x2,y2);
    }
}
