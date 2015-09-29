package my.thereisnospoon.webm.services.gridfs.impl;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import my.thereisnospoon.webm.services.gridfs.ContentType;
import my.thereisnospoon.webm.services.gridfs.GridFsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class GridFsServiceImpl implements GridFsService {

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Override
	public boolean isDataUnique(String dataHash, ContentType contentType) {
		return gridFsTemplate.findOne(query(where("md5").is(dataHash).and("contentType").is(ContentType.VIDEO))) == null;
	}

	@Override
	public GridFSFile storeData(byte[] data, ContentType contentType) {
		return gridFsTemplate.store(new ByteArrayInputStream(data), "", contentType.toString());
	}

	@Override
	public GridFSDBFile loadDataFromDB(String fileId) {
		return gridFsTemplate.findOne(query(where("_id").is(fileId)));
	}

	@Override
	public void deleteData(String fileId) {
		gridFsTemplate.delete(query(where("_id").is(fileId)));
	}
}
