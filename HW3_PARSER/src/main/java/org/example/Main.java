package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static class FileNotFoundException extends Exception {
        public FileNotFoundException(String message) {
            super(message);
        }
    }

    public static class UnsupportedValueTypeException extends Exception {
        public UnsupportedValueTypeException(String message) {
            super(message);
        }
    }

    public static class MissingFieldException extends Exception {
        public MissingFieldException(String message) {
            super(message);
        }
    }

    public static class InvalidDocumentTypeException extends Exception {
        public InvalidDocumentTypeException(String message) {
            super(message);
        }
    }

    public static class DocumentNotFoundException extends Exception {
        public DocumentNotFoundException(String message) {
            super(message);
        }
    }

    public abstract static class Document {
        protected String id;

        public Document(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

    public static class Library<T extends Document> {
        private final Map<String, T> documents = new HashMap<>();

        public void putDocument(T document) {
            documents.put(document.getId(), document);
        }

        public T getDocument(String id) throws DocumentNotFoundException {
            T document = documents.get(id);
            if (document == null) {
                throw new DocumentNotFoundException("Document with id " + id + " not found");
            }
            return document;
        }

        public void removeDocument(String id) throws DocumentNotFoundException {
            if (!documents.containsKey(id)) {
                throw new DocumentNotFoundException("Document with id " + id + " not found");
            }
            documents.remove(id);
        }
    }

    public static class Contract extends Document {
        private final int cost;
        private final String date;

        public Contract(String id, int cost, String date) {
            super(id);
            this.cost = cost;
            this.date = date;
        }
    }

    public static class Receipt extends Document {
        private final int moneyAmount;

        public Receipt(String id, int moneyAmount) {
            super(id);
            this.moneyAmount = moneyAmount;
        }
    }

    public static class Resume extends Document {
        private final String name;

        public Resume(String id, String name) {
            super(id);
            this.name = name;
        }
    }

    public static class DocumentParser {
        public Document parseDocument(String fileName) throws UnsupportedValueTypeException, MissingFieldException, InvalidDocumentTypeException, FileNotFoundException {
            try {
                String content = new String(Files.readAllBytes(Paths.get(fileName)));
                Map<String, Object> jsonMap = parseJson(content);

                if (!jsonMap.containsKey("document_type")) {
                    throw new MissingFieldException("'document_type' field is missing");
                }

                if (!jsonMap.containsKey("id")) {
                    throw new MissingFieldException("'id' field is missing");
                }

                String documentType = (String) jsonMap.get("document_type");
                String id = (String) jsonMap.get("id");

                return switch (documentType) {
                    case "CONTRACT" -> {
                        if (!jsonMap.containsKey("cost") || !jsonMap.containsKey("date")) {
                            throw new MissingFieldException("cost and date are missing for Contract");
                        }
                        yield new Contract(id, (int) jsonMap.get("cost"), (String) jsonMap.get("date"));
                    }
                    case "RECEIPT" -> {
                        if (!jsonMap.containsKey("money_amount")) {
                            throw new MissingFieldException("missing money_amount field for Receipt");
                        }
                        yield new Receipt(id, (int) jsonMap.get("money_amount"));
                    }
                    case "RESUME" -> {
                        if (!jsonMap.containsKey("name")) {
                            throw new MissingFieldException("name is missing for Resume");
                        }
                        yield new Resume(id, (String) jsonMap.get("name"));
                    }
                    default -> throw new InvalidDocumentTypeException("Invalid document type: " + documentType);
                };
            } catch (IOException e) {
                throw new FileNotFoundException("File not found: " + fileName);
            }

        }

        public Map<String, Object> parseJson(String jsonString) throws UnsupportedValueTypeException {
            Map<String, Object> result = new HashMap<>();
            jsonString = jsonString.trim();
            if (!jsonString.startsWith("{") || !jsonString.endsWith("}")) {
                throw new UnsupportedValueTypeException("given json format is not valid");
            }
            jsonString = jsonString.substring(1, jsonString.length() - 1);
            String[] pairs = jsonString.split(",");
            for (String pair : pairs) {
                String[] keyValue = pair.split(":");
                if (keyValue.length != 2) {
                    throw new UnsupportedValueTypeException("key-value pair not valid: " + pair);
                }
                String key = keyValue[0].trim().replaceAll("\"", "");
                String value = keyValue[1].trim();
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    result.put(key, value.substring(1, value.length() - 1));
                } else {
                    try {
                        result.put(key, Integer.parseInt(value));
                    } catch (NumberFormatException e) {
                        throw new UnsupportedValueTypeException("value type not supported: " + value);
                    }
                }
            }
            return result;
        }
    }
}