package docusign;

import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.FormBodyPartBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.util.Collections.singletonList;

public class MultipartExample2 {
    
    public static void main(String[] args) throws IOException {
        usingCloseableHttpClient();
    }
    
    private static EnvelopeDefinition getEnvelopeDefinition() {
        EnvelopeDefinition ed = new EnvelopeDefinition();
        
        ed.setEmailSubject("Royal Embedded Email Subject");
        ed.setEmailBlurb("Royal Embedded Testing Email Blurb");
        ed.status("created");
        
        Document document = new Document();
        //  document.documentBase64(Base64.getEncoder().encodeToString(readFileBytes()));
        document.setName("doc1");
        document.setFileExtension("pdf");
        document.setDocumentId("1");
        
        ed.setDocuments(singletonList(document));
        
        Signer signer = new Signer();
        signer.setEmail("sajit.gupta@knoldus.in");
        signer.setName("Crew Member");
        signer.recipientId("1");
        signer.routingOrder("1");
        signer.setClientUserId("82474ebe-9710-4015-a29c-47aa86bf1e06");
        
        SignHere signHere = new SignHere();
        signHere.setDocumentId("1");
        signHere.setRecipientId("1");
        signHere.setTabLabel("CrewMemberSignature");
        signHere.setAnchorString("without revolutionary ROI");
        
        Tabs tabs = new Tabs();
        tabs.signHereTabs(singletonList(signHere));
        
        signer.setTabs(tabs);
        
        Recipients recipients = new Recipients();
        recipients.setSigners(singletonList(signer));
        
        ed.setRecipients(recipients);
        
        return ed;
    }
    
    private static void usingCloseableHttpClient() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        HttpPost uploadFile = new HttpPost("https://demo.docusign.net/restapi/v2/accounts/4b84e552-c934-4aaa-8440-4497c54583c0/envelopes");
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        
        EnvelopeDefinition envelopeDefinition = getEnvelopeDefinition();
        
        builder.addTextBody("jsond", new ObjectMapper().writeValueAsString(envelopeDefinition), ContentType.APPLICATION_JSON);
        uploadFile.setHeader("Authorization", "Bearer eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQkAAAABAAUABwCAoE6QSnzWSAgAgAgT8lJ81kgCADdA5t01h_BEomb4nIrJOkgVAAEAAAAYAAEAAAAFAAAADQAkAAAAODI0NzRlYmUtOTcxMC00MDE1LWEyOWMtNDdhYTg2YmYxZTA2IwAkAAAAODI0NzRlYmUtOTcxMC00MDE1LWEyOWMtNDdhYTg2YmYxZTA2EgABAAAABgAAAGp3dF9icg.0XNORL5kQtpg0e8K1Ravkmno9vPlYPjzvUNp4N-GpYdrxo4ecnoCTQCQoYXeS9qAi8f-wEip2nmNlpRlhY1d_KKVqeLbL4ZErngAR16kwHFMnA7I-4mwLK567HwiPFaTq68ZACGYlEtoWkACu_NvQQD6nYEd_AKmoifBS3aNjc9Tdady526l-SuqC1ieiXBC7TURs9JGqwENwy-1EEtoPWuILVpfaKFY8C1rzSJl3gnc6sfWvyC8QLxQ1lm06q6PxNhtf1g_p5niIkslkR3gwV8JFM6BVPwSJdJvNn5RGZWpsxbeGbOD23ApYSTJDPBlePQMvLlzXQb1iLHcmH1YuQ");
        File f = new File("src/main/resources/corp_lorem.pdf");
        
        builder.addPart(FormBodyPartBuilder.create()
                .setName("file")
                .setField("Content-Disposition", "form-data; filename=\"corp_lorem.pdf\"; documentId=\"1\"")
                .setBody(new InputStreamBody(new FileInputStream(f), ContentType.APPLICATION_OCTET_STREAM))
                .build());
        
        HttpEntity multipart = builder.build();
        
        /*System.out.println("request :");
        multipart.writeTo(System.out);
        
        System.out.println("\n");*/
        
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        
        System.out.println(response.toString());
        HttpEntity responseEntity = response.getEntity();
        System.out.println(EntityUtils.toString(responseEntity));
    }
    
    
   
}
