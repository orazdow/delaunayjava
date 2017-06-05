package delaunay;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class main {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(500, 500);
        
        final Delaunay d = new Delaunay(500, 500);
        ArrayList<Point> list = new ArrayList<>();
        fillList(list, 200, 400, 400);
        
        for(Point p : list){
          d.addPoint(p.x, p.y);
        }
        
        d.drawDelaunay(false);
        d.drawVoronoi(true);
        d.drawBackground(true);
        
        JPanel p = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                d.draw(g);
            }
        };
        
        frame.add(p);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    static void fillList(List l, int size, int w, int h){
        for(int i = 0; i < size; i++)
        l.add(new Point((int)(Math.random()*w), (int)(Math.random()*h)));
    }
    
    static class Point{
        int x, y;
        Point(int x, int y){
            this.x = x; this.y = y;
        }
    }
    
}
