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
import com.itextpdf.text.pdf.PdfContentByte;
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
    private Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private Font font_2 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private Font font_3 = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private Font fontbd = FontFactory.getFont(FONTBD, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private Font fontbd_2 = FontFactory.getFont(FONTBD, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private Font font_under = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 10, Font.UNDERLINE);
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

    public void addCenter_UnderAndBD(String title, int size, int spaceBefore, int spaceAfter){
        try {
            fontbd.setSize(size);
            paragraph = new Paragraph(new Chunk(title, fontbd).setUnderline(0.5f, -2f));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            paragraph.setSpacingBefore(spaceBefore);
            paragraph.setSpacingAfter(spaceAfter);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addCenter_UnderAndBD", e.toString());
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

    public void addClimate(String[] usl, int size){
        try {
            font.setSize(size);
            font_under.setSize(size);
            Phrase phrase = new Phrase();
            phrase.add(new Chunk(usl[0], font));
            phrase.add(new Chunk(usl[1], font_under));
            phrase.add(new Chunk(usl[2], font));
            phrase.add(new Chunk(usl[3], font_under));
            phrase.add(new Chunk(usl[4], font));
            phrase.add(new Chunk(usl[5], font_under));
            phrase.add(new Chunk(usl[6], font));
            paragraph = new Paragraph(phrase);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addClimate", e.toString());
        }
    }

    public void addDate(String[] d, int size){
        try {
            font.setSize(size);
            font_under.setSize(size);
            Phrase phrase = new Phrase();
            phrase.add(new Chunk(d[0], font));
            phrase.add(new Chunk(d[1], font_under));
            phrase.add(new Chunk(d[2], font));
            phrase.add(new Chunk(d[3], font_under));
            phrase.add(new Chunk(d[4], font));
            if (d[5].equals("       "))
                phrase.add(new Chunk(d[5], font_under));
            else
                phrase.add(new Chunk(d[5], font));
            phrase.add(new Chunk(d[6], font));
            paragraph = new Paragraph(phrase);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(5);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addDate", e.toString());
        }
    }

    public void addParagraphUp_Title(String[] d, int size){
        try {
            font.setSize(size);
            fontbd.setSize(size);
            Phrase phrase = new Phrase();
            phrase.add(new Chunk(d[0], font));
            phrase.add(new Chunk(d[1], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[2], font));
            phrase.add(new Chunk(d[3], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[4], font));
            phrase.add(new Chunk(d[5], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[6], font));
            phrase.add(new Chunk(d[7], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[8], font));
            phrase.add(new Chunk(d[9], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[10], font));
            phrase.add(new Chunk(d[11], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[12], font));
            phrase.add(new Chunk(d[13], fontbd).setUnderline(0.5f, -2f));
            phrase.add(new Chunk(d[14], font));
            phrase.add(new Chunk(d[15], fontbd).setUnderline(0.5f, -2f));
            paragraph = new Paragraph(phrase);
            paragraph.setSpacingAfter(5);
            paragraph.setSpacingBefore(10);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraphUp_Title", e.toString());
        }
    }

    public void drawRectangleTitle(){
        try {
            PdfContentByte cb = pdfWriter.getDirectContent();
            cb.saveState();
            cb.setColorStroke(BaseColor.BLACK);
            Rectangle rect = new Rectangle(28,24,574,814);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth((float) 0.5);
            cb.rectangle(rect);
            rect = new Rectangle(30,26,572,812);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth(1);
            cb.rectangle(rect);
            rect = new Rectangle(32,28,570,810);
            rect.setBorder(Rectangle.BOX);
            rect.setBorderWidth((float) 0.5);
            cb.rectangle(rect);
            cb.stroke();
            cb.restoreState();
        }catch (Exception e) {
            Log.e("drawRectangleTitle", e.toString());
        }
    }

    public void addParagraphEnd_Insulation(String[] text, String[] sign) {
        try {
            fontbd.setSize(12);
            font_under.setSize(12);
            font.setSize(12);
            font_2.setSize(8);
            paragraph = new Paragraph();

            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], fontbd));
            phrase.add(new Chunk(text[1], font_under));
            phrase.add(new Chunk(separator_12));
            Paragraph paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(20);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[2], fontbd));
            phrase.add(new Chunk(text[3], font_under));
            phrase.add(new Chunk(separator_12));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(40);
            paragraph.add(paragraph1);

            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{25f, 30f, 5f, 15f, 5f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pdfPCell;
            int i, str = 1;
            for (i = 0; i < 24; i ++) {
                //ВЕРТИКАЛЬНОЕ РАСПОЖЕНИЕ + ОТСТУПЫ СНИЗУ
                if (str % 2 == 0) {
                    pdfPCell = new PdfPCell(new Phrase(sign[i],font_2));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPCell.setPaddingBottom(20f);
                }
                else {
                    pdfPCell = new PdfPCell(new Phrase(sign[i],font));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    if (i % 2 != 0) {
                        pdfPCell.setBorder(Rectangle.BOTTOM);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }
                    else {
                        pdfPCell.setBorder(Rectangle.NO_BORDER);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        pdfPCell.setPaddingLeft(40f);
                    }
                }
                pdfPTable.addCell(pdfPCell);
                if ((i + 1) % 6 == 0)
                    str++;
            }
            paragraph.add(pdfPTable);

            paragraph.setSpacingBefore(5);
            paragraph.setKeepTogether(true);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraphEnd_Ins", e.toString());
        }
    }

    public void addParagraphEnd_Ground(String[] text, String[] sign) {
        try {
            fontbd.setSize(12);
            font_under.setSize(12);
            font.setSize(12);
            font_2.setSize(8);
            paragraph = new Paragraph();

            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], fontbd));
            phrase.add(new Chunk(text[1], font_under));
            phrase.add(new Chunk(separator_12));
            Paragraph paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(20);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[2], fontbd));
            phrase.add(new Chunk(text[3], font_under));
            phrase.add(new Chunk(separator_12));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(40);
            paragraph.add(paragraph1);

            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{25f, 30f, 5f, 15f, 5f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pdfPCell;
            int i, str = 1;
            for (i = 0; i < 24; i ++) {
                //ВЕРТИКАЛЬНОЕ РАСПОЖЕНИЕ + ОТСТУПЫ СНИЗУ
                if (str % 2 == 0) {
                    pdfPCell = new PdfPCell(new Phrase(sign[i],font_2));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPCell.setPaddingBottom(20f);
                }
                else {
                    pdfPCell = new PdfPCell(new Phrase(sign[i],font));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    if (i % 2 != 0) {
                        pdfPCell.setBorder(Rectangle.BOTTOM);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }
                    else {
                        pdfPCell.setBorder(Rectangle.NO_BORDER);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        pdfPCell.setPaddingLeft(40f);
                    }
                }
                pdfPTable.addCell(pdfPCell);
                if ((i + 1) % 6 == 0)
                    str++;
            }
            paragraph.add(pdfPTable);

            paragraph.setSpacingBefore(5);
            paragraph.setKeepTogether(true);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraphEnd_Ground", e.toString());
        }
    }

    public void addParagraphEnd_RE(String[] text, String[] sign) {
        try {
            fontbd.setSize(12);
            font_under.setSize(10);
            font.setSize(12);
            font_2.setSize(8);
            font_3.setSize(10);
            paragraph = new Paragraph();

            Paragraph paragraph1 = new Paragraph("Заключение:", fontbd);
            paragraph1.setAlignment(Element.ALIGN_CENTER);
            paragraph.add(paragraph1);

            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], font_3));
            phrase.add(new Chunk(text[1], font_under));
            phrase.add(new Chunk(separator_10));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingBefore(5);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[2], font_3));
            phrase.add(new Chunk(text[3], font_under));
            phrase.add(new Chunk(separator_10));
            paragraph1 = new Paragraph(phrase);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[4], font_3));
            phrase.add(new Chunk(text[5], font_3).setUnderline(0.4f, 2f));
            phrase.add(new Chunk(text[6], font_3));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(30);
            paragraph.add(paragraph1);

            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{25f, 30f, 5f, 15f, 5f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pdfPCell;
            int i, str = 1;
            for (i = 0; i < 24; i ++) {
                //ВЕРТИКАЛЬНОЕ РАСПОЖЕНИЕ + ОТСТУПЫ СНИЗУ
                if (str % 2 == 0) {
                    pdfPCell = new PdfPCell(new Phrase(sign[i],font_2));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPCell.setPaddingBottom(20f);
                }
                else {
                    pdfPCell = new PdfPCell(new Phrase(sign[i],font));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    if (i % 2 != 0) {
                        pdfPCell.setBorder(Rectangle.BOTTOM);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    }
                    else {
                        pdfPCell.setBorder(Rectangle.NO_BORDER);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    }
                }
                pdfPTable.addCell(pdfPCell);
                if ((i + 1) % 6 == 0)
                    str++;
            }
            paragraph.add(pdfPTable);

            paragraph.setSpacingBefore(1);
            paragraph.setKeepTogether(true);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("addParagraphEnd_RE", e.toString());
        }
    }

    public void addParagraphEnd_AU(String[] text, String[] sign) {
        try {
            fontbd.setSize(12);
            fontbd_2.setSize(10);
            font_under.setSize(12);
            font_2.setSize(10);

            Paragraph paragraph1;

            paragraph = new Paragraph();
            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], fontbd));
            phrase.add(new Chunk(text[1], fontbd_2));
            phrase.add(new Chunk(text[2], font_2));
            phrase.add(new Chunk(text[3], fontbd_2));
            phrase.add(new Chunk(text[4], font_2));
            phrase.add(new Chunk(text[5], fontbd_2));
            phrase.add(new Chunk(text[6], font_2));
            phrase.add(new Chunk(text[7], fontbd_2));
            phrase.add(new Chunk(text[8], font_2));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(20);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[9], fontbd));
            phrase.add(new Chunk(text[10], font_under));
            phrase.add(new Chunk(separator_12));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(40);
            paragraph.add(paragraph1);

            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{25f, 30f, 5f, 15f, 5f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pdfPCell;
            int i, str = 1;
            for (i = 0; i < 24; i++) {
                //ВЕРТИКАЛЬНОЕ РАСПОЖЕНИЕ + ОТСТУПЫ СНИЗУ
                if (str % 2 == 0) {
                    pdfPCell = new PdfPCell(new Phrase(sign[i], font_2));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPCell.setPaddingBottom(20f);
                } else {
                    pdfPCell = new PdfPCell(new Phrase(sign[i], font));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    if (i % 2 != 0) {
                        pdfPCell.setBorder(Rectangle.BOTTOM);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    } else {
                        pdfPCell.setBorder(Rectangle.NO_BORDER);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        pdfPCell.setPaddingLeft(40f);
                    }
                }
                pdfPTable.addCell(pdfPCell);
                if ((i + 1) % 6 == 0)
                    str++;
            }
            paragraph.add(pdfPTable);

            paragraph.setSpacingBefore(5);
            paragraph.setKeepTogether(true);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraphEnd_AU", e.toString());
        }
    }

    public void addParagraphEnd_DAU(String[] text, String[] sign) {
        try {
            fontbd.setSize(12);
            font_2.setSize(10);
            font_under.setSize(12);

            Paragraph paragraph1;

            paragraph = new Paragraph();
            Phrase phrase = new Phrase();
            phrase.add(new Chunk(text[0], fontbd));
            phrase.add(new Chunk(text[1], font_under));
            phrase.add(new Chunk(separator_12));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingBefore(5);
            paragraph.add(paragraph1);

            phrase = new Phrase();
            phrase.add(new Chunk(text[2], font_under));
            phrase.add(new Chunk(separator_12));
            paragraph1 = new Paragraph(phrase);
            paragraph1.setSpacingAfter(40);
            paragraph.add(paragraph1);

            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{25f, 30f, 5f, 15f, 5f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(90);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            PdfPCell pdfPCell;
            int i, str = 1;
            for (i = 0; i < 24; i++) {
                //ВЕРТИКАЛЬНОЕ РАСПОЖЕНИЕ + ОТСТУПЫ СНИЗУ
                if (str % 2 == 0) {
                    pdfPCell = new PdfPCell(new Phrase(sign[i], font_2));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_TOP);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setBorder(Rectangle.NO_BORDER);
                    pdfPCell.setPaddingBottom(20f);
                } else {
                    pdfPCell = new PdfPCell(new Phrase(sign[i], font));
                    pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
                    if (i % 2 != 0) {
                        pdfPCell.setBorder(Rectangle.BOTTOM);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    } else {
                        pdfPCell.setBorder(Rectangle.NO_BORDER);
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        pdfPCell.setPaddingLeft(40f);
                    }
                }
                pdfPTable.addCell(pdfPCell);
                if ((i + 1) % 6 == 0)
                    str++;
            }
            paragraph.add(pdfPTable);

            paragraph.setSpacingBefore(5);
            paragraph.setKeepTogether(true);
            document.add(paragraph);
        } catch (Exception e) {
            Log.e("addParagraphEnd_DAU", e.toString());
        }
    }

    //TABLES

    public void createTableTitle(String addr, String targ) {
        try {
            fontbd.setSize(12);
            paragraph = new Paragraph();
            paragraph.setFont(fontbd);
            PdfPTable pdfPTable = new PdfPTable(2);
            float[] columnWidths = new float[]{14f, 90f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(86);
            pdfPTable.setHorizontalAlignment(Element.ALIGN_CENTER);
            PdfPCell pdfPCell;

            pdfPCell = new PdfPCell(new Phrase("по адресу:", fontbd));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            pdfPTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Phrase(addr, fontbd));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCell.setBorder(Rectangle.BOTTOM);
            pdfPTable.addCell(pdfPCell);

            pdfPCell = new PdfPCell(new Phrase("код ОКП:", fontbd));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            pdfPCell.setPaddingTop(15);
            pdfPTable.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Phrase(" ", fontbd));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCell.setBorder(Rectangle.BOTTOM);
            pdfPCell.setPaddingTop(15);
            pdfPTable.addCell(pdfPCell);

            paragraph.add(pdfPTable);

            PdfPTable pdfPTable2 = new PdfPTable(2);
            float[] columnWidths2 = new float[]{23f, 77f};
            pdfPTable2.setWidths(columnWidths2);
            pdfPTable2.setWidthPercentage(86);
            pdfPTable2.setHorizontalAlignment(Element.ALIGN_CENTER);

            pdfPCell = new PdfPCell(new Phrase("цель испытаний:", fontbd));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCell.setBorder(Rectangle.NO_BORDER);
            pdfPCell.setPaddingTop(15);
            pdfPTable2.addCell(pdfPCell);
            pdfPCell = new PdfPCell(new Phrase(targ, fontbd));
            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfPCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
            pdfPCell.setBorder(Rectangle.BOTTOM);
            pdfPCell.setPaddingTop(15);
            pdfPTable2.addCell(pdfPCell);

            paragraph.add(pdfPTable2);

            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableTitle", e.toString());
        }
    }

    public void createTableRE(String[]header) {
        try {
            font.setSize(10);
            fontbd.setSize(11);
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
                pdfPCell.setBorderWidthTop(2f);
                if (i == 0)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 5)
                    pdfPCell.setBorderWidthRight(2f);
                pdfPTable.addCell(pdfPCell);
            }
            for (i = 1; i < 7; i++) {
                PdfPTable help = new PdfPTable(1);
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPaddingBottom(5f);
                pdfPCell.setBorderWidthTop(2f);
                pdfPCell.setBorderWidthBottom(2f);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 6)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Phrase(" ", font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 6)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(help);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(pdfPCell);
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableRE", e.toString());
        }
    }

    public void createTableRE2(ArrayList<String> floors, ArrayList<Integer> countRooms, ArrayList<String> rooms, ArrayList<Integer> countElements, ArrayList<ArrayList> elements) {
        try {
            font.setSize(10);
            fontbd.setSize(11);
            paragraph = new Paragraph();
            PdfPTable pdfPTable = new PdfPTable(6);
            float[] columnWidths = new float[]{5.29f, 32.3f, 14.11f, 14.11f, 14.11f, 20f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j, k, q, indexRoom = 0, indexElement = 0, indexElementSecond;
            String block= "Блок розеток", udl = "Удлинитель", philtr = "Сетевой фильтр";
            String block_2 = "блок розеток", udl_2 = "удлинитель", philtr_2 = "сетевой фильтр";
            String nameEl, numbEl;

            //ШАПКА
            for (i = 1; i < 7; i++) {
                PdfPTable help = new PdfPTable(1);
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setPaddingBottom(5f);
                pdfPCell.setBorderWidthTop(2f);
                pdfPCell.setBorderWidthBottom(2f);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 6)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Phrase(" ", font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 6)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(help);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPTable.addCell(pdfPCell);
            }
            pdfPTable.setHeaderRows(1);
            pdfPTable.setSkipFirstHeader(true);

            //ОСНОВНАЯ ТАБЛИЦА
            for (i = 1; i <= floors.size(); i++) {

                //ЭТАЖ
                if(!floors.get(i - 1).equals("БЕЗ ЭТАЖА")) {
                    pdfPCell = new PdfPCell(new Phrase(floors.get(i - 1), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(6);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    pdfPTable.addCell(pdfPCell);
                }

                //КОМНАТА + ПУСТАЯ СТРОКА
                for (j = 1; j <= countRooms.get(i - 1); j++) {
                    PdfPTable roomTable = new PdfPTable(6);
                    roomTable.setWidths(columnWidths);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(indexRoom + 1) + ". " + rooms.get(indexRoom), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(6);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    roomTable.addCell(pdfPCell);
                    for (k = 0; k < 6; k++) {
                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        if (k == 0)
                            pdfPCell.setBorderWidthLeft(2f);
                        if (k == 5)
                            pdfPCell.setBorderWidthRight(2f);
                        roomTable.addCell(pdfPCell);
                    }
                    roomTable.setKeepTogether(true);
                    pdfPCell = new PdfPCell(roomTable);
                    pdfPCell.setColspan(6);
                    pdfPTable.addCell(pdfPCell);

                    //ЭЛЕМЕНТЫ И ПУСТАЯ СТРОКА
                    for (k = 1; k <= countElements.get(indexRoom); k++) {
                        indexElementSecond = 0;
                        if (k != countElements.get(indexRoom)) {
                            for (q = 0; q < 6; q++) {
                                if (q != 1) {
                                    if (q != 0) {
                                        pdfPCell = new PdfPCell(new Phrase(" " + String.valueOf(elements.get(indexElement).get(indexElementSecond)), font));
                                        indexElementSecond++;
                                    }
                                    else {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(k) + ".", font));
                                        pdfPCell.setBorderWidthLeft(2f);
                                    }
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    pdfPCell.setPaddingBottom(5f);
                                    if (q == 5)
                                        pdfPCell.setBorderWidthRight(2f);
                                    pdfPTable.addCell(pdfPCell);
                                }
                                else {
                                    nameEl = String.valueOf(elements.get(indexElement).get(indexElementSecond));
                                    //numbEl = String.valueOf(elements.get(indexElement).get(indexElementSecond + 1));
                                    //if (numbEl.equals("1")) {
                                    pdfPCell = new PdfPCell(new Phrase(nameEl, font));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    pdfPCell.setPaddingBottom(5f);
                                    pdfPTable.addCell(pdfPCell);
                                    indexElementSecond++;
                                    //}
                                    //else {
                                        // 1. РАСКОММЕНТИРУЙ, ЕСЛИ ХОЧЕШЬ, ЧТОБЫ ИМЯ И ШТ/ГН БЫЛИ ЧЕРЕЗ ПРОБЕЛ
//                                        if (nameEl.contains(block) || nameEl.contains(block_2) || nameEl.contains(udl)
//                                                || nameEl.contains(udl_2) || nameEl.contains(philtr) || nameEl.contains(philtr_2))
//                                            pdfPCell = new PdfPCell(new Phrase(nameEl + "    " + numbEl + " гн", font));
//                                        else
//                                            pdfPCell = new PdfPCell(new Phrase(nameEl + "    " + numbEl + " шт", font));
//                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                                        pdfPCell.setPaddingBottom(5f);

                                        // 2. РАССКОМЕНТИРУЙ, ЕСЛИ ХОЧЕШЬ ПРИБИТЬ ШТ/ГН СПРАВА
//                                        float[] columnWidths1 = new float[]{80f, 20f};
//                                        PdfPTable help = new PdfPTable(2);
//                                        help.setWidths(columnWidths1);
//                                        pdfPCell = new PdfPCell(new Phrase(nameEl, font));
//                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                                        pdfPCell.setPaddingBottom(5f);
//                                        pdfPCell.setBorder(Rectangle.NO_BORDER);
//                                        help.addCell(pdfPCell);
//                                        if (nameEl.contains(block) || nameEl.contains(block_2) || nameEl.contains(udl)
//                                            || nameEl.contains(udl_2) || nameEl.contains(philtr) || nameEl.contains(philtr_2))
//                                            pdfPCell = new PdfPCell(new Phrase(numbEl + " гн", font));
//                                        else
//                                            pdfPCell = new PdfPCell(new Phrase(numbEl + " шт", font));
//                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                                        pdfPCell.setBorder(Rectangle.NO_BORDER);
//                                        pdfPCell.setPaddingBottom(5f);
//                                        help.addCell(pdfPCell);
//                                        pdfPCell = new PdfPCell(help);

                                        // В ЛЮБОМ СЛУЧАЕ РАСКОММЕНТИРУЙ
//                                        pdfPTable.addCell(pdfPCell);
//                                        indexElementSecond++;
                                    //}
                                }
                            }
                        }
                        else {
                            PdfPTable lastElementTable = new PdfPTable(6);
                            lastElementTable.setWidths(columnWidths);
                            for (q = 0; q < 6; q++) {
                                if (q != 1) {
                                    if (q != 0) {
                                        pdfPCell = new PdfPCell(new Phrase(" " + String.valueOf(elements.get(indexElement).get(indexElementSecond)), font));
                                        indexElementSecond++;
                                    }
                                    else {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(k) + ".", font));
                                        pdfPCell.setBorderWidthLeft(2f);
                                    }
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    pdfPCell.setPaddingBottom(5f);
                                    if (q == 5)
                                        pdfPCell.setBorderWidthRight(2f);
                                    lastElementTable.addCell(pdfPCell);
                                }
                                else {
                                    nameEl = String.valueOf(elements.get(indexElement).get(indexElementSecond));
                                    //numbEl = String.valueOf(elements.get(indexElement).get(indexElementSecond + 1));
                                    //if (numbEl.equals("1")) {
                                    pdfPCell = new PdfPCell(new Phrase(nameEl, font));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    pdfPCell.setPaddingBottom(5f);
                                    lastElementTable.addCell(pdfPCell);
                                    indexElementSecond++;
                                    //}
                                    //else {
                                        // 1. РАСКОММЕНТИРУЙ, ЕСЛИ ХОЧЕШЬ, ЧТОБЫ ИМЯ И ШТ/ГН БЫЛИ ЧЕРЕЗ ПРОБЕЛ
//                                        if (nameEl.contains(block) || nameEl.contains(block_2) || nameEl.contains(udl)
//                                                || nameEl.contains(udl_2) || nameEl.contains(philtr) || nameEl.contains(philtr_2))
//                                            pdfPCell = new PdfPCell(new Phrase(nameEl + "    " + numbEl + " гн", font));
//                                        else
//                                            pdfPCell = new PdfPCell(new Phrase(nameEl + "    " + numbEl + " шт", font));
//                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                                        pdfPCell.setPaddingBottom(5f);

                                        // 2. РАССКОМЕНТИРУЙ, ЕСЛИ ХОЧЕШЬ ПРИБИТЬ ШТ/ГН СПРАВА
//                                        float[] columnWidths1 = new float[]{80f, 20f};
//                                        PdfPTable help = new PdfPTable(2);
//                                        help.setWidths(columnWidths1);
//                                        pdfPCell = new PdfPCell(new Phrase(nameEl, font));
//                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                                        pdfPCell.setPaddingBottom(5f);
//                                        pdfPCell.setBorder(Rectangle.NO_BORDER);
//                                        help.addCell(pdfPCell);
//                                        if (nameEl.contains(block) || nameEl.contains(block_2) || nameEl.contains(udl)
//                                                || nameEl.contains(udl_2) || nameEl.contains(philtr) || nameEl.contains(philtr_2))
//                                            pdfPCell = new PdfPCell(new Phrase(numbEl + " гн", font));
//                                        else
//                                            pdfPCell = new PdfPCell(new Phrase(numbEl + " шт", font));
//                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//                                        pdfPCell.setBorder(Rectangle.NO_BORDER);
//                                        pdfPCell.setPaddingBottom(5f);
//                                        help.addCell(pdfPCell);
//                                        pdfPCell = new PdfPCell(help);

                                        // В ЛЮБОМ СЛУЧАЕ РАСКОММЕНТИРУЙ
//                                        lastElementTable.addCell(pdfPCell);
//                                        indexElementSecond++;
                                    //}
                                }
                            }
                            for (q = 0; q < 6; q++) {
                                pdfPCell = new PdfPCell(new Phrase(" ", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                if (q == 0)
                                    pdfPCell.setBorderWidthLeft(2f);
                                if (q == 5)
                                    pdfPCell.setBorderWidthRight(2f);
                                if (i == floors.size() && j == countRooms.get(i - 1))
                                    pdfPCell.setBorderWidthBottom(2f);
                                lastElementTable.addCell(pdfPCell);
                            }
                            lastElementTable.setKeepTogether(true);
                            pdfPCell = new PdfPCell(lastElementTable);
                            pdfPCell.setColspan(6);
                            pdfPTable.addCell(pdfPCell);
                        }
                        indexElement++;
                    }
                    indexRoom++;
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableRE2", e.toString());
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
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if ((i > 1 && i < 7) || (i == 8)) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 8)
                        pdfPCell.setBorderWidthRight(2f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setPaddingTop(5f);
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
                    pdfPCell.setBorderWidthTop(2f);
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
            fontbd.setSize(11);
            for (i = 0; i < 9; i++) {
                if (i < 7 || i == 8) {
                    PdfPTable help = new PdfPTable(1);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setBorderWidthBottom(2f);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 8)
                        pdfPCell.setBorderWidthRight(2f);
                    help.addCell(pdfPCell);
                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 8)
                        pdfPCell.setBorderWidthRight(2f);
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
                        pdfPCell.setBorderWidthTop(2f);
                        pdfPCell.setBorderWidthBottom(2f);
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
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableInsulation", e.toString());
        }
    }

    public void createTableInsulation2(ArrayList<String> floors, ArrayList<Integer> countRooms,
                                       ArrayList<String> rooms, ArrayList<Integer> countLines,
                                       ArrayList<String> lines, ArrayList<Integer> countGroups, ArrayList<ArrayList> groups) {
        try {
            font.setSize(10);
            fontbd.setSize(11);
            paragraph = new Paragraph();
            PdfPTable pdfPTable = new PdfPTable(9);
            float[] columnWidths = new float[]{3.2f, 15.4f, 4.2f, 5.3f, 6.7f, 4.2f, 5.4f, 47.7f, 6.7f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int index = 1, i, j, k, q, n, indexRoom = 0, indexLine = 0, indexGroup = 0, indexGroupSecond;

            //ШАПКА
            for (i = 0; i < 9; i++) {
                if (i < 7 || i == 8) {
                    PdfPTable help = new PdfPTable(1);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setBorderWidthBottom(2f);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 8)
                        pdfPCell.setBorderWidthRight(2f);
                    help.addCell(pdfPCell);
                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 8)
                        pdfPCell.setBorderWidthRight(2f);
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
                        pdfPCell.setBorderWidthTop(2f);
                        pdfPCell.setBorderWidthBottom(2f);
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

            //ОСНОВНАЯ ТАБЛИЦА
            for (i = 1; i <= floors.size(); i++) {

                //ЭТАЖ
                if (!floors.get(i - 1).equals("БЕЗ ЭТАЖА")) {
                    pdfPCell = new PdfPCell(new Phrase(floors.get(i - 1), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(9);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    pdfPTable.addCell(pdfPCell);
                }

                //КОМНАТА И ПУСТАЯ СТРОКА
                for (j = 1; j <= countRooms.get(i - 1); j++) {
                    PdfPTable roomTable = new PdfPTable(9);
                    roomTable.setWidths(columnWidths);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(indexRoom + 1) + ". " + rooms.get(indexRoom), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(9);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    roomTable.addCell(pdfPCell);
                    for (k = 0; k < 9; k++) {
                        if (k != 7) {
                            pdfPCell = new PdfPCell(new Phrase(" ", font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            if (k == 0)
                                pdfPCell.setBorderWidthLeft(2f);
                            if (k == 8)
                                pdfPCell.setBorderWidthRight(2f);
                            roomTable.addCell(pdfPCell);
                        } else {
                            PdfPTable num = new PdfPTable(10);
                            float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                            num.setWidths(columnWidths1);
                            for (q = 0; q < 10; q++) {
                                pdfPCell = new PdfPCell(new Phrase(" ", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                num.addCell(pdfPCell);
                            }
                            pdfPCell = new PdfPCell(num);
                            pdfPCell.setPadding(0);
                            roomTable.addCell(pdfPCell);
                        }
                    }
                    roomTable.setKeepTogether(true);
                    pdfPCell = new PdfPCell(roomTable);
                    pdfPCell.setColspan(9);
                    pdfPTable.addCell(pdfPCell);

                    //ЩИТ
                    for (k = 1; k <= countLines.get(indexRoom); k++) {
                        for (q = 0; q < 9; q++) {
                            if (q != 0 && q != 1 && q != 7) {
                                pdfPCell = new PdfPCell(new Phrase(" ", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                if (q == 8)
                                    pdfPCell.setBorderWidthRight(2f);
                                pdfPTable.addCell(pdfPCell);
                            } else if (q == 0) {
                                pdfPCell = new PdfPCell(new Phrase(String.valueOf(k) + ".", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                pdfPCell.setPaddingBottom(5f);
                                pdfPCell.setBorderWidthLeft(2f);
                                pdfPTable.addCell(pdfPCell);
                            } else if (q == 1) {
                                pdfPCell = new PdfPCell(new Phrase(lines.get(indexLine), font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                pdfPCell.setPaddingBottom(5f);
                                pdfPTable.addCell(pdfPCell);
                            } else {
                                PdfPTable num = new PdfPTable(10);
                                float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                                num.setWidths(columnWidths1);
                                for (n = 0; n < 10; n++) {
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

                        //ГРУППЫ И ПУСТАЯ СТРОКА
                        for (q = 1; q <= countGroups.get(indexLine); q++) {
                            indexGroupSecond = 0;
                            if (q != countGroups.get(indexLine)) {
                                for (n = 0; n < 9; n++) {
                                    if (n != 0 && n != 7) {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(groups.get(indexGroup).get(indexGroupSecond)), font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 8)
                                            pdfPCell.setBorderWidthRight(2f);
                                        pdfPTable.addCell(pdfPCell);
                                        indexGroupSecond++;
                                    } else if (n == 0) {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setBorderWidthLeft(2f);
                                        pdfPTable.addCell(pdfPCell);
                                    } else {
                                        PdfPTable num = new PdfPTable(10);
                                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                                        num.setWidths(columnWidths1);
                                        for (index = 0; index < 10; index++) {
                                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(groups.get(indexGroup).get(indexGroupSecond)), font));
                                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            pdfPCell.setPaddingBottom(5f);
                                            num.addCell(pdfPCell);
                                            indexGroupSecond++;
                                        }
                                        pdfPCell = new PdfPCell(num);
                                        pdfPCell.setPadding(0);
                                        pdfPTable.addCell(pdfPCell);
                                    }
                                }
                            } else {
                                PdfPTable lastGroupTable = new PdfPTable(9);
                                lastGroupTable.setWidths(columnWidths);
                                for (n = 0; n < 9; n++) {
                                    if (n != 0 && n != 7) {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(groups.get(indexGroup).get(indexGroupSecond)), font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 8)
                                            pdfPCell.setBorderWidthRight(2f);
                                        lastGroupTable.addCell(pdfPCell);
                                        indexGroupSecond++;
                                    } else if (n == 0) {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setBorderWidthLeft(2f);
                                        lastGroupTable.addCell(pdfPCell);
                                    } else {
                                        PdfPTable num = new PdfPTable(10);
                                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                                        num.setWidths(columnWidths1);
                                        for (index = 0; index < 10; index++) {
                                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(groups.get(indexGroup).get(indexGroupSecond)), font));
                                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            pdfPCell.setPaddingBottom(5f);
                                            num.addCell(pdfPCell);
                                            indexGroupSecond++;
                                        }
                                        pdfPCell = new PdfPCell(num);
                                        pdfPCell.setPadding(0);
                                        lastGroupTable.addCell(pdfPCell);
                                    }
                                }
                                for (n = 0; n < 9; n++) {
                                    if (n != 7) {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 8)
                                            pdfPCell.setBorderWidthRight(2f);
                                        if (n == 0)
                                            pdfPCell.setBorderWidthLeft(2f);
                                        if (i == floors.size() && j == countRooms.get(i - 1) && k == countLines.get(indexRoom))
                                            pdfPCell.setBorderWidthBottom(2f);
                                        lastGroupTable.addCell(pdfPCell);
                                    } else {
                                        PdfPTable num = new PdfPTable(10);
                                        float[] columnWidths1 = new float[]{9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 9.3f, 11.4f, 11.4f, 11.4f, 9.3f};
                                        num.setWidths(columnWidths1);
                                        for (index = 0; index < 10; index++) {
                                            pdfPCell = new PdfPCell(new Phrase(" ", font));
                                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                            pdfPCell.setPaddingBottom(5f);
                                            if (i == floors.size() && j == countRooms.get(i - 1) && k == countLines.get(indexRoom))
                                                pdfPCell.setBorderWidthBottom(2f);
                                            num.addCell(pdfPCell);
                                        }
                                        pdfPCell = new PdfPCell(num);
                                        pdfPCell.setPadding(0);
                                        lastGroupTable.addCell(pdfPCell);
                                    }
                                }
                                lastGroupTable.setKeepTogether(true);
                                pdfPCell = new PdfPCell(lastGroupTable);
                                pdfPCell.setColspan(9);
                                pdfPTable.addCell(pdfPCell);
                            }
                            indexGroup++;
                        }
                        indexLine++;
                    }
                    indexRoom++;
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableInsulation2", e.toString());
        }
    }

    public void createTableGround(String[] header){
        try {
            font.setSize(10);
            font_2.setSize(9);
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
                    pdfPCell.setBorderWidthTop(2f);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if ((i == 3) || (i == 5)) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setBorderWidthTop(2f);
                    if (i == 5)
                        pdfPCell.setBorderWidthRight(2f);
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
                    pdfPCell.setBorderWidthTop(2f);
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
                            if (j == 0)
                                pdfPCell = new PdfPCell(new Phrase(header[index], font));
                            else
                                pdfPCell = new PdfPCell(new Phrase(header[index], font_2));
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
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setBorderWidthBottom(2f);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 5)
                        pdfPCell.setBorderWidthRight(2f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                else {
                    PdfPTable rTable = new PdfPTable(6);
                    float[] columnWidths1 = new float[]{5.65f, 50.85f, 12.99f, 11.86f, 5.37f, 12.429f};
                    rTable.setWidths(columnWidths1);
                    for (j = 0; j < 6; j++) {
                        if ((j == 0) || (j > 1)) {
                            pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            pdfPCell.setBorderWidthTop(2f);
                            pdfPCell.setBorderWidthBottom(2f);
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
                                pdfPCell.setBorderWidthTop(2f);
                                pdfPCell.setBorderWidthBottom(2f);
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
                    pdfPCell.setBorderWidthBottom(2f);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 5)
                        pdfPCell.setBorderWidthRight(2f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                else {
                    PdfPTable rTable = new PdfPTable(6);
                    float[] columnWidths1 = new float[]{5.65f, 50.85f, 12.99f, 11.86f, 5.37f, 12.429f};
                    rTable.setWidths(columnWidths1);
                    for (j = 0; j < 6; j++) {
                        if ((j == 0) || (j > 1)) {
                            pdfPCell = new PdfPCell(new Phrase(str[index], font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            pdfPCell.setPaddingBottom(5f);
                            pdfPCell.setBorderWidthBottom(2f);
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
                                pdfPCell.setBorderWidthBottom(2f);
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

    public void createTableAutomatics(String[]header) {
        try {
            font.setSize(9);
            fontbd.setSize(10);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(10);
            float[] columnWidths = new float[]{2.3f, 11.2f, 3.9f, 10.8f, 9f, 5.6f, 4.5f, 4.5f, 41.9f, 6.3f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(20);
            PdfPCell pdfPCell;
            int index = 0, i, j;
            for (i = 0; i < 10; ++i) {
                if (i == 0 || i == 1 || i == 3) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if (i == 2 || i == 5 || i == 6 || i == 7 || i == 9) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 9)
                        pdfPCell.setBorderWidthRight(2f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setPaddingTop(5f);
                    pdfPCell.setRotation(90);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                if (i == 4) {
                    PdfPTable type = new PdfPTable(2);
                    float[] columnWidths1 = new float[]{50f, 50f};
                    type.setWidths(columnWidths1);
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setColspan(2);
                    type.addCell(pdfPCell);
                    index++;
                    for (j = 0; j < 2; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setPaddingTop(5f);
                        pdfPCell.setRotation(90);
                        type.addCell(pdfPCell);
                        index++;
                    }
                    pdfPCell = new PdfPCell(type);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
                if (i == 8) {
                    PdfPTable set_and_check = new PdfPTable(4);
                    float[] columnWidths1 = new float[]{10.725f, 13.525f, 35.525f, 40.225f};
                    set_and_check.setWidths(columnWidths1);
                    for (j = 0; j < 2; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setBorderWidthTop(2f);
                        pdfPCell.setColspan(2);
                        set_and_check.addCell(pdfPCell);
                        index++;
                    }
                    for (j = 0; j < 2; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setPaddingTop(5f);
                        pdfPCell.setRotation(90);
                        set_and_check.addCell(pdfPCell);
                        index++;
                    }

                    //ВЛОЖЕННАЯ ТАБЛИЦА С ПЕРЕГРУЗКОЙ В ПРОВЕРКЕ
                    PdfPTable check_overload = new PdfPTable(2);
                    float[] columnWidths2 = new float[]{24.5f, 75.5f};
                    check_overload.setWidths(columnWidths2);

                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(2);
                    check_overload.addCell(pdfPCell);
                    index++;

                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setPaddingTop(5f);
                    pdfPCell.setRotation(90);
                    check_overload.addCell(pdfPCell);
                    index++;

                        //ВЛОЖЕННАЯ ТАЛИЦА СО ВРЕМЕНЕМ В ПЕРЕГРУЗКЕ В ПРОВЕРКЕ
                        PdfPTable check_overload_time = new PdfPTable(2);
                        float[] columnWidths3 = new float[]{50f, 50f};
                        check_overload_time.setWidths(columnWidths3);

                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setColspan(2);
                        check_overload_time.addCell(pdfPCell);
                        index++;

                        for (j = 0; j < 2; j++) {
                            pdfPCell = new PdfPCell(new Phrase(header[index], font));
                            pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                            check_overload_time.addCell(pdfPCell);
                            index++;
                        }
                        pdfPCell = new PdfPCell(check_overload_time);
                        pdfPCell.setPadding(0);
                        check_overload.addCell(pdfPCell);

                    pdfPCell = new PdfPCell(check_overload);
                    pdfPCell.setPadding(0);
                    set_and_check.addCell(pdfPCell);

                    //ВЛОЖЕННАЯ ТАБЛИЦА С К.З. В ПРОВЕРКУ
                    PdfPTable check_kz = new PdfPTable(3);
                    float[] columnWidths4 = new float[]{33.4f, 33.3f, 33.3f};
                    check_kz.setWidths(columnWidths4);

                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(3);
                    check_kz.addCell(pdfPCell);
                    index++;

                    for (j = 0; j < 3; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setPaddingTop(5f);
                        pdfPCell.setRotation(90);
                        check_kz.addCell(pdfPCell);
                        index++;
                    }

                    pdfPCell = new PdfPCell(check_kz);
                    pdfPCell.setPadding(0);
                    set_and_check.addCell(pdfPCell);

                    pdfPCell = new PdfPCell(set_and_check);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            paragraph.add(pdfPTable);

            //НУМЕРАЦИЯ СТОЛБЦОВ
            index = 1;
            PdfPTable pdfPTableIndex = new PdfPTable(18);
            float[] columnWidths6 = new float[]{2.3f, 11.2f, 3.9f, 10.8f, 4.5f, 4.5f, 5.6f, 4.5f, 4.5f, 4.5f, 5.7f, 3.6f, 5.6f, 5.6f, 5.6f, 5.6f, 5.6f, 6.3f};
            pdfPTableIndex.setWidths(columnWidths6);
            pdfPTableIndex.setWidthPercentage(100);
            for (i = 0; i < 18; i++) {
                    PdfPTable help = new PdfPTable(1);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPCell.setBorderWidthBottom(2f);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 17)
                        pdfPCell.setBorderWidthRight(2f);
                    help.addCell(pdfPCell);
                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                        if (i == 17)
                        pdfPCell.setBorderWidthRight(2f);
                    help.addCell(pdfPCell);
                    pdfPCell = new PdfPCell(help);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPTableIndex.addCell(pdfPCell);
                    index++;
            }
            paragraph.add(pdfPTableIndex);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableAutomatics", e.toString());
        }
    }

    public void createTableAutomatics2(ArrayList<String> floors, ArrayList<Integer> countRooms,
                                       ArrayList<String> rooms, ArrayList<Integer> countLines,
                                       ArrayList<String> lines, ArrayList<Integer> countAutomatics,
                                       ArrayList<ArrayList> automatics) {
        try {
            font.setSize(10);
            fontbd.setSize(10);
            fontbd_2.setSize(11);
            paragraph = new Paragraph();
            PdfPTable pdfPTable = new PdfPTable(18);
            float[] columnWidths = new float[]{2.3f, 11.2f, 3.9f, 10.8f, 4.5f, 4.5f, 5.6f, 4.5f, 4.5f, 4.5f, 5.7f, 3.6f, 5.6f, 5.6f, 5.6f, 5.6f, 5.6f, 6.3f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j, k, q, n, indexRoom = 0, indexLine = 0, indexAutomatic = 0, indexAutomaticSecond;

            //ШАПКА
            for (i = 1; i <= 18; i++) {
                PdfPTable help = new PdfPTable(1);
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setPaddingBottom(5f);
                pdfPCell.setBorderWidthTop(2f);
                pdfPCell.setBorderWidthBottom(2f);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 18)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Phrase(" ", font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 18)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(help);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(pdfPCell);
            }
            pdfPTable.setHeaderRows(1);
            pdfPTable.setSkipFirstHeader(true);

            //ОСНОВНАЯ ТАБЛИЦА
            for (i = 1; i <= floors.size(); i++) {

                //ЭТАЖ
                if(!floors.get(i - 1).equals("БЕЗ ЭТАЖА")) {
                    pdfPCell = new PdfPCell(new Phrase(floors.get(i - 1), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(18);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    pdfPTable.addCell(pdfPCell);
                }

                //КОМНАТА + ПУСТАЯ СТРОКА
                for (j = 1; j <= countRooms.get(i - 1); j++){
                    PdfPTable roomTable = new PdfPTable(18);
                    roomTable.setWidths(columnWidths);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(indexRoom + 1) + ". " + rooms.get(indexRoom), fontbd_2));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(18);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    roomTable.addCell(pdfPCell);
                    for (k = 0; k < 18; k++) {
                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        if (k == 0)
                            pdfPCell.setBorderWidthLeft(2f);
                        if (k == 17)
                            pdfPCell.setBorderWidthRight(2f);
                        roomTable.addCell(pdfPCell);
                    }
                    roomTable.setKeepTogether(true);
                    pdfPCell = new PdfPCell(roomTable);
                    pdfPCell.setColspan(18);
                    pdfPTable.addCell(pdfPCell);

                    //ЩИТ
                    for (k = 1; k <= countLines.get(indexRoom); k++) {
                        for (q = 0; q < 18; q++) {
                            if (q != 0 && q != 1) {
                                pdfPCell = new PdfPCell(new Phrase(" ", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                if (q == 17)
                                    pdfPCell.setBorderWidthRight(2f);
                                pdfPTable.addCell(pdfPCell);
                            }
                            else
                                if (q == 0) {
                                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(k) + ".", font));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    pdfPCell.setPaddingBottom(5f);
                                    pdfPCell.setBorderWidthLeft(2f);
                                    pdfPTable.addCell(pdfPCell);
                                }
                                else {
                                    pdfPCell = new PdfPCell(new Phrase(lines.get(indexLine), font));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    pdfPCell.setPaddingBottom(5f);
                                    pdfPTable.addCell(pdfPCell);
                                }
                        }

                        //АВТОМАТЫ И ПУСТАЯ СТРОКА
                        for(q = 1; q <= countAutomatics.get(indexLine); q++) {
                            indexAutomaticSecond = 0;
                            if (q != countAutomatics.get(indexLine)) {
                                for (n = 0; n < 18; n++) {
                                    if (n != 0) {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(automatics.get(indexAutomatic).get(indexAutomaticSecond)), font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 17)
                                            pdfPCell.setBorderWidthRight(2f);
                                        pdfPTable.addCell(pdfPCell);
                                        indexAutomaticSecond++;
                                    } else {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setBorderWidthLeft(2f);
                                        pdfPTable.addCell(pdfPCell);
                                    }
                                }
                            }
                            else {
                                PdfPTable lastAutomaticTable = new PdfPTable(18);
                                lastAutomaticTable.setWidths(columnWidths);
                                for (n = 0; n < 18; n++) {
                                    if (n != 0) {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(automatics.get(indexAutomatic).get(indexAutomaticSecond)), font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 17)
                                            pdfPCell.setBorderWidthRight(2f);
                                        lastAutomaticTable.addCell(pdfPCell);
                                        indexAutomaticSecond++;
                                    } else {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setBorderWidthLeft(2f);
                                        lastAutomaticTable.addCell(pdfPCell);
                                    }
                                }
                                for (n = 0; n < 18; n++) {
                                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    pdfPCell.setPaddingBottom(5f);
                                    if (n == 17)
                                        pdfPCell.setBorderWidthRight(2f);
                                    if (n == 0)
                                        pdfPCell.setBorderWidthLeft(2f);
                                    if (i == floors.size() && j == countRooms.get(i - 1) && k == countLines.get(indexRoom))
                                        pdfPCell.setBorderWidthBottom(2f);
                                    lastAutomaticTable.addCell(pdfPCell);
                                }
                                lastAutomaticTable.setKeepTogether(true);
                                pdfPCell = new PdfPCell(lastAutomaticTable);
                                pdfPCell.setColspan(18);
                                pdfPTable.addCell(pdfPCell);
                            }
                            indexAutomatic++;
                        }
                        indexLine++;
                    }
                    indexRoom++;
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableAutomatics2", e.toString());
        }
    }

    public void createTableDifAutomatics(String[]header) {
        try {
            font.setSize(9);
            fontbd.setSize(10);
            paragraph = new Paragraph();
            paragraph.setFont(font);
            PdfPTable pdfPTable = new PdfPTable(7);
            float[] columnWidths = new float[]{2.5f, 10.9f, 7.9f, 7.9f, 3.4f, 58.5f, 8.9f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            pdfPTable.setSpacingBefore(20);
            PdfPCell pdfPCell;
            int index = 0, i, j;
            for (i = 0; i < 7; ++i) {
                if (i != 5) {
                    pdfPCell = new PdfPCell(new Phrase(header[index], font));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    if (i == 0)
                        pdfPCell.setBorderWidthLeft(2f);
                    if (i == 6)
                        pdfPCell.setBorderWidthRight(2f);
                    pdfPCell.setBorderWidthTop(2f);
                    pdfPTable.addCell(pdfPCell);
                    index++;
                }
                else {
                    PdfPTable help_table = new PdfPTable(11);
                    float[] columnWidths1 = new float[]{9.91452991f, 9.91452991f, 7.86324786f, 7.86324786f, 12.4786325f, 7.69230769f, 7.69230769f, 9.57264957f, 7.69230769f, 9.57264957f, 9.74358974f};
                    help_table.setWidths(columnWidths1);
                    for (j = 0; j < 3; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        pdfPCell.setBorderWidthTop(2f);
                        if (j == 0)
                            pdfPCell.setColspan(2);
                        else if (j == 1)
                            pdfPCell.setColspan(3);
                        else if (j == 2)
                            pdfPCell.setColspan(6);
                        help_table.addCell(pdfPCell);
                        index++;
                    }
                    for (j = 0; j < 11; j++) {
                        pdfPCell = new PdfPCell(new Phrase(header[index], font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        pdfPCell.setPaddingBottom(5f);
                        help_table.addCell(pdfPCell);
                        index++;
                    }
                    pdfPCell = new PdfPCell(help_table);
                    pdfPCell.setPadding(0);
                    pdfPTable.addCell(pdfPCell);
                }
            }
            paragraph.add(pdfPTable);

            //НУМЕРАЦИЯ СТОЛБЦОВ
            index = 1;
            PdfPTable pdfPTableIndex = new PdfPTable(17);
            float[] columnWidths6 = new float[]{2.5f, 10.9f, 7.9f, 7.9f, 3.4f, 5.8f, 5.8f, 4.6f, 4.6f, 7.3f, 4.5f, 4.5f, 5.6f, 4.5f, 5.6f, 5.7f, 8.9f};
            pdfPTableIndex.setWidths(columnWidths6);
            pdfPTableIndex.setWidthPercentage(100);
            for (i = 0; i < 17; i++) {
                PdfPTable help = new PdfPTable(1);
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(index), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setPaddingBottom(5f);
                pdfPCell.setBorderWidthTop(2f);
                pdfPCell.setBorderWidthBottom(2f);
                if (i == 0)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 16)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Phrase(" ", font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (i == 0)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 16)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(help);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTableIndex.addCell(pdfPCell);
                index++;
            }
            paragraph.add(pdfPTableIndex);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableDA", e.toString());
        }
    }

    public void createTableDifAutomatics2(ArrayList<String> floors, ArrayList<Integer> countRooms,
                                       ArrayList<String> rooms, ArrayList<Integer> countLines,
                                       ArrayList<String> lines, ArrayList<Integer> countAutomatics,
                                       ArrayList<ArrayList> automatics) {
        try {
            font.setSize(10);
            fontbd.setSize(10);
            fontbd_2.setSize(11);
            paragraph = new Paragraph();
            PdfPTable pdfPTable = new PdfPTable(17);
            float[] columnWidths = new float[]{2.5f, 10.9f, 7.9f, 7.9f, 3.4f, 5.8f, 5.8f, 4.6f, 4.6f, 7.3f, 4.5f, 4.5f, 5.6f, 4.5f, 5.6f, 5.7f, 8.9f};
            pdfPTable.setWidths(columnWidths);
            pdfPTable.setWidthPercentage(100);
            PdfPCell pdfPCell;
            int i, j, k, q, n, indexRoom = 0, indexLine = 0, indexAutomatic = 0, indexAutomaticSecond;

            //ШАПКА
            for (i = 1; i <= 17; i++) {
                PdfPTable help = new PdfPTable(1);
                pdfPCell = new PdfPCell(new Phrase(String.valueOf(i), fontbd));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPCell.setPaddingBottom(5f);
                pdfPCell.setBorderWidthTop(2f);
                pdfPCell.setBorderWidthBottom(2f);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 17)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(new Phrase(" ", font));
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if (i == 1)
                    pdfPCell.setBorderWidthLeft(2f);
                if (i == 17)
                    pdfPCell.setBorderWidthRight(2f);
                help.addCell(pdfPCell);
                pdfPCell = new PdfPCell(help);
                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                pdfPTable.addCell(pdfPCell);
            }
            pdfPTable.setHeaderRows(1);
            pdfPTable.setSkipFirstHeader(true);

            //ОСНОВНАЯ ТАБЛИЦА
            for (i = 1; i <= floors.size(); i++) {

                //ЭТАЖ
                if(!floors.get(i - 1).equals("БЕЗ ЭТАЖА")) {
                    pdfPCell = new PdfPCell(new Phrase(floors.get(i - 1), fontbd));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(17);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    pdfPTable.addCell(pdfPCell);
                }

                //КОМНАТА + ПУСТАЯ СТРОКА
                for (j = 1; j <= countRooms.get(i - 1); j++){
                    PdfPTable roomTable = new PdfPTable(17);
                    roomTable.setWidths(columnWidths);
                    pdfPCell = new PdfPCell(new Phrase(String.valueOf(indexRoom + 1) + ". " + rooms.get(indexRoom), fontbd_2));
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfPCell.setPaddingBottom(5f);
                    pdfPCell.setColspan(17);
                    pdfPCell.setBorderWidthLeft(2f);
                    pdfPCell.setBorderWidthRight(2f);
                    roomTable.addCell(pdfPCell);
                    for (k = 0; k < 17; k++) {
                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        if (k == 0)
                            pdfPCell.setBorderWidthLeft(2f);
                        if (k == 16)
                            pdfPCell.setBorderWidthRight(2f);
                        roomTable.addCell(pdfPCell);
                    }
                    roomTable.setKeepTogether(true);
                    pdfPCell = new PdfPCell(roomTable);
                    pdfPCell.setColspan(17);
                    pdfPTable.addCell(pdfPCell);

                    //ЩИТ
                    for (k = 1; k <= countLines.get(indexRoom); k++) {
                        for (q = 0; q < 17; q++) {
                            if (q != 0 && q != 1) {
                                pdfPCell = new PdfPCell(new Phrase(" ", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                if (q == 16)
                                    pdfPCell.setBorderWidthRight(2f);
                                pdfPTable.addCell(pdfPCell);
                            }
                            else
                            if (q == 0) {
                                pdfPCell = new PdfPCell(new Phrase(String.valueOf(k) + ".", font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                pdfPCell.setPaddingBottom(5f);
                                pdfPCell.setBorderWidthLeft(2f);
                                pdfPTable.addCell(pdfPCell);
                            }
                            else {
                                pdfPCell = new PdfPCell(new Phrase(lines.get(indexLine), font));
                                pdfPCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                pdfPCell.setPaddingBottom(5f);
                                pdfPTable.addCell(pdfPCell);
                            }
                        }

                        //АВТОМАТЫ И ПУСТАЯ СТРОКА
                        for(q = 1; q <= countAutomatics.get(indexLine); q++) {
                            indexAutomaticSecond = 0;
                            if (q != countAutomatics.get(indexLine)) {
                                for (n = 0; n < 17; n++) {
                                    if (n != 0) {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(automatics.get(indexAutomatic).get(indexAutomaticSecond)), font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 16)
                                            pdfPCell.setBorderWidthRight(2f);
                                        pdfPTable.addCell(pdfPCell);
                                        indexAutomaticSecond++;
                                    } else {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setBorderWidthLeft(2f);
                                        pdfPTable.addCell(pdfPCell);
                                    }
                                }
                            }
                            else {
                                PdfPTable lastAutomaticTable = new PdfPTable(17);
                                lastAutomaticTable.setWidths(columnWidths);
                                for (n = 0; n < 17; n++) {
                                    if (n != 0) {
                                        pdfPCell = new PdfPCell(new Phrase(String.valueOf(automatics.get(indexAutomatic).get(indexAutomaticSecond)), font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setPaddingBottom(5f);
                                        if (n == 16)
                                            pdfPCell.setBorderWidthRight(2f);
                                        lastAutomaticTable.addCell(pdfPCell);
                                        indexAutomaticSecond++;
                                    } else {
                                        pdfPCell = new PdfPCell(new Phrase(" ", font));
                                        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                        pdfPCell.setBorderWidthLeft(2f);
                                        lastAutomaticTable.addCell(pdfPCell);
                                    }
                                }
                                for (n = 0; n < 17; n++) {
                                    pdfPCell = new PdfPCell(new Phrase(" ", font));
                                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                    pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                                    pdfPCell.setPaddingBottom(5f);
                                    if (n == 16)
                                        pdfPCell.setBorderWidthRight(2f);
                                    if (n == 0)
                                        pdfPCell.setBorderWidthLeft(2f);
                                    if (i == floors.size() && j == countRooms.get(i - 1) && k == countLines.get(indexRoom))
                                        pdfPCell.setBorderWidthBottom(2f);
                                    lastAutomaticTable.addCell(pdfPCell);
                                }
                                lastAutomaticTable.setKeepTogether(true);
                                pdfPCell = new PdfPCell(lastAutomaticTable);
                                pdfPCell.setColspan(17);
                                pdfPTable.addCell(pdfPCell);
                            }
                            indexAutomatic++;
                        }
                        indexLine++;
                    }
                    indexRoom++;
                }
            }
            paragraph.add(pdfPTable);
            document.add(paragraph);
        }catch (Exception e) {
            Log.e("createTableDA2", e.toString());
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
