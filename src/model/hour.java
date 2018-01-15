package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.StringTokenizer;

/**
 * Created by dmitry on 5/16/17.
 */
@XmlRootElement
public class hour {
    private Integer oclock;
    private Integer numberOfPassenger;
    private String comment;


    public hour(Integer oclock,Integer numberOfPassenger, String comment){
        this.comment = comment;
        this.numberOfPassenger = numberOfPassenger;
        if (oclock < 0 || oclock > 23){
            System.out.println("wrong oclock!!!");
        } else {
            this.oclock = oclock;
        }
    }
    public hour() {
    }

    public void setOclock(Integer oclock){

            this.oclock = oclock;

    }

    public void setNumberOfPassenger(Integer numberOfPassenger){
        this.numberOfPassenger = numberOfPassenger;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    @XmlElement
    public Integer getOclock(){
        return oclock;
    }
    @XmlElement
    public Integer getNumberOfPassenger(){
        return numberOfPassenger;
    }
    @XmlElement
    public String getComment(){
        return comment;
    }
    @Override
    public String toString(){
       return getOclock()+"\n"+getComment() +"\n"+getNumberOfPassenger() + "\n";
    }

    public Integer countOfWords(){
        String delimiters = " ?><.,|';\"/][}{=-+)(*&^%$#@! ";
        StringTokenizer ins = new StringTokenizer(comment,delimiters);
        return ins.countTokens();
    }
}
