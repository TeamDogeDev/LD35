package de.dogedev.ld35.assets.enums;

/**
 * Project: game
 * Package: de.dogedev.ld35.assets.enums
 * Date: 16.04.2016
 *
 * @author elektropapst
 */
public enum ShaderPrograms {

    SHADOWMAP("shader/passthrough.vsh", "shader/shadowmap.fsh"),
    SHADOWRENDER("shader/passthrough.vsh", "shader/shadowrender.fsh");

    public String fragmentShader;
    public String vertexShader;

    ShaderPrograms(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

}
