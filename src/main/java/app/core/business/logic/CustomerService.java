package app.core.business.logic;

import static app.core.business.model.mapping.ToBeChecked.Status.*;

import app.core.business.model.mapping.UserAccount;
import app.core.business.model.mapping.person.insuree.Customer;
import app.core.web.logic.helper.VelocityTemplateResolver;
import app.core.web.model.databinding.validation.RegistrationValidator;

import app.core.web.model.databinding.command.Registration;

import app.core.web.model.persistence.*;

import static com.itextpdf.text.pdf.BaseFont.*;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Created by alexandremasanes on 05/03/2017.
 */
@Service
public class CustomerService extends BaseService {

    public static class RegistrationResult extends Result {

        private boolean successful;

        public boolean isSuccessful() {
            return successful;
        }

        public void setSuccessful() {
            this.successful = true;
        }
    }

    private static final short TOKEN_LEN = 8;

    private static final List<Character> tokenCharSet;

    static {
      tokenCharSet = initTokenCharSet();
    }

    private final HashMap<String, Registration> awaitingRegistrations;

    @Value("${customerService.documentsDir.idCard}")
    private String idCardBaseName;

    @Value("${customerService.documentsDir.sepa")
    private String sepaFileBaseName;

    @Autowired
    private RegistrationValidator registrationValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private  VelocityTemplateResolver velocityTemplateResolver;

    {
        awaitingRegistrations = new HashMap<String, Registration>();
    }

    @Transactional
    public RegistrationResult preRegister(Registration registration) {
        String token;
        RegistrationResult preRegistrationResult;

        preRegistrationResult = new RegistrationResult();

        token = generateToken();

        if(!sendValidationMail(registration, token)) {
            preRegistrationResult.setMessageCode(
                    "notification.invalidEmailAddress"
            );
            return preRegistrationResult;
        }

        preRegistrationResult.setMessageCode(
                "notification.emailSent"
        );
        awaitingRegistrations.put(token, registration);
        preRegistrationResult.setSuccessful();
        return preRegistrationResult;
    }

    @Transactional
    public synchronized boolean register(String token) throws IOException, NoSuchAlgorithmException {
        Registration registration;
        Customer customer;
        UserAccount userAccount;
        String email;
        String hash;
        MultipartFile idCardFile;
        long nextId;
        File file;
        String path;
        RegistrationResult registrationResult;

        registrationResult = new RegistrationResult();

        if(!awaitingRegistrations.containsKey(token))
            return false;

        registration = awaitingRegistrations.get(token);
        customer     = new Customer();

        customer.setFirstName(registration.getFirstName());
        customer.setLastName(registration.getLastName());
        customer.setAddress(registration.getAddress());
        customer.setZipCode(registration.getZipCode());
        customer.setCity(registration.getCity());
        customer.setPhoneNumber(registration.getPhoneNumber());
        customer.setStatus(AWAITING);

        userService.createUserAccount(registration, customer);

        idCardFile = registration.getIdCard();
        nextId     = dao.getNextId(Customer.class);

        path = idCardBaseName + nextId + '.' +
               idCardFile.getContentType().split("/")[1];

        file = new File(path);

        customer.setIdCard(path);

        registration.getIdCard().transferTo(file);

        dao.save(customer);

        awaitingRegistrations.remove(token);

        return true;
    }

    public boolean hasPendingRegistration(String token) {
        return awaitingRegistrations.containsKey(token);
    }

    public byte[] generateSepa(Customer customer) throws IOException, DocumentException {
        ByteArrayOutputStream stream;
        PdfReader reader;
        PdfStamper stamper;
        BaseFont baseFont;
        PdfContentByte over;

        String id, formatedId, part0, part1;


        stream   = new ByteArrayOutputStream();
        reader   = new PdfReader("/Users/alexandremasanes/Desktop/Ben Hamed/Java/aaa_web/src/main/webapp/WEB-INF/resources/private/SEPA-AAA.pdf");
        stamper  = new PdfStamper(reader, stream);
        baseFont = createFont(HELVETICA, CP1252, NOT_EMBEDDED);
        over     = stamper.getOverContent(1);

        id           = Long.toString(customer.getId());
        formatedId   = StringUtils.repeat("0", 21);
        formatedId   = formatedId.substring(0, id.length())+id;


        over.beginText();
        over.setFontAndSize(baseFont, 12);
        over.setTextMatrix(57, 45);
        over.showText(formatedId);
        over.setTextMatrix(38, 54);
        over.showText(customer.getLastName() + " " + customer.getFirstName());
        over.setTextMatrix(38, 59);
        over.showText(customer.getAddress());
        over.setTextMatrix(38, 68.5f);
        over.showText(Integer.toString(customer.getZipCode()));
        over.setTextMatrix(64, 68.5f);
        over.showText(customer.getCity());
        over.setTextMatrix(38, 74);
        over.showText("France");
        over.endText();
        stamper.close();
        return stream.toByteArray();
    }

    public void saveSepa(byte[] sepa, Customer customer) throws IOException {
        FileOutputStream outputStream;
        String fileName;

        fileName = sepaFileBaseName + customer.getId() + ".pdf";

        outputStream = new FileOutputStream(sepaFileBaseName);
        outputStream.write(sepa);
        outputStream.close();

        customer.setSepa(fileName);
        customer.setStatus(AWAITING);
        dao.save(customer);

    }

    private String generateToken() {
        StringBuilder tokenBuilder;
        Random random;
        int randomIndex;
        char randomChar;

        tokenBuilder = new StringBuilder();
        random       = new Random();

        do {
            while(tokenBuilder.length() != 8) {
                randomIndex = random.nextInt(tokenCharSet.size());
                randomChar  = tokenCharSet.get(randomIndex);
                tokenBuilder.append(randomChar);
            }

        } while(awaitingRegistrations.containsKey(tokenBuilder.toString()));

        return tokenBuilder.toString();
    }

    private boolean sendValidationMail(Registration registration, String token) {
        SimpleMailMessage mailMessage;

        mailMessage = new SimpleMailMessage();

        mailMessage.setText(velocityTemplateResolver.getRegistrationValidationMailTemplate(generateToken()));

        mailMessage.setSubject("Bienvenue " + registration.getFirstName() + " " + registration.getLastName());

        mailMessage.setSentDate(new Date());

        try {
            mailSender.send(mailMessage);
            return true;
        } catch (MailException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    private static List<Character> initTokenCharSet() {
        ArrayList<Character> tokenCharSet;

        tokenCharSet = new ArrayList<Character>();

        for(char c = '0'; c <= 'z' ; ++c)
            if(c >= '0' && c <= '9'
            || c >= 'A' && c <= 'Z'
            || c >= 'a' && c <= 'z')
                tokenCharSet.add(c);

        return tokenCharSet;
    }
}