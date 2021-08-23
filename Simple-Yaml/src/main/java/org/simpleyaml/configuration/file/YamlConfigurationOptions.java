package org.simpleyaml.configuration.file;

import org.simpleyaml.utils.Validate;
import org.yaml.snakeyaml.DumperOptions;

import java.util.Objects;

/**
 * Various settings for controlling the input and output of a {@link YamlConfiguration}
 *
 * @author Bukkit
 * @see <a href="https://github.com/Bukkit/Bukkit/tree/master/src/main/java/org/bukkit/configuration/file/YamlConfigurationOptions.java">Bukkit Source</a>
 */
public class YamlConfigurationOptions extends FileConfigurationOptions {

    private int indent = 2;
    private int width = 512;
    private DumperOptions.FlowStyle flowStyle;
    private DumperOptions.ScalarStyle scalarStyle;

    protected YamlConfigurationOptions(final YamlConfiguration configuration) {
        super(configuration);
    }

    @Override
    public YamlConfiguration configuration() {
        return (YamlConfiguration) super.configuration();
    }

    @Override
    public YamlConfigurationOptions copyDefaults(final boolean value) {
        super.copyDefaults(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions pathSeparator(final char value) {
        super.pathSeparator(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions header(final String value) {
        super.header(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions copyHeader(final boolean value) {
        super.copyHeader(value);
        return this;
    }

    @Override
    public YamlConfigurationOptions indent(final int value) {
        Validate.isTrue(value >= 2, "Indent must be at least 2 characters");
        Validate.isTrue(value <= 9, "Indent cannot be greater than 9 characters");

        super.indent(value);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YamlConfigurationOptions)) return false;
        if (!super.equals(o)) return false;
        YamlConfigurationOptions that = (YamlConfigurationOptions) o;
        return indent == that.indent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), indent);
    }

    public int width() {
        return width;
    }

    public YamlConfigurationOptions width(int width) {
        this.width = width;
        return this;
    }

    public DumperOptions.FlowStyle flowStyle() {
        return flowStyle == null ? DumperOptions.FlowStyle.BLOCK : flowStyle;
    }

    public YamlConfigurationOptions flowStyle(DumperOptions.FlowStyle flowStyle) {
        this.flowStyle = flowStyle;
        return this;
    }

    public DumperOptions.ScalarStyle scalarStyle() {
        return scalarStyle == null ? DumperOptions.ScalarStyle.PLAIN : scalarStyle;
    }

    public YamlConfigurationOptions scalarStyle(DumperOptions.ScalarStyle scalarStyle) {
        this.scalarStyle = scalarStyle;
        return this;
    }

}
