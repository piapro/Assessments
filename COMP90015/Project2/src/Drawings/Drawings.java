/**
 * @since
 */

import java.awt.*;
import java.io.*;


public class Drawings implements Serializable {

    //set object color using RGB color space
    int R,G,B;
    //set width of stylus
    float width;
    //dashed line: line + gap
    float dash [] = {5.0f,5.0f,5.0f};


    //coordinate gor each objects
    int x1,y1;
    int x2,y2;


    void Drawings(Graphics2D g2d){
    }

}
