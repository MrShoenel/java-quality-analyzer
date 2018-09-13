package se.lnu.qualityanalyzer.service.report.impl;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import se.lnu.qualityanalyzer.converter.CommitAnalysisOutputToCommitAnalysisReportConverter;
import se.lnu.qualityanalyzer.model.analysis.AnalysisOutput;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisInput;
import se.lnu.qualityanalyzer.model.analysis.CommitAnalysisOutput;
import se.lnu.qualityanalyzer.model.report.CommitAnalysisReport;
import se.lnu.qualityanalyzer.service.report.ReportService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class XLSXCommitReportService implements ReportService {

    private static CommitAnalysisOutputToCommitAnalysisReportConverter analysisOutputConverter;

    private static final String[] HEADERS = { "Commit", "Date", "Email", "Name", "Message length", "Comments", "Additions",
            "Deletions", "Total changes", "Added files", "Changed files", "Deleted files", "Total files",
            "Cohesion", "Size", "Complexity", "Hierarchy", "Quality", "Coupling", "Cloning issues", "Readability",
            "Testing issues", "Total quality" };

    public XLSXCommitReportService() {
        this.analysisOutputConverter = new CommitAnalysisOutputToCommitAnalysisReportConverter();
    }

    @Override
    public void create(CommitAnalysisInput commitAnalysisInput, List<? extends AnalysisOutput> commitAnalysisOutputList) {
        List<CommitAnalysisReport> commitAnalysisReportList = commitAnalysisOutputList.stream()
                .map(o -> analysisOutputConverter.apply((CommitAnalysisOutput) o)).collect(Collectors.toList());

        String excelFilePath = commitAnalysisInput.getRepositoryName() + " (commit analysis in " + commitAnalysisInput.getBranchName() + " branch) - "
                + new Date() + ".xlsx";
        String sheetName = "Sheet1";

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(sheetName) ;

        writeHeaders(sheet);
        writeCommitReports(commitAnalysisReportList, sheet, 1);

        try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHeaders(XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);

        for (int i = 0; i < HEADERS.length; i++) {
            createCell(row, i, HEADERS[i]);
        }
    }

    private void writeCommitReports(List<CommitAnalysisReport> commitAnalysisReportList, XSSFSheet sheet, int rowOffset) {
        int rowCount = rowOffset;
        for (CommitAnalysisReport report : commitAnalysisReportList) {
            XSSFRow row = sheet.createRow(rowCount++);
            writeCommitReport(report, row);
        }
    }

    private void writeCommitReport(CommitAnalysisReport commitAnalysisReport, XSSFRow row) {
        int cellCount = 0;

        // Info about commit

        createCell(row, cellCount++, commitAnalysisReport.getSha());
        createCell(row, cellCount++, commitAnalysisReport.getDate());
        createCell(row, cellCount++, commitAnalysisReport.getAuthorEmail());
        createCell(row, cellCount++, commitAnalysisReport.getAuthorName());
        createCell(row, cellCount++, commitAnalysisReport.getMessageLength());
        createCell(row, cellCount++, commitAnalysisReport.getCommentCount());
        createCell(row, cellCount++, commitAnalysisReport.getAdditions());
        createCell(row, cellCount++, commitAnalysisReport.getDeletions());
        createCell(row, cellCount++, commitAnalysisReport.getTotalChanges());
        createCell(row, cellCount++, commitAnalysisReport.getAddedFiles());
        createCell(row, cellCount++, commitAnalysisReport.getModifiedFiles());
        createCell(row, cellCount++, commitAnalysisReport.getRemovedFiles());
        createCell(row, cellCount++, commitAnalysisReport.getTotalFiles());

        // Info about metrics

        createCell(row, cellCount++, commitAnalysisReport.getCohesion());
        createCell(row, cellCount++, commitAnalysisReport.getSize());
        createCell(row, cellCount++, commitAnalysisReport.getComplexity());
        createCell(row, cellCount++, commitAnalysisReport.getHierarchy());
        createCell(row, cellCount++, commitAnalysisReport.getQuality());
        createCell(row, cellCount++, commitAnalysisReport.getCoupling());
        createCell(row, cellCount++, commitAnalysisReport.getCloning());
        createCell(row, cellCount++, commitAnalysisReport.getReadability());
        createCell(row, cellCount++, commitAnalysisReport.getTesting());
        createCell(row, cellCount++, commitAnalysisReport.getTotalQuality());
    }

    private void createCell(XSSFRow row, int cellCount, Object data) {
        XSSFCell cell = row.createCell(cellCount);

        if (data instanceof Date) {
            cell.setCellValue((Date) data);
        } else if (data instanceof String) {
            cell.setCellValue((String) data);
        } else if (data instanceof Integer) {
            cell.setCellValue((Integer) data);
        } else if (data instanceof Double) {
            cell.setCellValue((Double) data);
        }
    }

}
