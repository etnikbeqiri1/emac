package helpers;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

public final class StringHelpers {

    public static String snakeCaseToTitleCase(String snakeCaseString) {
        String camelCase = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, snakeCaseString);

        return StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(camelCase), StringUtils.SPACE);
    }

    public static String titleCaseToSnakeCase(String titleCaseString){
        return StringUtils.join(titleCaseString.split(" "), "_").toLowerCase().trim();
    }

    public static String titleCase(String text){
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String FullNameFormat(String text){
        String[] arrayText = text.split(" ");
        String formattedName = "";
        for(String forText : arrayText){
            formattedName += forText.substring(0,1).toUpperCase()+forText.substring(1, forText.length()).toLowerCase()+" ";
        }
        return formattedName.trim();
    }

    public static boolean isValidInteger(String number){
        try
        {
            // checking valid integer using parseInt() method
            Integer.parseInt(number);
            return true;
        }
        catch (NumberFormatException e)
        {
           return false;
        }
    }
}
