import org.example.Main;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Nested
    class LibraryTest {
        private Main.Library<Main.Document> library;
        private Main.Contract contract;
        private Main.Receipt receipt;
        private Main.Resume resume;

        @BeforeEach
        void setUp() {
            library = new Main.Library<>();
            contract = new Main.Contract("C001", 1000, "2024-01-01");
            receipt = new Main.Receipt("R001", 500);
            resume = new Main.Resume("RS001", "John Doe");
        }

        @Test
        void testPutAndGetDocument() throws Main.DocumentNotFoundException {
            try{
                library.putDocument(contract);
                assertEquals(contract, library.getDocument("C001"));

            }catch (Main.DocumentNotFoundException e){
                throw new Main.DocumentNotFoundException("Document with id " + contract.getId() + " not found");
            }
        }

        @Test
        void testGetNonExistentDocument() {
            assertThrows(Main.DocumentNotFoundException.class, () ->
                    library.getDocument("nonexistent"));
        }

        @Test
        void testRemoveDocument() throws Main.DocumentNotFoundException {
            library.putDocument(contract);
            library.removeDocument("C001");
            assertThrows(Main.DocumentNotFoundException.class, () ->
                    library.getDocument("C001"));
        }

        @Test
        void testRemoveNonExistentDocument() {
            assertThrows(Main.DocumentNotFoundException.class, () ->
                    library.removeDocument("nonexistent"));
        }
    }

    @Nested
    class DocumentParserTest {
        private Main.DocumentParser parser;
        private File testFile;

        @BeforeEach
        void setUp() {
            parser = new Main.DocumentParser();
        }

        private void createTestFile(String content) throws IOException {
            testFile = File.createTempFile("test", ".json");
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write(content);
            }
            testFile.deleteOnExit();
        }

        @Test
        void testParseContract() throws Exception {
            String json = "{\"document_type\":\"CONTRACT\",\"id\":\"C001\",\"cost\":1000,\"date\":\"2024-01-01\"}";
            createTestFile(json);

            Main.Document doc = parser.parseDocument(testFile.getPath());

            assertInstanceOf(Main.Contract.class, doc);
            assertEquals("C001", doc.getId());
        }

        @Test
        void testParseReceipt() throws Exception {
            String json = "{\"document_type\":\"RECEIPT\",\"id\":\"R001\",\"money_amount\":500}";
            createTestFile(json);

            Main.Document doc = parser.parseDocument(testFile.getPath());

            assertInstanceOf(Main.Receipt.class, doc);
            assertEquals("R001", doc.getId());
        }

        @Test
        void testParseResume() throws Exception {
            String json = "{\"document_type\":\"RESUME\",\"id\":\"RS001\",\"name\":\"John Doe\"}";
            createTestFile(json);

            Main.Document doc = parser.parseDocument(testFile.getPath());

            assertInstanceOf(Main.Resume.class, doc);
            assertEquals("RS001", doc.getId());
        }

        @Test
        void testInvalidDocumentType() throws IOException {
            String json = "{\"document_type\":\"INVALID\",\"id\":\"X001\"}";
            createTestFile(json);

            assertThrows(Main.InvalidDocumentTypeException.class, () ->
                    parser.parseDocument(testFile.getPath()));
        }

        @Test
        void testMissingRequiredField() throws IOException {
            String json = "{\"document_type\":\"CONTRACT\",\"id\":\"C001\"}";
            createTestFile(json);

            assertThrows(Main.MissingFieldException.class, () ->
                    parser.parseDocument(testFile.getPath()));
        }

        @Test
        void testMissingDocumentType() throws IOException {
            String json = "{\"id\":\"C001\",\"cost\":1000,\"date\":\"2024-01-01\"}";
            createTestFile(json);

            assertThrows(Main.MissingFieldException.class, () ->
                    parser.parseDocument(testFile.getPath()));
        }

        @Test
        void testInvalidJsonFormat() throws IOException {
            String json = "invalid json";
            createTestFile(json);

            assertThrows(Main.UnsupportedValueTypeException.class, () ->
                    parser.parseDocument(testFile.getPath()));
        }

        @Test
        void testFileNotFound() {
            assertThrows(Main.FileNotFoundException.class, () ->
                    parser.parseDocument("nonexistent.json"));
        }
    }

    @Nested
    class JsonParserTest {
        private Main.DocumentParser parser;

        @BeforeEach
        void setUp() {
            parser = new Main.DocumentParser();
        }

        @Test
        void testParseValidJson() throws Exception {
            String json = "{\"key1\":\"value1\",\"key2\":42}";
            var result = parser.parseJson(json);

            assertEquals("value1", result.get("key1"));
            assertEquals(42, result.get("key2"));
        }

        @Test
        void testInvalidJsonFormat() {
            String json = "invalid json";
            assertThrows(Main.UnsupportedValueTypeException.class, () ->
                    parser.parseJson(json));
        }

        @Test
        void testInvalidKeyValuePair() {
            String json = "{key1:value1:extra}";
            assertThrows(Main.UnsupportedValueTypeException.class, () ->
                    parser.parseJson(json));
        }

        @Test
        void testUnsupportedValueType() {
            String json = "{\"key1\":true}";
            assertThrows(Main.UnsupportedValueTypeException.class, () ->
                    parser.parseJson(json));
        }
    }
}