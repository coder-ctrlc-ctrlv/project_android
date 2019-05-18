package com.example.protokol.create_protokol;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.ArrayList;

public class TemplatePDF {
    public static final String FONTBD = "res/font/timesbd.ttf";
    public static final String FONT = "res/font/times.ttf";
    private Context context;
    private File pdfFile, folder;
    private Document document;
    private PdfWriter pdfWriter;
    private Paragraph paragraph;
    private Font font = FontFactory.getFont(FONT, "Cp1251", BaseFont.EMBEDDED);
    private Font fontbd = FontFactory.getFont(FONTBD, "Cp1251", BaseFont.EMBEDDED);
    private Font font_under = FontFactory.getFont(FONT, "Cp1251", BaseFont.EMBEDDED, 10, Font.UNDERLINE);
    private LineSeparator separator_10 = new LineSeparator((float) 0.67, 100, new BaseColor(0, 0,0), Element.ALIGN_CENTER, (float) -3.33);
    private LineSeparator separator_12 = new LineSeparator((float) 0.8, 100, new BaseColor(0, 0,0), Element.ALIGN_CENTER, (float) -4);

    public TemplatePDF(Context context) {
        this.context = context;
    }

    public void openDocument(String namefile, Boolean horizon) {
        createFile(namefile);
        try {
            if (!horizon)
                document = new Document(PageSize.A4);
            else
                document = new Document(PageSize.A4.rotate());
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
            document.open();
        }catch (Exception e) {
            Log.e("openDocument", e.toString());
        }
    }

    private void createFile(String namefile) {
        folder = new File(Environment.getExternalStorageDirectory().toString(), "PDF");
        if (!folder.exists())
            folder.mkdirs();
        pdfFile = new File(folder, namefile + ".pdf");
    }

    public void closeDocument() {
        document.close();
    }

    public void addMetaData(String title, String subject, String author){
        document.addTitle(title);
        document.addSubject(subject);
        document.addAuthor(author);
    }

    public void addTitles(String title, String subTitle, int size) {
        try {
            fontbd.setSize(size);
            paragraph = new Paragraph();
            addChildP(new Paragraph(title, fontbd));
            addChildP(new Paragraph(subTitle, fontbd));
            paragraph.setSpacingAfter(5);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addTitles_BD", e.toString());
        }
    }

    public void addChildP(Paragraph childParagraph) {
        childParagraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(childParagraph);
    }

    public void addCenter_BD(String title, int size, int spaceBefore, int spaceAfter) {
        try {
            fontbd.setSize(size);
            paragraph = new Paragraph(title, fontbd);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingBefore(spaceBefore);
            paragraph.setSpacingAfter(spaceAfter);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addCenter_BD", e.toString());
        }
    }

    public void addCenter_Under(String line, int size, int spaceBefore, int spaceAfter){
        try {
            font_under.setSize(size);
            paragraph = new Paragraph(line, font_under);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingBefore(spaceBefore);
            paragraph.setSpacingAfter(spaceAfter);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addCenter_Under", e.toString());
        }
    }

    public void addCenter_Nomal(String title, int size, int spaceBefore, int spaceAfter) {
        try {
            font.setSize(size);
            paragraph = new Paragraph(title, font);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingBefore(spaceBefore);
            paragraph.setSpacingAfter(spaceAfter);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addCenter_Nomal", e.toString());
        }
    }

    public void addParagraph_BD(String text, int size, int spaceBefore, int spaceAfter){
        try {
            fontbd.setSize(size);
            paragraph = new Paragraph(text,fontbd);
            paragraph.setSpacingAfter(spaceAfter);
            paragraph.setSpacingBefore(spaceBefore);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraph_BD", e.toString());
        }
    }

    public void addParagraph_Normal(String text, int size, int spaceBefore, int spaceAfter){
        try {
            font.setSize(size);
            paragraph = new Paragraph(text,font);
            paragraph.setSpacingAfter(spaceAfter);
            paragraph.setSpacingBefore(spaceBefore);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraph_Normal", e.toString());
        }
    }

