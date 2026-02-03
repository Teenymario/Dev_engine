package engine.graphics.models;

public class BlockModel {
    public boolean transparent = false;
    public ModelType type;
    public int[] faces = new int[6];

    /* Face order goes as follows:
    0 - North
    1 - South
    2 - West
    3 - East
    4 - Top
    5 - Bottom
     */
    public void setTextureCoords(int[] faces) {
        this.faces = faces;
    }

    public void setTransparent() {
        this.transparent = true;
    }

    public void setType(ModelType type) {
        this.type = type;
    }
}
