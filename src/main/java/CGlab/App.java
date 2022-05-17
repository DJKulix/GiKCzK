package CGlab;

import CGlab.Renderer.LineAlgo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    String version = "0.02";

    public static void main(String[] args) {

        String path = "render.png";
        int h = 1000;
        int w = 1000;
        LineAlgo algo = LineAlgo.NAIVE;

//        try{
//
//                path = args[0];
//                w = Integer.parseInt(args[1]);
//                h = Integer.parseInt(args[2]);
//                algo = LineAlgo.valueOf(args[3]);
//        } catch(Exception e) {
//            System.out.println("Podaj dobra �cie�k�, szeroko�� oraz wysoko�� zdj�cia");
//            return;
//        }

//        Renderer mainRenderer = new Renderer(System.getProperty("user.home")+ "/" + path, h, w, algo);
//        mainRenderer.clear();
//        mainRenderer.drawPoint(100, 100);
//
//        mainRenderer.drawTriangle(new Vec2f(100,110), new Vec2f(44,245), new Vec2f(100,177), new Vec3i(231,124,15));
//        mainRenderer.drawTriangle(new Vec2f(30,40), new Vec2f(80,100), new Vec2f(50,60), new Vec3i(11,241,42));
//        mainRenderer.drawTriangle(new Vec2f(110,96), new Vec2f(40,3), new Vec2f(20,51), 130);



        RandomColorRenderer randomColorRenderer = new RandomColorRenderer(System.getProperty("user.home")+ "/" + path, h, w, algo);
        Model model = new Model();
        try {
            model.readOBJ("deer.obj");
            randomColorRenderer.render(model);
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }



        try {
//            mainRenderer.save();
            randomColorRenderer.save();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getVersion() {
        return this.version;
    }
}
