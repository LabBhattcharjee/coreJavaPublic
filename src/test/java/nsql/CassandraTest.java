package nsql;

import java.net.InetSocketAddress;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

//https://cassandra.apache.org/quickstart/
//https://stackoverflow.com/questions/18712967/how-to-list-all-the-available-keyspaces-in-cassandra
//https://www.baeldung.com/cassandra-with-java
//https://stackoverflow.com/questions/45124300/cant-connect-to-cassandra-docker-container-from-my-node-js-app
//docker run --name cassandraDb -d -p 7199:7199 -p 7000:7000 -p 9042:9042 -p 9160:9160 -p7001:7001 cassandra
//https://docs.datastax.com/en/cql-oss/3.3/cql/cqlIntro.html

//https://docs.datastax.com/en/developer/java-driver/4.0/upgrade_guide/

public class CassandraTest {

	@Test
	public void myTestMethod() {

		final String node = "172.18.17.244";

		final CqlSession session = CqlSession.builder().addContactPoint(new InetSocketAddress(node, 9042))
				// .withKeyspace("SomeKS")
				.withLocalDatacenter("datacenter1").build();

//		com.datastax.oss.driver.api.core.type.codec.CodecNotFoundException: Codec not found for requested operation: [null <-> java.sql.Timestamp]
//				at com.datastax.oss.driver.internal.core.type.codec.registry.CachingCodecRegistry.createCodec(CachingCodecRegistry.java:639)

		session.execute(SimpleStatement.builder(
				"CREATE KEYSPACE IF NOT EXISTS store WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : '1' }")
				.build());

		session.execute(SimpleStatement
				.builder("CREATE TABLE IF NOT EXISTS store.shopping_cart ( "
						+ "userid text PRIMARY KEY,   item_count int," + "  last_update_timestamp timestamp  )")
				.build());

		session.execute(SimpleStatement.builder("CREATE TABLE IF NOT EXISTS store.book (\r\n" + "id UUID, \r\n"
				+ "   isbn text,\r\n" + "   title text,\r\n" + "   author text,\r\n" + "  publisher text,\r\n"
				+ "   category text,\r\n" + "   PRIMARY KEY ((title, publisher),id)\r\n"
				+ ")   WITH CLUSTERING ORDER BY (id DESC)").build());

		// session.execute(SimpleStatement.builder("ALTER TABLE store.book ADD tags
		// set<text>").build());

		{

			final Instant now = Instant.now();
			session.execute(

					SimpleStatement.newInstance(
							"INSERT INTO store.shopping_cart	(userid, item_count, last_update_timestamp) VALUES (?, ?, ?)",
							"9876", 20, now));

			session.execute(SimpleStatement.builder(
					"INSERT INTO store.shopping_cart	(userid, item_count, last_update_timestamp) VALUES (?, ?, ?)")
					.addPositionalValue("1234").addPositionalValue(5).addPositionalValue(now).build());

			session.execute(SimpleStatement.builder(
					"INSERT INTO store.shopping_cart	(userid, item_count, last_update_timestamp) VALUES (?, ?, ?)")
					.addPositionalValue("9876").addPositionalValue(21).addPositionalValue(now).build());

//			-- Insert some data
//			INSERT INTO store.shopping_cart	(userid, item_count, last_update_timestamp) VALUES ('9876', 2, toTimeStamp(now()));
//			INSERT INTO store.shopping_cart (userid, item_count, last_update_timestamp) VALUES ('1234', 5, toTimeStamp(now()));

			final ResultSet rs = session.execute(SimpleStatement.builder("SELECT * FROM store.shopping_cart").build());
			rs.all().stream().forEach(row -> {
				final Object object = row.getObject(1);
				final Instant object2 = row.getInstant(2);
				System.out.printf("\n%s %s %s %s %s\n", row.getString("userid"), object,
						object == null ? null : object.getClass(), object2,
						object2 == null ? null : object2.getClass());

			});

			// Return the first element of ResultSet
			// final Row row = rs.one();
			// Print out the firstname and email
			// System.out.format("%s %s\n", row.getString("userid"), row.getInt(2),
			// row.getLocalDate(3));

		}

		{
			final ResultSet rs = session.execute(SimpleStatement.builder("SELECT * FROM store.book").build());
			rs.all().stream().forEach(row -> {

				final int size = row.size();
				for (int idx = 0; idx < size; idx++) {
					System.out.println(row.getObject(idx));
				}
				// ColumnDefinitions columnDefinitions = row.getColumnDefinitions();

			});
		}

		try {
			// Select for the user
			// final ResultSet rs =
			// https://stackoverflow.com/questions/29314578/cassandra-query-making-cannot-execute-this-query-as-it-might-involve-data-filt

			// session.execute(SimpleStatement
			// .builder("SELECT * FROM store.shopping_cart WHERE item_count =
			// ?").addPositionalValue(20).build());

//			com.datastax.oss.driver.api.core.servererrors.InvalidQueryException: Cannot execute this query as it might involve data filtering and thus may have unpredictable performance. If you want to execute this query despite the performance unpredictability, use ALLOW FILTERING
//			at com.datastax.oss.driver.api.core.servererrors.InvalidQueryException.copy(InvalidQueryException.java:48)
//			at com.datastax.oss.driver.internal.core.util.concurrent.CompletableFutures.getUninterruptibly(CompletableFutures.java:149)
//			at com.datastax.oss.driver.internal.core.cql.CqlRequestSyncProcessor.process(CqlRequestSyncProcessor.java:53)
//			at com.datastax.oss.driver.internal.core.cql.CqlRequestSyncProcessor.process(CqlRequestSyncProcessor.java:30)
//			at com.datastax.oss.driver.internal.core.session.DefaultSession.execute(DefaultSession.java:230)
//			at com.datastax.oss.driver.api.core.cql.SyncCqlSession.execute(SyncCqlSession.java:54)
//			at nsql.CassandraTest.myTestMethod(CassandraTest.java:87)

		} catch (final Exception e) {
			e.printStackTrace();
		}

		final ResultSet rs = session.execute(SimpleStatement
				.builder("SELECT * FROM store.shopping_cart WHERE userid = ?").addPositionalValue("9876").build());

		rs.all().stream().forEach(row -> {
			final Object object = row.getObject(1);
			final Instant object2 = row.getInstant(2);
			System.out.printf("\n%s %s %s %s %s\n", row.getString("userid"), object,
					object == null ? null : object.getClass(), object2, object2 == null ? null : object2.getClass());

		});
	}

}
