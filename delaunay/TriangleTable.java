package delaunay;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TriangleTable {
    
public LinkedHashMap<String, Triangle> triangles = new LinkedHashMap<>();
public LinkedHashMap<String, ArrayList<Triangle>> edgetriangles = new LinkedHashMap<>();


void add(Triangle t){
    
String key = t.a.label+","+t.b.label+","+t.c.label; 
triangles.put(key, t);

String[] edgekeys = {t.a.label+","+t.b.label, t.b.label+","+t.c.label, t.a.label+","+t.c.label, 
        t.b.label+","+t.a.label, t.c.label+","+t.b.label, t.c.label+","+t.a.label};

    for (String edgekey : edgekeys) {
        if(!edgetriangles.containsKey(edgekey)){
            ArrayList<Triangle> a = new ArrayList<>();
            a.add(t);
            edgetriangles.put(edgekey, a);
        }else{
            edgetriangles.get(edgekey).add(t);
        }          
    }   
}

Triangle get(Triangle t){
    String key = t.a.label+","+t.b.label+","+t.c.label; 
    return triangles.get(key);
}

ArrayList<Triangle> get(Node a, Node b){
    return edgetriangles.get(a.label+","+b.label);
}

Triangle getNeighbor(Triangle t, Node a, Node b){
    Triangle rtn = null;
    if(edgetriangles.containsKey(a.label+","+b.label)){ //<necessary?      
        for (Triangle tri : edgetriangles.get(a.label+","+b.label) ){
            if(!tri.equals(t)){
                rtn = tri;  break;    
            }
        }       
    }
    return rtn;
}

void remove(Triangle t){
String key = t.a.label+","+t.b.label+","+t.c.label; 
triangles.remove(key);

String[] edgekeys = {t.a.label+","+t.b.label, t.b.label+","+t.c.label, t.a.label+","+t.c.label, 
        t.b.label+","+t.a.label, t.c.label+","+t.b.label, t.c.label+","+t.a.label};    

   for (String edgekey : edgekeys){
       if(edgetriangles.containsKey(edgekey)){
           edgetriangles.get(edgekey).remove(t);
       }
       
   }
  
}

}
