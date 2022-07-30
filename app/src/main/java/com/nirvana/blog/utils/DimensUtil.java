package com.nirvana.blog.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 *
 * 源文件: values/dimens
 * 目标文件: values-w360dp、values-w390dp、values-w410dp(须保证文件存在)
 * 适配范围: 屏幕最大宽度在360dp~420dp(720px~1440px)的手机
 *
 * dimens标准: 源文件与values-w360dp/dimens保持一致性
 *            values-w390dp/dimens中数值与原数值成 1080/2.75/360 的关系
 *            values-w410dp/dimens中数值与原数值成 1440/3.5/360 的关系
 */
public final class DimensUtil {

    public static void main(String[] args) throws IOException {
        //生成标准dimens文件
        //generateStandardFile_360dp();

        //生成其它dimens文件
        startGenerate();
    }

    /**
     * 根据标准dimens文件 开始生成其它的dimens文件
     */
    private static void startGenerate() {
        System.out.println("generate ---> start");
        File rootFile =  new File("./app/src/main/res/values/dimens.xml");

        StringBuilder w360dpBuilder = new StringBuilder();
        StringBuilder w390dpBuilder = new StringBuilder();
        StringBuilder w410dpBuilder = new StringBuilder();

        BufferedReader reader = null;
        try {

            File file360 = new File("./app/src/main/res/values-w360dp");
            file360.mkdir();
            File file390 = new File("./app/src/main/res/values-w390dp");
            file390.mkdir();
            File file410 = new File("./app/src/main/res/values-w410dp");
            file410.mkdir();
            reader = new BufferedReader(new FileReader(rootFile));
            String lineStr;

            while ((lineStr = reader.readLine()) != null) {
                if (!lineStr.contains("</dimen>")) {
                    w360dpBuilder.append(lineStr).append("\r\n");
                    w390dpBuilder.append(lineStr).append("\r\n");
                    w410dpBuilder.append(lineStr).append("\r\n");
                } else {
                    String startStr = lineStr.substring(0, lineStr.indexOf(">") + 1);
                    String endStr = lineStr.substring(lineStr.lastIndexOf("<") - 2);

                    int startIndex = lineStr.indexOf(">") + 1;
                    int endIndex = lineStr.indexOf("</") - 2;
                    Float rootValue = Float.parseFloat(lineStr.substring(startIndex, endIndex));

                    w360dpBuilder.append(lineStr).append("\r\n");
                    float scale;
                    if (rootValue < 10) scale = 100f;
                    else scale = 10f;
                    //保留两位小数
                    w390dpBuilder.append(startStr)
                            .append(Math.round(rootValue * 1080f / 2.75f / 360 * scale) / scale)
                            .append(endStr)
                            .append("\r\n");
                    //保留两位小数
                    w410dpBuilder.append(startStr)
                            .append(Math.round(rootValue * 1440f / 3.5f / 360f * scale) / scale)
                            .append(endStr)
                            .append("\r\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("generate failed ---> file not found!");
        } catch (IOException e) {
            System.out.println("generate failed ---> reader read file error!");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //此时读取数据、生成字符串完成

        String w360dpFilePath = "./app/src/main/res/values-w360dp/dimens.xml";
        String w390dpFilePath = "./app/src/main/res/values-w390dp/dimens.xml";
        String w410dpFilePath = "./app/src/main/res/values-w410dp/dimens.xml";
        writeDimensFile(w360dpFilePath, w360dpBuilder);
        writeDimensFile(w390dpFilePath, w390dpBuilder);
        writeDimensFile(w410dpFilePath, w410dpBuilder);

        System.out.println("generate ---> finish!");
    }

    private static void writeDimensFile(String path, StringBuilder stringBuilder) {
        PrintWriter writer = null;
        //File file = new File(path);
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(path)));
            writer.println(stringBuilder);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("generate failed ---> print writer error!");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * 自动生成标准dimens文件
     */
    private static void generateStandardFile_360dp() throws IOException {
        System.out.println("generate ---> start");
        File rootFile =  new File("./app/src/main/res/values/dimens.xml");
        Writer writer = new FileWriter(rootFile, false);
        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        final String head = "\t";
        final String beforeName = "<dimen name=\"";
        final String beforeValue = "\">";
        final String tail = "</dimen>";
        final String unit = "dp";

        bufferedWriter.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        bufferedWriter.newLine();
        bufferedWriter.write("<resources>");
        bufferedWriter.newLine();
        bufferedWriter.flush();

        bufferedWriter.write("\t<!-- From DimensUtil.class -->");
        bufferedWriter.newLine();
        bufferedWriter.write("\t<!-- round: 0.1~1.5 & 2~50 & 52~140 & 145~360 -->");
        bufferedWriter.newLine();
        bufferedWriter.newLine();
        bufferedWriter.flush();

        System.out.println("generate ---> 0.1dp - 1.5dp");
        //生成0.1dp-1.5dp
        float start1 = 0.1f;
        while (start1 <= 1.6f) {
            start1 = (int) (start1 * 10) / 10f;
            String valueStr = start1 + unit;
            String name = unit + String.valueOf(start1).replace(".", "_");
            bufferedWriter.write(head + beforeName + name + beforeValue + valueStr + tail);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            start1 += 0.1f;
        }

        System.out.println("generate ---> 2dp - 50dp");
        //生成2dp-50dp;
        int start2 = 2;
        while (start2 <= 50) {
            String valueStr = start2 + unit;
            String name = unit + start2;
            bufferedWriter.write(head + beforeName + name + beforeValue + valueStr + tail);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            ++start2;
        }

        System.out.println("generate ---> 52dp - 140dp");
        int start3 = 52;
        while (start3 <= 140) {
            String valueStr = start3 + unit;
            String name = unit + start3;
            bufferedWriter.write(head + beforeName + name + beforeValue + valueStr + tail);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            start3 += 2;
        }

        System.out.println("generate ---> 145dp - 360dp");
        int start4 = 145;
        while (start4 <= 360) {
            String valueStr = start4 + unit;
            String name = unit + start4;
            bufferedWriter.write(head + beforeName + name + beforeValue + valueStr + tail);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            start4 += 5;
        }

        bufferedWriter.write("</resources>");
        bufferedWriter.flush();

        System.out.println("generate ---> finished!");
        bufferedWriter.close();
    }
}