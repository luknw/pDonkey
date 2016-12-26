package agh.cs.pdonkey;

/**
 * pDonkey
 * Created by luknw on 15.12.2016
 */

public interface OptionsParser {
    AppConfig parse(String[] options);
}
