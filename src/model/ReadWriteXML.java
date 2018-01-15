package model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;

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

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            obj = (ReadWriteXML) jaxbUnmarshaller.unmarshal(file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
