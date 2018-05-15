import static org.junit.Assert.assertTrue;

import hello.Application;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Types;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class BinaryStreamTest {

  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  private final DefaultLobHandler lobHandler = new DefaultLobHandler();

  @Test
  public void getBinaryStream() throws IOException {

    int id = getId();

    InputStream inputStream = getInputStream(id, (rs, rowNum) -> rs.getBinaryStream("Data"));

    assertTrue(inputStream.available() > 0);
  }

  @Test
  public void getBlobGetBinaryStream() throws IOException {

    int id = getId();

    InputStream inputStream = getInputStream(id, (rs, rowNum) -> rs.getBlob("Data").getBinaryStream());

    assertTrue(inputStream.available() > 0);
  }


  @Test
  public void lobHandler() throws IOException {

    int id = getId();

    InputStream inputStream = getInputStream(id, (rs, rowNum) -> lobHandler.getBlobAsBinaryStream(rs, "Data"));

    assertTrue(inputStream.available() > 0);
  }

  private int getId() {
    String sql =
        " INSERT INTO test (Data) " +
        " VALUES (:data) ";

    KeyHolder keyHolder = new GeneratedKeyHolder();

    SqlParameterSource params = new MapSqlParameterSource().
        addValue("data", new SqlLobValue("Hello World"), Types.BLOB);

    jdbcTemplate.update(sql, params, keyHolder);

    return keyHolder.getKey().intValue();
  }

  private InputStream getInputStream(int id, RowMapper<InputStream> rowMapper) {
    String selectData =
        " SELECT Data " +
        " FROM test " +
        " WHERE id = :id ";

    SqlParameterSource selectParams = new MapSqlParameterSource("id", id);

    return jdbcTemplate.queryForObject(selectData, selectParams, rowMapper);
  }
}
