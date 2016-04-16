package de.dogedev.ld35.michelangelo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class ScreenshotFactory {

    private static int counter = 1;

    public static void saveScreenshot(){
        try{
            FileHandle fh;
            do{
                fh = Gdx.files.external("inn/screenshot" + counter++ + ".png");
            }while (fh.exists());
            Pixmap pixmap = getScreenshot();
            PixmapIO.writePNG(fh, pixmap);
            pixmap.dispose();
        }catch (Exception e){           
        }
    }

    private static Pixmap getScreenshot(){
//        final Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(x, y, w, h);
//        if (yDown) {
//            // Flip the pixmap upside down
//            ByteBuffer pixels = pixmap.getPixels();
//            int numBytes = w * h * 4;
//            byte[] lines = new byte[numBytes];
//            int numBytesPerLine = w * 4;
//            for (int i = 0; i < h; i++) {
//                pixels.position((h - i - 1) * numBytesPerLine);
//                pixels.get(lines, i * numBytesPerLine, numBytesPerLine);
//            }
//            pixels.clear();
//            pixels.put(lines);
//            pixels.clear();
//        }

        byte[] pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGBA8888);
        BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);

        Pixmap pixmap2 = new Pixmap(Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), Pixmap.Format.RGB888);
        pixmap2.drawPixmap(pixmap, 0, 0);

        return pixmap2;
    }
}