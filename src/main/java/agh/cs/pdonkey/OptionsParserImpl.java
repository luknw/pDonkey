package agh.cs.pdonkey;

import java.util.HashMap;
import java.util.Map;

/**
 * pDonkey
 * Created by luknw on 16.12.2016
 */

public class OptionsParserImpl implements OptionsParser {

    private static final String PROVIDE_MP_IDENTITY = "Provide MP identity";
    private static final String ENTER_VALID_MODE = "Enter valid mode";

    private static final Map<String, AppMode> BY_ARGUMENT = new HashMap<>();

    static {
        BY_ARGUMENT.put("sum", AppMode.SUM_EXPENSES);
        BY_ARGUMENT.put("minor", AppMode.MINOR_EXPENSES);
        BY_ARGUMENT.put("avg", AppMode.AVG_EXPENSES);
        BY_ARGUMENT.put("travels", AppMode.TOP_TRAVELS_ABROAD);
        BY_ARGUMENT.put("time", AppMode.TOP_TIME_ABROAD);
        BY_ARGUMENT.put("cost", AppMode.MOST_EXPENSIVE_TRAVEL);
        BY_ARGUMENT.put("italy", AppMode.MACARONIERIS);
    }

    private AppMode getModeByArgument(String mode) {
        AppMode chosen = BY_ARGUMENT.get(mode);
        if (chosen == null) {
            throw new IllegalArgumentException(ENTER_VALID_MODE);
        }
        return chosen;
    }

    private MP getMPByArgument(String identity) {
        return new MPImpl(identity);
    }

    public AppConfig parse(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException(ENTER_VALID_MODE);
        }
        AppMode mode = getModeByArgument(args[0]);

        MP mp = (args.length >= 2) ? getMPByArgument(args[1]) : null;
        if (mp == null && (mode == AppMode.SUM_EXPENSES || mode == AppMode.MINOR_EXPENSES))
            throw new IllegalArgumentException(PROVIDE_MP_IDENTITY);

        return new AppConfigImpl(mode, mp);
    }
}
