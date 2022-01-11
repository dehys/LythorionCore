package com.dehys.lythorioncore.object.storage;

import com.dehys.lythorioncore.Main;
import com.dehys.lythorioncore.factory.StorageFactory;
import com.google.gson.Gson;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Storage  {
    public static List<Storage> storages = new ArrayList<>();

    public String name;
    private final File file;
    public final StorageType type;

    private FileConfiguration yaml;
    private Gson json;

    public Storage(String name, StorageType type){
        this.name = name;
        this.type = type;

        this.file = new File(Main.plugin.getDataFolder(), name + type.getExtension());
    }

    public Storage create(){
        if(type == StorageType.YAML){
            json = null;
            yaml = StorageController.makeYaml(this.name, this.file);
        } else if (type == StorageType.JSON){
            json = StorageController.makeJSON(this.name);
            yaml = null;
        }
        storages.add(this);
        return this;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public FileConfiguration getYaml(){
        return this.yaml;
    }

    public Gson getJson(){
        return this.json;
    }

    public void save(){
        try {
            getYaml().save(getFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum StorageType {
        YAML(".yml"),
        JSON(".json"),
        SQL("");

        private final String extension;

        StorageType(String extension){
            this.extension = extension;
        }

        public String getExtension() {
            return extension;
        }
    }

}