    public void addParagraph_RE_end(String text1, String no, String text2, String nz, String text3, String space, int size){
        try {
            font.setSize(size);
            font_under.setSize(size);
            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text1, font));
            phrase.add(new Chunk(no, font_under));
            phrase.add(new Chunk(text2, font));
            phrase.add(new Chunk(nz, font_under));
            phrase.add(new Chunk(text3, font));
            phrase.add(new Chunk(space, font_under));
            paragraph = new Paragraph(phrase);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraph_RE_end", e.toString());
        }
    }

    public void addParagraph_Ground_BeforeTable(String[] text) {
        try {
            fontbd.setSize(10);
            font.setSize(8);
            font_under.setSize(10);
            paragraph = new Paragraph();

            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], fontbd));
            phrase.add(new Chunk(text[1], font_under));
            phrase.add(new Chunk(separator_10));
            paragraph.add(new Paragraph(phrase));

            phrase = new Phrase();
            phrase.add(new Chunk(text[2], fontbd));
            phrase.add(new Chunk(text[3], font_under));
            phrase.add(new Chunk(separator_10));
            paragraph.add(new Paragraph(phrase));

            phrase = new Phrase();
            phrase.add(new Chunk(text[4], fontbd));
            phrase.add(new Chunk(text[5], font_under));
            phrase.add(new Chunk(separator_10));
            paragraph.add(new Paragraph(phrase));

            phrase = new Phrase();
            phrase.add(new Chunk(text[6], font));
            phrase.add(new Chunk(text[7], fontbd));
            phrase.add(new Chunk(text[8], font_under));
            phrase.add(new Chunk(separator_10));
            paragraph.add(new Paragraph(phrase));

            phrase = new Phrase();
            phrase.add(new Chunk(text[9], fontbd));
            phrase.add(new Chunk(text[10], font_under));
            phrase.add(new Chunk(text[11], font));
            phrase.add(new Chunk(text[12], fontbd));
            phrase.add(new Chunk(text[13], font_under));
            paragraph.add(new Paragraph(phrase));

            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraphBeforeTabGr", e.toString());
        }
    }

    public void addParagraph_Ground_end(String[] text) {
        try {
            fontbd.setSize(12);
            font_under.setSize(12);
            paragraph = new Paragraph();

            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], fontbd));
            phrase.add(new Chunk(text[1], font_under));
            phrase.add(new Chunk(separator_12));
            Paragraph paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(10);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[2], fontbd));
            phrase.add(new Chunk(text[3], font_under));
            phrase.add(new Chunk(separator_12));
            paragraph.add(new Paragraph(phrase));

            paragraph.setSpacingBefore(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraph_Ground_end", e.toString());
        }
    }

    //TABLES

    public void createTableRE(String[]header) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(header.length);
            float[] columnWidths = new float[]{5.29f, 32.3f, 14.11f, 14.11f, 14.11f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(20);
            PdfPCell pdfPCell;
            int i;
            for (i = 0; i < 6; i++) {
                pdfPCell = new PdfPCell(new Phrase(header[i], font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setPaddingBottom(5f);
                pdfPTable.addCell(pdfPCell);
            }
            for (i = 1; i < 7; i++) {
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPaddingBottom(5f);
                pdfPTable.addCell(pdfPCell);
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableRE", e.toString());
        }
    }

    public void emptyStringsRE(int number) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{5.29f, 32.3f, 14.11f, 14.11f, 14.11f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j;
            for (i = 0; i < number; i++)
                for (j = 0; j < 6; j++) {
                    if (j == 0)
                        pdfPCell = new PdfPCell(new Phrase("\u2014", font));
                    else if (j == 1)
                        pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014", font));
                        else if (j == 2 || j == 3 || j == 4)
                            pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014\u2014\u2014", font));
                            else
                                pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(pdfPCell);
                }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("emptyStringsRE", e.toString());
        }
    }

    public void addRoomRE(String nameRoom, String number, Boolean head, int empty){
        try {
            if(empty != 0)
                emptyStringsRE(empty);
            paragraph = new Paragraph();
            paragraph.setFont(fontbd);
            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{5.29f, 32.3f, 14.11f, 14.11f, 14.11f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i;
            if (head)
                for (i = 1; i < 7; i++) {
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(pdfPCell);
                }
            for (i = 1; i < 14; i++) {
                if (i != 7) {
                    pdfPCell = new PdfPCell(new Phrase(" ", fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(pdfPCell);
                }
                else {
                    pdfPCell = new PdfPCell(new Phrase(number + nameRoom, fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(6);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            pdfPTable.setKeepTogether(true);
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addRoomRE", e.toString());
        }
    }

    public void addElementRE(ArrayList<ArrayList> elements) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{5.29f, 32.3f, 14.11f, 14.11f, 14.11f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j;
            for (i = 1; i < 7; i++) {
                PdfPTable help = new PdfPTable(1);
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPaddingBottom(5f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Phrase(" ", font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(help);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(pdfPCell);
            }
            pdfPTable.setHeaderRows(1);
            pdfPTable.setSkipFirstHeader(true);
            String block= "Блок розеток", udl = "Удлинитель", philtr = "Сетевой фильтр";
            String block_2 = "блок розеток", udl_2 = "удлинитель", philtr_2 = "сетевой фильтр";
            String nameEl, numbEl;
            for (i = 0; i < elements.size(); i++)
                for (j = 0; j < 6; j++)
                    if (j != 1) {
                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(elements.get(i).get(j)), font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPTable.addCell(pdfPCell);
                    }
                    else {
                        nameEl = String.valueOf(elements.get(i).get(j));
                        numbEl = String.valueOf(elements.get(i).get(j+1));
                        if (numbEl.equals("1")) {
                            pdfPCell = new PdfPCell(new Phrase(nameEl, font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            pdfPCell.setPaddingBottom(5f);
                            pdfPTable.addCell(pdfPCell);
                        }
                        else {
                            float[] columnWidths1 = new float[]{80f, 20f};
                            PdfPTable help = new PdfPTable(2);
                            help.setWidths(columnWidths1);
                            pdfPCell = new PdfPCell(new Phrase(nameEl, font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            pdfPCell.setPaddingBottom(5f);
                            pdfPCell.setBorder(Rectangle.NO_BORDER);
                            help.addCell(pdfPCell);
                            if (nameEl.contains(block) || nameEl.contains(block_2) || nameEl.contains(udl)
                                || nameEl.contains(udl_2) || nameEl.contains(philtr) || nameEl.contains(philtr_2))
                                pdfPCell = new PdfPCell(new Phrase(numbEl + " гн", font));
                            else
                                pdfPCell = new PdfPCell(new Phrase(numbEl + " шт", font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                            pdfPCell.setBorder(Rectangle.NO_BORDER);
                            pdfPCell.setPaddingBottom(5f);
                            help.addCell(pdfPCell);
                            pdfPCell = new PdfPCell(help);
                            pdfPTable.addCell(pdfPCell);
                        }
                    }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addElementRE", e.toString());
        }
    }

    public void createTableInsulation(String[]header) {
        try {
            font.setSize(10);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(9);
            float[] columnWidths = new float[]{3.2f, 15.4f, 4.2f, 5.3f, 6.7f, 4.2f, 5.4f, 47.7f, 6.7f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(20);
            PdfPCell pdfPCell;
            int index = 0, i, j;
            for (i = 0; i < 9; ++i) {
                if (i == 0 || i == 1) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if ((i > 1 && i < 7) || (i == 8)) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setRotation(90);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if (i == 7) {
                    font.setSize(9);
                    PdfPTable r = new PdfPTable(10);
                    float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                    r.setWidths(columnWidths1);
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(10);
                    r.addCell(pdfPCell);
                    index++;
                    for (j = 0; j < 10; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        r.addCell(pdfPCell);
                        index++;
                    }
                    font.setSize(10);
                    pdfPCell = new PdfPCell(r);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            index = 1;
            fontbd.setSize(12);
            for (i = 0; i < 9; i++) {
                if (i < 7 || i == 8) {
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                else {
                    PdfPTable num = new PdfPTable(10);
                    float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                    num.setWidths(columnWidths1);
                    for (j = 0; j < 10; j++) {
                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        num.addCell(pdfPCell);
                        index++;
                    }
                    pdfPCell = new PdfPCell(num);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableInsulation", e.toString());
        }
    }

    public void emptyStringsInsulation(int number) {
        try {
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(9);
            float[] columnWidths = new float[]{3.2f, 15.4f, 4.2f, 5.3f, 6.7f, 4.2f, 5.4f, 47.7f, 6.7f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j, q;
            for (i = 0; i < number; i++)
                for (j = 0; j < 9; j++)
                    if (j != 7) {
                        if (j == 0)
                            pdfPCell = new PdfPCell(new Phrase("\u2014", font));
                        else if (j == 1)
                            pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014\u2014", font));
                            else if (j == 2 || j == 5)
                                pdfPCell = new PdfPCell(new Phrase("\u2014", font));
                                else if (j == 3)
                                    pdfPCell = new PdfPCell(new Phrase("\u2014\u2014", font));
                                    else if (j == 4)
                                        pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014\u2014", font));
                                        else if (j == 6)
                                            pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014", font));
                                            else
                                                pdfPCell = new PdfPCell(new Phrase("\u2014\u2014\u2014", font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPTable.addCell(pdfPCell);
                    }
                    else {
                        PdfPTable num = new PdfPTable(10);
                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                        num.setWidths(columnWidths1);
                        for (q = 0; q < 10; q++) {
                            pdfPCell = new PdfPCell(new Phrase("\u2014", font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            num.addCell(pdfPCell);
                        }
                        pdfPCell = new PdfPCell(num);
                        pdfPCell.setPadding(0);
                        pdfPTable.addCell(pdfPCell);
                    }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("emptyStringsInsulation", e.toString());
        }
    }

    public void addRoomInsulation(String nameRoom, Boolean head, int empty) {
        try {
            if(empty != 0)
                emptyStringsInsulation(empty);
            font.setSize(10);
            fontbd.setSize(12);
            paragraph = new Paragraph();
            paragraph.setFont(fontbd);
            PdfPTable pdfPTable = new PdfPTable(9);
            float[] columnWidths = new float[]{3.2f, 15.4f, 4.2f, 5.3f, 6.7f, 4.2f, 5.4f, 47.7f, 6.7f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j, numb_st = 1;
            if (head) {
                for (i = 0; i < 9; i++)
                    if (i != 7) {
                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(numb_st++), fontbd));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPTable.addCell(pdfPCell);
                    }
                    else {
                        PdfPTable num = new PdfPTable(10);
                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                        num.setWidths(columnWidths1);
                        for (j = 0; j < 10; j++) {
                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(numb_st++), fontbd));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            num.addCell(pdfPCell);
                        }
                        pdfPCell = new PdfPCell(num);
                        pdfPCell.setPadding(0);
                        pdfPTable.addCell(pdfPCell);
                    }
            }

            for (i = 0; i < 10; i++) {
                if (i != 7 && i != 9) {
                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                }
                else {
                    if (i == 7) {
                        PdfPTable num = new PdfPTable(10);
                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                        num.setWidths(columnWidths1);
                        for (j = 0; j < 10; j++) {
                            pdfPCell = new PdfPCell(new Phrase(" ", font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            num.addCell(pdfPCell);
                        }
                        pdfPCell = new PdfPCell(num);
                        pdfPCell.setPadding(0);
                        pdfPTable.addCell(pdfPCell);
                    }
                    else {
                        fontbd.setSize(11);
                        pdfPCell = new PdfPCell(new Phrase(nameRoom, fontbd));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setColspan(9);
                        pdfPTable.addCell(pdfPCell);
                        fontbd.setSize(12);
                    }
                }
            }
            pdfPTable.setKeepTogether(true);
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addRoomInsulation", e.toString());
        }
    }

    public void addLineInsulation(String numberLine, String nameLine, Boolean head, int empty) {
        try {
            if(empty != 0)
                emptyStringsInsulation(empty);
            font.setSize(10);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(9);
            float[] columnWidths = new float[]{3.2f, 15.4f, 4.2f, 5.3f, 6.7f, 4.2f, 5.4f, 47.7f, 6.7f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j, numb_st = 1;
            if (head) {
                for (i = 0; i < 9; i++)
                    if (i != 7) {
                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(numb_st++), fontbd));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPTable.addCell(pdfPCell);
                    }
                    else {
                        PdfPTable num = new PdfPTable(10);
                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                        num.setWidths(columnWidths1);
                        for (j = 0; j < 10; j++) {
                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(numb_st++), fontbd));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            num.addCell(pdfPCell);
                        }
                        pdfPCell = new PdfPCell(num);
                        pdfPCell.setPadding(0);
                        pdfPTable.addCell(pdfPCell);
                    }
            }

            for (i = 0; i < 18; i++) {
                if (i != 7 && i != 9 && i != 10 && i != 16) {
                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                }
                else
                    if (i == 9) {
                        pdfPCell = new PdfPCell(new Phrase(numberLine, font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPTable.addCell(pdfPCell);
                    }
                    else
                        if (i == 10) {
                            pdfPCell = new PdfPCell(new Phrase(nameLine, font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            pdfPTable.addCell(pdfPCell);
                        }
                        else {
                            PdfPTable num = new PdfPTable(10);
                            float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                            num.setWidths(columnWidths1);
                            for (j = 0; j < 10; j++) {
                                pdfPCell = new PdfPCell(new Phrase(" ", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                num.addCell(pdfPCell);
                            }
                            pdfPCell = new PdfPCell(num);
                            pdfPCell.setPadding(0);
                            pdfPTable.addCell(pdfPCell);
                        }
            }
            pdfPTable.setKeepTogether(true);
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addLineInsulation", e.toString());
        }
    }

    public void addGroupsInsulation(ArrayList<ArrayList> groups) {
        try {
            font.setSize(10);
            fontbd.setSize(12);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(9);
            float[] columnWidths = new float[]{3.2f, 15.4f, 4.2f, 5.3f, 6.7f, 4.2f, 5.4f, 47.7f, 6.7f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int index = 1, i, j;

            for (i = 0; i < 9; i++) {
                if (i < 7 || i == 8) {
                    PdfPTable help = new PdfPTable(1);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    help.addCell(pdfPCell);
                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    help.addCell(pdfPCell);
                    pdfPCell = new PdfPCell(help);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                else {
                    PdfPTable num = new PdfPTable(10);
                    float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                    num.setWidths(columnWidths1);
                    for (j = 0; j < 10; j++) {
                        PdfPTable help = new PdfPTable(1);
                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        help.addCell(pdfPCell);
                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        help.addCell(pdfPCell);
                        pdfPCell = new PdfPCell(help);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        num.addCell(pdfPCell);
                        index++;
                    }
                    pdfPCell = new PdfPCell(num);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            pdfPTable.setHeaderRows(1);
            pdfPTable.setSkipFirstHeader(true);

            int index_first, index_second;
            for (index_first = 0; index_first < groups.size(); index_first++) {
                index_second = 0;
                for (i = 0; i < 9; i++) {
                    if (i != 0 && i != 7) {
                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(groups.get(index_first).get(index_second)), font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPTable.addCell(pdfPCell);
                        index_second++;
                    } else if (i == 0) {
                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPTable.addCell(pdfPCell);
                    } else {
                        PdfPTable num = new PdfPTable(10);
                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                        num.setWidths(columnWidths1);
                        for (j = 0; j < 10; j++) {
                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(groups.get(index_first).get(index_second)), font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            num.addCell(pdfPCell);
                            index_second++;
                        }
                        pdfPCell = new PdfPCell(num);
                        pdfPCell.setPadding(0);
                        pdfPTable.addCell(pdfPCell);
                    }
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addGroupsInsulation", e.toString());
        }
    }

    public void createTableGround(String[] header){
        try {
            font.setSize(10);
            fontbd.setSize(10);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{2.527f, 8.8448f, 10.83f, 5.415f, 63.899f, 8.3f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(15);
            PdfPCell pdfPCell;
            int index = 0, i, j, k;
            for (i = 0; i < 6; ++i) {
                if (i < 3) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if ((i == 3) || (i == 5)) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setRotation(90);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if (i == 4) {
                    PdfPTable rTable = new PdfPTable(6);
                    float[] columnWidths1 = new float[]{5.65f, 50.85f, 12.99f, 11.86f, 5.37f, 12.429f};
                    rTable.setWidths(columnWidths1);
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(6);
                    rTable.addCell(pdfPCell);
                    index++;
                    for (j = 0; j < 6; j++) {
                        if (j == 2) {
                            pdfPCell = new PdfPCell(new Phrase(header[index], font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            rTable.addCell(pdfPCell);
                            index++;
                        }
                        if ((j == 0) || (j > 2)) {
                            pdfPCell = new PdfPCell(new Phrase(header[index], font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setRotation(90);
                            rTable.addCell(pdfPCell);
                            index++;
                        }
                        if (j == 1) {
                            PdfPTable r2Table = new PdfPTable(9);
                            float[] columnWidths2 = new float[]{11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f};
                            r2Table.setWidths(columnWidths2);
                            pdfPCell = new PdfPCell(new Phrase(header[index], font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            pdfPCell.setColspan(9);
                            r2Table.addCell(pdfPCell);
                            index++;
                            for (k = 0; k < 9; k++) {
                                pdfPCell = new PdfPCell(new Phrase(header[index], font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                r2Table.addCell(pdfPCell);
                                index++;
                            }
                            pdfPCell = new PdfPCell(r2Table);
                            pdfPCell.setPadding(0);
                            rTable.addCell(pdfPCell);
                        }
                    }
                    pdfPCell = new PdfPCell(rTable);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            index = 1;
            for (i = 0; i < 6; ++i) {
                if ((i < 4) || (i == 5)) {
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if (i == 4) {
                    PdfPTable rTable = new PdfPTable(6);
                    float[] columnWidths1 = new float[]{5.65f, 50.85f, 12.99f, 11.86f, 5.37f, 12.429f};
                    rTable.setWidths(columnWidths1);
                    for (j = 0; j < 6; j++) {
                        if ((j == 0) || (j > 1)) {
                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            rTable.addCell(pdfPCell);
                            index++;
                        }
                        if (j == 1) {
                            PdfPTable r2Table = new PdfPTable(9);
                            float[] columnWidths2 = new float[]{11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f};
                            r2Table.setWidths(columnWidths2);
                            for (k = 0; k < 9; k++) {
                                pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                pdfPCell.setPaddingBottom(5f);
                                r2Table.addCell(pdfPCell);
                                index++;
                            }
                            pdfPCell = new PdfPCell(r2Table);
                            pdfPCell.setPadding(0);
                            rTable.addCell(pdfPCell);
                        }
                    }
                    pdfPCell = new PdfPCell(rTable);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableGround", e.toString());
        }
    }

    public void addElemGround(String[] str) {
        try {
            font.setSize(10);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{2.527f, 8.8448f, 10.83f, 5.415f, 63.899f, 8.3f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int index = 0, i, j, k;
            for (i = 0; i < 6; ++i) {
                if ((i < 4) || (i == 5)) {
                    pdfPCell = new PdfPCell(new Phrase(str[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if (i == 4) {
                    PdfPTable rTable = new PdfPTable(6);
                    float[] columnWidths1 = new float[]{5.65f, 50.85f, 12.99f, 11.86f, 5.37f, 12.429f};
                    rTable.setWidths(columnWidths1);
                    for (j = 0; j < 6; j++) {
                        if ((j == 0) || (j > 1)) {
                            pdfPCell = new PdfPCell(new Phrase(str[index], font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            rTable.addCell(pdfPCell);
                            index++;
                        }
                        if (j == 1) {
                            PdfPTable r2Table = new PdfPTable(9);
                            float[] columnWidths2 = new float[]{11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f, 11.1f};
                            r2Table.setWidths(columnWidths2);
                            for (k = 0; k < 9; k++) {
                                pdfPCell = new PdfPCell(new Phrase(str[index], font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                pdfPCell.setPaddingBottom(5f);
                                r2Table.addCell(pdfPCell);
                                index++;
                            }
                            pdfPCell = new PdfPCell(r2Table);
                            pdfPCell.setPadding(0);
                            rTable.addCell(pdfPCell);
                        }
                    }
                    pdfPCell = new PdfPCell(rTable);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addElemGround", e.toString());
        }
    }

    public void appViewPDF(Activity activity) {
        if (pdfFile.exists()) {
            Uri uri = FileProvider.getUriForFile(activity,
                    BuildConfig.APPLICATION_ID + ".provider",
                    pdfFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://deatails?id=com.adobe.reader")));
                Toast.makeText(activity.getApplicationContext(), "Не установлено приложение для чтения pdf", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(activity.getApplicationContext(), "Ошибка", Toast.LENGTH_LONG).show();
        }
    }
}
