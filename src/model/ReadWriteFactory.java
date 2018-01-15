package model;

public class ReadWriteFactory {
    public static ReadWrite getRW(String type, metroStation ms){
        switch (type.toUpperCase()){
            case "XML":
                return new ReadWriteXML(ms);
            case "TXT":
                return new ReadWriteTXT(ms);
            default:
                return null;
        }
    }

    public static ReadWrite getRW(String type){
        switch (type.toUpperCase()){
            case "XML":
                return new ReadWriteXML();
            case "TXT":
                return new ReadWriteTXT();
            default:
                return null;
        }
    }
}
