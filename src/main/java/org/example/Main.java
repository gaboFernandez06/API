package org.example;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.docs.v1.Docs;
import com.google.api.services.docs.v1.DocsScopes;
import com.google.api.services.docs.v1.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// Client ID :267255116747-8c8k42jq5kmdrh4nt9prnbin719nj0c2.apps.googleusercontent.com
// Client secrent : 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
public class Main {
        /**
         * Application name.
         */
        private static final String APPLICATION_NAME = "Google Docs API Java Quickstart";
        /**
         * Global instance of the JSON factory.
         */
        private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
        /**
         * Directory to store authorization tokens for this application.
         */
        private static final String TOKENS_DIRECTORY_PATH = "tokens";
        private static final String DOCUMENT_ID = "1FTptqsg-yZnml4-4MsS7GjtejouHUhBEyWQFjB2XuFU";

        /**
         * Global instance of the scopes required by this quickstart.
         * If modifying these scopes, delete your previously saved tokens/ folder.
         */
        private static final List<String> SCOPES =
                Collections.singletonList(DocsScopes.DOCUMENTS);
        private static final String CREDENTIALS_FILE_PATH = "/clientSecret.json";
        /**
         * Creates an authorized Credential object.
         *
         * @param HTTP_TRANSPORT The network HTTP Transport.
         * @return An authorized Credential object.
         * @throws IOException If the credentials.json file cannot be found.
         */
        private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
      throws IOException {
            // Load client secrets.
            InputStream in = Main.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
            if (in == null) {
                throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
            }
            GoogleClientSecrets clientSecrets =
                    GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

            // Build flow and trigger user authorization request.
            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
            LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
            Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            //returns an authorized Credential object.
            return credential;
        }
    private static String readParagraphElement(ParagraphElement element) {
        TextRun run = element.getTextRun();
        if (run == null || ((TextRun) run).getContent() == null) {
            // The TextRun can be null if there is an inline object.
            return "";
        }
        return run.getContent();
    }
    private static String readStructuralElements(List<StructuralElement> elements) {
        StringBuilder sb = new StringBuilder();
        for (StructuralElement element : elements) {
            if (element.getParagraph() != null) {
                for (ParagraphElement paragraphElement : element.getParagraph().getElements()) {
                    sb.append(readParagraphElement(paragraphElement));
                }
            } else if (element.getTable() != null) {
                // The text in table cells are in nested Structural Elements and tables may be
                // nested.
                for (TableRow row : element.getTable().getTableRows()) {
                    for (TableCell cell : row.getTableCells()) {
                        sb.append(readStructuralElements(cell.getContent()));
                    }
                }
            } else if (element.getTableOfContents() != null) {
                // The text in the TOC is also in a Structural Element.
                sb.append(readStructuralElements(element.getTableOfContents().getContent()));
            }
        }
        return sb.toString();
    }
        public static void outputCode () throws IOException, GeneralSecurityException {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Docs service =
                    new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            Document doc = service.documents().get(DOCUMENT_ID).execute();
            System.out.println(readStructuralElements(doc.getBody().getContent()));
         }
         public static void getBody( ) throws GeneralSecurityException , IOException
         {
             final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
             Docs service =
                     new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                             .setApplicationName(APPLICATION_NAME)
                             .build();

             Document doc = service.documents().get(DOCUMENT_ID).execute();
             System.out.println(readStructuralElements(doc.getBody().getContent()));
         }
        public static void main(String... args) throws IOException, GeneralSecurityException {
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Docs service = new Docs.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            // Prints the title of the requested doc:
            // https://docs.google.com/document/d/195j9eDD3ccgjQRttHhJPymLJUCOUjs-jmwTrekvdjFE/edit
            Document response = service.documents().get(DOCUMENT_ID).execute();
            String title = response.getTitle();

            System.out.printf("The title of the doc is: %s\n", title);
            getBody();

            /*
            1. Method Rename a document title
            2. Method Output body of google doc as a String
            3. Have a string method that will write to a google doc (AKA take in a string)
            */
        }
       // Client ID :267255116747-8c8k42jq5kmdrh4nt9prnbin719nj0c2.apps.googleusercontent.com
       // Client secrent : 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
    }
