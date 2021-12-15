package helpers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PdfHelpers {

    public static String tableStyle(){
        String css = "<style>";
        css += "table { border-collapse: collapse; width: 100%; } th, td { text-align: left; padding: 8px; } tr:nth-child(even){background-color: #f2f2f2} th { background-color:  #074c9d; color: white; }";
        css += "</style>";
        return css;
    }

    public static String pdfHeader(String reportName){
        String header = "";
        header += "<center>";
        header += "<img src='https://i.imgur.com/CfPmIw1.png'></img>";
        header += "<h1 style='color:  #074c9d;'>Exams Manager And Creator</h1>";
        header += "</center>";
        header += "<hr> <br>";
        header += "<h2>"+reportName+"</h2>";
        header += " <br>";
        return header;
    }

    public static String pdfFooter(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String footer = "<br> <br> <hr>";
        footer += "<center>";
        footer += "<span>Generated on: "+dtf.format(now)+"<span>";
        footer += "</center>";
        return footer;
    }

    public static String pdfTable(String headers[]){
        String table = "<table>";
        table += "<thead>";
        for(String header : headers){
            table += "<th>"+header+"</th>";
        }
        table += "</thead>";
        table += "<tbody>";
        return table;
    }
}
