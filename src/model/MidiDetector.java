package model;

import java.awt.*;
import java.awt.event.*; 
import javax.sound.midi.*;
import javax.swing.*; 

public class MidiDetector  extends JFrame implements KeyListener {
    private Synthesizer synthesizer;
    private MidiChannel channel;
    private JLabel lJLabel;

    public MidiDetector() {
        // Set up the JFrame
        setTitle("MIDI Keyboard detector and sound generator");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setLayout(new FlowLayout());

        //initialize the label
        lJLabel=new JLabel();
        lJLabel.setFont(new Font("Arial", Font.PLAIN, 28));
        add(lJLabel);
        setVisible(true);
        MidiDevice device;
        MidiDevice.Info[] deviceInfo= MidiSystem.getMidiDeviceInfo();
        for(int i=0; i< deviceInfo.length;i++){
            try {
                device = MidiSystem.getMidiDevice(deviceInfo[i]);
                if (!device.isOpen()){
                    device.open();
                }
                System.out.println("Successfully connected to: "+deviceInfo[i]);
                lJLabel.setText("Successfully connected to "+ deviceInfo[i].getName());
                // set the MIDI input reciever to get MIDI data from the device 
                device.getTransmitter().setReceiver(new MidiInputReceiver(device.getDeviceInfo().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Initialize MIDI synthesizer
        try {
            
            synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            channel = synthesizer.getChannels()[0]; // Get the first MIDI channel
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
     // Custom Receiver class to process MIDI messages from the external keyboard
     public class MidiInputReceiver implements Receiver {
        private String name;

        public MidiInputReceiver(String name) {
            this.name = name;
        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (message instanceof ShortMessage) {
                ShortMessage sm = (ShortMessage) message;
                int command = sm.getCommand();
                int key = sm.getData1();   // Note value (MIDI number)
                int velocity = sm.getData2(); // Velocity value

                if (command == ShortMessage.NOTE_ON && velocity > 0) {
                    System.out.println("Note ON: " + key + " Velocity: " + velocity);
                    channel.noteOn(key, velocity); // Play the note with velocity
                } else if (command == ShortMessage.NOTE_OFF || (command == ShortMessage.NOTE_ON && velocity == 0)) {
                    System.out.println("Note OFF: " + key);
                    channel.noteOff(key); // Stop the note
                }
            }
        }
            @Override
            public void close() {
            // Implement close if needed for resource management
            
        }
        }


    @Override
    public void keyPressed(KeyEvent e) {
        // Generate sound based on computer keyboard
        // int midiNote = mapKeyToMidi(e.getKeyCode());
        // if (midiNote != -1) {
        //     channel.noteOn(midiNote, 100); // Play note with velocity 100
        // }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // int midiNote = mapKeyToMidi(e.getKeyCode());
        // if (midiNote != -1) {
        //     channel.noteOff(midiNote); // Stop the note
        // }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not implemented yet
    }

    // private int mapKeyToMidi(int keyCode) {
    //     // Simple mapping of key codes to MIDI note numbers (C4 to B4)
    //     switch (keyCode) {
    //         case KeyEvent.VK_A: return 60; // C4
    //         case KeyEvent.VK_S: return 61; // C#4
    //         case KeyEvent.VK_D: return 62; // D4
    //         case KeyEvent.VK_F: return 63; // D#4
    //         case KeyEvent.VK_G: return 64; // E4
    //         case KeyEvent.VK_H: return 65; // F4
    //         case KeyEvent.VK_J: return 66; // F#4
    //         case KeyEvent.VK_K: return 67; // G4
    //         case KeyEvent.VK_L: return 68; // G#4
    //         case KeyEvent.VK_SEMICOLON: return 69; // A4
    //         case KeyEvent.VK_QUOTE: return 70; // A#4
    //         case KeyEvent.VK_ENTER: return 71; // B4
    //         default: return -1; // No mapping
    //     }
    // }

    public static void main(String[] args) {
        new MidiDetector();
    }
    

}