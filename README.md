# Turkana Message Backup Service
This application provide a service to allow backing up of client messages exchanged using
Muzima mobile applications.

## REST API Specification
Provided using [Swagger 2 | https://swagger.io]. To access these API docs using the browser simply visit
 `/swagger-ui.html` and to get a json response use the endpoint `/v2/api-docs`
 
## MESSAGE/PAYLOAD VERIFICATION
Before saving submitted messages to be backed up, the server provides the facility to verify them. The process works as follows.

### Single SMS submission
The sender signs the message body using their private key. The algorithm used for this is `SHA256withRSA`. The URL base64 encoded signature is then
sent along with the payload as the `signature` query parameter. Below is the java snippet to accomplish that.
```java
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;

class SignDemo {
    public static final String ALGORITHM = "SHA256withRSA";
    
    public static String signMessage(String messageBody, PrivateKey privateKey)  throws NoSuchAlgorithmException, InvalidKeyException,
        SignatureException {
        Signature rsa = Signature.getInstance(ALGORITHM);
        rsa.initSign(privateKey);
        rsa.update(messageBody.getBytes());
        
        byte[] signature = rsa.sign();
        return Base64.getUrlEncoder().encodeToString(signature);
    }
}
```

### Multiple SMSes SENT TOGETHER.
The procedure is similar to single SMS except in this one, all message bodies are concatenated, hashed using the `SHA-256` algorithm, signed and then 
signed. See example snippet below.

*Note:* The order of messages matters.
```java
import java.security.Signature;
import java.security.SignatureException;
import java.util.Base64;
import java.security.MessageDigest;

class SignDemo {
    public static final String ALGORITHM = "SHA256withRSA";
    public static final String MD_ALG = "SHA-256";
    
    public static String signMessage(String[] messageBodies, PrivateKey privateKey)  throws NoSuchAlgorithmException, InvalidKeyException,
        SignatureException {
        Signature rsa = Signature.getInstance(ALGORITHM);
        rsa.initSign(privateKey);
        
        MessageDigest md = MessageDigest.getInstance(MD_ALG);
         
        StringBuilder sb = new StringBuilder();
        for(String messageBody: messageBodies) {
            sb.append(messageBody);
        }
        // Digest.
        byte[] digested = md.digest(sb.toString().getBytes());
        rsa.update(digested);
        byte[] signature = rsa.sign();
        return Base64.getUrlEncoder().encodeToString(signature);
    }
}
```

### Single & Multiple MMS
These work similar to SMS implementation.

### Media (Image, Audio, Video, Documents) Uploads.
This uses three metadata fields namely phonenumber, size, and media type in that order. The three are concatenated and signed.
