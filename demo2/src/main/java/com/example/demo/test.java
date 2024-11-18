package com.example.demo;

import java.util.List;

import model.Chord;
import model.MeasureGenerator;
import model.MidiInputProcessor;



public class test {
    public static void main(String[] args) {
       MeasureGenerator generator = new MeasureGenerator(1, 11, 1,4);
        List<Chord> currentBar = generator.nextMeasure(0,0);
       int currentIndex = 0;
       MidiInputProcessor processor = new MidiInputProcessor(null);
       boolean once = true;
       while (true) {
            if(processor.getInputs().equals(currentBar.get(currentIndex).getNotes())){
                System.out.println(currentBar.get(currentIndex).toString());
            }        
       }}
}
