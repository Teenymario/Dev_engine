package engine.graphics.models;

import java.util.Arrays;

public class BlockModel {
    public boolean transparent = false;
    public int[] faces = new int[6];
    public ModelType type;

    /* Face order goes as follows:
    0 - North
    1 - South
    2 - West
    3 - East
    4 - Top
    5 - Bottom
     */
    public void setTransparent() {
        this.transparent = true;
    }

    public void setNorthFace(int face) {
        faces[0] = face;
    }

    public void setSouthFace(int face) {
        faces[1] = face;
    }

    public void setWestFace(int face) {
        faces[2] = face;
    }

    public void setEastFace(int face) {
        faces[3] = face;
    }

    public void setTopFace(int face) {
        faces[4] = face;
    }

    public void setBottomFace(int face) {
        faces[5] = face;
    }

    public void setFaces(int north, int south, int west, int east, int top, int bottom) {
        faces[0] = north;
        faces[1] = south;
        faces[2] = west;
        faces[3] = east;
        faces[4] = top;
        faces[5] = bottom;
    }

    public void setFaces(int face) {
        Arrays.fill(faces, face);
    }

    public void setType(ModelType type) {
        this.type = type;
    }
}
