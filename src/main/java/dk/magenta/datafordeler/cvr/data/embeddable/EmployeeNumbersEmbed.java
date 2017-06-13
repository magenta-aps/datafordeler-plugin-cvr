package dk.magenta.datafordeler.cvr.data.embeddable;

import dk.magenta.datafordeler.core.exception.InvalidClientInputException;
import dk.magenta.datafordeler.core.exception.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lars on 12-06-17.
 */
public class EmployeeNumbersEmbed {

    protected void checkBounds(int low, int high) throws InvalidClientInputException {
        if (high < low) {
            throw new InvalidClientInputException("Upper bound must not be less than lower bound (low: "+low+", high: "+high+")");
        }
        if (low < 0) {
            throw new InvalidClientInputException("Lower bound must not be less than zero (low: "+low+")");
        }
    }

    protected String formatInterval(int low, int high) {
        if (low == high) {
            return Integer.toString(low);
        }
        return low + " - " + high;
    }

    private static Pattern intervalPattern = Pattern.compile("^(\\d+)\\s*-\\s*(\\d+)$");

    protected int parseLow(String interval) throws ParseException {
        try {
            return Integer.parseInt(interval);
        } catch (NumberFormatException e) {
            Matcher m = intervalPattern.matcher(interval);
            if (m.matches()) {
                return Integer.parseInt(m.group(1));
            }
            throw new ParseException("Cannot parse interval "+interval+", needing to match "+intervalPattern.toString());
        }
    }

    protected int parseHigh(String interval) throws ParseException {
        Matcher m = intervalPattern.matcher(interval);
        if (m.matches()) {
            return Integer.parseInt(m.group(2));
        }
        throw new ParseException("Cannot parse interval "+interval+", needing to match "+intervalPattern.toString());
    }

}
