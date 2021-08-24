package org.simpleyaml.configuration.comments;

import org.simpleyaml.configuration.file.YamlConfigurationOptions;

import java.io.IOException;
import java.io.Reader;
import java.util.function.Function;
import java.util.stream.Collectors;

public class YamlCommentDumper extends YamlCommentReader {

    private final YamlCommentMapper yamlCommentMapper;

    private StringBuilder builder;

    public YamlCommentDumper(final YamlConfigurationOptions options, final YamlCommentMapper yamlCommentMapper, final Reader reader) {
        super(options, reader);
        this.yamlCommentMapper = yamlCommentMapper;
    }

    /**
     * Merge comments from the comment mapper with lines from the reader.
     *
     * @return the resulting String
     * @throws IOException if any problem while reading arise
     */
    public String dump() throws IOException {
        if (this.yamlCommentMapper == null) {
            return this.reader.lines().collect(Collectors.joining("\n"));
        }

        this.builder = new StringBuilder();

        while (this.nextLine()) {
            if (!this.isComment()) { // Avoid duplicating header
                final String path = this.track().getPath();
                KeyTree.Node node = this.getNode(path);
                if (node == null) {
                    node = tryQuotedPath(path);
                }
                this.append(node, KeyTree.Node::getComment);
                this.builder.append(this.currentLine);
                this.append(node, KeyTree.Node::getSideComment);
                this.builder.append('\n');
            }
        }

        // Append end of file comment (null path), if found
        this.append(this.getNode(null), KeyTree.Node::getComment);

        this.reader.close();

        return this.builder.toString();
    }

    private KeyTree.Node tryQuotedPath(String path) {
        KeyTree.Node node = null;

        String quotedPath = getQuotedPath(path, "\"");
        if (quotedPath != null) {
            node = this.getNode(quotedPath);
        }

        if (node == null) {
            quotedPath = getQuotedPath(path, "'");
        }
        if (quotedPath != null) {
            node = this.getNode(quotedPath);
        }
        return node;
    }

    private String getQuotedPath(String path, String quote) {
        if (path == null) {
            return null;
        }

        char pathSeparator = this.keyTree.getPathSeparator();
        int i = path.indexOf(pathSeparator);
        if (i < 0 || i >= path.length() - 1) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(path, 0, i);
        stringBuilder.append(path.charAt(i));
        stringBuilder.append(quote);
        stringBuilder.append(path.substring(i + 1));
        stringBuilder.append(quote);
        return stringBuilder.toString();
    }

    @Override
    protected KeyTree.Node getNode(final String path) {
        return this.yamlCommentMapper.getNode(path);
    }

    private void append(final KeyTree.Node node, final Function<KeyTree.Node, String> getter) {
        if (node != null) {
            final String s = getter.apply(node);
            if (s != null) {
                this.builder.append(s);
            }
        }
    }

}
