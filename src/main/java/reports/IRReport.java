package reports;

import irplugin.CompResult;
import rma.util.RMAIO;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
        File file = new File(reportPath);
        file.mkdirs();
        String filename = reportPath + RMAIO.separator + "IRreport.rpt";
        System.out.println("showReport: report file is " + filename);
        _report = new BufferedWriter(new FileWriter(filename,false));
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

    public ReportBuilder filepath (String path){
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
