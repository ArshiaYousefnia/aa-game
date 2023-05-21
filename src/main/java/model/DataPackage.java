package model;

public class DataPackage {
    private GameData initialData;
    private GameData currentData;

    public DataPackage(GameData initialData, GameData currentData) {
        this.initialData = initialData;
        this.currentData = currentData;
    }

    public GameData getInitialData() {
        return initialData;
    }

    public GameData getCurrentData() {
        return currentData;
    }

    public void setInitialData(GameData initialData) {
        this.initialData = initialData;
    }

    public void setCurrentData(GameData currentData) {
        this.currentData = currentData;
    }
}