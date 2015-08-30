package my.thereisnospoon.webm.services;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class LuceneTest {

	private IndexReader reader;
	private Analyzer analyzer;
	private IndexSearcher searcher;

	private static final String TITLE_FIELD_NAME = "title";
	private static final String CONTENT_FIELD_NAME = "content";

	@Before
	public void setUp() throws Exception {

		Directory directory = new RAMDirectory();
		analyzer = new EnglishAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, config);

		Document doc1 = new Document();
		doc1.add(new TextField(TITLE_FIELD_NAME, "the quick brown fox jumps over the lazy dog", Field.Store.NO));
		doc1.add(new TextField(CONTENT_FIELD_NAME, "i don't know what to write here", Field.Store.YES));

		Document doc2 = new Document();
		doc2.add(new TextField(TITLE_FIELD_NAME, "another stupid sentence about dog", Field.Store.NO));
		doc2.add(new TextField(CONTENT_FIELD_NAME, "a batch of words is situated here", Field.Store.YES));

		indexWriter.addDocument(doc1);
		indexWriter.addDocument(doc2);
		indexWriter.close();

		reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
	}

	@After
	public void tearDown() throws Exception {
		reader.close();
	}

	@Test
	public void test1() throws Exception {

		QueryParser queryParser = new QueryParser("title", analyzer);
		TopDocs topDocs = searcher.search(queryParser.parse("content:what AND title:dog"), 10);

		assertEquals("Hits count should 1", 1, topDocs.totalHits);
		assertEquals(searcher.search(queryParser.parse("title:dogs"), 10).totalHits, 2);
	}

	@Test
	public void test2() throws Exception {

		Query query = new PhraseQuery.Builder()
				.add(new Term(TITLE_FIELD_NAME, "quick"))
				.add(new Term(TITLE_FIELD_NAME, "fox"))
				.setSlop(1)
				.build();

		TopDocs topDocs = searcher.search(query, 10);
		assertEquals(1, topDocs.totalHits);

		Document document = searcher.doc(topDocs.scoreDocs[0].doc);
	}
}
