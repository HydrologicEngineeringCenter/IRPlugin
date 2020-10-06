package reports;

import com.rma.io.RmaFile;
import irplugin.CompResult;
import rma.util.RMAFile;
import rma.util.RMAIO;

import java.io.*;
import java.sql.Time;
import java.util.List;

public class IRReport {
    private List<String> errorMessages;
    private List<String> compMessages;
    private String reportPath;
    private BufferedWriter _report;

IRReport(){
}
private void writeReport(){
    try {
        System.out.println("writeReport: report file is " + reportPath);
        _report = new BufferedWriter(new FileWriter(reportPath,false));
        for (String messages: compMessages){
            _report.write(messages);
            _report.newLine();
        }
        for (String messages: errorMessages) {
            _report.write(messages);
        }
        _report.close();
        }
    catch ( java.io.IOException ioe)
        {
            _report = null;
        }
    }
    public static String readReport(String filename){
        File file =  new File(filename);

        String input = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = null;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }

            input += line + "\n";
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
public static class ReportBuilder{
    private List<String> errorMessages;
    private List<String> compMessages;
    private String reportPath;

    public ReportBuilder errorMessages(CompResult result){
        this.errorMessages = result.getErrorMessages();
        return this;

    }
    public ReportBuilder compMessages(CompResult result) {
        this.compMessages = result.getComputeMessages();
        return this;
    }

    public ReportBuilder reportPath(String path){
        this.reportPath = path;
        return this;
    }
    public IRReport build(){
        IRReport report = new IRReport();
        report.compMessages = this.compMessages;
        report.errorMessages = this.errorMessages;
        report.reportPath = this.reportPath;
        report.writeReport();
        return report;

    }



}
}
