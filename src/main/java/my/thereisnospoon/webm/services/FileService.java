package my.thereisnospoon.webm.services;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class FileService {

	@Autowired
	private GridFsTemplate gridFsTemplate;

	public GridFSFile storeWebM(InputStream inputStream, String name) {

		return gridFsTemplate.store(inputStream, name);
	}
}
