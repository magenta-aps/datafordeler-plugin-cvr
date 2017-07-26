package dk.magenta.datafordeler.cvr.synchronization;

import dk.magenta.datafordeler.core.io.PluginSourceData;

public class CvrSourceData implements PluginSourceData {

    private String schema;
    private String data;
    private String id;

    public CvrSourceData(String schema, String data, String id) {
        this.schema = schema;
        this.data = data;
        this.id = id;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public String getReference() {
        return null;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
