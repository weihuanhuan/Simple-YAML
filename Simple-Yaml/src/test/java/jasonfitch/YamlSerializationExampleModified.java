package jasonfitch;

import org.simpleyaml.configuration.MemorySection;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.DumperOptions;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

public final class YamlSerializationExampleModified {

    public static void main(final String[] args) {

        final YamlFile yamlFile = new YamlFile("examples/config.yml");
        yamlFile.options().pathSeparator('#');
        yamlFile.options().width(512);
        yamlFile.options().flowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlFile.options().scalarStyle(DumperOptions.ScalarStyle.PLAIN);

        try {
            yamlFile.loadWithComments();

            System.out.println("############## get hookWhite #################");
            Object hookWhite = yamlFile.get("hook.white");
            System.out.println("Loaded hook.white:\n " + hookWhite);

            if (hookWhite instanceof MemorySection) {
                MemorySection memorySection = (MemorySection) hookWhite;
                memorySection.remove("localhost:8080/vulns/011-ssrf-commons-httpclient.jsp");

                Map<String, Object> whiteItem = new LinkedHashMap<>();
                List<String> types = new ArrayList<>();
                types.add("type1");
                types.add("type2");
                whiteItem.put("type", types);
                whiteItem.put("description", "test-desc");
                whiteItem.put("enabled", Boolean.FALSE);
                memorySection.set("localhost:8080/vulns/011-ssrf-commons-httpclient-mod.jsp", whiteItem);
            }

            System.out.println("############## set hookWhite #################");
            Map<String, Object> whiteItem2 = new LinkedHashMap<>();
            List<String> types2 = new ArrayList<>();
            types2.add("type21");
            types2.add("type22");
            whiteItem2.put("type", types2);
            whiteItem2.put("description", "test-desc2");
            whiteItem2.put("enabled", Boolean.TRUE);
            yamlFile.set("hook.white" + "#localhost:8080/vulns/011-ssrf-commons-httpclient-mod2.jsp", whiteItem2);

            Map<String, Object> mapValues = yamlFile.getMapValues(false);
            Object mapValuesHookWhite = mapValues.get("hook.white");
            if (mapValuesHookWhite instanceof Map) {
                Map<String, Object> stringObjectMap = (Map<String, Object>) mapValuesHookWhite;
                Map<String, Object> whiteItem3 = new LinkedHashMap<>();
                List<String> types3 = new ArrayList<>();
                types3.add("type31");
                types3.add("type32");
                whiteItem3.put("type", types3);
                whiteItem3.put("description", "test-desc3");
                whiteItem3.put("enabled", Boolean.TRUE);
                stringObjectMap.put("localhost:8080/vulns/011-ssrf-commons-httpclient-mod3.jsp", whiteItem3);
            }
            yamlFile.set("hook.white", mapValuesHookWhite);

            System.out.println("############## config-mod.yml output #################");
            String s = yamlFile.saveToString();
            FileOutputStream fileOutputStream = new FileOutputStream("F:\\JetBrains\\IntelliJ IDEA\\Simple-YAML\\examples\\config-mod.yml");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(s);
            outputStreamWriter.close();

            System.out.println("############## config-mod-copy.yml output #################");
            final YamlFile yamlFileMod = new YamlFile("examples/config-mod.yml");
            yamlFileMod.options().pathSeparator('#');
            yamlFileMod.options().width(512);
            yamlFileMod.options().flowStyle(DumperOptions.FlowStyle.BLOCK);
            yamlFileMod.options().scalarStyle(DumperOptions.ScalarStyle.PLAIN);

            yamlFileMod.loadWithComments();

            String yamlFileCopy = yamlFileMod.saveToString();
            FileOutputStream fileOutputStreamCopy = new FileOutputStream("F:\\JetBrains\\IntelliJ IDEA\\Simple-YAML\\examples\\config-mod-copy.yml");
            OutputStreamWriter outputStreamWriterCopy = new OutputStreamWriter(fileOutputStreamCopy);
            outputStreamWriterCopy.write(yamlFileCopy);
            outputStreamWriterCopy.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
