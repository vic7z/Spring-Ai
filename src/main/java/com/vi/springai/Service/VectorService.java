package com.vi.springai.Service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class VectorService {
    private final SimpleVectorStore vectorStore;

    @Value("classpath:/story.txt")
    private Resource story;

    public VectorService(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void addFile(Resource data, File file) {
        if (file.exists()) {
            vectorStore.load(file);
        } else {
            TextReader textReader = new TextReader(data);
            textReader.getCustomMetadata()
                    .put("filename", data.getFilename());
            List<Document> documents = textReader.get();
            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();
            List<Document> split = tokenTextSplitter.split(documents);
            vectorStore.add(split);
            vectorStore.save(file);
        }
    }
    public void populateData(){
        addFile(story,new File("vectors.json"));
    }
    public void addFilesFromDirectory(){

    }
}
