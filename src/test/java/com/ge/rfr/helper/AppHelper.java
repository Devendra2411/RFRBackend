package com.ge.rfr.helper;

import com.ge.rfr.RfrBackend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class AppHelper {

    private static final Logger logger = LoggerFactory.getLogger(AppHelper.class);
    
    private EmbeddedWebApplicationContext ctx;
    
	private String baseUrl;
   
    private final SmtpServerHelper smtpHelper = new SmtpServerHelper();

    public void start() throws Exception {
        smtpHelper.start();
        String db = System.getProperty("datasource", "jdbc:postgresql://localhost:5433/rfr-test");
        String user = System.getProperty("datauser", "rfr-test");
        String pass = System.getProperty("datapass", "rfr-test");

        String[] args = new String[]{
                "--server.port=0",
                "--spring.datasource.url=" + db,
                "--spring.datasource.username=" + user,
                "--spring.datasource.password=" + pass,
                // Set the embedded SMTP server as the sender
                "--spring.mail.host=127.0.0.1",
                "--spring.mail.port=" + smtpHelper.getSmtpPort()
                // Settings to override the local application.properties
        };

        SpringApplication app = new SpringApplication(RfrBackend.class);
        
        ctx = (EmbeddedWebApplicationContext) app.run(args);
        int port = ctx.getEmbeddedServletContainer().getPort();
        baseUrl = "http://127.0.0.1:" + port + "/";
        logger.info("Service started on {}", baseUrl);
        
        ClassPathResource pathResource=new ClassPathResource("insertfspeventdata.properties");  
        FileReader file =new FileReader(pathResource.getFile());
        Properties propeties=new Properties();  
        propeties.load(file);  
        String insertFspEventDataQuery=propeties.getProperty("insertQueries");
        String insertPgsActonItemsQuery=propeties.getProperty("insertQueries2");
        
        Connection connection = DriverManager.getConnection(db,user,pass);
		Statement statement = connection.createStatement();
    
		statement.addBatch(insertFspEventDataQuery);
		statement.addBatch(insertPgsActonItemsQuery);
		statement.executeBatch();
		statement.close();
		connection.close();
        
    }
    
    public void stop() throws Exception {

        smtpHelper.stop();

        if (ctx != null) {
            try {
                ctx.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public MailSpy getMailSpy() {
        return smtpHelper.getMailSpy();
    }

    /**
     * Causes all fields with @Autowired to be injected in the given object.
     */
    public void autowire(Object object) {
        ctx.getAutowireCapableBeanFactory().autowireBean(object);
    }

}