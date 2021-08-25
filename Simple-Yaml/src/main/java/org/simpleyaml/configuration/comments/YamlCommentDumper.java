package org.simpleyaml.configuration.comments;

import org.simpleyaml.configuration.file.YamlConfigurationOptions;

import java.io.IOException;
import java.io.Reader;

public class YamlCommentDumper extends YamlCommentReader {

    private final YamlCommentMapper yamlCommentMapper;

    private StringBuilder builder;

    public YamlCommentDumper(final YamlConfigurationOptions options, final YamlCommentMapper yamlCommentMapper, final Reader reader) {
        super(options, reader);
        this.yamlCommentMapper = yamlCommentMapper;
    }

    private String joiningLines() throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        if (reader == null) {
            return stringBuilder.toString();
        }

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Merge comments from the comment mapper with lines from the reader.
     *
     * @return the resulting String
     * @throws IOException if any problem while reading arise
     */
    public String dump() throws IOException {
        if (this.yamlCommentMapper == null) {
            return joiningLines();
        }

        this.builder = new StringBuilder();

        while (this.nextLine()) {
            if (!this.isComment()) { // Avoid duplicating header
                final String path = this.track().getPath();
                KeyTree.Node node = this.getNode(path);
                if (node == null) {
                    node = tryQuotedPath(path);
                }
                this.append(node, CommentSupplier.INSTANCE);
                this.builder.append(this.currentLine);
                this.append(node, SideCommentSupplier.INSTANCE);
                this.builder.append('\n');
            }
        }

        // Append end of file comment (null path), if found
        this.append(this.getNode(null), CommentSupplier.INSTANCE);

        this.reader.close();

        return this.builder.toString();
    }

    private KeyTree.Node tryQuotedPath(String path) {
        String quotedPath;
        KeyTree.Node node = null;

        if (node == null && (quotedPath = getQuotedPath(path, "\"")) != null) {
            node = this.getNode(quotedPath);
        }

        if (node == null && (quotedPath = getQuotedPath(path, "'")) != null) {
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

        String rawKey = getLastRawKey(path, i);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(path, 0, i);
        stringBuilder.append(path.charAt(i));
        stringBuilder.append(quote);
        stringBuilder.append(rawKey);
        stringBuilder.append(quote);
        return stringBuilder.toString();
    }

    private String getLastRawKey(String path, int i) {
        int startIndex = i + 1;
        int endIndex = path.length() - 1;
        if (path.charAt(startIndex) == path.charAt(endIndex)
                && startIndex + 1 < endIndex
                && (path.charAt(endIndex) == '\'' || path.charAt(endIndex) == '"')) {
            return path.substring(startIndex + 1, endIndex);
        }

        return path.substring(startIndex);
    }

    @Override
    protected KeyTree.Node getNode(final String path) {
        return this.yamlCommentMapper.getNode(path);
    }

    private void append(final KeyTree.Node node, final Supplier supplier) {
        if (node != null && supplier != null) {
            final String s = supplier.apply(node);
            if (s != null) {
                this.builder.append(s);
            }
        }
    }

    private interface Supplier {

        String apply(KeyTree.Node node);

    }

    private static class CommentSupplier implements Supplier {

        private static final Supplier INSTANCE = new CommentSupplier();

        @Override
        public String apply(KeyTree.Node node) {
            return node == null ? null : node.getComment();
        }
    }

    private static class SideCommentSupplier implements Supplier {

        private static final Supplier INSTANCE = new SideCommentSupplier();

        @Override
        public String apply(KeyTree.Node node) {
            return node == null ? null : node.getSideComment();
        }
    }

}
