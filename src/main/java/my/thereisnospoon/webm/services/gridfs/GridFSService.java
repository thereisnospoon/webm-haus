package my.thereisnospoon.webm.services.gridfs;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

public interface GridFsService {

	boolean isDataUnique(String dataHash, ContentType contentType);

	GridFSFile storeData(byte[] data, ContentType contentType);

	GridFSDBFile loadDataFromDB(String fileId);

	void deleteData(String fileId);
}
