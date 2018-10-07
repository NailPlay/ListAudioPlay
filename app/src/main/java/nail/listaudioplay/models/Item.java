package nail.listaudioplay.models;

public class Item {
    private String status;
    private int id;
    private String name;
    private String path;
    private String audio;
    private String image;

    public Item(String status, int id, String name, String path, String audio, String image) {
        this.status = status;
        this.id = id;
        this.name = name;
        this.path = path;
        this.audio = audio;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
