package com.wh.crm;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author wu
 * @createTime 2022/3/8  0:45
 * @description
 */
public class FilePoiTest {
    public static void main(String[] args) throws IOException {

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("我的表格");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell = row.createCell(1);
        cell.setCellValue("姓名");
        cell = row.createCell(2);
        cell.setCellValue("学号");
        cell = row.createCell(3);
        cell.setCellValue("年龄");
        cell = row.createCell(4);
        cell.setCellValue("性别");
        cell = row.createCell(5);
        cell.setCellValue("年级");

        for (int i = 0; i < 10; i++) {
            HSSFRow row1 = sheet.createRow(i+1);
            HSSFCell cell1 = row1.createCell(0);
            cell1.setCellValue(i+1);
            cell1 = row1.createCell(1);
            cell1.setCellValue("zhangsan");
            cell1 = row1.createCell(2);
            cell1.setCellValue(20220308+i);
            cell1 = row1.createCell(3);
            cell1.setCellValue(15+i);
            cell1 = row1.createCell(4);
            cell1.setCellValue("男");
            cell1 = row1.createCell(5);
            cell1.setCellValue("高一");
        }
        FileOutputStream out = new FileOutputStream("D:\\WorkSpeace\\xueshen.xls");
        wb.write(out);

        out.flush();
        out.close();
        wb.close();

    }

    @Test
    public void numTest() {

        String num = "310c0a4c20df46acaefff35a5b0dcdc1";


        System.out.println(num.length());
    }
}
