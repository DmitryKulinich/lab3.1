package model;

import javax.xml.bind.annotation.*;
import java.util.Arrays;

/**
 * Created by dmitry on 5/16/17.
 */


@XmlRootElement
public class metroStation {
    private String name;
    private Integer yearOfOpening;
    private hour[] hours = new hour[24];
    public metroStation(String name, Integer yearOfOpening) {
        this.name = name;
        this.yearOfOpening = yearOfOpening;
        array();
    }

    public metroStation() {
    }

    @XmlElement
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @XmlElement
    public Integer getYearOfOpening() {
        return yearOfOpening;
    }
    public void setYearOfOpening(Integer yearOfOpening) {
        this.yearOfOpening = yearOfOpening;
    }
    @XmlElement
    public hour[] getHours() {
        return hours;
    }
    public void setHours(hour[] hours) {
        this.hours = hours;
    }

    public void setHoursElement(hour element, int index){
        if(index >=0 && index <24){
            hours[index] = element;
        }
        else{
            System.out.println("error");
        }
    }
    public hour getHoursElement(int index){
        return hours[index];
    }

    @Override
    public String toString() {
        String forRet = name + '\n'  + yearOfOpening +'\n';
        for(hour temp:hours){
            forRet += temp.toString();
        }
        return forRet;
    }

    public void array(){
        for (int i = 0; i < 24 ; i++) {
            hours[i] = new hour(i,0,"");
        }
    }
    public void sortByPasenger(){
        hour[] h = this.getHours();
        Arrays.sort(h, new CompareByNumOfPas());
        this.setHours(h);
    }

    public void sortByCommentWords(){
        hour[] h = this.getHours();
        Arrays.sort(h, new CompareByCommentWords());
        this.setHours(h);
    }
}
