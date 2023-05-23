package model;

public class DataPackage {
    private GameData initialData;
    private GameData currentData;

    public DataPackage(GameData initialData, GameData currentData) {
        this.initialData = new GameData(initialData);
        this.currentData = new GameData(currentData);
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