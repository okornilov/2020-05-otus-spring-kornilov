package ru.otus.library.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.PrintStream;

@Service
public class LibraryIOService implements IOService {

    private final PrintStream printStream;

    public LibraryIOService(@Value("#{ T(java.lang.System).out}") PrintStream printStream) {
        this.printStream = printStream;
    }

    @Override
    public void outLine(String line) {
        printStream.println(line);
    }
}
