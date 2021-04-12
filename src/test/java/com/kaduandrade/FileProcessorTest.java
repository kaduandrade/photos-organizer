package com.kaduandrade;

import junit.framework.TestCase;

import java.io.File;

public class FileProcessorTest extends TestCase {

    public void testCopyWithDifferentName() {

        File file = new File("teste.txt");
        String a = "1111111.txt";
        String b = "2222222.txt";

        FileProcessor.copyWithDifferentName(file, a, b);

    }
}