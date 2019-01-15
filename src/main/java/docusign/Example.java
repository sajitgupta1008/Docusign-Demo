package docusign;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.client.auth.OAuth;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import static java.util.Collections.singletonList;

public class Example {
    
    private static final String INTEGRATION_KEY = "82474ebe-9710-4015-a29c-47aa86bf1e06";
    
    public static void main(String[] args) throws IOException, ApiException {
        
        ApiClient apiClient = new ApiClient("https://demo.docusign.net/restapi");
        
        apiClient.setAccessToken("eyJ0eXAiOiJNVCIsImFsZyI6IlJTMjU2Iiwia2lkIjoiNjgxODVmZjEtNGU1MS00Y2U5LWFmMWMtNjg5ODEyMjAzMzE3In0.AQkAAAABAAUABwAAO28r0HrWSAgAAKMzjdh61kgCADdA5t01h_BEomb4nIrJOkgVAAEAAAAYAAEAAAAFAAAADQAkAAAAODI0NzRlYmUtOTcxMC00MDE1LWEyOWMtNDdhYTg2YmYxZTA2IwAkAAAAODI0NzRlYmUtOTcxMC00MDE1LWEyOWMtNDdhYTg2YmYxZTA2EgABAAAABgAAAGp3dF9icg.ynA-lt686swPE-SMHb9ixO3dwCGWoDpY_hWiKUdCFHxQRJKU1IB2S6JihcoRn5qpuy4i8uK_ocKgvAjl-PI3GDCP5SF4eW5kuBx_K8avew7NNneUbQy0-VHo90cStwCyesOBu0xtgoBB0wZ1qx-ry2Bp9gjuFhBI-8K5SSO1rE8o_JynHzvb4ts_kwzNVST7D-8gtaa3AHKUICWFPiguOL1_7pEqgpq2_gGK8JOxmcZqHUjtMVkSVw3-TIKvAepITIBWCfATrY7yBF0iUgU7IDwyMElob8nVk_-II9JK1o2P0OB1MqTVrJTX1_5JJfeAoN4UPNqIr6YCvMUY2YbPIg"
                , 3600L);
        
        System.out.println("access token :" + apiClient.getAccessToken());
        
        OAuth.UserInfo userInfo = apiClient.getUserInfo(apiClient.getAccessToken());
        System.out.println(userInfo);
        
        apiClient.setBasePath(userInfo.getAccounts().get(0).getBaseUri() + "/restapi");
        String accountId = userInfo.getAccounts().get(0).getAccountId();
        System.out.println("account Id : " + accountId);
        
        Configuration.setDefaultApiClient(apiClient);
        
        EnvelopesApi envelopesApi = new EnvelopesApi();
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, getEnvelopeDefinition());
        
        System.out.println(envelopeSummary);
        
    }
    
    private static EnvelopeDefinition getEnvelopeDefinition() {
        EnvelopeDefinition ed = new EnvelopeDefinition();
        
        ed.setEmailSubject("Royal Embedded Email Subject");
        ed.setEmailBlurb("Royal Embedded Testing Email Blurb");
        ed.status("created");
        
        Document document = new Document();
        document.documentBase64(Base64.getEncoder().encodeToString(readFileBytes()));
        document.setName("doc1");
        document.setFileExtension("pdf");
        document.setDocumentId("1");
        
        ed.setDocuments(singletonList(document));
        
        Signer signer = new Signer();
        signer.setEmail("sajit.gupta@knoldus.in");
        signer.setName("Crew Member");
        signer.recipientId("1");
        signer.routingOrder("1");
        signer.setClientUserId(INTEGRATION_KEY);
        
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
    
    private static byte[] readFileBytes() {
        try {
            return Files.readAllBytes(Paths.get("src/main/resources", "corp_lorem.pdf"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
