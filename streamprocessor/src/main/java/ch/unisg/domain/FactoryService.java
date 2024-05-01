package ch.unisg.domain;

import ch.unisg.domain.stations.*;
import ch.unisg.domain.util.RuntimeTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class FactoryService {

    public List<String> simpleRead(String path) {
        File file = new File(path);
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                lines.add(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public List<Station> readFile(String path) {
        File file = new File(path);
        List<Station> stations = new ArrayList<>();

        RuntimeTypeAdapterFactory<Station> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(Station.class, "station")
                .registerSubtype(VGR_1.class, "VGR_1")
                .registerSubtype(MM_1.class, "MM_1")
                .registerSubtype(HBW_1.class, "HBW_1")
                .registerSubtype(EC_1.class, "EC_1")
                .registerSubtype(SM_1.class, "SM_1")
                .registerSubtype(OV_1.class, "OV_1")
                .registerSubtype(WT_1.class, "WT_1");

        Gson gson = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();


        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                Station station = gson.fromJson(currentLine, Station.class);
                stations.add(station);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stations;

    }
}
