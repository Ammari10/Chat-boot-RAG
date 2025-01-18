package achraf.chatbootrag.config;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Component
public class RagDataLoader {

    private final EmbeddingModel embeddingModel;
    @Value("classpath:/PDFs/Cv.pdf")
    private Resource pdfResource;

    @Value("store-data-v1.json")
    private String storeFile;

    public RagDataLoader(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }
    @Bean
    public SimpleVectorStore simpleVectorStore(EmbeddingModel embeddingModel) {
        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingModel);
        String fileStore = Path.of("src", "main", "resources", "store")
                .toAbsolutePath().toString() + "/" + storeFile;

        File file = new File(fileStore);
        if (!file.exists()) {
            // Initialize PagePdfDocumentReader to read the PDF
            PagePdfDocumentReader pdfDocumentReader =
                    new PagePdfDocumentReader(pdfResource);
            List<Document> documents = pdfDocumentReader.get();

            // Use TokenTextSplitter to split the documents
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> chunks = textSplitter.split(documents); // enregistre sur le store

            // Add the split documents to the vector store
            vectorStore.accept(chunks);
            vectorStore.save(file);
        } else  {
            vectorStore.load(file);
        }

        return vectorStore; // vectore store utilise  le model embiding pour
        // faire une presentation victorielle avant qu il soit enregistre dans la BD
    }
}