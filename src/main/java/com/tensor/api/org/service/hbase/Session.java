package com.tensor.api.org.service.hbase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {

    private Table table;

    public void close() {
        try {
            table.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
