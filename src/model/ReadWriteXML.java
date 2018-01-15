package model;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

/**
 * Created by dmitry on 9/19/17.
 */

@XmlRootElement
public class ReadWriteXML extends metroStation implements ReadWrite{

    public ReadWriteXML(String name, Integer yearOfOpening)
    {
        super(name, yearOfOpening);
    }

    public ReadWriteXML(metroStation other){
        super(other.getName(),other.getYearOfOpening());
        super.setHours(other.getHours());
    }

    public ReadWriteXML() {
    }

    public void write(String title){
        try {
            File file = new File(title);
            JAXBContext jaxbContext = JAXBContext.newInstance(ReadWriteXML.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
            Schema schema = null;
            try {
                schema = sf.newSchema(new File("src/schemaXsd/schema.xsd"));
            } catch( SAXException e){
                e.printStackTrace();
            }
            JAXBSource source = new JAXBSource(jaxbContext, this);
            Validator validator = schema.newValidator();
            validator.setErrorHandler(new CustomValidationErrorHandler());
            try {
                validator.validate(source);
            } catch( SAXException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
            jaxbMarshaller.setSchema(schema);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(this, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }


    public ReadWriteXML read(File file){
        ReadWriteXML obj = new ReadWriteXML();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(ReadWriteXML.class);
            SchemaFactory sf = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
            Schema schema = null;
            try {
                schema = sf.newSchema(new File("src/schemaXsd/schema.xsd"));
            } catch( SAXException e){
                e.printStackTrace();
            }
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setSchema(schema);
            jaxbUnmarshaller.setEventHandler(new CustomValidationEventHandler());
            obj = (ReadWriteXML) jaxbUnmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
