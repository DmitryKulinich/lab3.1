package main;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.Date;

/**
 * Created by dmitry on 9/19/17.
 */

@XmlRootElement
public class ReadWriteXML extends metroStation {

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

    public void WriteXML(String title){
//        Date date = new Date();
//        String Date = date.toString();
//        String[] splitDate = Date.split(" ");
//        Date = "";
//        for(int i = 0; i<splitDate.length; i++){
//            if(i == splitDate.length -1){
//                Date += splitDate[i];
//            }else {
//                Date += splitDate[i] + "_";
//            }
//        }
//        String title = "./xmlFiles/SavedData.xml";
        try {
            File file = new File(title);
            JAXBContext jaxbContext = JAXBContext.newInstance(ReadWriteXML.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            jaxbMarshaller.marshal(this, file);
            jaxbMarshaller.marshal(this, System.out);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public ReadWriteXML readXML(File file){
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
