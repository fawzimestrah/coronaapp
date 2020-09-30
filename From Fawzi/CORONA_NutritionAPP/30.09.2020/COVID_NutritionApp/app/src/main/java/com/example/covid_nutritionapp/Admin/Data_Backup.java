package com.example.covid_nutritionapp.Admin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.inputmethodservice.Keyboard;
import android.os.Environment;
import android.view.View;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Data_Backup {

    public static void BackupExecel(Context context) {
        /*
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

            return;
        }

        String fileName = "FileName.xlsx"; //Name of the file
        File sdCard = Environment.getExternalStorageDirectory();

        File directory = new File (sdCard.getAbsolutePath() + "/newfolder");
        directory.mkdirs();

        File file = new File(directory, fileName);

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook;
        try {
            int a = 1;
            workbook = Workbook.createWorkbook(file, wbSettings);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);
            Label label = new Label(0, 2, "SECOND");
            Label label1 = new Label(0,1,"first");
            Label label0 = new Label(0,0,"HEADING");
            Label label3 = new Label(1,0,"Heading2");
            Label label4 = new Label(1,1,String.valueOf(a));

            try {
                sheet.addCell(label);
                sheet.addCell(label1);
                sheet.addCell(label0);
                sheet.addCell(label4);
                sheet.addCell(label3);
            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();

            try {
                workbook.close();
            } catch (WriteException e) {

                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

*/
    }
}
