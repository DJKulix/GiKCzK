package CGlab;


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Renderer {

    public enum LineAlgo {NAIVE, DDA, BRESENHAM, BRESENHAM_INT;}

    public BufferedImage render;
    public int h = 200;
    public int w = 200;

    private String filename;
    private LineAlgo lineAlgo = LineAlgo.NAIVE;

    public Renderer(String filename, int h, int w, LineAlgo lineAlgo) {
        render = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        this.filename = filename;
        this.h = h;
        this.w = w;
        this.lineAlgo = lineAlgo;
    }

    public void drawPoint(int x, int y) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);
        render.setRGB(x, y, white);
    }
    public void drawPoint(int x, int y,Vec3i color) {
        int draw =  (255 << 24) + (color.x << 16) + (color.y << 8) + color.z;
        render.setRGB(x, y, draw);
    }
    public void drawPoint(int x, int y, int color) {
        int draw =  color;
        render.setRGB(x, y, draw);
    }

    public void drawLine(int x0, int y0, int x1, int y1, LineAlgo lineAlgo) {
        if (lineAlgo == LineAlgo.NAIVE) drawLineNaive(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.DDA) drawLineDDA(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.BRESENHAM) drawLineBresenham(x0, y0, x1, y1);
        if (lineAlgo == LineAlgo.BRESENHAM_INT) drawLineBresenhamInt(x0, y0, x1, y1);
    }

    public void drawLineNaive(int x0, int y0, int x1, int y1) {

        int dx = x1 - x0;
        int dy = y1 - y0;

        if (dx != 0 && dy <= dx) {

            float m = (float) dy / dx;
            float y = y0;

            for (int x = x0 + 1; x <= x1; x++) {
                y += m;
                drawPoint(x, Math.round(y));
            }
        } else if (dx != 0) {

            float m = (float) dx / dy;
            float x = x0;

            drawPoint(x0, y0);

            for (int y = y0 - 1; y <= y1; y++) {
                x += m;
                drawPoint(Math.round(x), y);
            }
        }
    }

    public void drawLineDDA(int x0, int y0, int x1, int y1) {

        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1-x0;
        int dy = y1-y0;
        int derr = Math.abs(dy/(dx));
        int err = 0;

        int y = y0;

        for (int x=x0; x<=x1; x++) {
            render.setRGB(x, y, white);
            err += dy;
            if (err > 0.5*2*dx) {
                if (err > dx) {
                    y += (y1 > y0 ? 1 : -1);
                    err -= 1.;
                }
            }
        }
    }

    public void drawLineBresenham(int x0, int y0, int x1, int y1) {
        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1 - x0;
        int dy = y1 - y0;
        float derr = Math.abs(dy / (float) (dx));
        float err = 0;

        int y = y0;

        for (int x = x0; x <= x1; x++) {
            render.setRGB(x, y, white);
            err += derr;
            if (err > 0.5) {
                y += (y1 > y0 ? 1 : -1);
                err -= 1.;
            }
        } // Oktanty: 1, 8
    }

    public void drawLineBresenhamInt(int x0, int y0, int x1, int y1) {

        int white = 255 | (255 << 8) | (255 << 16) | (255 << 24);

        int dx = x1 - x0;
        int dy = y1 - y0;

        float err = 0;

        if (Math.abs(dx) > Math.abs(dy) && dx > 0) {

            float derr = 2 * Math.abs(dy / (float) (dx));
            int y = y0;

            for (int x = x0; x <= x1; x++) {
                render.setRGB(x, y, white);
                err += derr;
                if (err > 1) {
                    y += (y1 > y0 ? 1 : -1);
                    err -= 2.;
                }
            }
        } else if (Math.abs(dx) > Math.abs(dy) && dx <= 0) {

            float derr = 2 * Math.abs(dy / (float) (dx));
            int y = y0;

            for (int x = x0; x >= x1; x--) {
                render.setRGB(x, y, white);
                err += derr;
                if (err > 1) {
                    y += (y1 > y0 ? 1 : -1);
                    err -= 2.;
                }
            }

        } else if (Math.abs(dx) <= Math.abs(dy) && dx > 0) {

            float derr = 2 * Math.abs(dx / (float) (dy));
            int x = x0;

            for (int y = y0; y <= y1; y++) {
                render.setRGB(x, y, white);
                err += derr;
                if (err > 1) {
                    x += (x1 > x0 ? 1 : -1);
                    err -= 2.;
                }
            }

        } else if (Math.abs(dx) <= Math.abs(dy) && dx <= 0) {

            float derr = 2 * Math.abs(dx / (float) (dy));
            int x = x0;

            for (int y = y0; y >= y1; y--) {
                render.setRGB(x, y, white);
                err += derr;
                if (err > 1) {
                    x += (x1 > x0 ? 1 : -1);
                    err -= 2.;
                }
            }

        }

    }

    public void save() throws IOException {
        File outputfile = new File(filename);
        render = Renderer.verticalFlip(render);
        ImageIO.write(render, "png", outputfile);
    }

    public void clear() {
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int black = 0 | (0 << 8) | (0 << 16) | (255 << 24);
                render.setRGB(x, y, black);
            }
        }
    }

    public static BufferedImage verticalFlip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage flippedImage = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = flippedImage.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return flippedImage;
    }

    public Vec3f barycentric(Vec2f A, Vec2f B, Vec2f C, Vec2f P) {

        Vec3f v1 = new Vec3f((B.x - A.x), (C.x = A.x), (A.x - P.x));
        Vec3f v2 = new Vec3f((B.y - A.y), (C.y - A.y), (A.y - P.y));
        Vec3f cross = cross(v1, v2);
        Vec2f uv = new Vec2f(cross.x / cross.z, cross.y / cross.z);
        Vec3f barycentric = new Vec3f(uv.x, uv.y, 1 - uv.x - uv.y);

        return barycentric;
    }

    public Vec3f cross(Vec3f v1, Vec3f v2) {
        Vec3f result;

        float x = (v1.y * v2.z) - (v2.y * v1.z);
        float y = ((v1.x * v2.z) - (v2.x * v1.z)) * -1;
        float z = (v1.x * v2.y) - (v2.x * v1.y);

        result = new Vec3f(x, y, z);
        return result;
    }

    public void drawTriangle(Vec2f A, Vec2f B, Vec2f C, Vec3i color) {

        color.x = colors(color.x);
        color.y = colors(color.y);
        color.x = colors(color.z);

        float[] a = new float[3];
        a[0] = A.x;
        a[1] = B.x;
        a[2] = C.x;

        float[] b = new float[3];
        b[0] = A.y;
        b[1] = B.y;
        b[2] = C.y;

        Vec2i boxa = Box(a);
        Vec2i boxb = Box(b);

        for(int i = boxa.x; i < boxb.y; i++){
            for(int j = boxb.x; j < boxb.y; j++){
                Vec3f bary = barycentric(A, B, C, new Vec2f(i,j));
                if(bary.x > 0 && bary.x < 1 && bary.y > 0 && bary.y < 1 && bary.z > 0 && bary.z < 1){
                    drawPoint(i,j,color);
                }
            }
        }
    }

    public void drawTriangle(Vec2f A, Vec2f B, Vec2f C, int color) {

        float[] a = new float[3];
        a[0] = A.x;
        a[1] = B.x;
        a[2] = C.x;

        float[] b = new float[3];
        b[0] = A.y;
        b[1] = B.y;
        b[2] = C.y;

        Vec2i boxa = Box(a);
        Vec2i boxb = Box(b);

        for(int i = boxa.x; i < boxb.y; i++){
            for(int j = boxb.x; j < boxb.y; j++){
                Vec3f bary = barycentric(A, B, C, new Vec2f(i,j));
                if(bary.x > 0 && bary.x < 1 && bary.y > 0 && bary.y < 1 && bary.z > 0 && bary.z < 1){
                    drawPoint(i,j,color);
                }
            }
        }
    }



    private int colors(int color){
        if(color < 0){
            return 0;
        }

        if(color > 255){
            return 255;
        }else{
            return color;
        }
    }
    private Vec2i Box(float[] tab){
        Vec2i result;
        float minimum = Integer.MAX_VALUE;
        float maximum = Integer.MIN_VALUE;

        for(int i = 0; i < tab.length; i++){
            if(tab[i] < minimum) minimum = tab[i];
            if(tab[i] > maximum) maximum = tab[i];
        }

        int minimum2 = (int) minimum;
        int maximum2 = (int) maximum;
        result = new Vec2i(minimum2, maximum2);

        return result;
    }

    public void drawTriangle(Vec2i A, Vec2i B, Vec2i C, int color) {

        float[] a = new float[3];
        a[0] = A.x;
        a[1] = B.x;
        a[2] = C.x;

        float[] b = new float[3];
        b[0] = A.y;
        b[1] = B.y;
        b[2] = C.y;

        Vec2i boxa = Box(a);
        Vec2i boxb = Box(b);

        for(int i = boxa.x; i < boxb.y; i++){
            for(int j = boxb.x; j < boxb.y; j++){
                Vec3f bary = barycentric(A, B, C, new Vec2f(i,j));
                if(bary.x > 0 && bary.x < 1 && bary.y > 0 && bary.y < 1 && bary.z > 0 && bary.z < 1){
                    drawPoint(i,j,color);
                }
            }
        }
    }

    public Vec3f barycentric(Vec2i A, Vec2i B, Vec2i C, Vec2f P) {

        Vec3f v1 = new Vec3f((B.x - A.x), (C.x = A.x), (A.x - P.x));
        Vec3f v2 = new Vec3f((B.y - A.y), (C.y - A.y), (A.y - P.y));
        Vec3f cross = cross(v1, v2);
        Vec2f uv = new Vec2f(cross.x / cross.z, cross.y / cross.z);
        Vec3f barycentric = new Vec3f(uv.x, uv.y, 1 - uv.x - uv.y);

        return barycentric;
    }

}
