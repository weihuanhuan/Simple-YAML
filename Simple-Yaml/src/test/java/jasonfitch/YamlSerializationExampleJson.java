package jasonfitch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Map;

public final class YamlSerializationExampleJson {

    public static void main(final String[] args) {

        final YamlFile yamlFile = new YamlFile("examples/config.yml");
        yamlFile.options().pathSeparator('#');
        yamlFile.options().width(512);
        yamlFile.options().flowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlFile.options().scalarStyle(DumperOptions.ScalarStyle.PLAIN);

        try {
            yamlFile.loadWithComments();

            System.out.println("############## Map<String, Object> #################");
            Map<String, Object> values = yamlFile.getValues(false);
            System.out.println(values);
            Map<String, Object> mapValues = yamlFile.getMapValues(false);
            System.out.println(mapValues);

            System.out.println("############## gson fromJson #################");
            Gson gson = new Gson();
            File jsonFile = new File("F:\\JetBrains\\IntelliJ IDEA\\Simple-YAML\\examples\\config.json");
            FileReader fileReader = new FileReader(jsonFile);
            JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
            System.out.println(jsonObject);

            System.out.println("############## gson toJsonTree #################");
            Gson gsonTree = new Gson();
            JsonObject jsonElement = (JsonObject) gsonTree.toJsonTree(mapValues);
            System.out.println(jsonElement);

            //disableHtmlEscaping
            Gson toGson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String toJson = toGson.toJson(jsonElement);

            FileOutputStream fileOutputStream = new FileOutputStream("F:\\JetBrains\\IntelliJ IDEA\\Simple-YAML\\examples\\config-new.json");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(toJson);
            outputStreamWriter.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
