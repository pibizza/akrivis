package org.kie.akrivis.quarkus.result;

import java.util.ArrayList;
import java.util.Collection;

public class Records {
    private Collection<Record> records = new ArrayList<>();

    public Records() {
    }

    public Records(Collection<Record> records) {
        this.records.addAll(records);
    }

    public Collection<Record> getRecords() {
        return records;
    }
}
