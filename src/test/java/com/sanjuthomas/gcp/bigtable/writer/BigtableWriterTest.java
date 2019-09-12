package com.sanjuthomas.gcp.bigtable.writer;

import com.sanjuthomas.gcp.bigtable.bean.WritableRow;
import com.sanjuthomas.gcp.bigtable.config.WriterConfig;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.connect.sink.SinkRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.sanjuthomas.gcp.bigtable.config.TransformerConfig;
import com.sanjuthomas.gcp.bigtable.transform.JsonEventTransformer;
import com.sanjuthomas.gcp.resolvers.SinkRecordResolver;

@Integration
public class BigtableWriterTest {

	private BigtableWriter writer;
	private JsonEventTransformer transformer;
	private final List<String> keyQualifiers = Arrays.asList(new String[] { "symbol" });
	private final List<String> families = Arrays.asList(new String[] { "data", "metadata" });
	private Map<String, List<String>> familyToQualifierMapping;

	@BeforeEach
	public void setup() throws FileNotFoundException, IOException {
		this.familyToQualifierMapping = new HashMap<>();
		this.familyToQualifierMapping.put("data", Arrays.asList(new String[] { "symbol", "name", "sector" }));
		this.familyToQualifierMapping.put("metadata",
				Arrays.asList(new String[] { "create_time", "processing_time", "topic" }));
		final TransformerConfig config = new TransformerConfig(this.keyQualifiers, "_", this.families,
				this.familyToQualifierMapping);
		this.transformer = new JsonEventTransformer(config);
		this.writer = new BigtableWriter(new WriterConfig("/Users/sathomas/keys/demo-key.json", "demo-project",
				"demo-instance", "demo-table"));
	}

	@Test
	@ExtendWith(SinkRecordResolver.class)
	public void shouldWrite(final SinkRecord record) {
		final WritableRow row = this.transformer.transform(record);
		writer.buffer(row);
		writer.flush();
	}

}