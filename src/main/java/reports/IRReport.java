package reports;

import com.rma.io.RmaFile;
import hec2.plugin.model.ComputeOptions;
import irplugin.CompResult;
import irplugin.IRMain;
import rma.util.RMAFile;
import rma.util.RMAIO;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
            return data;
        }
        return data;
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
    public static String getReportFilename(ComputeOptions cco, String altName){
        String rundir = cco.getRunDirectory();
        String plugdir = RMAIO.concatPath(rundir, IRMain.get_pluginSubDirectory());
        String filename = RMAIO.concatPath(plugdir,altName+".rpt");
        return filename;
    }



}
}
