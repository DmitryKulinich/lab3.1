package main;

import java.util.Comparator;
import java.util.StringTokenizer;

/**
 * Created by dmitry on 9/19/17.
 */

public class CompareByCommentWords implements Comparator<hour> {
    public int compare(hour h1, hour h2){

        return Integer.compare(h2.countOfWords(),h1.countOfWords());
    }


}
