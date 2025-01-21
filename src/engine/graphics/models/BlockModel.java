package engine.graphics.models;

public class BlockModel {
    public boolean transparent = false;
    public int topFace;
    public int bottomFace;
    public int northFace;
    public int southFace;
    public int westFace;
    public int eastFace;
    public ModelType type;

    //Getters are here to insert data into the object more easily through loops when reading a file
    public void setTransparent() {
        this.transparent = true;
    }

    public void setTopFace(int topFace) {
        this.topFace = topFace;
    }

    public void setBottomFace(int bottomFace) {
        this.bottomFace = bottomFace;
    }

    public void setNorthFace(int northFace) {
        this.northFace = northFace;
    }

    public void setSouthFace(int southFace) {
        this.southFace = southFace;
    }

    public void setWestFace(int westFace) {
        this.westFace = westFace;
    }

    public void setEastFace(int eastFace) {
        this.eastFace = eastFace;
    }

    public void setFaces(int face) {
        this.topFace = face;
        this.bottomFace = face;
        this.northFace = face;
        this.southFace = face;
        this.westFace = face;
        this.eastFace = face;
    }

    public void setType(ModelType type) {
        this.type = type;
    }
}
