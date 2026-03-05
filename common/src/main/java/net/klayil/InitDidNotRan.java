package net.klayil;

import java.lang.invoke.LambdaConversionException;

public class InitDidNotRan extends LambdaConversionException {
    private static String createMessageFromName(String name) {
        return "%s's init method has not been called".formatted(name);
    }

    @SafeVarargs
    public <E extends Exception> InitDidNotRan(String name, E... causes) {
        super(createMessageFromName(name), (causes.length < 1) ? null : causes[0]);
    }
}
