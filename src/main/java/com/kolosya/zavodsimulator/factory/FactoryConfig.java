package com.kolosya.zavodsimulator.factory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class FactoryConfig {
    private final TreeMap<String, String> properties;

    public FactoryConfig(File configFile) throws FileNotFoundException {
        properties = new TreeMap<>();
        Scanner scanner = new Scanner(configFile);
        while (scanner.hasNext()) {
            String[] temp = scanner.nextLine().split("=");
            properties.put(temp[0], temp[1]);
        }
    }

    public String getProperty(String name) {
        return properties.get(name);
    }
}
