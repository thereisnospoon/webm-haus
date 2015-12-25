package my.thereisnospoon.webm.services.gridfs;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import my.thereisnospoon.webm.AbstractIntegrationTest;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

public class GridFsServiceTest extends AbstractIntegrationTest {

	private static final Integer DATA_SIZE = 10_000;

	private static Random randomGenerator;

	@Autowired
	private GridFsService gridFsService;

	private byte[] data;
	private String dataHash;
	private String fileId;

	@BeforeClass
	public static void prepareTestCases() {
		randomGenerator = new Random(System.currentTimeMillis());
	}

	@Before
	public void setUp() {

		data = new byte[DATA_SIZE];
		randomGenerator.nextBytes(data);
		dataHash = DigestUtils.md5DigestAsHex(data);
	}

	@After
	public void cleanUp() {
		gridFsService.deleteData(fileId);
	}

	@Test
	public void testStoreData() {

		GridFSFile dataFile = gridFsService.storeData(data, ContentType.VIDEO);
		fileId = dataFile.getId().toString();
		assertEquals(dataHash, dataFile.getMD5());
	}

	@Test
	public void testLoadData() throws IOException {

		GridFSFile dataFile = gridFsService.storeData(data, ContentType.VIDEO);
		fileId = dataFile.getId().toString();
		GridFSDBFile loadedData = gridFsService.loadDataFromDB(fileId);

		assertEquals(dataHash, loadedData.getMD5());
		assertArrayEquals(data, IOUtils.toByteArray(loadedData.getInputStream()));
	}

	@Test
	public void testIsDataUnique() {

		gridFsService.storeData(data, ContentType.VIDEO);
		assertFalse(gridFsService.isDataUnique(dataHash, ContentType.VIDEO));
	}

	@Test
	public void testDeleteData() {

		GridFSFile dataFile = gridFsService.storeData(data, ContentType.VIDEO);
		fileId = dataFile.getId().toString();
		gridFsService.deleteData(fileId);

		GridFSFile dataFileAfterDeletion = gridFsService.loadDataFromDB(fileId);
		assertNull(dataFileAfterDeletion);
	}
}