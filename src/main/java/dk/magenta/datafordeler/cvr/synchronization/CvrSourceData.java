package dk.magenta.datafordeler.cvr.synchronization;

import dk.magenta.datafordeler.core.io.PluginSourceData;

public class CvrSourceData implements PluginSourceData {

    private String schema;
    private String data;

    public CvrSourceData(String schema, String data) {
        this.schema = schema;
        this.data = data;
    }

    @Override
    public String getSchema() {
        return this.schema;
    }

    @Override
    public String getData() {
        return this.data;
    }

}
