package model;

import java.io.File;

public interface ReadWrite {
    void write(String title);
    metroStation read(File file);
}
