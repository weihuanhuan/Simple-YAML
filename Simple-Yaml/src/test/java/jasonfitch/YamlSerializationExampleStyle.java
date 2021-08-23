package jasonfitch;

import org.simpleyaml.configuration.Configuration;
import org.simpleyaml.configuration.comments.CommentType;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.DumperOptions;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public final class YamlSerializationExampleStyle {

    public static void main(final String[] args) {

        final YamlFile yamlFile = new YamlFile("examples/config.yml");
        yamlFile.options().pathSeparator('#');
        yamlFile.options().width(512);
        yamlFile.options().flowStyle(DumperOptions.FlowStyle.BLOCK);
        yamlFile.options().scalarStyle(DumperOptions.ScalarStyle.PLAIN);

//        yamlFile.options().flowStyle(DumperOptions.FlowStyle.FLOW);
//        yamlFile.options().scalarStyle(DumperOptions.ScalarStyle.DOUBLE_QUOTED);

        try {
            yamlFile.loadWithComments();

            System.out.println("############## root #################");
            Configuration root = yamlFile.getRoot();
            System.out.println(root);
            System.out.println(root.getName() == "" ? "EMPTY_STRING" : root.getName());

            System.out.println("############## get value #################");
            final String p1 = (String) yamlFile.get("instance.name");
            System.out.println("Loaded object1:\n " + p1);
            final String p2 = (String) yamlFile.get("inject.custom_headers#X-Protected-By");
            System.out.println("Loaded object2:\n " + p2);

            System.out.println("############## set comment #################");
            yamlFile.set("cluster.name", "clu1");
            yamlFile.setComment("cluster.name", "block comment");
            yamlFile.setComment("cluster.name", "inline comment", CommentType.SIDE);
            String blockComment = yamlFile.getComment("cluster.name");
            System.out.println(blockComment);
            String inlineComment = yamlFile.getComment("cluster.name", CommentType.SIDE);
            System.out.println(inlineComment);

            System.out.println("############## file output #################");
            String s = yamlFile.saveToString();
            FileOutputStream fileOutputStream = new FileOutputStream("F:\\JetBrains\\IntelliJ IDEA\\Simple-YAML\\examples\\config-new.yml");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(s);
            outputStreamWriter.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}
