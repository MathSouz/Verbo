package com.android.verbo.bible.models;

public class Book
{
    private final int id;
    private String abbrev;
    private String name;
    private int chapters;

    public Book(int id, String abbrev, String name, int chapters)
    {
        this.id = id;
        this.abbrev = abbrev;
        this.name = name;
        this.chapters = chapters;
    }

    public int getId() {
        return id;
    }

    public String getAbbrev() {
        return abbrev;
    }

    public String getName() {
        return name;
    }

    public int getChapters() {
        return chapters;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", abbrev='" + abbrev + '\'' +
                ", name='" + name + '\'' +
                ", chapters=" + chapters +
                '}';
    }
}
