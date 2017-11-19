package org.aaa.core.web.common.business.logic;

import static org.aaa.core.business.mapping.entity.ToBeChecked.Status.*;

import org.aaa.orm.custom.Address;
import org.aaa.orm.custom.Names;
import org.aaa.core.business.mapping.entity.UserAccount;
import org.aaa.core.business.mapping.entity.person.insuree.Customer;
import org.aaa.core.web.common.helper.VelocityTemplateResolver;
import org.aaa.core.web.app.model.validation.RegistrationValidator;

import org.aaa.core.web.app.model.Registration;

import static com.itextpdf.text.pdf.BaseFont.*;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.*;

import static org.apache.commons.lang.ArrayUtils.toPrimitive;
import static org.apache.commons.lang.StringUtils.repeat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static java.util.Arrays.copyOf;
import static java.util.stream.IntStream.range;
import static java.util.stream.Collectors.toList;

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

        public void setSuccessful(boolean successful) {
            this.successful = successful;
        }
    }

    private static final short TOKEN_LEN = 8;

    private static final int[] tokenCharSet;

    static {
      tokenCharSet = initTokenCharSet();
    }

    private final HashMap<String, Registration> awaitingRegistrations;


    @Value("${customerService.documentsDir.idCard}")
    private String idCardBaseName;

    @Value("${customerService.documentsDir.sepa")
    private String sepaFileBaseName;

    @Autowired
    private RegistrationValidator    registrationValidator;

    @Autowired
    private UserService              userService;

    @Autowired
    private MailSender               mailSender;

    @Autowired
    private VelocityTemplateResolver velocityTemplateResolver;

    {
        awaitingRegistrations = new HashMap<String, Registration>();
    }

    public RegistrationResult preRegister(Registration registration, String serverName) {
        String token;
        RegistrationResult preRegistrationResult;

        preRegistrationResult = new RegistrationResult();

        token = generateToken();

        if(!sendValidationMail(registration, serverName, token)) {
            preRegistrationResult.setMessageCode(
                    "notification.invalidEmailAddress"
            );
            return preRegistrationResult;
        }

        preRegistrationResult.setMessageCode(
                "notification.emailSent"
        );
        awaitingRegistrations.put(token, registration);
        preRegistrationResult.setSuccessful(true);
        return preRegistrationResult;
    }

    @Transactional
    public boolean register(String token) throws IOException, NoSuchAlgorithmException {
        Registration registration;
        Names names;
        String[] addressParts;
        Address address;
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

        names        = new Names();
        names.setFirstName(registration.getFirstName());
        names.setLastName(registration.getLastName());

        address      = new Address();
        addressParts = registration.getAddress().split("#(\\d)+ (bis)?,? [rue [de|du|d\']|avenue [de|du|d\']|chemin [de|du|d\']] (\\w+)+#");
        address.setNumber(Short.valueOf(addressParts[0]));
        address.setBis(!addressParts[1].isEmpty());
        address.setStreet(addressParts[2]);
        address.setZipCode(registration.getZipCode());
        address.setCity(registration.getCity());

        customer     = new Customer(registration.getNirNumber());
        customer.setNames(names);
        customer.setAddress(address);

        customer.setPhoneNumber(registration.getPhoneNumber());
        customer.setStatus(AWAITING);


        userService.createUserAccount(registration, customer);

        //   dao.save(customer);

        idCardFile = registration.getIdCard();



        nextId     = customer.getId(); //dao.getNextId(Customer.class);

        path = idCardBaseName + nextId + '.' +
               idCardFile.getContentType().split("/")[1];

        file = new File(path);



        registration.getIdCard().transferTo(file);


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
        formatedId   = repeat("0", 21);
        formatedId   = formatedId.substring(0, id.length())+id;


        over.beginText();
        over.setFontAndSize(baseFont, 12);
        over.setTextMatrix(57, 45);
        over.showText(formatedId);
        over.setTextMatrix(38, 54);
        over.showText(customer.getNames().getLastName() + " " + customer.getNames().getFirstName());
        over.setTextMatrix(38, 59);
        over.showText(customer.getAddress().getNumber()
                + (customer.getAddress().isBis() ? " bis" : "")
                + customer.getAddress().getStreet());
        over.setTextMatrix(38, 68.5f);
        over.showText(Integer.toString(customer.getAddress().getZipCode()));
        over.setTextMatrix(64, 68.5f);
        over.showText(customer.getAddress().getCity());
        over.setTextMatrix(38, 74);
        over.showText("France");
        over.endText();
        stamper.close();
        return stream.toByteArray();
    }

    @Transactional
    public void saveSepa(byte[] sepa, Customer customer) throws IOException {
        FileOutputStream outputStream;
        String fileName;

        fileName = sepaFileBaseName + customer.getId() + ".pdf";

        outputStream = new FileOutputStream(sepaFileBaseName);
        outputStream.write(sepa);
        outputStream.close();

        customer.setSepaDocumentPresent(true);
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
                randomIndex = random.nextInt(tokenCharSet.length);
                randomChar  = (char) tokenCharSet[randomIndex];
                tokenBuilder.append(randomChar);
            }

        } while(awaitingRegistrations.containsKey(tokenBuilder.toString()));

        return tokenBuilder.toString();
    }

    private boolean sendValidationMail(Registration registration, String serverName, String token) {
        SimpleMailMessage mailMessage;

        mailMessage = new SimpleMailMessage();

        mailMessage.setText(velocityTemplateResolver.getRegistrationValidationMailTemplate(serverName, generateToken()));

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

    private static int[] initTokenCharSet() {
        List<Integer> ints;

        ints = range('0', '9').boxed().collect(toList());
        ints.addAll(range('A', 'Z').boxed().collect(toList()));
        ints.addAll(range('a', 'z').boxed().collect(toList()));

        return toPrimitive(
                copyOf(
                        ints.toArray(),
                        ints.size(),
                        Integer[].class
                )
        );
    }
}